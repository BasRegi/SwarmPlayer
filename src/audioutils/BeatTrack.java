/*  
 * 
 * This code was adapted from BeatRoot: An interactive beat tracking system
 * Copyright (C) 2001, 2006 by Simon Dixon
 * 
 * 
*/

package audioutils;

import audio.AudioProcessor;

/**
 * Class to carry out the beat tracking segment of the BeatRoot algorithm
 *
 */
public class BeatTrack {
	
	/** For compiler warnings */
	public static final long serialVersionUID = 0;
	
	/** Function to create new Beat */
	public static Event newBeat(double time, int beatNum) 
	{
		return new Event(time,time, time, 56, 64, beatNum, 0, 1);
	}
 
	/**
	 * Function to call beat track below
	 * @param events EventList of onsets
	 * @param audioProcessor Instance of AudioProcessor
	 * @return
	 */
	public static EventList beatTrack(EventList events, AudioProcessor audioProcessor) 
	{
		return beatTrack(events, null, audioProcessor);
	}

	/**
	 * Beat Tracking Function
	 * @param events EventList of onsets
	 * @param audioProcessor Instance of AudioProcessor to be updated with best-fit Tempo
	 * @return EventList of tracked beats
	 */
	public static EventList beatTrack(EventList events, EventList beats, AudioProcessor audioProcessor) 
	{
		AgentList agents = null;
		int count = 0;
		double beatTime = -1;
		if (beats != null) {
			//Not required
			count = beats.size() - 1;
			beatTime = beats.l.getLast().keyDown;
		}
		if (count > 0) {
			//Not Required
			double ioi = (beatTime - beats.l.getFirst().keyDown) / count;
			agents = new AgentList(new Agent(ioi), null);
		} else									
			agents = Induction.beatInduction(events);
		if (beats != null)
			//Not Required
			for (AgentList ptr = agents; ptr.ag != null; ptr = ptr.next) {
				ptr.ag.beatTime = beatTime;
				ptr.ag.beatCount = count;
				ptr.ag.events = new EventList(beats);
			}
		agents.beatTrack(events, -1);
		Agent best = agents.bestAgent();
		if (best != null) {
			audioProcessor.setTempo((int)Math.round(((1/best.beatInterval)*60)));
			System.out.println("Tempo Hypothesis " + audioProcessor.getTempo());
			best.fillBeats(beatTime);
			return best.events;
		}
		return new EventList();
	}
	
}