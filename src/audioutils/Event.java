/*  
 * 
 * This code was taken from BeatRoot: An interactive beat tracking system
 * Copyright (C) 2001, 2006 by Simon Dixon
 * 
 * 
*/

package audioutils;

/**
 * Class to hold information about each onset
 *
 */
public class Event implements Comparable<Object>, Cloneable, java.io.Serializable {

	private static final long serialVersionUID = 0;
	
	public double keyDown, keyUp, pedalUp, scoreBeat, scoreDuration, salience;
	public int midiPitch, midiVelocity, flags, midiCommand, midiChannel,
				midiTrack;

	public Event(double onset, double offset, double eOffset, int pitch,
				 int velocity, double beat, double duration, int eventFlags,
				 int command, int channel, int track) {
		this(onset, offset, eOffset, pitch, velocity, beat,duration,eventFlags);
		midiCommand = command;
		midiChannel = channel;
		midiTrack = track;
	} // constructor

	public Event(double onset, double offset, double eOffset, int pitch,
				 int velocity, double beat, double duration, int eventFlags) {
		keyDown = onset;
		keyUp = offset;
		pedalUp = eOffset;
		midiPitch = pitch;
		midiVelocity = velocity;
		scoreBeat = beat;
		scoreDuration = duration;
		flags = eventFlags;
		midiCommand = javax.sound.midi.ShortMessage.NOTE_ON;
		midiChannel = 1;
		midiTrack = 0;
		salience = 0;
	} // constructor

	public Event clone() {
		return new Event(keyDown, keyUp, pedalUp, midiPitch, midiVelocity,
					scoreBeat, scoreDuration, flags, midiCommand, midiChannel,
					midiTrack);
	} // clone()

	// Interface Comparable
	public int compareTo(Object o) {
		Event e = (Event) o;
		return (int)Math.signum(keyDown - e.keyDown);
	} // compareTo()

	public String toString() {
		return " t = " + pedalUp + ", s = " + salience;
	} // toString()


} // class Event
