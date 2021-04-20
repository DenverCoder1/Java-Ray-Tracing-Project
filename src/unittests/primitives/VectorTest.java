package unittests.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import primitives.Point3D;
import primitives.Vector;
import static primitives.Util.*;

/**
 * Unit tests for primitives.Vector class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 * 
 */
public class VectorTest {

	/**
	 * Test method for
	 * {@link primitives.Vector#Vector(double x, double y, double z)}
	 * {@link primitives.Vector#Vector()}
	 */
	@Test
	public void testConstructor() {
		assertThrows("head vector as doubles being zero does not throw an exception", IllegalArgumentException.class,
				() -> new Vector(0, 0, 0));
		assertThrows("head vector as point being zero does not throw an exception", IllegalArgumentException.class,
				() -> new Vector(new Point3D(0, 0, 0)));
	}

	/**
	 * Test method for {@link primitives.Vector#subtract(Vector other)}
	 */
	@Test
	public void testSubtract() {
		// ============ Equivalence Partitions Tests ==============

		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(4, 5, 6);
		assertEquals(new Vector(-3.0, -3.0, -3.0), v1.subtract(v2));

		// =============== Boundary Values Tests ==================

		assertThrows("subtract to get zero vector does not throw an exception", IllegalArgumentException.class,
				() -> v1.subtract(v1));
	}

	/**
	 * Test method for {@link primitives.Vector#add(Vector other)}
	 */
	@Test
	public void testAdd() {
		// ============ Equivalence Partitions Tests ==============

		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(4, 5, 6);
		assertEquals(new Vector(5.0, 7.0, 9.0), v1.add(v2));

		// =============== Boundary Values Tests ==================

		Vector v3 = new Vector(-1, -2, -3);
		assertThrows("add to get zero vector does not throw an exception", IllegalArgumentException.class,
				() -> v1.add(v3));
	}

	/**
	 * Test method for {@link primitives.Vector#scale(double scalar)}
	 */
	@Test
	public void testScale() {
		// ============ Equivalence Partitions Tests ==============

		Vector v1 = new Vector(1, 2, 3);
		assertEquals(new Vector(3.0, 6.0, 9.0), v1.scale(3.0));

		// =============== Boundary Values Tests ==================

		assertThrows("scale to get zero vector does not throw an exception", IllegalArgumentException.class,
				() -> v1.scale(0));
	}

	/**
	 * Test method for {@link primitives.Vector#dotProduct(Vector other)}
	 */
	@Test
	public void testDotProduct() {
		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(4, 5, 6);
		assertTrue(isZero(v1.dotProduct(v2) - 32));
	}

	/**
	 * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
	 */
	@Test
	public void testCrossProduct() {
		Vector v1 = new Vector(1, 2, 3);

		// ============ Equivalence Partitions Tests ==============
		Vector v2 = new Vector(0, 3, -2);
		Vector vr = v1.crossProduct(v2);

		// TC01: Test that length of cross-product is proper (orthogonal vectors taken
		// for simplicity)
		assertEquals("crossProduct() wrong result length", v1.length() * v2.length(), vr.length(), 0.00001);

		// TC02: Test cross-product result orthogonality to its operands
		assertTrue("crossProduct() result is not orthogonal to 1st operand", isZero(vr.dotProduct(v1)));
		assertTrue("crossProduct() result is not orthogonal to 2nd operand", isZero(vr.dotProduct(v2)));

		// =============== Boundary Values Tests ==================

		// TC11: test zero vector from cross-productof co-lined vectors
		Vector v3 = new Vector(-2, -4, -6);
		assertThrows("crossProduct() for parallel vectors does not throw an exception", IllegalArgumentException.class,
				() -> v1.crossProduct(v3));
	}

	/**
	 * Test method for {@link primitives.Vector#lengthSquared()}.
	 */
	@Test
	public void testLengthSquared() {
		Vector v1 = new Vector(1, 2, 3);
		assertTrue(isZero(v1.lengthSquared() - 14));
	}

	/**
	 * Test method for {@link primitives.Vector#length())}.
	 */
	@Test
	public void testLength() {
		Vector v1 = new Vector(1, 2, 3);
		assertTrue(isZero(v1.length() - Math.sqrt(14)));
	}

	/**
	 * Test method for {@link primitives.Vector#normalize())}.
	 */
	@Test
	public void testNormalize() {
		Vector v1 = new Vector(3, 0, 0);
		assertEquals(new Vector(1, 0, 0), v1.normalize());
	}
}
