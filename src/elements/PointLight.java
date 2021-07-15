package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Point light class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class PointLight extends Light implements LightSource {
  /**
   * Point light position
   */
  protected Point3D position;

  /**
   * attenuation coefficients
   */
  protected double kC, kL, kQ;

  /**
   * constructor
   * 
   * @param intensity
   * @param position
   * @param kC
   * @param kL
   * @param kQ
   */
  public PointLight(Color intensity, Point3D position, double kC, double kL, double kQ) {
    super(intensity);
    this.position = position;
    this.kC = kC;
    this.kL = kL;
    this.kQ = kQ;
  }

  /**
   * constructor with default attenuation coefficients
   * 
   * @param intensity
   * @param position
   * @param kC
   * @param kL
   * @param kQ
   */
  public PointLight(Color intensity, Point3D position) {
    this(intensity, position, 1, 0, 0);
  }

  /**
   * kC getter
   * 
   * @param kC
   * @return point light
   */
  public PointLight setKC(double kC) {
    this.kC = kC;
    return this;
  }

  /**
   * kL getter
   * 
   * @param kL
   * @return point light
   */
  public PointLight setKL(double kL) {
    this.kL = kL;
    return this;
  }

  /**
   * kQ getter
   * 
   * @param kQ
   * @return point light
   */
  public PointLight setKQ(double kQ) {
    this.kQ = kQ;
    return this;
  }

  @Override
  public Color getIntensity(Point3D p) {
    double d = position.distance(p);
    return (intensity.reduce(kC + kL * d + kQ * d * d));
  }

  @Override
  public Vector getL(Point3D p) {
    if (p.equals(position)) {
      return null;
    }
    return p.subtract(position).normalize();
  }

  @Override
  public double getDistance(Point3D p) {
    return position.distance(p);
  }
}
