package swarmplayer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import audio.AudioPlayer;
import audio.AudioProcessor;
import swarm.BoidWorld;
import visualisation.PlayBar;
import visualisation.Toolbar;
import visualisation.VisualisationPanel;
import visutils.EventTimer;

/**
 * SwarmPlayer Main Class
 * @author Basil Regi
 *
 */
public class SwarmPlayer 
{

	public static void main(String[] args) 
	{
        //Height and Width of screen
		int height = 725;
		int width = 1275;
		
		JFrame window = new JFrame("SwarmPlayer");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(new Dimension(width, height));
		window.setResizable(false);
		window.getContentPane().setBackground(Color.BLACK);
		
		//Number of boids
		int numBoids = 500;
		
		BoidWorld world = new BoidWorld(numBoids, height, width);
		
		PlayBar playbar = new PlayBar();
		
		VisualisationPanel vispanel = new VisualisationPanel(world, height, width, numBoids);
		
		AudioProcessor audioProcessor = new AudioProcessor();
		
		AudioPlayer audioPlayer = new AudioPlayer(audioProcessor);
		
		EventTimer timer = new EventTimer(world, vispanel, playbar, audioPlayer, audioProcessor);
		
		Toolbar toolbar = new Toolbar(window, vispanel, timer, world, audioPlayer, playbar);
		
		audioPlayer.addObserver(toolbar);
		
		window.add(toolbar, BorderLayout.PAGE_START);
		window.add(vispanel);
		
		window.setVisible(true);

	}
	

}
