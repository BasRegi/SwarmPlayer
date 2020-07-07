package swarm;

import java.util.ArrayList;
import java.util.stream.Collectors;

import visutils.Vector2D;

/**
 * 
 * Class to hold the vehicle model information about each boid
 * @author basilregi
 *
 */
public class Boid
{
	/** Vectors for position, velocity and acceleration of boid */
	private Vector2D position, velocity, acceleration;
	
	/** Diameter of boid to be drawn on screen */
	private int diameter;
	
	/** Perception radius of each boid */
	private double perceptionRadius = 50;
	
	/**
	 * Constructor
	 * @param position Vector for initial position of boid
	 * @param velocity Vector for initial velocity of boid
	 * @param acceleration Vector for initial acceleration of boid
	 * @param diameter Diameter of boid as an int
	 */
	public Boid(Vector2D position, Vector2D velocity, Vector2D acceleration, int diameter)
	{
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.diameter = diameter;
	}
	
	/** Getter for diameter */
	public int getDiameter()
	{
		return diameter;
	}
	
	/** Getter for perception radius */
	public double getPercRadius()
	{
		return perceptionRadius;
	}
	
	/** Getter for position of the boid */
	public Vector2D getPosition()
	{
		return position;
	}
	
	/** Getter for velocity of boid */
	public Vector2D getVelocity()
	{
		return velocity;
	}
	
	/** Getter for acceleration of boid */
	public Vector2D getAcceleration()
	{
		return acceleration;
	}
	
	/** Setter for position of boid */
	public void setPosition(Vector2D position)
	{
		this.position = position;
	}
	
	/** Setter for velocity of boid */
	public void setVelocity(Vector2D velocity)
	{
		this.velocity = velocity;
	}
	
	/** Setter for acceleration of boid */
	public void setAcceleration(Vector2D acceleration)
	{
		this.acceleration = acceleration;
	}
	
	/** 
	 * Function to get neighbours of the boid from a list of all boids in wraparound world
	 * @param boids ArrayList of all boids
	 * @param maxWidth Maximum width of screen
	 * @param maxHeight Maximum height of screen
	 * @return
	 */
	public ArrayList<Boid> getNeighbours(ArrayList<Boid> boids, int maxWidth, int maxHeight)
	{
		ArrayList<Boid> neighbours = (ArrayList<Boid>) boids.stream()
				.filter(e -> !e.equals(this) 
						&& (this.getPosition().wraparoundDir(e.getPosition(), maxWidth, maxHeight).mag() <= this.getPercRadius()))
				.collect(Collectors.toList());
		return neighbours;
	}
	
	/** toString method to print boid information nicely */
	@Override 
	public String toString()
	{
		return "Boid @" + position + " - Travelling @" + velocity;
	}

}
