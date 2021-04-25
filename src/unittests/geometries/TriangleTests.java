package unittests.geometries;

import static org.junit.Assert.*;

import java.util.List;

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

    /**
     * Test method for
     * {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        List<Point3D> expected;
        List<Point3D> actual;
        Triangle triangle = new Triangle(new Point3D(0, 0, 1), new Point3D(0, 1, 1), new Point3D(1, 0, 1));
        // ============ Equivalence Partitions Tests ==============

        // has intersection
        Ray ray1 = new Ray(new Point3D(0.5, 0.5, 0), new Vector(0, 0, 1));
        expected = List.of(new Point3D(0.25, 0.25, 1));
        actual = triangle.findIntersections(ray1);
        assertEquals("has intersection failed", expected, actual);

        // no intersection
        Ray ray2 = new Ray(new Point3D(2, 2, 0), new Vector(0, 0, 1));
        actual = triangle.findIntersections(ray2);
        assertNull("no intersection should be null", actual);

        // =============== Boundary Values Tests ==================

        // **** group: parallel rays

        // ray has origin on the triangle and is parallel to the triangle
        Ray ray3 = new Ray(new Point3D(0.25, 0.25, 1), new Vector(0, 1, 0));
        actual = triangle.findIntersections(ray3);
        assertNull("inside triangle should be null", actual);

        // ray is parallel and does not intersection
        Ray ray4 = new Ray(new Point3D(0.25, 0.25, 2), new Vector(0, 1, 0));
        actual = triangle.findIntersections(ray4);
        assertNull("parallel ray should be null", actual);

        // **** group: orthogonal rays

        // ray has origin on the triangle and is orthogonal to the triangle
        Ray ray5 = new Ray(new Point3D(0.25, 0.25, 1), new Vector(0, 0, 1));
        actual = triangle.findIntersections(ray5);
        assertNull("orthogonal with only origin on triangle should be null", actual);

        // ray starts before the triangle and is orthogonal to the triangle
        Ray ray6 = new Ray(new Point3D(0.25, 0.25, 0), new Vector(0, 0, 1));
        expected = List.of(new Point3D(0.25, 0.25, 1));
        actual = triangle.findIntersections(ray6);
        assertEquals("orthogonal before failed", expected, actual);

        // ray starts after the triangle and is orthogonal to the triangle
        Ray ray7 = new Ray(new Point3D(0.25, 0.25, 2), new Vector(0, 0, 1));
        actual = triangle.findIntersections(ray7);
        assertNull("orthogonal after should not intersect", actual);

        // ray has origin on triangle and is not orthogonal or parallel
        Ray ray8 = new Ray(new Point3D(0.25, 0.25, 1), new Vector(0, 1, 1));
        actual = triangle.findIntersections(ray8);
        assertNull("Origin on triangle and not orthogonal failed", actual);

        // ray does not start on triangle and is not orthogonal or parallel
        Ray ray9 = new Ray(new Point3D(0, 1, -1), new Vector(0, 1, 1));
        expected = List.of(new Point3D(0.17, 0.17, 1));
        actual = triangle.findIntersections(ray9);
        assertEquals("not orthogonal or parallel failed", expected, actual);

        // ray crosses through the edge of the triangle
        Ray ray10 = new Ray(new Point3D(0.5, 0.5, 0), new Vector(0, 0, 1));
        actual = triangle.findIntersections(ray10);
        assertNull("cross through edge failed", actual);
    }

}
