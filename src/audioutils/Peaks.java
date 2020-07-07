/*  
 * 
 * This code was taken from BeatRoot: An interactive beat tracking system
 * Copyright (C) 2001, 2006 by Simon Dixon
 * 
 * 
*/

package audioutils;

import java.util.LinkedList;

public class Peaks {

	public static boolean debug = false;
	public static int pre = 3;
	public static int post = 1;
	
	/** General peak picking method for finding n local maxima in an array
	 *  @param data input data
	 *  @param peaks list of peak indexes
	 *  @param width minimum distance between peaks
	 */
	public static int findPeaks(double[] data, int[] peaks, int width) {
		int peakCount = 0;
		int maxp = 0;
		int mid = 0;
		int end = data.length;
		while (mid < end) {
			int i = mid - width;
			if (i < 0)
				i = 0;
			int stop = mid + width + 1;
			if (stop > data.length)
				stop = data.length;
			maxp = i;
			for (i++; i < stop; i++)
				if (data[i] > data[maxp])
					maxp = i;
			if (maxp == mid) {
				int j;
				for (j = peakCount; j > 0; j--) {
					if (data[maxp] <= data[peaks[j-1]])
						break;
					else if (j < peaks.length)
						peaks[j] = peaks[j-1];
				}
				if (j != peaks.length)
					peaks[j] = maxp;
				if (peakCount != peaks.length)
					peakCount++;
			}
			mid++;
		}
		return peakCount;
	} // findPeaks()

	/** General peak picking method for finding local maxima in an array
	 *  @param data input data
	 *  @param width minimum distance between peaks
	 *  @param threshold minimum value of peaks
	 *  @return list of peak indexes
	 */
	public static LinkedList<Integer> findPeaks(double[] data, int width,
												double threshold) {
		return findPeaks(data, width, threshold, 0, false);
	} // findPeaks()
	
	/** General peak picking method for finding local maxima in an array
	 *  @param data input data
	 *  @param width minimum distance between peaks
	 *  @param threshold minimum value of peaks
	 *  @param decayRate how quickly previous peaks are forgotten
	 *  @param isRelative minimum value of peaks is relative to local average
	 *  @return list of peak indexes
	 */
	public static LinkedList<Integer> findPeaks(double[] data, int width,
				double threshold, double decayRate, boolean isRelative) {
		LinkedList<Integer> peaks = new LinkedList<Integer>();
		int maxp = 0;
		int mid = 0;
		int end = data.length;
		double av = data[0];
		while (mid < end) {
			av = decayRate * av + (1 - decayRate) * data[mid];
			if (av < data[mid])
				av = data[mid];
			int i = mid - width;
			if (i < 0)
				i = 0;
			int stop = mid + width + 1;
			if (stop > data.length)
				stop = data.length;
			maxp = i;
			for (i++; i < stop; i++)
				if (data[i] > data[maxp])
					maxp = i;
			if (maxp == mid) {
				if (overThreshold(data, maxp, width, threshold, isRelative,av)){
					if (debug)
						System.out.println(" peak");
					peaks.add(new Integer(maxp));
				} else if (debug)
					System.out.println();
			}
			mid++;
		}
		return peaks;
	} // findPeaks()

	public static boolean overThreshold(double[] data, int index, int width,
										double threshold, boolean isRelative,
										double av) {
		if (debug)
			System.out.printf("%4d : %6.3f     Av1: %6.3f    ",
								index, data[index], av);
		if (data[index] < av)
			return false;
		if (isRelative) {
			int iStart = index - pre * width;
			if (iStart < 0)
				iStart = 0;
			int iStop = index + post * width;
			if (iStop > data.length)
				iStop = data.length;
			double sum = 0;
			int count = iStop - iStart;
			while (iStart < iStop)
				sum += data[iStart++];
			if (debug)
				System.out.printf("    %6.3f    %6.3f   ", sum / count,
							data[index] - sum / count - threshold);
			return (data[index] > sum / count + threshold);
		} else
			return (data[index] > threshold);
	} // overThreshold()

	public static void normalise(double[] data) {
		double sx = 0;
		double sxx = 0;
		for (int i = 0; i < data.length; i++) {
			sx += data[i];
			sxx += data[i] * data[i];
		}
		double mean = sx / data.length;
		double sd = Math.sqrt((sxx - sx * mean) / data.length);
		if (sd == 0)
			sd = 1;		// all data[i] == mean  -> 0; avoids div by 0
		for (int i = 0; i < data.length; i++) {
			data[i] = (data[i] - mean) / sd;
		}
	} // normalise()

	public static double min(double[] arr) { return arr[imin(arr)]; }

	public static double max(double[] arr) { return arr[imax(arr)]; }

	public static int imin(double[] arr) {
		int i = 0;
		for (int j = 1; j < arr.length; j++)
			if (arr[j] < arr[i])
				i = j;
		return i;
	} 

	public static int imax(double[] arr) {
		int i = 0;
		for (int j = 1; j < arr.length; j++)
			if (arr[j] > arr[i])
				i = j;
		return i;
	} 

} // class Peaks
