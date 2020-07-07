package visualisation;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

import audio.AudioPlayer;
import swarm.BoidWorld;
import visutils.EventTimer;
import visutils.WindowButton;

/**
 * Class to create Toolbar and hold components
 *
 */
public class Toolbar extends JToolBar implements Observer
{
	private static final long serialVersionUID = 0;
	
	/** Buttons */
	private JButton valueEdit, startstop, load, reset;
	
	/** Window */
	private JFrame window;
	
	/** Components */
	private BoidWorld world;
	private VisualisationPanel vispanel;
	private EventTimer timer;
	private AudioPlayer audioPlayer;
	private PlayBar playbar;
	
	/** Flag to determine if SwarmMixer window is open */
	private boolean editWin = false;
	
	/** Flag to determine if visualisation + audio is running */
	private boolean running = false;
	
	/** Flag to determine if file needs to be reloaded */
	private boolean reLoad = false;
	
	/** String to hold parent path of last opened audio file */
	private String lastOpenPath = null;
	
	/** File path of currently open file */
	private String audioFilePath = null;
	
	
	/** 
	 * Constructor
	 * @param window Main Window
	 * @param vispanel Visualisation Panel
	 * @param timer Event Timer
	 * @param world World Model
	 * @param audioPlayer Audio Player
	 * @param playbar Progress Bar and Time Label
	 */
	public Toolbar(JFrame window, VisualisationPanel vispanel, EventTimer timer, BoidWorld world, AudioPlayer audioPlayer, PlayBar playbar)
	{
		super(JToolBar.HORIZONTAL);
		
		this.window = window;
		this.vispanel = vispanel;
		this.timer = timer;
		this.world = world;
		this.audioPlayer = audioPlayer;
		this.playbar = playbar;
		setBackground(Color.BLACK);
		setFloatable(false);
		
		makeMixerButton();
		add(valueEdit);
		
		addSeparator();
		
		makeStartPauseButton();
		add(startstop);
		
		addSeparator();
		
		makeLoadButton();
		add(load);
		
		addSeparator();
		
		makeResetButton();
		add(reset);
		
		
		add(playbar);
	}
	
	/** Function to open file selection window and allow file selection */
	private boolean openFile(JFrame window)
	{
		JFileChooser fileChooser = null;
		
		if(lastOpenPath != null && !lastOpenPath.equals(""))
		{
			fileChooser = new JFileChooser(lastOpenPath);
		}
		else
		{
			fileChooser = new JFileChooser();
		}
		
		FileFilter fileTypeFilter = new FileFilter() {

			@Override
			public boolean accept(File f) 
			{
				if(f.isDirectory())
				{
					return true;
				}
				else
				{
					return f.getName().toLowerCase().endsWith(".wav");
				}
			}

			@Override
			public String getDescription() 
			{
				//. Wav files only accepted
				return ".WAV Files Only";
			}
			
		};
		
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(fileTypeFilter);
		fileChooser.setDialogTitle("Load Audio File");
		
		int fileChoice = fileChooser.showDialog(window, "Select");
		if(fileChoice == JFileChooser.APPROVE_OPTION)
		{
			audioFilePath = fileChooser.getSelectedFile().getAbsolutePath();
			lastOpenPath = fileChooser.getSelectedFile().getParent();
		}
		else if(fileChoice == JFileChooser.CANCEL_OPTION)
		{
			return false;
		}
		return true;
	}
	
	/** Function to make SwarmMixer button */
	private void makeMixerButton()
	{
		valueEdit = new WindowButton("SWARM MIXER");
		valueEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!editWin)
				{
					editWin = true;
					SwarmMixer editWindow = new SwarmMixer(world);
					editWindow.addWindowListener(new WindowAdapter() {
						public void windowClosed(WindowEvent e)
						{
							editWin = false;
						}
					});
				}
			}
			
		});
	}
	
	/** Function to make Start/Pause button */
	private void makeStartPauseButton()
	{
		startstop = new WindowButton("START");
		startstop.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(!running)
						{
							timer.start();
							running = true;
							startstop.setText("PAUSE");
						}
						else
						{
							timer.pause();
							running = false;
							startstop.setText("START");
						}
					}
				});
		startstop.setEnabled(false);
	}
	
	/** Function to make Load button */
	private void makeLoadButton()
	{
		load = new WindowButton("LOAD");
		load.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(!openFile(window) || audioFilePath == null)
						{
							return;
						}
						System.out.println(audioFilePath);
						if(reLoad) 
						{
							timer.stop();
							timer.reset();
							playbar.reset();
						}
						System.out.println(audioFilePath);
						try 
						{
							audioPlayer.load(audioFilePath);
							playbar.initialise(audioPlayer.getAudioClip().getMicrosecondLength());
						} 
						catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) 
						{
							JOptionPane.showMessageDialog(window, "No File Selected", "Error", JOptionPane.WARNING_MESSAGE);
							e1.printStackTrace();
							return;
						}
						
						reLoad = true;
						running = false;
						
						load.setText("RE-LOAD");
						startstop.setText("START");
						
						startstop.setEnabled(true);
						reset.setEnabled(true);
						
						world.start();
						vispanel.repaint();
					}
				});
	}
	
	/** Function to make Reset button */
	private void makeResetButton()
	{
		reset = new WindowButton("RESET");
		reset.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						timer.stop();
						timer.reset();
						playbar.reset();
						world.clear();
						vispanel.repaint();
						
						running = false;
						reLoad = false;
						
						startstop.setText("START");
						load.setText("LOAD");
						
						startstop.setEnabled(false);
						reset.setEnabled(false);
						
						audioFilePath = null;
					}
				});
		reset.setEnabled(false);
	}
	
	/** Function to clear window and reset everything */
	@Override
	public void update(Observable o, Object arg) 
	{
		System.err.println("RESETTING TOOLBAR");
		timer.stop();
		timer.reset();
		world.clear();
		playbar.reset();
		vispanel.repaint();
		
		running = false;
		startstop.setText("START");
		reset.setEnabled(false);
		startstop.setEnabled(false);
		reset.setEnabled(false);
		
		audioFilePath = null;
		
	}

}
