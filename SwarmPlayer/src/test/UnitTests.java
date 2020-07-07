package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import swarm.Boid;
import swarm.SteeringBehaviours;
import visutils.Vector2D;

public class UnitTests {


	@Test
	public void neighboursTest1() 
	{
		/** 200 x 200 world
		 *  1 principle boid in centre of screen
		 *  10 other boids in total
		 *  5 within perception radius of 50
		 *  5 boids outside of perception radius
		 *  velocity and acceleration can be ignored
		 */
		ArrayList<Boid> world = new ArrayList<Boid>();
		
		Boid principleBoid = new Boid(new Vector2D(100,100), null, null, 5);
		
		Boid boid1 = new Boid(new Vector2D(110,110), null, null, 5);
		Boid boid2 = new Boid(new Vector2D(140,70), null, null, 5);
		Boid boid3 = new Boid(new Vector2D(90,90), null, null, 5);
		Boid boid4 = new Boid(new Vector2D(130,100), null, null, 5);
		Boid boid5 = new Boid(new Vector2D(100,60), null, null, 5);
		Boid boid6 = new Boid(new Vector2D(180,190), null, null, 5);
		Boid boid7 = new Boid(new Vector2D(10,100), null, null, 5);
		Boid boid8 = new Boid(new Vector2D(50,150), null, null, 5);
		Boid boid9 = new Boid(new Vector2D(20,20), null, null, 5);
		Boid boid10 = new Boid(new Vector2D(100,170), null, null, 5);
		
		world.add(principleBoid);
		world.add(boid1);
		world.add(boid2);
		world.add(boid3);
		world.add(boid4);
		world.add(boid5);
		world.add(boid6);
		world.add(boid7);
		world.add(boid8);
		world.add(boid9);
		world.add(boid10);
		
		ArrayList<Boid> expected = new ArrayList<Boid>();
		expected.add(boid1);
		expected.add(boid2);
		expected.add(boid3);
		expected.add(boid4);
		expected.add(boid5);
		
		ArrayList<Boid> out = principleBoid.getNeighbours(world, 200, 200);
		
		assertEquals(out, expected);
		
	}
	
	@Test 
	public void neighboursTest2()
	{
		/** 200 x 200 world
		 *  1 principle boid on the right edge of screen
		 *  10 other boids in total
		 *  5 within perception radius of 50
		 *  5 boids outside of perception radius
		 *  velocity and acceleration can be ignored
		 */
		ArrayList<Boid> world = new ArrayList<Boid>();
		
		Boid principleBoid = new Boid(new Vector2D(180,100), null, null, 5);
		
		Boid boid1 = new Boid(new Vector2D(170,90), null, null, 5);
		Boid boid2 = new Boid(new Vector2D(180,130), null, null, 5);
		Boid boid3 = new Boid(new Vector2D(190,90), null, null, 5);
		Boid boid4 = new Boid(new Vector2D(10,100), null, null, 5);
		Boid boid5 = new Boid(new Vector2D(5,80), null, null, 5);
		Boid boid6 = new Boid(new Vector2D(180,190), null, null, 5);
		Boid boid7 = new Boid(new Vector2D(20,30), null, null, 5);
		Boid boid8 = new Boid(new Vector2D(100,120), null, null, 5);
		Boid boid9 = new Boid(new Vector2D(180,20), null, null, 5);
		Boid boid10 = new Boid(new Vector2D(40,100), null, null, 5);
		
		world.add(principleBoid);
		world.add(boid1);
		world.add(boid2);
		world.add(boid3);
		world.add(boid4);
		world.add(boid5);
		world.add(boid6);
		world.add(boid7);
		world.add(boid8);
		world.add(boid9);
		world.add(boid10);
		
		ArrayList<Boid> expected = new ArrayList<Boid>();
		expected.add(boid1);
		expected.add(boid2);
		expected.add(boid3);
		expected.add(boid4);
		expected.add(boid5);
		
		ArrayList<Boid> out = principleBoid.getNeighbours(world, 200, 200);
		
		assertEquals(out, expected);
	}
	
