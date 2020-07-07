package audio;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import audioutils.BeatTrack;

/**
 * Class responsible for controlling the audio playback within the application
 * Uses a Clip to store the audio file and various booleans to control the playback.
 * @author Basil Regi
 *
 */
public class AudioPlayer extends Observable implements LineListener
{
	/** Flag for debugging purposes */
	private final boolean debug = true;
	
	/** Flag to determine when the audio playback is finished */
	private volatile boolean isFinished = false;
	
	/** Flag to determine whether the audio playback has started */
	private volatile boolean isPlaying = false;
	
	/** Flag to determine if the audio playback has been paused */
	private volatile boolean isPaused = false;
	
	/** Clip to store the audio file to be played */
	private Clip audioClip;
	
	/** AudioProcessor required to carry out beat tracking once a file is loaded in */
	private AudioProcessor audioProcessor;
	
	/**
	 * Constructor for AudioPlayer: Only created once and then re-used by changing the file loaded in
	 * @param audioProcessor The Audio Processor required for beat tracking
	 */
	public AudioPlayer(AudioProcessor audioProcessor)
	{
		this.audioProcessor = audioProcessor;
	}
	
	/**
	 * Function to load in the audio file ready for play back. Also triggers the beat tracking in the AudioProcessor
	 * @param filePath String path to the audio file
	 * @return Returns True if file is loaded successfully
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public boolean load(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		File audioFile = new File(filePath);
		
		//Trigger Beat Tracking
		audioProcessor.setInputFile(filePath);
		audioProcessor.processFile();
		BeatTrack.beatTrack(audioProcessor.onsetList, audioProcessor); 
		
		AudioInputStream audioInput =  AudioSystem.getAudioInputStream(audioFile);
		
		AudioFormat audioFormat = audioInput.getFormat();
		
		DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
		
		audioClip = (Clip)AudioSystem.getLine(info);
		
		// LineListener to listen for when the track completes
		audioClip.addLineListener(this);
		
		audioClip.open(audioInput);
		
		return true;
	}
	
	/**
	 * Function to start the playback of the audio file. 
	 * Uses a while loop to repeatedly check the boolean flags to determine status of playback
	 * Exits loop when isFinished is set to True
	 */
	public void play()
	{
		isPlaying = true;
		if(debug) System.err.println("AUDIO PLAYER STARTED");
		
		//Start Playback
		audioClip.start();
		
		// Playback loop
		while(!isFinished)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// Case when audio player is stopped
				if(!isPlaying)
				{
					if(debug) System.err.println("AUDIO PLAYER STOPPED");
					audioClip.stop();
					break;
				}
				// Case when audio player is paused
				if(isPaused)
				{
					if(debug) System.err.println("AUDIO PLAYER PAUSED");
					audioClip.stop();
				}
				// Case when audio player is resumed
				else
				{
					if(debug) System.err.println("AUDIO PLAYER RESUMED");
					audioClip.start();
				}
			}
		}
		if(debug) System.err.println("EXITING PLAY LOOP");
		audioClip.close();
	}
	
	/**
	 * Function to stop the audio playback
	 */
	public void stop()
	{
		isPlaying = false;
	}
	
	/**
	 * Function to pause the audio playback
	 */
	public void pause()
	{
		isPaused = true;
	}
	
	/**
	 * Function to resume the audio playback
	 */
	public void resume()
	{
		isPaused = false;
	}

	/**
	 * LineListener method to update the flag when the track is finished 
	 */
	@Override
	public void update(LineEvent le) 
	{
		LineEvent.Type eventType = le.getType();
		if(eventType == LineEvent.Type.STOP)
		{
			if(!isPlaying || !isPaused)
			{
				if(debug) System.err.println("AUDIO PLAYER FINISHED");
				isFinished = true;
			}
		}
		else if(eventType == LineEvent.Type.CLOSE)
		{
			setChanged();
			this.notifyObservers();
		}
	}
	
	/**
	 * Function to reset all flags
	 */
	public void reset()
	{
		isFinished = false;
		isPlaying = false;
		isPaused = false;
	}
	
	/**
	 * Function to get the Clip of the audio
	 * @return Audio file as a Clip
	 */
	public Clip getAudioClip()
	{
		return audioClip;
	}
}
