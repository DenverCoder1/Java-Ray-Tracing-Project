package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Light source interface
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public interface LightSource {
  /**
   * getter for intensity
   * 
   * @param p
   * @return intensity color
   */
  public Color getIntensity(Point3D p);

  /**
   * getter for the direction
   * 
   * @param p
   * @return direction as a vector
   */
  public Vector getL(Point3D p);

  /**
   * getter for the direction
   * 
   * @param p
   * @return direction as a vector
   */
  public double getDistance(Point3D p);
}
