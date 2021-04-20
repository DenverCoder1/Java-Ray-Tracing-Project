package unittests.geometries;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

/**
 * Unit tests for geometries.Triangle class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class TriangleTests {

    /**
     * Test method for
     * {@link geometries.Triangle#Triangle(primitives.Point3D, primitives.Point3D, primitives.Point3D, primitives.Point3D)}.
     */
    @Test
    public void testConstructor() {
        // =============== Boundary Values Tests ==================

        // Last point = first point
        assertThrows("Last point is on first point", IllegalArgumentException.class,
                () -> new Triangle(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 0, 1)));

        // Colocated points
        assertThrows("Last point is on first point", IllegalArgumentException.class,
                () -> new Triangle(new Point3D(0, 0, 1), new Point3D(0, 0, 1), new Point3D(0, 1, 0)));

    }

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        Triangle triangle = new Triangle(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals("Bad normal to triangle", new Vector(sqrt3, sqrt3, sqrt3),
                triangle.getNormal(new Point3D(0, 0, 1)));
    }

}
