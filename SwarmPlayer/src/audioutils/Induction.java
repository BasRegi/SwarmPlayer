/*  
 * 
 * This code was taken from BeatRoot: An interactive beat tracking system
 * Copyright (C) 2001, 2006 by Simon Dixon
 * 
 * 
*/

package audioutils;

import java.util.ListIterator;

/** Performs tempo induction by finding clusters of similar
 *  inter-onset intervals (IOIs), ranking them according to the number
 *  of intervals and relationships between them, and returning a set
 *  of tempo hypotheses for initialising the beat tracking agents.
 */
class Induction {

	/** The maximum difference in IOIs which are in the same cluster */ 
	public static double clusterWidth = 0.025;
	
	/** The minimum IOI for inclusion in a cluster */
	public static double minIOI = 0.070;
	
	/** The maximum IOI for inclusion in a cluster */
	public static double maxIOI = 2.500;
	
	/** The minimum inter-beat interval (IBI), i.e. the maximum tempo
	 *  hypothesis that can be returned.
	 *  0.30 seconds == 200 BPM
	 *  0.25 seconds == 240 BPM
	 */
	public static double minIBI = 0.3; 

	/** The maximum inter-beat interval (IBI), i.e. the minimum tempo
	 *  hypothesis that can be returned.
	 *  1.00 seconds ==  60 BPM
	 *  0.75 seconds ==  80 BPM
	 *  0.60 seconds == 100 BPM
	 */
	public static double maxIBI = 1.0;	//  60BPM	// was 0.75 =>  80
	
	/** The maximum number of tempo hypotheses to return */
	public static int topN = 10;
	
	/** Flag to enable debugging output */
	public static boolean debug = false;
	
