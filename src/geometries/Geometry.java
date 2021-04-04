package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * Abstract interface Geometry is the basic interface representing a geometry in
 * Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public abstract interface Geometry {

  /**
   * Get the normal vector of the geometry
   * 
   * @param point the point at which to get the normal vector
   * @return the normal to the geometry at the point
   */
  public abstract Vector getNormal(Point3D point);

}
