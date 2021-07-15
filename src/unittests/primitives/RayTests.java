package unittests.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import geometries.*;
import geometries.Intersectable.GeoPoint;
import primitives.*;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for primitives.Ray class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 * 
 */
public class RayTests {

  /**
   * Test {@link primitives.Ray#findClosestGeoPoint(List<GeoPoint> pointsList)}
   */
  @Test
  public void testFindClosestGeoPoint() {
    Ray ray = new Ray(new Point3D(1, 0, 0), new Vector(-1, 0, 3));
    // ============ Equivalence Partitions Tests ==============
    Sphere sphere1 = new Sphere(new Point3D(0, 3, 0), 1d);
    GeoPoint geo1 = new GeoPoint(sphere1, new Point3D(0, 3, 0));
    Triangle triangle = new Triangle(new Point3D(0, -1, 0.39), new Point3D(0, 1, 0.39), new Point3D(2, 0, 0.39));
    GeoPoint geo2 = new GeoPoint(triangle, new Point3D(0.87, 0, 0.39));
    Sphere sphere2 = new Sphere(new Point3D(0, 0, 4), 0.78);
    GeoPoint geo3 = new GeoPoint(sphere2, new Point3D(0, 0, 4));
    List<GeoPoint> geoPointList = Arrays.asList(geo1, geo2, geo3);
    assertEquals("middle of list", geoPointList.get(1), ray.findClosestGeoPoint(geoPointList));
    // =============== Boundary Values Tests ==================
    // empty list
    List<GeoPoint> emptyList = Arrays.asList();
    assertNull("empty list failed", ray.findClosestGeoPoint(emptyList));
    // closest at beginning of list
    List<GeoPoint> listBegin = Arrays.asList(geo2, geo1, geo3);
    assertEquals("beginning of list failed", listBegin.get(0), ray.findClosestGeoPoint(listBegin));
    // closest at end
    List<GeoPoint> listEnd = Arrays.asList(geo1, geo3, geo2);
    assertEquals("end of list failed", listEnd.get(2), ray.findClosestGeoPoint(listEnd));
  }
}