	/** Performs tempo induction (see JNMR 2001 paper by Simon Dixon for details). 
	 *  @param events The onsets (or other events) from which the tempo is induced
	 *  @return A list of beat tracking agents, where each is initialised with one
	 *          of the top tempo hypotheses but no beats
	 */
	public static AgentList beatInduction(EventList events) {
		int i, j, b, bestCount;
		boolean submult;
		int intervals = 0;			// number of interval clusters
		int[] bestn = new int[topN];// count of high-scoring clusters
		double ratio, err;
		int degree;
		int maxClusterCount = (int) Math.ceil((maxIOI - minIOI) / clusterWidth);
		double[] clusterMean = new double[maxClusterCount];
		int[] clusterSize = new int[maxClusterCount];
		int[] clusterScore = new int[maxClusterCount];
		
		ListIterator<Event> ptr1, ptr2;
		Event e1,e2;
		ptr1 = events.listIterator();
		while (ptr1.hasNext()) {
			e1 = ptr1.next();
			ptr2 = events.listIterator();
			e2 = ptr2.next();
			while (e2 != e1)
				e2 = ptr2.next();
			while (ptr2.hasNext()) {
				e2 = ptr2.next();
				double ioi = e2.keyDown - e1.keyDown;
				if (ioi < minIOI)		// skip short intervals
					continue;
				if (ioi > maxIOI)		// ioi too long
					break;
				for (b = 0; b < intervals; b++)		// assign to nearest cluster
					if (Math.abs(clusterMean[b] - ioi) < clusterWidth) {
						if ((b < intervals - 1) && (
								Math.abs(clusterMean[b+1] - ioi) <
								Math.abs(clusterMean[b] - ioi)))
							b++;		// next cluster is closer
						clusterMean[b] = (clusterMean[b] * clusterSize[b] +ioi)/
											(clusterSize[b] + 1);
						clusterSize[b]++;
						break;
					}
				if (b == intervals) {	// no suitable cluster; create new one
					if (intervals == maxClusterCount) {
						System.err.println("Warning: Too many clusters");
						continue; // ignore this IOI
					}
					intervals++;
					for ( ; (b>0) && (clusterMean[b-1] > ioi); b--) {
						clusterMean[b] = clusterMean[b-1];
						clusterSize[b] = clusterSize[b-1];
					}
					clusterMean[b] = ioi;
					clusterSize[b] = 1;
				}
			}
		}
		if (debug) { // output IOI histogram in Matlab format
			System.out.println("Inter-onset interval histogram:\n" +
					"StartMatlabCode\n" +
					"ioi = [");
			for (b = 0; b < intervals; b++)
				System.out.printf("%4d %7.3f %7d\n",
									b, clusterMean[b], clusterSize[b]);
			System.out.println("]; ioiclusters(ioi, name);\nEndMatlabCode\n");
		}
		for (b = 0; b < intervals; b++)	// merge similar intervals
		// TODO: they are now in order, so don't need the 2nd loop
		// TODO: check BOTH sides before averaging or upper gps don't work
			for (i = b+1; i < intervals; i++)
				if (Math.abs(clusterMean[b] - clusterMean[i]) < clusterWidth) {
					clusterMean[b] = (clusterMean[b] * clusterSize[b] +
										clusterMean[i] * clusterSize[i]) /
										(clusterSize[b] + clusterSize[i]);
					clusterSize[b] = clusterSize[b] + clusterSize[i];
					--intervals;
					for (j = i+1; j <= intervals; j++) {
						clusterMean[j-1] = clusterMean[j];
						clusterSize[j-1] = clusterSize[j];
					}
				}
		if (intervals == 0)
			return new AgentList();
		for (b = 0; b < intervals; b++)
			clusterScore[b] = 10 * clusterSize[b];
		bestn[0] = 0;
		bestCount = 1;
		for (b = 0; b < intervals; b++)
			for (i = 0; i <= bestCount; i++)
				if ((i < topN) && ((i == bestCount) ||
								 (clusterScore[b] > clusterScore[bestn[i]]))){
					if (bestCount < topN)
						bestCount++;
					for (j = bestCount - 1; j > i; j--)
						bestn[j] = bestn[j-1];
					bestn[i] = b;
					break;
				}
		if (debug) {
			System.out.println("Best " + bestCount + " clusters (before):");
			for (b = 0; b < bestCount; b++)
				System.out.printf("%5.3f : %5d\n", clusterMean[bestn[b]],
													clusterScore[bestn[b]]);
		}
		for (b = 0; b < intervals; b++)	// score intervals
			for (i = b+1; i < intervals; i++) {
				ratio = clusterMean[b] / clusterMean[i];
				submult = ratio < 1;
				if (submult)
					degree = (int) Math.round(1/ratio);
				else
					degree = (int) Math.round(ratio);
				if ((degree >= 2) && (degree <= 8)) {
					if (submult)
						err = Math.abs(clusterMean[b]*degree - clusterMean[i]);
					else
						err = Math.abs(clusterMean[b] - clusterMean[i]*degree);
					if (err < (submult? clusterWidth : clusterWidth * degree)) {
						if (degree >= 5)
							degree = 1;
						else
							degree = 6 - degree;
						clusterScore[b] += degree * clusterSize[i];
						clusterScore[i] += degree * clusterSize[b];
					}
				}
			}
		if (debug) {
			System.out.println("Best " + bestCount + " clusters (after):");
			for (b = 0; (b < bestCount); b++)
				System.out.printf("%5.3f : %5d\n", clusterMean[bestn[b]],
													clusterScore[bestn[b]]);
		}
		if (debug) {
			System.out.println("Inter-onset interval histogram 2:");
			for (b = 0; b < intervals; b++)
				System.out.printf("%3d: %5.3f : %3d (score: %5d)\n",
						b, clusterMean[b], clusterSize[b], clusterScore[b]);
		}

		AgentList a = new AgentList();
		for (int index = 0; index < bestCount; index++) {
			b = bestn[index];
			// Adjust it, using the size of super- and sub-intervals
			double newSum = clusterMean[b] * clusterScore[b];
			int newWeight = clusterScore[b];
			for (i = 0; i < intervals; i++) {
				if (i == b)
					continue;
				ratio = clusterMean[b] / clusterMean[i];
				if (ratio < 1) {
					degree = (int) Math.round(1 / ratio);
					if ((degree >= 2) && (degree <= 8)) {
						err = Math.abs(clusterMean[b]*degree - clusterMean[i]);
						if (err < clusterWidth) {
							newSum += clusterMean[i] / degree * clusterScore[i];
							newWeight += clusterScore[i];
						}
					}
				} else {
					degree = (int) Math.round(ratio);
					if ((degree >= 2) && (degree <= 8)) {
						err = Math.abs(clusterMean[b] - degree*clusterMean[i]);
						if (err < clusterWidth * degree) {
							newSum += clusterMean[i] * degree * clusterScore[i];
							newWeight += clusterScore[i];
						}
					}
				}
			}
			double beat = newSum / newWeight;
			// Scale within range ... hope the grouping isn't ternary :(
			while (beat < minIBI)		// Maximum speed
				beat *= 2.0;
			while (beat > maxIBI)		// Minimum speed
				beat /= 2.0;
			if (beat >= minIBI) {
				a.add(new Agent(beat));
				if (debug)
					System.out.printf(" %5.3f", beat);
			}
		}
		if (debug)
			System.out.println(" IBI");
		return a;
	} // beatInduction()

	/** For variable cluster widths in newInduction().
	 * @param low The lowest IOI allowed in the cluster
 	 * @return The highest IOI allowed in the cluster
	 */
	protected static int top(int low) {
		return low + 25; // low/10;
	} // top()

} // class Induction
