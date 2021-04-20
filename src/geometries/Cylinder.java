package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Class Cylinder is the basic class representing a cylinder of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Cylinder extends Tube {
  protected final double height;

  /**
   * Constructor that takes 3 points and calculates the normal given the 3 points
   * 
   * @param first  the first point
   * @param second the second point
   * @param third  the third point
   */
  public Cylinder(Ray axis, double radius, double height) {
    super(axis, radius);
    this.height = height;
  }

  /**
   * Getter for the height
   * 
   * @return the height
   */
  public double getHeight() {
    return this.height;
  }

  @Override
  public Vector getNormal(Point3D point) {
    Point3D p2 = axis.getOrigin().add(axis.getDirection().scale(height));
    if (point.subtract(axis.getOrigin()).dotProduct(axis.getDirection()) == 0) {
      return axis.getDirection();
    }
    if (point.subtract(p2).dotProduct(axis.getDirection()) == 0) {
      return axis.getDirection().scale(-1);
    }
    return super.getNormal(point);
  }

  @Override
  public String toString() {
    return String.format("{ Axis: %s, Radius: %d, Height: %d }", this.axis.toString(), this.radius, this.height);
  }

}
