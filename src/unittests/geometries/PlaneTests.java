package unittests.geometries;

import static org.junit.Assert.*;

import java.util.List;

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

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        List<Point3D> expected;
        List<Point3D> actual;
        Plane plane = new Plane(new Point3D(0, 1, 0), new Point3D(2, 0, 0), new Point3D(0, 2, 0));
        // ============ Equivalence Partitions Tests ==============

        // has intersection
        Ray ray1 = new Ray(new Point3D(0, 2, 1), new Vector(0, 0, -1));
        expected = List.of(new Point3D(0, 2, 0));
        actual = plane.findIntersections(ray1);
        assertEquals(expected, actual);

        // no intersection
        Ray ray2 = new Ray(new Point3D(0, 2, 1), new Vector(0, 0, 1));
        actual = plane.findIntersections(ray2);
        assertEquals(null, actual);

        // =============== Boundary Values Tests ==================

        // group: parallel rays

        // ray has origin on the plane and is parallel to the plane
        Ray ray3 = new Ray(new Point3D(0, 1, 0), new Vector(0, -1, 0));
        actual = plane.findIntersections(ray3);
        assertEquals(null, actual);

        // ray is parallel and does not intersection
        Ray ray4 = new Ray(new Point3D(0, 1, 1), new Vector(0, 1, 0));
        actual = plane.findIntersections(ray4);
        assertEquals(null, actual);

        // group: orthogonal rays

        // ray has origin on the plane and is orthogonal to the plane
        Ray ray5 = new Ray(new Point3D(0, 1, 0), new Vector(0, 0, 1));
        actual = plane.findIntersections(ray5);
        assertEquals(null, actual);

        // ray has below before the plane and is orthogonal to the plane
        Ray ray6 = new Ray(new Point3D(0, 1, -1), new Vector(0, 0, 1));
        expected = List.of(new Point3D(0, 1, 0));
        actual = plane.findIntersections(ray6);
        assertEquals(expected, actual);

        // ray has above before the plane and is orthogonal to the plane
        Ray ray7 = new Ray(new Point3D(0, 1, 1), new Vector(0, 0, -1));
        expected = List.of(new Point3D(0, 1, 0));
        actual = plane.findIntersections(ray7);
        assertEquals(expected, actual);

        // ray has origin on plane and is not orthogonal or parallel
        Ray ray8 = new Ray(new Point3D(0, 1, 0), new Vector(0, 1, 1));
        actual = plane.findIntersections(ray8);
        assertEquals(null, actual);

        // ray does not start on plane and is not orthogonal or parallel
        Ray ray9 = new Ray(new Point3D(0, 1, -1), new Vector(0, 1, 1));
        expected = List.of(new Point3D(0, 2, 0));
        actual = plane.findIntersections(ray9);
        assertEquals(expected, actual);
    }
}
