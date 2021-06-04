package unittests.geometries;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.*;
import geometries.Intersectable.GeoPoint;
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
     * {@link geometries.Triangle#findGeoIntersections(primitives.Ray)}.
     */
    @Test
    public void testfindGeoIntersections() {
        List<GeoPoint> expected;
        List<GeoPoint> actual;
        Triangle triangle = new Triangle(new Point3D(0, 0, 1), new Point3D(0, 1, 1), new Point3D(1, 0, 1));
        // ============ Equivalence Partitions Tests ==============

        // has intersection
        Ray ray1 = new Ray(new Point3D(0.25, 0.25, 0), new Vector(0, 0, 1));
        expected = List.of(new GeoPoint(triangle, new Point3D(0.25, 0.25, 1)));
        actual = triangle.findGeoIntersections(ray1);
        assertEquals("has intersection failed", expected, actual);

        // no intersection and reaches plane by side of triangle
        Ray ray2 = new Ray(new Point3D(2, 2, 0), new Vector(0, 0, 1));
        actual = triangle.findGeoIntersections(ray2);
        assertNull("no intersection beside triangle should be null", actual);

        // no intersection and reaches plane by corner
        Ray ray3 = new Ray(new Point3D(2, -0.5, 1), new Vector(0, 0, 1));
        actual = triangle.findGeoIntersections(ray3);
        assertNull("no intersection by corner should be null", actual);

        // =============== Boundary Values Tests ==================

        // **** group: parallel rays

        // ray has origin on the triangle and is parallel to the triangle
        Ray ray4 = new Ray(new Point3D(0.25, 0.25, 1), new Vector(0, 1, 0));
        actual = triangle.findGeoIntersections(ray4);
        assertNull("inside triangle should be null", actual);

        // ray is parallel and does not intersection
        Ray ray5 = new Ray(new Point3D(0.25, 0.25, 2), new Vector(0, 1, 0));
        actual = triangle.findGeoIntersections(ray5);
        assertNull("parallel ray should be null", actual);

        // **** group: orthogonal rays

        // ray has origin on the triangle and is orthogonal to the triangle
        Ray ray6 = new Ray(new Point3D(0.25, 0.25, 1), new Vector(0, 0, 1));
        actual = triangle.findGeoIntersections(ray6);
        assertNull("orthogonal with only origin on triangle should be null", actual);

        // ray starts before the triangle and is orthogonal to the triangle
        Ray ray7 = new Ray(new Point3D(0.25, 0.25, 0), new Vector(0, 0, 1));
        expected = List.of(new GeoPoint(triangle, new Point3D(0.25, 0.25, 1)));
        actual = triangle.findGeoIntersections(ray7);
        assertEquals("orthogonal before failed", expected, actual);

        // ray starts after the triangle and is orthogonal to the triangle
        Ray ray8 = new Ray(new Point3D(0.25, 0.25, 2), new Vector(0, 0, 1));
        actual = triangle.findGeoIntersections(ray8);
        assertNull("orthogonal after should not intersect", actual);

        // ray has origin on triangle and is not orthogonal
        Ray ray9 = new Ray(new Point3D(0.25, 0.25, 1), new Vector(0, 1, 1));
        actual = triangle.findGeoIntersections(ray9);
        assertNull("Origin on triangle and not orthogonal failed", actual);

        // ray does not start on triangle and is not orthogonal or parallel
        Ray ray10 = new Ray(new Point3D(0.25, -1.25, -0.5), new Vector(0, 1, 1));
        expected = List.of(new GeoPoint(triangle, new Point3D(0.25, 0.25, 1)));
        actual = triangle.findGeoIntersections(ray10);
        assertEquals("not orthogonal or parallel failed", expected, actual);

        // ray crosses through the edge of the triangle
        Ray ray11 = new Ray(new Point3D(0.5, 0.5, 0), new Vector(0, 0, 1));
        actual = triangle.findGeoIntersections(ray11);
        assertNull("cross through edge should not intersect", actual);

        // ray crosses through the corner of the triangle
        Ray ray12 = new Ray(new Point3D(0, 1, 0), new Vector(0, 0, 1));
        actual = triangle.findGeoIntersections(ray12);
        assertNull("ray through corner should not intersect", actual);

        // ray through the line of the triangle but outside
        Ray ray13 = new Ray(new Point3D(2, 0, 0), new Vector(0, 0, 1));
        actual = triangle.findGeoIntersections(ray13);
        assertNull("ray crosses along line of triangle should not intersect", actual);
    }

}
