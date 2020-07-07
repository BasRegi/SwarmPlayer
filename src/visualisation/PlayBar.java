package visualisation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import visutils.SliderUI;

/**
 * Class to hold the progress bar and time labels in GUI
 *
 */
public class PlayBar extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JSlider playSlider;
	private JLabel timer;
	private long totalTime;
	
	/** Constructor */
	public PlayBar()
	{
		this.setBackground(Color.BLACK);
		this.playSlider = new JSlider();
		playSlider.setUI(new SliderUI(playSlider));
		playSlider.setBackground(Color.BLACK);
		playSlider.setValue(0);
		playSlider.setMajorTickSpacing(1);
		playSlider.setEnabled(false);
		playSlider.setPreferredSize(new Dimension(500,20));

	
		this.timer = new JLabel("00:00:00/00:00:00");
		timer.setForeground(Color.WHITE);
		
		add(playSlider, BorderLayout.EAST);
		add(timer, BorderLayout.EAST);
	}
	
	/** Initialise with time of audio file */
	public void initialise(long totaltime)
	{
		this.totalTime = totaltime;
		long seconds = (totaltime / 1_000_000);
		playSlider.setMaximum((int)seconds);
		playSlider.setValue(0);
		timer.setText("00:00:00/" + formatTime(totalTime));
	}
	
	/** Update time label and progress bar */
	public void update(long currentTime)
	{
		int seconds = (int)(currentTime / 1_000_000);
		playSlider.setValue(seconds);
		timer.setText("" + formatTime(currentTime) + "/" + formatTime(totalTime));
	}
	
	/** Reset progress bar and time label */
	public void reset()
	{
		playSlider.setValue(0);
		timer.setText("00:00:00/00:00:00");
	}
	
	/** Private function to format audio time from milliseconds into correct format */
	private String formatTime(long time)
	{
		String t = "";
		long miliseconds = (time / 10000) % 100;
		long seconds = (time / 1_000_000) % 60;
		long minutes = (time / (60 * 1_000_000)) % 60;
		t += String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + ":" + String.format("%02d", miliseconds);
		return t;
	}
}
