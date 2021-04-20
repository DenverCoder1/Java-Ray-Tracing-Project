package unittests.geometries;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

/**
 * Unit tests for geometries.Plane class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class PlaneTests {

    /**
     * Test method for
     * {@link geometries.Plane#Plane(primitives.Point3D, primitives.Point3D, primitives.Point3D, primitives.Point3D)}.
     */
    @Test
    public void testConstructor() {
        // =============== Boundary Values Tests ==================

        // Last point = first point
        assertThrows("Last point is on first point", IllegalArgumentException.class,
                () -> new Plane(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 0, 1)));

        // Colocated points
        assertThrows("Last point is on first point", IllegalArgumentException.class,
                () -> new Plane(new Point3D(0, 0, 1), new Point3D(0, 0, 1), new Point3D(0, 1, 0)));
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        Plane plane = new Plane(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals("Bad normal to plane", new Vector(sqrt3, sqrt3, sqrt3), plane.getNormal(new Point3D(0, 0, 1)));
    }
}
