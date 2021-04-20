package primitives;

import java.util.Objects;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in
 * Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Ray {

  final Point3D origin;
  final Vector direction;

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
