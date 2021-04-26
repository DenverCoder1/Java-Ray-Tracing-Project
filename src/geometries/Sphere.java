package geometries;

import java.util.ArrayList;
import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * Class Sphere is the basic class representing a sphere of Euclidean geometry
 * in Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Sphere implements Geometry {
  protected final Point3D center;
  protected final double radius;

  /**
   * Constructor that takes a center and radius
   * 
   * @param center the center point
   * @param radius the radius
   */
  public Sphere(Point3D center, double radius) {
    this.center = center;
    this.radius = radius;
  }

  /**
   * Getter for the center
   * 
   * @return the center point
   */
  public Point3D getCenter() {
    return this.center;
  }

  /**
   * Getter for the radius
   * 
   * @return the radius
   */
  public double getRadius() {
    return this.radius;
  }

  @Override
  public Vector getNormal(Point3D point) {
    return point.subtract(center).normalize();
  }

  @Override
  public String toString() {
    return String.format("{ Center: %s, Radius: %d }", this.center.toString(), this.radius);
  }

  @Override
  public List<Point3D> findIntersections(Ray ray) {
    // vector from ray origin to center
    Vector u;
    try {
      u = center.subtract(ray.getOrigin());
    } catch (IllegalArgumentException e) {
      // ray starts at origin, so intersects at radius
      return List.of(ray.getPoint(radius));
    }
    // distance to midpoint of solutions
    double tM = ray.getDirection().dotProduct(u);
    // distance from center of circle to tM
    double d = Math.sqrt(u.lengthSquared() - tM * tM);
    // if radius is less than d, ray will miss the sphere
    if (radius < d) {
      return null;
    }
    // get distance to each point from midpoint
    double tH = Math.sqrt(radius * radius - d * d);
    // if distance is 0, it hits a tangent point
    if (isZero(tH)) {
      return null;
    }
    // create null list of intersection points
    List<Point3D> intersections = null;
    // if first point location lies in front of ray
    if (tM - tH > 0) {
      // create list and add first point
      Point3D p1 = ray.getPoint(tM - tH);
      intersections = new ArrayList<>(List.of(p1));
    }
    // if second point location lies in front of ray
    if (tM + tH > 0) {
      // add second point to list
      Point3D p2 = ray.getPoint(tM + tH);
      // if no list is created return a list of second point
      if (intersections == null) {
        return List.of(p2);
      }
      // add p2 to the list of intersections
      intersections.add(p2);
    }
    return intersections;
  }

}
