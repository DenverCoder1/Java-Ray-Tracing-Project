package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * Class Plane is the basic class representing a plane of Euclidean geometry in
 * Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Plane implements Geometry {
  protected final Point3D origin;
  protected final Vector normal;

  /**
   * Constructor that takes 3 points and calculates the normal given the 3 points
   * 
   * @param first  the first point
   * @param second the second point
   * @param third  the third point
   */
  public Plane(Point3D first, Point3D second, Point3D third) {
    this.origin = first;
    this.normal = second.subtract(first).crossProduct(third.subtract(first)).normalize();
  }

  /**
   * Constructor that takes a point and the normal
   * 
   * @param origin a point on the plane
   * @param normal the normal vector
   */
  public Plane(Point3D origin, Vector normal) {
    this.origin = origin;
    this.normal = normal;
  }

  /**
   * Getter for the origin
   * 
   * @return the origin
   */
  public Point3D getOrigin() {
    return this.origin;
  }

  /**
   * Getter for the normal
   * 
   * @return the normal
   */
  public Vector getNormal() {
    return this.normal;
  }

  @Override
  public Vector getNormal(Point3D point) {
    return null;
  }

  @Override
  public String toString() {
    return String.format("{ Origin: %s, Normal: %s }", this.origin.toString(), this.normal.toString());
  }

}
