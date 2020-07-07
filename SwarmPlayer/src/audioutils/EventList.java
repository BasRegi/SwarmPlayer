/*  
 * 
 * This code was taken from BeatRoot: An interactive beat tracking system
 * Copyright (C) 2001, 2006 by Simon Dixon
 * 
 * 
*/

package audioutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

// Adapted from eventList::readMatchFile in beatroot/src/eventMidi.cpp

// A score/match/midi file is represented as an EventList object,
//  which contains pointers to the head and tail links, and some
//  class-wide parameters. Parameters are class-wide, as it is
//  assumed that the Worm has only one input file at a time.
public class EventList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0;

	public LinkedList<Event> l;

	protected static boolean timingCorrection = false;
	protected static double timingDisplacement = 0;
	protected static int clockUnits = 480;
	protected static int clockRate = 500000;
	protected static double metricalLevel = 0;
	public static final double UNKNOWN = Double.NaN;
	protected static boolean noMelody = false;
	protected static boolean onlyMelody = false;

	public EventList() {
		l = new LinkedList<Event>();
	} // constructor

	public EventList(EventList e) {
		this();
		ListIterator<Event> it = e.listIterator();
		while (it.hasNext())
			add(it.next());
	} // constructor

	public EventList(Event[] e) {
		this();
		for (int i=0; i < e.length; i++)
			add(e[i]);
	} // constructor

	public void add(Event e) {
		l.add(e);
	} // add()

	public void add(EventList ev) {
		l.addAll(ev.l);
	} // add()
	
	public void sort(Comparator<Event> c)
	{
		l.sort(c);
	}

	public void insert(Event newEvent, boolean uniqueTimes) {
		ListIterator<Event> li = l.listIterator();
		while (li.hasNext()) {
			int sgn = newEvent.compareTo(li.next());
			if (sgn < 0) {
				li.previous();
				break;
			} else if (uniqueTimes && (sgn == 0)) {
				li.remove();
				break;
			}
		}
		li.add(newEvent);
	} // insert()

	public ListIterator<Event> listIterator() {
		return l.listIterator();
	} // listIterator()

	public Iterator<Event> iterator() {
		return l.iterator();
	} // iterator()

	public int size() {
		return l.size();
	} // size()
	
	public Event pop()
	{
		return l.pop();
	}

	public Event[] toArray() {
		return toArray(0);
	} // toArray()

	public double[] toOnsetArray() {
		double[] d = new double[l.size()];
		int i = 0;
		for (Iterator<Event> it = l.iterator(); it.hasNext(); i++)
			d[i] = it.next().keyDown;
		return d;
	} // toOnsetArray()

	public Event[] toArray(int match) {
		int count = 0;
		for (Event e : l)
			if ((match == 0) || (e.midiCommand == match))
				count++;
		Event[] a = new Event[count];
		int i = 0;
		for (Event e : l)
			if ((match == 0) || (e.midiCommand == match))
				a[i++] = e;
		return a;
	} // toArray()

	public void writeBinary(String fileName) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
										new FileOutputStream(fileName));
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	} // writeBinary()

	public static EventList readBinary(String fileName) {
		try {
			ObjectInputStream ois = new ObjectInputStream(
										new FileInputStream(fileName));
			EventList e = (EventList) ois.readObject();
			ois.close();
			return e;
		} catch (IOException e) {
			System.err.println(e);
			return null;
		} catch (ClassNotFoundException e) {
			System.err.println(e);
			return null;
		}
	} // readBinary()

	public static void setTimingCorrection(double corr) {
		timingCorrection = corr >= 0;
		timingDisplacement = corr;
	} // setTimingCorrection()

	public void writeBeatsAsText(String fileName) throws Exception {
		PrintStream out = new PrintStream(new File(fileName));
		char separator = '\n';
		if (fileName.endsWith(".csv"))
			separator = ',';
		for (Iterator<Event> it = iterator(); it.hasNext(); ) {
			Event e = it.next();
			out.printf("%5.3f%c", e.keyDown, it.hasNext()? separator: '\n');
		}
		out.close();
	} // writeBeatsAsText()

	public void writeBeatTrackFile(String fileName) throws Exception {
		if (fileName.endsWith(".txt") || fileName.endsWith(".csv"))
			writeBeatsAsText(fileName);
		else {
			PrintStream out = new PrintStream(new File(fileName));
			out.println("MFile 0 1 500");
			out.println("MTrk");
			out.println("     0 Tempo 500000");
			int time = 0;
			for (Iterator<Event> it = iterator(); it.hasNext(); ) {
				Event e = it.next();
				time = (int) Math.round(1000 * e.keyDown);
				out.printf("%6d On   ch=%3d n=%3d v=%3d\n",
							time, e.midiChannel, e.midiPitch, e.midiVelocity);
				out.printf("%6d Off  ch=%3d n=%3d v=%3d\n",
							time, e.midiChannel, e.midiPitch, e.flags);
			}
			out.printf("%6d Meta TrkEnd\nTrkEnd\n", time);
			out.close();
		}
	} // writeBeatTrackFile()

} // class EventList
