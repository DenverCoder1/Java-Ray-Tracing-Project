package elements;

import primitives.Color;

/**
 * Class representing Ambient Light
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 * 
 */
public class AmbientLight extends Light {
  /**
   * Default constructor for using black
   */
  public AmbientLight() {
    super(Color.BLACK);
  }

  /**
   * Set intensity from Ia and Ka
   * 
   * @param ia intensity
   * @param ka scalar
   */
  public AmbientLight(Color ia, double ka) {
    super(ia.scale(ka));
  }
}