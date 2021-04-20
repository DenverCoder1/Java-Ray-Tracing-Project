package geometries;

import primitives.Point3D;
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
}
