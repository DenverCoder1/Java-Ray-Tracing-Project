package elements;

import primitives.Color;

/**
 * Class representing Ambient Light
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 * 
 */
public class AmbientLight {
  /**
   * Color intensity
   */
  private Color intensity;

  /**
   * Set intensity from Ia and Ka
   */
  public AmbientLight(Color ia, double ka) {
    intensity = ia.scale(ka);
  }

  /**
   * @return intensity
   */
  public Color getIntensity() {
    return intensity;
  }
}