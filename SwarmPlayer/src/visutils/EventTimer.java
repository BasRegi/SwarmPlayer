package visutils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import audio.AudioPlayer;
import audio.AudioProcessor;
import audioutils.Event;
import audioutils.EventList;
import swarm.BoidWorld;
import visualisation.PlayBar;
import visualisation.VisualisationPanel;

/**
 * Event Timer class to trigger events in the simulation/audio playback
 *
 */
public class EventTimer 
{
	/** For debugging */
	private final boolean debug = true;
	
	/** Timer to trigger events periodically */
	private Timer timer;
	
	/** Flag to trigger repaint of visualisation */
	private boolean repaint;
	
	/** Flag determine whether audio/visualisation is paued */
	private boolean isPaused = false;
	
	/** Components */
	private AudioPlayer audioPlayer;
	private BoidWorld world;
	private AudioProcessor audioProcessor;
	
	/** Separate thread to handle playback */
	private Thread playThread;
	
	/** Beat times list produced by beat tracking */
	private EventList onsetList;
	
	/** Current onset being reacted to, if any */
	private Event currentOnset;
	
	/** Variable to hold approaching onset */
	private Event nextOnset;
	
	/** Time for swarms to implode for to finish pulse */
	private int decayTime = 0;
	
	/** Constructor */
	public EventTimer(BoidWorld world, VisualisationPanel vispanel, PlayBar playbar, AudioPlayer audioPlayer, AudioProcessor audioProcessor)
	{
		this.world = world;
		this.audioPlayer = audioPlayer;
		repaint = false;
		this.audioProcessor = audioProcessor;
		timer = new Timer(15, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						long time = audioPlayer.getAudioClip().getMicrosecondPosition();
						playbar.update(time);
						updateOnset(time);
						if(repaint)
						{
							// Repaint visualisation
							vispanel.repaint();
							repaint = false;
						}
						else
						{
							if(decayTime > 0)
							{
								//Implosion effect
								world.setSep(0);
								world.setCoh(1);
								decayTime -= 5;
							}
							else if(currentOnset != null)
							{
								//Onset approached - explode
								world.setSep(1);
								world.setCoh(0);
							}
							else
							{
								//Reset to default
								world.resetVals();
							}
							synchronized(world)
							{
								//Update simulation model on step
								world.step();
							}
							repaint = true;
						}
					}
				});
		//Set up new thread for playback
		initialisePlayThread();
	}
	
	/** Function to choose when to update current and next onset */
	private void updateOnset(long time)
	{
		double dtime = (double)time / 1_000_000;
		if(currentOnset != null &&  (currentOnset.pedalUp - dtime) < -0.05)
		{
			System.err.println("Past Onset: " + currentOnset);
			currentOnset = null;
			decayTime = 30;
		}
		
		//If no more onsets, exit
		if(onsetList.size() == 0) return;
		
		double ndiff = (nextOnset.pedalUp - dtime); 
		double diffsq = ndiff * ndiff;
		if(diffsq < 0.07)
		{
			//Start slightly before onset to synchronise explosion
			System.err.println("Approaching Onset: " + nextOnset);
			currentOnset = nextOnset;
			nextOnset = onsetList.pop();
		}
		
	}
	
	/** Start timer to trigger visualisation and audio */
	public void start()
	{
		timer.start();
		if(!isPaused)
		{
			if(debug) System.err.println("START EVENT");
			
			//Tempo to speed mapping
			double speed = ((audioProcessor.getTempo() - 50) / 150.0) * 20;
			if(debug) System.err.println("Speed Set: " + speed);
			world.setMaxSpeed(speed);
			
			//Onset list - filtered
			this.onsetList = audioProcessor.filterMajorOnsets(15);
			
			this.currentOnset = null;
			this.nextOnset = onsetList.pop();
			if(playThread == null) initialisePlayThread();
			//Start playback thread
			playThread.start();
		}
		else
		{
			if(debug) System.err.println("RESUME EVENT");
			isPaused = false;
			audioPlayer.resume();
			playThread.interrupt();
		}
	}
	
	/** Pause audio playback and visualisation */
	public void pause()
	{
		if(debug) System.err.println("PAUSE EVENT");
		isPaused = true;
		timer.stop();
		audioPlayer.pause();
		playThread.interrupt();
	}
	
	/** Stop audio playback and visualisation */
	public void stop()
	{
		if(debug) System.err.println("STOP EVENT");
		audioPlayer.stop();
		if(playThread != null && playThread.isAlive()) 
		{
			playThread.interrupt();
			try 
			{
				playThread.join();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		//Wait till thread is stopped
		while(audioPlayer.getAudioClip().isRunning())
		{
			try 
			{
				Thread.sleep(100);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		timer.stop();
		isPaused = false;
	}
	
	/** Reset audio player and playback thread */
	public void reset()
	{
		if(debug) System.err.println("RESET EVENT");
		audioPlayer.reset();
		playThread = null;
	}
	
	/** Helper function to set up playback thread */
	private void initialisePlayThread()
	{
		playThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				audioPlayer.play();
			}
		});
	}
}