	@Test 
	public void neighboursTest3()
	{
		/** 200 x 200 world
		 *  1 principle boid on the bottom right corner of the screen
		 *  10 other boids in total
		 *  5 within perception radius of 50
		 *  5 boids outside of perception radius
		 *  velocity and acceleration can be ignored
		 */
		ArrayList<Boid> world = new ArrayList<Boid>();
		
		Boid principleBoid = new Boid(new Vector2D(190,190), null, null, 5);
		
		Boid boid1 = new Boid(new Vector2D(10,10), null, null, 5);
		Boid boid2 = new Boid(new Vector2D(190,10), null, null, 5);
		Boid boid3 = new Boid(new Vector2D(10,190), null, null, 5);
		Boid boid4 = new Boid(new Vector2D(190,180), null, null, 5);
		Boid boid5 = new Boid(new Vector2D(180,190), null, null, 5);
		Boid boid6 = new Boid(new Vector2D(100,100), null, null, 5);
		Boid boid7 = new Boid(new Vector2D(20,100), null, null, 5);
		Boid boid8 = new Boid(new Vector2D(180,100), null, null, 5);
		Boid boid9 = new Boid(new Vector2D(100,20), null, null, 5);
		Boid boid10 = new Boid(new Vector2D(100,180), null, null, 5);
		
		world.add(principleBoid);
		world.add(boid1);
		world.add(boid2);
		world.add(boid3);
		world.add(boid4);
		world.add(boid5);
		world.add(boid6);
		world.add(boid7);
		world.add(boid8);
		world.add(boid9);
		world.add(boid10);
		
		ArrayList<Boid> expected = new ArrayList<Boid>();
		expected.add(boid1);
		expected.add(boid2);
		expected.add(boid3);
		expected.add(boid4);
		expected.add(boid5);
		
		ArrayList<Boid> out = principleBoid.getNeighbours(world, 200, 200);
		
		assertEquals(out, expected);
	}
	
	@Test
	public void alignTest1()
	{
		/**
		 * 5 neighbouring boids, travelling in various directions
		 * Principle boid not required
		 * Position and acceleration can be ignored; assume all boids are within perception radius
		 */
		
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		Boid boid1 = new Boid(null, new Vector2D(2,2), null, 5);
		Boid boid2 = new Boid(null, new Vector2D(1,2), null, 5);
		Boid boid3 = new Boid(null, new Vector2D(1,1), null, 5);
		Boid boid4 = new Boid(null, new Vector2D(-1,1), null, 5);
		Boid boid5 = new Boid(null, new Vector2D(-1,-2), null, 5);
		
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);

		Vector2D expected = new Vector2D(2.0/5,4.0/5);
		Vector2D out = SteeringBehaviours.align(neighbours);
		
