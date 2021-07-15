package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * class SpotLight to apply spot lighting
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class SpotLight extends PointLight {
  /**
   * light direction
   */
  private Vector direction;

  /**
   * light concentration
   */
  private double concentration;

  /**
   * constructor
   * 
   * @param intensity
   * @param position
   * @param direction
   * @param kC
   * @param kL
   * @param kQ
   * @param concentration
   */
  public SpotLight(Color intensity, Point3D position, Vector direction, double kC, double kL, double kQ,
      double concentration) {
    super(intensity, position, kC, kL, kQ);
    this.direction = direction.normalized();
    this.concentration = concentration;
  }

  /**
   * constructor with default attenuation coefficients and concentration
   * 
   * @param intensity
   * @param position
   * @param direction
   */
  public SpotLight(Color intensity, Point3D position, Vector direction) {
    this(intensity, position, direction, 1, 0, 0, 1);
  }

  @Override
  public Color getIntensity(Point3D p) {
    double projection = direction.dotProduct(getL(p));
    if (isZero(projection)) {
      return Color.BLACK;
    }
    double factor = Math.max(0, projection);
    Color pointlightIntensity = super.getIntensity(p);
    if (concentration != 1) {
      factor = Math.pow(factor, concentration);
    }
    return (pointlightIntensity.scale(factor));
  }
}
