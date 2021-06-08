package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * Class Tube is the basic class representing a tube of Euclidean geometry in
 * Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Tube extends Geometry {
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
    double scalar = axis.getDirection().dotProduct(point.subtract(axis.getOrigin()));
    if (isZero(scalar)) {
      return point.subtract(axis.getOrigin()).normalize();
    }
    Point3D p2 = axis.getOrigin().add(axis.getDirection().scale(scalar));
    return point.subtract(p2).normalize();
  }

  @Override
  public String toString() {
    return String.format("{ Axis: %s, Radius: %d }", this.axis.toString(), this.radius);
  }

  @Override
  public List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
    // TODO Auto-generated method stub
    return null;
  }

}