		assertEquals(out, expected);
		
	}
	
	@Test
	public void alignTest2()
	{
		/**
		 * 5 neighbouring boids, travelling in various directions
		 * Principle boid not required
		 * Position and acceleration can be ignored; assume all boids are within perception radius
		 */
		
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		Boid boid1 = new Boid(null, new Vector2D(3,2), null, 5);
		Boid boid2 = new Boid(null, new Vector2D(-2,3), null, 5);
		Boid boid3 = new Boid(null, new Vector2D(5,-1), null, 5);
		Boid boid4 = new Boid(null, new Vector2D(-3,-4), null, 5);
		Boid boid5 = new Boid(null, new Vector2D(4,-5), null, 5);
		
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);

		Vector2D expected = new Vector2D(7.0/5,-5.0/5);
		Vector2D out = SteeringBehaviours.align(neighbours);
		
		assertEquals(out, expected);
		
	}
	
	@Test
	public void alignTest3()
	{
		/**
		 * 5 neighbouring boids, travelling in various directions
		 * Principle boid not required
		 * Position and acceleration can be ignored; assume all boids are within perception radius
		 */
		
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		Boid boid1 = new Boid(null, new Vector2D(4,-2), null, 5);
		Boid boid2 = new Boid(null, new Vector2D(-2,-5), null, 5);
		Boid boid3 = new Boid(null, new Vector2D(6,-3), null, 5);
		Boid boid4 = new Boid(null, new Vector2D(7,-7), null, 5);
		Boid boid5 = new Boid(null, new Vector2D(2,-5), null, 5);
		
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);

		Vector2D expected = new Vector2D(17.0/5,-22.0/5);
		Vector2D out = SteeringBehaviours.align(neighbours);
		
		assertEquals(out, expected);
		
	}
	
	@Test
	public void cohesionTest1()
	{
		/** 200 x 200 world
		 *  1 principle boid in centre of screen
		 *  5 neighbouring boids
		 *  velocity and acceleration can be ignored
		 */
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		Boid principleBoid = new Boid(new Vector2D(100,100), null, null, 5);
		
		Boid boid1 = new Boid(new Vector2D(130,120), null, null, 5);
		Boid boid2 = new Boid(new Vector2D(140,70), null, null, 5);
		Boid boid3 = new Boid(new Vector2D(90,90), null, null, 5);
		Boid boid4 = new Boid(new Vector2D(130,100), null, null, 5);
		Boid boid5 = new Boid(new Vector2D(100,60), null, null, 5);
		
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);
		
		Vector2D expected = new Vector2D(18.0,-12.0);
		Vector2D out = SteeringBehaviours.cohesion(neighbours, principleBoid, 200, 200);
		
		assertEquals(out, expected);
	}
	
	@Test
	public void cohesionTest2()
	{
		/** 200 x 200 world
		 *  1 principle boid on the right edge of screen
		 *  5 neighbouring boids
		 *  velocity and acceleration can be ignored
		 */
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		Boid principleBoid = new Boid(new Vector2D(180,100), null, null, 5);
		
		Boid boid1 = new Boid(new Vector2D(170,90), null, null, 5);
		Boid boid2 = new Boid(new Vector2D(180,130), null, null, 5);
		Boid boid3 = new Boid(new Vector2D(190,90), null, null, 5);
		Boid boid4 = new Boid(new Vector2D(10,100), null, null, 5);
		Boid boid5 = new Boid(new Vector2D(5,80), null, null, 5);
		
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);
		
		Vector2D expected = new Vector2D(11.0,-2.0);
		Vector2D out = SteeringBehaviours.cohesion(neighbours, principleBoid, 200, 200);
		
		assertEquals(out, expected);
	}
	
	@Test
	public void cohesionTest3()
	{
		/** 200 x 200 world
		 *  1 principle boid on the bottom right corner of the screen
		 *  5 neighbouring boids
		 *  velocity and acceleration can be ignored
		 */
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		Boid principleBoid = new Boid(new Vector2D(190,190), null, null, 5);
		
		Boid boid1 = new Boid(new Vector2D(10,10), null, null, 5);
		Boid boid2 = new Boid(new Vector2D(190,10), null, null, 5);
		Boid boid3 = new Boid(new Vector2D(10,190), null, null, 5);
		Boid boid4 = new Boid(new Vector2D(190,180), null, null, 5);
		Boid boid5 = new Boid(new Vector2D(180,190), null, null, 5);
		
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);
		
		Vector2D expected = new Vector2D(6.0,6.0);
		Vector2D out = SteeringBehaviours.cohesion(neighbours, principleBoid, 200, 200);
		
		assertEquals(out, expected);
	}
	
	@Test
	public void separationTest1()
	{
		/** 200 x 200 world
		 *  1 principle boid in centre of screen
		 *  5 neighbouring boids
		 *  velocity and acceleration can be ignored
		 */
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		Boid principleBoid = new Boid(new Vector2D(100,100), null, null, 5);
		
		Boid boid1 = new Boid(new Vector2D(130,120), null, null, 5);
		Boid boid2 = new Boid(new Vector2D(140,70), null, null, 5);
		Boid boid3 = new Boid(new Vector2D(100,90), null, null, 5);
		Boid boid4 = new Boid(new Vector2D(130,100), null, null, 5);
		Boid boid5 = new Boid(new Vector2D(100,60), null, null, 5);
		
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);
		
		Vector2D expected = new Vector2D(-20.0,12.0);
		Vector2D out = SteeringBehaviours.separation(neighbours, principleBoid, 200, 200);
		
		assertEquals(out, expected);
	}
	
	@Test
	public void separationTest2()
	{
		/** 200 x 200 world
		 *  1 principle boid on the right edge of screen
		 *  5 neighbouring boids
		 *  velocity and acceleration can be ignored
		 */
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		Boid principleBoid = new Boid(new Vector2D(180,100), null, null, 5);
		
		Boid boid1 = new Boid(new Vector2D(170,90), null, null, 5);
		Boid boid2 = new Boid(new Vector2D(180,130), null, null, 5);
		Boid boid3 = new Boid(new Vector2D(190,90), null, null, 5);
		Boid boid4 = new Boid(new Vector2D(10,100), null, null, 5);
		Boid boid5 = new Boid(new Vector2D(5,80), null, null, 5);
		
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);
		
		Vector2D expected = new Vector2D(-11.0,2.0);
		Vector2D out = SteeringBehaviours.separation(neighbours, principleBoid, 200, 200);
		
		assertEquals(out, expected);
	}
	
	@Test
	public void separationTest3()
	{
		/** 200 x 200 world
		 *  1 principle boid on the bottom right corner of the screen
		 *  5 neighbouring boids
		 *  velocity and acceleration can be ignored
		 */
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		
		Boid principleBoid = new Boid(new Vector2D(190,190), null, null, 5);
		
		Boid boid1 = new Boid(new Vector2D(10,10), null, null, 5);
		Boid boid2 = new Boid(new Vector2D(190,10), null, null, 5);
		Boid boid3 = new Boid(new Vector2D(10,190), null, null, 5);
		Boid boid4 = new Boid(new Vector2D(190,180), null, null, 5);
		Boid boid5 = new Boid(new Vector2D(180,190), null, null, 5);
		
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);
		
		Vector2D expected = new Vector2D(-6.0,-6.0);
		Vector2D out = SteeringBehaviours.separation(neighbours, principleBoid, 200, 200);
		
		assertEquals(out, expected);
	}
	
	@Test
	public void swarmingTest()
	{
		/** 200 x 200 world
		 *  1 principle boid in centre of screen
		 *  5 neighbouring boids travelling in various directions at various speeds
		 */
		
		Boid principleBoid = new Boid(new Vector2D(100,100), new Vector2D(1,-2), null, 5);
		
		ArrayList<Boid> neighbours = new ArrayList<Boid>();
		Boid boid1 = new Boid(new Vector2D(130,120), new Vector2D(2,2), null, 5);
		Boid boid2 = new Boid(new Vector2D(140,70), new Vector2D(1,2), null, 5);
		Boid boid3 = new Boid(new Vector2D(100,90), new Vector2D(1,1), null, 5);
		Boid boid4 = new Boid(new Vector2D(130,100), new Vector2D(-1,1), null, 5);
		Boid boid5 = new Boid(new Vector2D(100,60), new Vector2D(-1,-2), null, 5);
		neighbours.add(boid1);
		neighbours.add(boid2);
		neighbours.add(boid3);
		neighbours.add(boid4);
		neighbours.add(boid5);
		
		Vector2D expected = new Vector2D(0.14321662565180215,0.8689774119312277);
		
		Vector2D out = new Vector2D(0,0);
		SteeringBehaviours.flock(out, neighbours, principleBoid, 0.3, 0.5, 0.2, 200, 200, 10, 1.5);
		

		assertEquals(out, expected);
	}

}
