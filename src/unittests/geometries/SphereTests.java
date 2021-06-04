package unittests.geometries;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.*;
import geometries.Intersectable.GeoPoint;
import primitives.*;

/**
 * Unit tests for geometries.Sphere class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class SphereTests {
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        Sphere sphere = new Sphere(Point3D.ZERO, 5.0);
        assertEquals(new Vector(new Point3D(0, 0, 1)), sphere.getNormal(new Point3D(0, 0, 3)));
    }

    /**
     * Test method for
     * {@link geometries.Sphere#findGeoIntersections(primitives.Ray)}.
     */
    @Test
    public void testfindGeoIntersections() {
        List<GeoPoint> result;
        Sphere sphere = new Sphere(new Point3D(1, 0, 0), 1d);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull("Ray's line out of sphere",
                sphere.findGeoIntersections(new Ray(new Point3D(-1, 0, 0), new Vector(1, 1, 0))));

        // TC02: Ray starts before and crosses the sphere (2 points)
        GeoPoint p1 = new GeoPoint(sphere, new Point3D(0.0651530771650466, 0.355051025721682, 0));
        GeoPoint p2 = new GeoPoint(sphere, new Point3D(1.53484692283495, 0.844948974278318, 0));
        result = sphere.findGeoIntersections(new Ray(new Point3D(-1, 0, 0), new Vector(3, 1, 0)));
        assertEquals("Ray crossing sphere wrong number of points", 2, result.size());
        if (result.get(0).getPoint().getX() > result.get(1).getPoint().getX()) {
            result = List.of(result.get(1), result.get(0));
        }
        assertEquals("Ray crosses sphere", List.of(p1, p2), result);

        // TC03: Ray starts inside the sphere (1 point)
        result = sphere.findGeoIntersections(new Ray(new Point3D(0.75, 0, 0), new Vector(0, 0, 1)));
        assertEquals("Ray in sphere wrong number of points", 1, result.size());

        // TC04: Ray starts after the sphere (0 points)
        result = sphere.findGeoIntersections(new Ray(new Point3D(1, 0, 3), new Vector(0, 0, 1)));
        assertNull("Ray should not intersect", result);

        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        result = sphere.findGeoIntersections(new Ray(new Point3D(2, 0, 0), new Vector(-1, 1, 1)));
        assertEquals("Ray at sphere going in wrong number of points", 1, result.size());

        // TC12: Ray starts at sphere and goes outside (0 points)
        result = sphere.findGeoIntersections(new Ray(new Point3D(2, 0, 0), new Vector(1, -1, -1)));
        assertNull("Ray at sphere going out wrong number of points", result);

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        result = sphere.findGeoIntersections(new Ray(new Point3D(3, 0, 0), new Vector(-1, 0, 0)));
        assertEquals("Ray before sphere wrong number of points", 2, result.size());

        // TC15: Ray starts inside (1 points)
        result = sphere.findGeoIntersections(new Ray(new Point3D(1.5, 0, 0), new Vector(-1, 1, 1)));
        assertEquals("Ray starting inside sphere wrong number of points", 1, result.size());

        // TC16: Ray starts at the center (1 points)
        result = sphere.findGeoIntersections(new Ray(new Point3D(1, 0, 0), new Vector(-1, 1, 1)));
        assertEquals("Ray starting center wrong number of points", 1, result.size());

        // TC18: Ray starts after sphere (0 points)
        result = sphere.findGeoIntersections(new Ray(new Point3D(-1, 0, 0), new Vector(-1, 0, 0)));
        assertNull("Ray after sphere should not intersect", result);

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        result = sphere.findGeoIntersections(new Ray(new Point3D(2, 0, -1), new Vector(0, 0, 1)));
        assertNull("Ray before tangent should not intersect", result);

        // TC20: Ray starts at the tangent point
        result = sphere.findGeoIntersections(new Ray(new Point3D(2, 0, 0), new Vector(0, 0, 1)));
        assertNull("Ray on tangent should not intersect", result);

        // TC21: Ray starts after the tangent point
        result = sphere.findGeoIntersections(new Ray(new Point3D(2, 0, 1), new Vector(0, 0, 1)));
        assertNull("Ray after tangent should not intersect", result);

        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's
        // center line
        result = sphere.findGeoIntersections(new Ray(new Point3D(-1, 0, 0), new Vector(0, 0, 1)));
        assertNull("Ray orthogonal to sphere's diameter should not intersect", result);
    }
}
