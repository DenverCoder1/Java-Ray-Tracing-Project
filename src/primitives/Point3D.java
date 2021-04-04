package primitives;

import java.util.Objects;

/**
 * Class Point3D is the basic class representing a 3D point of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Point3D {
  public final Coordinate x;
  public final Coordinate y;
  public final Coordinate z;

  public static final Point3D ZERO = new Point3D(0, 0, 0);

  /**
   * Constructor that takes three coordinates as arguments
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   */
  public Point3D(Coordinate x, Coordinate y, Coordinate z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Constructor that takes three doubles as arguments
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   */
  public Point3D(double x, double y, double z) {
    this.x = new Coordinate(x);
    this.y = new Coordinate(y);
    this.z = new Coordinate(z);
  }

  /**
   * Subtract one point from another to form a vector
   * 
   * @param other the point to subtract
   * @return vector from other point to the point the action is performed on
   */
  public Vector subtract(Point3D other) {
    return new Vector(this.x.coord - other.x.coord, this.y.coord - other.y.coord, this.z.coord - other.z.coord);
  }

  /**
   * Add one point from another to form a vector
   * 
   * @param direction the point to subtract
   * @return vector from other point to the point the action is performed on
   */
  public Point3D add(Vector direction) {
    return new Point3D(this.x.coord + direction.getHead().x.coord, this.y.coord + direction.getHead().y.coord,
        this.z.coord + direction.getHead().z.coord);
  }

  /**
   * Calculate the distance squared between two points
   * 
   * @param other other point
   * @return the distance between the points squared
   */
  public double distanceSquared(Point3D other) {
    double diffX = this.x.coord - other.x.coord;
    double diffY = this.y.coord - other.y.coord;
    double diffZ = this.z.coord - other.z.coord;
    return diffX * diffX + diffY * diffY + diffZ * diffZ;
  }

  /**
   * Calculate the distance between two points
   * 
   * @param other other point
   * @return the distance between the points
   */
  public double distance(Point3D other) {
    return Math.sqrt(this.distanceSquared(other));
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Point3D)) {
      return false;
    }
    Point3D point3D = (Point3D) o;
    return Objects.equals(x, point3D.x) && Objects.equals(y, point3D.y) && Objects.equals(z, point3D.z);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    return String.format("(%s, %s, %s)", x.toString(), y.toString(), z.toString());
  }

}
