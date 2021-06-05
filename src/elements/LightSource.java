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
   * @return color
   */
  public Color getIntensity(Point3D p);

  /**
   * getter for the direction
   * 
   * @param p
   * @return vector-direction
   */
  public Vector getL(Point3D p);
}
