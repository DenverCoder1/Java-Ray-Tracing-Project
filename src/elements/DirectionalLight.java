package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Directional lighting class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class DirectionalLight extends Light implements LightSource {
  /**
   * direction of the light
   */
  private Vector direction;

  /**
   * constructor
   * 
   * @param intensity
   * @param direction
   */
  public DirectionalLight(Color intensity, Vector direction) {
    super(intensity);
    this.direction = direction.normalized();
  }

  @Override
  public Color getIntensity(Point3D p) {
    return super.getIntensity();
  }

  @Override
  public Vector getL(Point3D p) {
    return direction;
  }

  @Override
  public double getDistance(Point3D p) {
    return Double.POSITIVE_INFINITY;
  }
}
