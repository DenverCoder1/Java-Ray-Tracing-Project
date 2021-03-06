package unittests.geometries;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.Geometries;
import geometries.Plane;
import geometries.Sphere;
import geometries.Intersectable.GeoPoint;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for geometries.Geometries class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class GeometriesTests {

    /**
     * Test method for
     * {@link geometries.Geometries#add(geometries.Intersectable...)}
     */
    @Test
    public void testAdd() {
        Plane plane = new Plane(new Point3D(0, 1, 0), new Point3D(2, 0, 0), new Point3D(0, 2, 0));
        Sphere sphere = new Sphere(new Point3D(0, 0, 1), 1);
        Geometries geometries;
        Geometries expected;
        // ============ Equivalence Partitions Tests ==============
        // add geometry list
        geometries = new Geometries(plane);
        expected = new Geometries(plane, sphere);
        geometries.add(sphere);
        assertEquals("add geometry failed", geometries.getGeometryList(), expected.getGeometryList());

        // =============== Boundary Values Tests ==================
        // add empty list to geometries
        geometries = new Geometries(plane, sphere);
        geometries.add();
        assertEquals("empty list failed", geometries.getGeometryList(), geometries.getGeometryList());

        // add geometries to empty geometries
        geometries = new Geometries();
        expected = new Geometries(plane, sphere);
        geometries.add(plane, sphere);
        assertEquals("add to empty failed", geometries.getGeometryList(), expected.getGeometryList());
    }

    /**
     * Test method for
     * {@link geometries.Geometries#findGeoIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindGeoIntersections() {
        List<GeoPoint> expected;
        List<GeoPoint> actual;
        Plane plane = new Plane(new Point3D(0, 1, 0), new Point3D(2, 0, 0), new Point3D(0, 2, 0));
        Sphere sphere = new Sphere(new Point3D(0, -2, 3), 1);
        // ============ Equivalence Partitions Tests ==============

        // has some intersections
        Ray ray1 = new Ray(new Point3D(0, 2, 1), new Vector(0, 0, -1));
        Geometries geometries = new Geometries(plane, sphere);
        expected = List.of(new GeoPoint(plane, new Point3D(0, 2, 0)));
        actual = geometries.findGeoIntersections(ray1);
        assertEquals("intersect some geometries failed", expected, actual);

        // =============== Boundary Values Tests ==================

        // geometry list is empty
        actual = new Geometries().findGeoIntersections(ray1);
        assertNull("empty geometry list should return null", actual);

        // no geometries are intersected
        Ray ray2 = new Ray(new Point3D(0, 2, 1), new Vector(0, 0, 1));
        actual = geometries.findGeoIntersections(ray2);
        assertNull("no geometries intersected should return null", actual);

        // one geometry is intersected
        expected = List.of(new GeoPoint(plane, new Point3D(0, 2, 0)));
        actual = geometries.findGeoIntersections(ray1);
        assertEquals("one geometry intersected failed", expected, actual);

        // all geometries are intersected
        Ray ray3 = new Ray(new Point3D(0, -2, -1), new Vector(0, 0, 1));
        expected = List.of(new GeoPoint(plane, new Point3D(0, -2, 0)), new GeoPoint(sphere, new Point3D(0, -2, 2)),
                new GeoPoint(sphere, new Point3D(0, -2, 4)));
        actual = geometries.findGeoIntersections(ray3);
        assertEquals("all geometries intersected failed", expected, actual);
    }
}
