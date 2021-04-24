package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

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
    // TODO Auto-generated method stub
    return null;
  }

}
