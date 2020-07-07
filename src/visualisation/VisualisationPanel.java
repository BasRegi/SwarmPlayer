package visualisation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import swarm.Boid;
import swarm.BoidWorld;

/** 
 * Visualisation Class which draws the boids periodically
 *
 */
public class VisualisationPanel extends JPanel
{

	private static final long serialVersionUID = 0;
	
	/** For Debugging */
	private static final boolean debug = false;
	
	/** Colour of boids */
	private static Color color = Color.CYAN;
	
	/** World model */
	private BoidWorld world;
	
	/**
	 * Constructor
	 * @param world World model
	 * @param height Height of the screen
	 * @param width Width of the screen
	 * @param numBoids Number of boids in simulation
	 */
	public VisualisationPanel(BoidWorld world, int height, int width, int numBoids)
	{
		this.world  = world;
		this.setPreferredSize(new Dimension(height,width));
		this.setBackground(Color.BLACK);
	}
	
	/** Function to draw boid at given co-ordinates */
	private void drawCenteredCircle(Graphics g, Boid boid)
	{
		g.setColor(color);
		double x = boid.getPosition().getX() - boid.getDiameter()/2;
		double y = boid.getPosition().getY() - boid.getDiameter()/2;
		g.fillOval((int)x, (int)y, boid.getDiameter(), boid.getDiameter());
		
		if(debug)
		{
			//Draws perception radius when debugging
			g.setColor(Color.GREEN);
			double xx = boid.getPosition().getX() - boid.getPercRadius()/2;
			double yy = boid.getPosition().getY() - boid.getPercRadius()/2;
			g.drawOval((int)xx, (int)yy, (int)boid.getPercRadius(), (int)boid.getPercRadius());
		}
	}
	
	/** Paint Component to draw boids on screen */
	@Override
	public void paintComponent(Graphics g)
	{
		ArrayList<Boid> bworld = world.getWorld();
		synchronized(bworld)
		{
			for(Boid boid: bworld)
			{
				drawCenteredCircle(g, boid);
			}
		}
	}
}
