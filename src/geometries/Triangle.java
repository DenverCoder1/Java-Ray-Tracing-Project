package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Class Triangle is the basic class representing a triangle of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Triangle extends Polygon {
  /**
   * Constructor that takes 3 points and calls the Polygon constructor
   * 
   * @param first  the first point
   * @param second the second point
   * @param third  the third point
   * 
   * @throws IllegalArgumentException in any case Polygon does
   */
  public Triangle(Point3D first, Point3D second, Point3D third) {
    super(first, second, third);
  }

  @Override
  public Vector getNormal(Point3D point) {
    return plane.getNormal();
  }

  @Override
  public List<GeoPoint> findGeoIntersections(Ray ray) {
    Plane plane = new Plane(vertices.get(0), vertices.get(1), vertices.get(2));
    List<GeoPoint> intersections = plane.findGeoIntersections(ray);
    if (intersections == null) {
      return null;
    }
    GeoPoint intersection = intersections.get(0);
    Vector v1 = vertices.get(0).subtract(ray.getOrigin());
    Vector v2 = vertices.get(1).subtract(ray.getOrigin());
    Vector v3 = vertices.get(2).subtract(ray.getOrigin());
    Vector n1 = v1.crossProduct(v2).normalize();
    Vector n2 = v2.crossProduct(v3).normalize();
    Vector n3 = v3.crossProduct(v1).normalize();
    double d1 = intersection.getPoint().subtract(ray.getOrigin()).dotProduct(n1);
    double d2 = intersection.getPoint().subtract(ray.getOrigin()).dotProduct(n2);
    double d3 = intersection.getPoint().subtract(ray.getOrigin()).dotProduct(n3);
    if ((d1 > 0 && d2 > 0 && d3 > 0) || (d1 < 0 && d2 < 0 && d3 < 0)) {
      return List.of(intersection);
    }
    return null;
  }
}
