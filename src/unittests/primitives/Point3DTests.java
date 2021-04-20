package unittests.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import primitives.Point3D;
import primitives.Vector;
import static primitives.Util.*;

/**
 * Unit tests for primitives.Point3D class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 * 
 */
public class Point3DTests {

	/**
	 * Test method for {@link primitives.Point3D#subtract(Point3D other)}.
	 */
	@Test
	public void testSubtract() {
		Point3D p1 = new Point3D(1, 2, 3);
		Point3D p2 = new Point3D(4, 5, 6);
		assertEquals(new Vector(-3.0, -3.0, -3.0), p1.subtract(p2));
	}

	/**
	 * Test method for {@link primitives.Point3D#add(Point3D other)}.
	 */
	@Test
	public void testAdd() {
		Point3D p1 = new Point3D(1, 2, 3);
		Vector v1 = new Vector(4, 5, 6);
		assertEquals(new Point3D(5.0, 7.0, 9.0), p1.add(v1));
	}

	/**
	 * Test method for {@link primitives.Point3D#distanceSquared(Point3D other)}.
	 */
	@Test
	public void testDistanceSquared() {
		Point3D p1 = new Point3D(1, 2, 3);
		Point3D p2 = new Point3D(4, 5, 6);
		assertTrue(isZero(p1.distanceSquared(p2) - 27));
	}

	/**
	 * Test method for {@link primitives.Point3D#distance(Point3D other)}.
	 */
	@Test
	public void testDistance() {
		Point3D p1 = new Point3D(1, 2, 3);
		Point3D p2 = new Point3D(4, 5, 6);
		assertTrue(isZero(p1.distance(p2) - Math.sqrt(27)));
	}
}
