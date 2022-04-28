package edu.nmsu.cs.circles;

/***
 * Example JUnit testing class for Circle1 (and Circle)
 *
 * - must have your classpath set to include the JUnit jarfiles - to run the test do: java
 * org.junit.runner.JUnitCore Circle1Test - note that the commented out main is another way to run
 * tests - note that normally you would not have print statements in a JUnit testing class; they are
 * here just so you see what is happening. You should not have them in your test cases.
 ***/

import org.junit.*;

public class Circle1Test
{
	// Data you need for each test case
	private Circle1 circle1;

	//
	// Stuff you want to do before each test case
	//
	@Before
	public void setup()
	{
		System.out.println("\nTest starting...");
		circle1 = new Circle1(1, 2, 3);
	}

	//
	// Stuff you want to do after each test case
	//
	@After
	public void teardown()
	{
		System.out.println("\nTest finished.");
	}

	//
	// Test a simple positive move
	//
	@Test
	public void simpleMove()
	{
		Point p;
		System.out.println("Running test simpleMove.");
		p = circle1.moveBy(1, 1);
		Assert.assertTrue(p.x == 2 && p.y == 3);
	}

	//
	// Test a simple negative move
	//
	@Test
	public void simpleMoveNeg()
	{
		Point p;
		System.out.println("Running test simpleMoveNeg.");
		p = circle1.moveBy(-1, -1);
		Assert.assertTrue(p.x == 0 && p.y == 1);
	}

	
	/**
	 * Tests case for when two circles are not touching.
	 */
	@Test
	public void noIntersection() {
		System.out.println("Running test: noIntersection");
		
		System.out.println("noIntersection -- next to each other");
		Circle1 circleA = new Circle1(0,50,10); 
		Circle1 circleB = new Circle1(0,0,5);
		Assert.assertFalse(circleA.intersects(circleB));
		Assert.assertFalse(circleB.intersects(circleA));
		
		
		System.out.println("noIntersection -- extremely close to each other");
		circleA = new Circle1(1,10,2.9); 
		circleB = new Circle1(1,5,2);
		Assert.assertFalse(circleA.intersects(circleB));
		Assert.assertFalse(circleB.intersects(circleA));
	}
	
	
	/**
	 * Tests case for perfect intersection
	 */
	@Test
	public void completeOverlap() {
		System.out.println("Running test: completeOverlap");
		
		System.out.println("completeOverlap -- on top of each other at origin");
		Circle1 circleA = new Circle1(0,0,10); 
		Circle1 circleB = new Circle1(0,0,10);
		Assert.assertTrue(circleA.intersects(circleB));
		Assert.assertTrue(circleB.intersects(circleA));
	}
	
	/**
	 * Test for the case the the circles are touching at only one point
	 */
	@Test
	public void onePointOverlap() {
		System.out.println("Running test: onePointOverlap");
		
		System.out.println("onePointOverlap -- touch on x-axis");
		Circle1 circleA = new Circle1(0,0,10); 
		Circle1 circleB = new Circle1(20,0,10);
		Assert.assertTrue(circleA.intersects(circleB));
		Assert.assertTrue(circleB.intersects(circleA));
		
		System.out.println("onePointOverlap -- touch on y-axis");
		circleA = new Circle1(0,0,10); 
		circleB = new Circle1(0,20,10);
		Assert.assertTrue(circleA.intersects(circleB));
		Assert.assertTrue(circleB.intersects(circleA));
	}
	
	/**
	 * Test case for when the two circles are touching and intersecting at more than one point
	 */
	@Test
	public void multiplePointOverlap() {
		System.out.println("Running test: multiplePointOverlap");
		
		System.out.println("multiplePointOverlap -- circle origins on x-axis");
		Circle1 circleA = new Circle1(0,0,10); 
		Circle1 circleB = new Circle1(10,0,10);
		Assert.assertTrue(circleA.intersects(circleB));
		Assert.assertTrue(circleB.intersects(circleA));
		
		System.out.println("multiplePointOverlap -- circle origins on y-axis");
		circleA = new Circle1(0,0,10); 
		circleB = new Circle1(0,10,10);
		Assert.assertTrue(circleA.intersects(circleB));
		Assert.assertTrue(circleB.intersects(circleA));
	}
	
	/**
	 * Test case for when the two circles where one is inside the other circle. 
	 * So one of the circles has to be smaller than the other
	 */
	@Test
	public void smallerCircleInside() {
		System.out.println("Running test: smallerCircleInside()");
		
		System.out.println("smallerCircleInside() -- both circles at origin");
		Circle1 circleA = new Circle1(0,0,10); 
		Circle1 circleB = new Circle1(0,0,5);
		Assert.assertTrue(circleA.intersects(circleB));
		Assert.assertTrue(circleB.intersects(circleA));
	}
	
	
	/***
	 * NOT USED public static void main(String args[]) { try { org.junit.runner.JUnitCore.runClasses(
	 * java.lang.Class.forName("Circle1Test")); } catch (Exception e) { System.out.println("Exception:
	 * " + e); } }
	 ***/

}
