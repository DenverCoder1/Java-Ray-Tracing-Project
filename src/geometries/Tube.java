package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Class Tube is the basic class representing a tube of Euclidean geometry in
 * Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Tube implements Geometry {
  protected final Ray axis;
  protected final double radius;

  /**
   * Constructor that takes axis and radius
   * 
   * @param axis   the axis ray
   * @param radius the radius
   */
  public Tube(Ray axis, double radius) {
    this.axis = axis;
    this.radius = radius;
  }

  /**
   * Getter for the axis
   * 
   * @return the axis
   */
  public Ray getAxis() {
    return this.axis;
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
    return null;
  }

  @Override
  public String toString() {
    return String.format("{ Axis: %s, Radius: %d }", this.axis.toString(), this.radius);
  }

}
