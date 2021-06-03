package geometries;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Abstract interface Geometry is the basic interface representing a geometry in
 * Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public abstract class Geometry implements Intersectable {

  /**
   * Emission color
   */
  protected Color emission = Color.BLACK;

  /**
   * get emission color
   * 
   * @return emission
   */
  public Color getEmission() {
    return this.emission;
  }

  /**
   * set emission color
   * 
   * @param emission
   * @return geometry object
   */
  public Geometry setEmission(Color emission) {
    this.emission = emission;
    return this;
  }

  /**
   * Get the normal vector of the geometry
   * 
   * @param point the point at which to get the normal vector
   * @return the normal to the geometry at the point
   */
  public abstract Vector getNormal(Point3D point);

}
