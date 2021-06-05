package elements;

import primitives.Color;

/**
 * Light abstract class for scene lighting
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
abstract class Light {
  /**
   * light intensity color
   */
  private Color intensity;

  /**
   * constructor for light
   * 
   * @param ic
   */
  protected Light(Color ic) {
    intensity = new Color(ic);
  }

  /**
   * getter for intensity
   * 
   * @return intensity
   */
  public Color getIntensity() {
    return intensity;
  }
}
