package swarm;
import java.util.ArrayList;

import visutils.Vector2D;

/**
 * Factory class of steering behaviours
 *
 */
public class SteeringBehaviours 
{
	/**
	 * Alignment Behaviour
	 * @param neighbours List of neighbours to find average velocity of
	 * @return Vector representing desired steering force of behaviour
	 */
	public static Vector2D align(ArrayList<Boid> neighbours)
	{
		Vector2D steer = new Vector2D(0,0);
		for(int i = 0; i < neighbours.size(); i++)
		{
			steer.add(neighbours.get(i).getVelocity());
		}
		steer.div(neighbours.size());
		return steer;
	}
	
	/**
	 * Cohesion Behaviour
	 * @param neighbours List of neighbours to find average position of
	 * @param boid Principle boid
	 * @param maxHeight Max height of screen
	 * @param maxWidth Max width of screen
	 * @return Vector representing desired steering force of behaviour
	 */
	public static Vector2D cohesion(ArrayList<Boid> neighbours, Boid boid, int maxHeight, int maxWidth)
	{
		Vector2D steer = new Vector2D(0,0);
		for(int i = 0; i < neighbours.size(); i++)
		{
			steer.add(boid.getPosition().wraparoundPos(neighbours.get(i).getPosition(), maxHeight, maxWidth));
		}
		steer.div(neighbours.size());
		steer = steer.wraparoundDir(boid.getPosition(), maxHeight, maxWidth);
		steer.inverseDir();
		return steer;
	}
	
	/**
	 * Separation Behaviour
	 * @param neighbours List of neighbours to separate away from
	 * @param boid Principle boid
	 * @param maxHeight Max height of screen
	 * @param maxWidth Max width of screen
	 * @return Vector representing desired steering force of behaviour
	 */
	public static Vector2D separation(ArrayList<Boid> neighbours, Boid boid, int maxHeight, int maxWidth)
	{
		Vector2D steer = new Vector2D(0,0);
		Vector2D avoiddir;
		for(int i = 0; i < neighbours.size(); i++)
		{
			avoiddir = boid.getPosition().wraparoundDir(neighbours.get(i).getPosition(), maxHeight, maxWidth);
			avoiddir.inverseDir();
			//avoiddir.normalise();
			steer.add(avoiddir);
		}
		steer.div(neighbours.size());
		return steer;
	}
	
	/**
	 * Flocking/Swarming
	 * @param steer Vector to be updated with desired steering force of behaviour
	 * @param neighbours List of neighbours of principle boid
	 * @param current Principle boid
	 * @param separationval Separation behaviour coefficient
	 * @param alignval Alignment behaviour coefficient
	 * @param cohesionval Cohesion behaviour coefficient
	 * @param maxHeight Max height of screen
	 * @param maxWidth Max width of screen
	 * @param maxSpeed Max speed of boids
	 * @param maxForce Max force of boids
	 */
	public static void flock(Vector2D steer, ArrayList<Boid> neighbours, Boid current, 
			double separationval, double alignval, double cohesionval, int maxHeight, int maxWidth, 
			double maxSpeed, double maxForce)
	{
		Vector2D separation = separation(neighbours, current, maxHeight, maxWidth);
		separation.setMag(maxSpeed);
		separation.sub(current.getVelocity());
		separation.limit(maxForce);
		separation.mult(separationval);
		
		Vector2D align = align(neighbours);
		align.setMag(maxSpeed);
		align.sub(current.getVelocity());
		align.limit(maxForce);
		align.mult(alignval);
		
		Vector2D cohesion = cohesion(neighbours, current, maxHeight, maxWidth);
		cohesion.setMag(maxSpeed);
		cohesion.sub(current.getVelocity());
		cohesion.limit(maxForce);
		cohesion.mult(cohesionval);
		
		steer.add(separation);
		steer.add(align);
		steer.add(cohesion);
	}
}
