package swarm;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import visutils.Vector2D;

/**
 * Class responsible for modelling the swarm simulation
 *
 */
public class BoidWorld extends Observable
{
	/** Default values for weighting coefficients of behaviours */
	private volatile double SEPVAL = 0.5;
	private volatile double ALIGNVAL = 0.4;
	private volatile double COHVAL = 0.2;
	
	/** Values for number of boids, maximum height and width of screen */
	private int numBoids, maxHeight, maxWidth;
	
	private Random rand;
	
	/** List of all boids in world */
	private ArrayList<Boid> boids, temp;
	
	/** Temporary values for weighting coefficients of behaviours */
	private double sepval = 0.1;
	private double alignval = 0.5;
	private double cohval = 0.1;
	
	/** Max speed and force of each boid */
	public double maxBoidSpeed = 10;
	public double maxBoidForce = 1.5;
	
	/**
	 * Constructor
	 * @param numBoids Number of boids to be used in simulation
	 * @param maxHeight Maximum height of screen
	 * @param maxWidth Maximum width of screen
	 */
	public BoidWorld(int numBoids, int maxHeight, int maxWidth)
	{
		this.numBoids = numBoids;
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
		rand = new Random();
		boids = new ArrayList<Boid>();
		temp = new ArrayList<Boid>();
	}
	
	/**
	 * Getter for the list of boids in simualtion
	 * @return ArrayList of all boids
	 */
	public ArrayList<Boid> getWorld()
	{
		return boids;
	}
	
	/** Getter for separation behaviour coefficient */
	public double getSep()
	{
		return sepval;
	}
	
	/** Getter for alignment behaviour coefficient */
	public double getAlign()
	{
		return alignval;
	}
	
	/** Getter for cohesion behaviour coefficient */
	public double getCoh()
	{
		return cohval;
	}
	
	/** Getter for separation behaviour coefficient */
	public double getDefaultSep()
	{
		return SEPVAL;
	}
	
	/** Getter for alignment behaviour coefficient */
	public double getDefaultAlign()
	{
		return ALIGNVAL;
	}
	
	/** Getter for cohesion behaviour coefficient */
	public double getDefaultCoh()
	{
		return COHVAL;
	}
	
	/** Getter for max speed of boids */
	public double getMaxSpeed()
	{
		return maxBoidSpeed;
	}
	
	/** Getter for max force of boids */
	public double getMaxForce()
	{
		return maxBoidForce;
	}
	
	/** Setter for separation behaviour coefficient */
	public synchronized void setSep(double sepval)
	{
		this.sepval = sepval;
		setChanged();
		notifyObservers();
	}
	
	/** Setter for default value of separation behaviour coefficient for swarmmixer */
	public synchronized void setDefaultSep(double sepval)
	{
		this.SEPVAL = sepval;
	}
	
	/** Setter for alignment behaviour coefficient */
	public synchronized void setAlign(double alignval)
	{
		this.alignval = alignval;
		setChanged();
		notifyObservers();
	}
	
	/** Setter for default value of alignment behaviour coefficient for swarmmixer */
	public synchronized void setDefaultAlign(double alignval)
	{
		this.ALIGNVAL = alignval;
	}
	
	/** Setter for cohesion behaviour coefficient */
	public synchronized void setCoh(double cohval)
	{
		this.cohval = cohval;
		setChanged();
		notifyObservers();
	}
	
	/** Setter for default value of cohesion behaviour coefficient for swarmmixer */
	public synchronized void setDefaultCoh(double cohval)
	{
		this.COHVAL = cohval;
	}
	
	/** Setter for max speed of boids */
	public synchronized void setMaxSpeed(double speed)
	{
		maxBoidSpeed = speed;
		setChanged();
		notifyObservers();
	}
	
	/** Setter for max force of boids */
	public synchronized void setMaxForce(double force)
	{
		maxBoidForce = force;
		setChanged();
		notifyObservers();
	}
	
	/** Function to reset temporary coefficients of behaviours */
	public synchronized void resetVals()
	{
		this.sepval = SEPVAL;
		this.alignval = ALIGNVAL;
		this.cohval = COHVAL;
		setChanged();
		notifyObservers();
	}
	
	/** Initialise world model with boids with random non-zero velocities */
	private void init()
	{
		for(int i = 0; i < numBoids; i++)
		{
			double velx = -1 + rand.nextDouble() * 2;
			double vely = -1 + rand.nextDouble() * 2;
			while(velx == 0)
			{
				velx = -1 + rand.nextDouble() * 2;
			}
			while(vely == 0)
			{
				vely = -1 + rand.nextDouble() * 2;
			}
			Vector2D velocity = new Vector2D(velx, vely);
			
			double x = rand.nextDouble() * maxWidth;
			double y = rand.nextDouble() * maxHeight;
			Vector2D position = new Vector2D(x,y);
			
			Boid boid = new Boid(position, velocity, new Vector2D(0,0), 5);
			synchronized(boids)
			{
				boids.add(i, boid);
			}
		}
	}
	
	/** Function to alter position of boids if they move past the edge of the screen */
	private void wraparound(Vector2D position, Boid boid)
	{
		if(position.getX() > maxWidth + boid.getDiameter()/2)
		{
			position.setX(position.getX() - maxWidth);
		}
		else if(position.getX() <= 0 - boid.getDiameter()/2)
		{
			position.setX(position.getX() + maxWidth);
		}
		
		if(position.getY() > maxHeight + boid.getDiameter()/2)
		{
			position.setY(0);
		}
		else if(position.getY() <= 0 - boid.getDiameter()/2)
		{
			position.setY(maxHeight);
		}
	}
	
	/** Function to initialise boid world */
	public void start()
	{
		synchronized(boids)
		{
			boids = new ArrayList<Boid>(numBoids);
		}
		init();
	} 
	
	/** Function to advance the simulation by one time step */
	public void step()
	{
		for(int i = 0; i < boids.size(); i++)
		{
			Boid current = boids.get(i);
			//Filter neighbours 
			ArrayList<Boid> neighbours = current.getNeighbours(boids, maxWidth, maxHeight);
			
			Vector2D steer = new Vector2D(0,0);
			
			if(neighbours.size() > 0)
			{
				//Update velocity of boid
				SteeringBehaviours.flock(steer, neighbours, current, sepval, alignval, cohval, maxHeight, maxWidth, maxBoidSpeed, maxBoidForce);
			}
			
			Vector2D velocity = current.getVelocity().copy();
			velocity.add(steer);
			velocity.limit(maxBoidSpeed);
			
			Vector2D position = current.getPosition().copy();
			position.add(velocity);
			wraparound(position, current);
			
			// Create new boid and place in temporary world list
			temp.add(new Boid(position, velocity, new Vector2D(0,0), current.getDiameter()));
		}
		
		synchronized(boids)
		{
			//Update world with temporary once new boids have all been calculated
			boids = temp;
		}
		
		temp = new ArrayList<Boid>(numBoids);
	}
	
	/** Function to clear boid world */
	public synchronized void clear()
	{
		boids.clear();
		//boids = new ArrayList<Boid>();
	}
}
