package primitives;

import java.util.List;
import java.util.Objects;

import geometries.Intersectable.GeoPoint;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in
 * Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Ray {

  private static final double DELTA = 0.1;

  /**
   * ray origin
   */
  public final Point3D origin;

  /**
   * ray direction vector
   */
  public final Vector direction;

  /**
   * Constructor that takes the origin and direction
   * 
   * @param origin    the origin point
   * @param direction the direction vector
   */
  public Ray(Point3D origin, Vector direction) {
    this.origin = origin;
    this.direction = direction.normalized();
  }

  /**
   * constructor that moves point by a constant delta in a certain direction
   * 
   * @param origin    the origin point
   * @param direction the direction vector
   * @param normal    normal vector for displacement direction
   */
  public Ray(Point3D point, Vector direction, Vector normal) {
    this.direction = direction.normalized();
    double dotProduct = normal.dotProduct(this.direction);
    Vector delta = normal.scale(dotProduct >= 0 ? DELTA : -DELTA);
    this.origin = point.add(delta);
  }

  /**
   * Get origin
   * 
   * @return origin
   */
  public Point3D getOrigin() {
    return this.origin;
  }

  /**
   * Get direction
   * 
   * @return direction
   */
  public Vector getDirection() {
    return this.direction;
  }

  /**
   * Get the point after scaling the direction vector from the origin
   * 
   * @param t The scalar to multiply by
   * @return The point scaled by the direction vector
   */
  public Point3D getPoint(double t) {
    return origin.add(direction.scale(t));
  }

  /**
   * find the closest Point to Ray origin
   * 
   * @param pointsList intersections point List
   * @return closest point
   */
  public GeoPoint findClosestGeoPoint(List<GeoPoint> pointsList) {
    GeoPoint result = null;
    double closestDistance = Double.MAX_VALUE;
    if (pointsList == null) {
      return null;
    }
    for (GeoPoint p : pointsList) {
      double temp = p.point.distanceSquared(origin);
      if (temp < closestDistance) {
        closestDistance = temp;
        result = p;
      }
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Ray)) {
      return false;
    }
    Ray ray = (Ray) o;
    return Objects.equals(origin, ray.origin) && Objects.equals(direction, ray.direction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(origin, direction);
  }

  @Override
  public String toString() {
    return String.format("{ Origin: %s, Direction: %s }", this.origin.toString(), this.direction.toString());
  }

}
