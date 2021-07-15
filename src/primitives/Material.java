package primitives;

/**
 * Class Material is the basic class representing a geometry's material
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Material {

  /**
   * diffuse
   */
  public double kD;

  /**
   * specular
   */
  public double kS;

  /**
   * transparency
   */
  public double kT;

  /**
   * reflection
   */
  public double kR;

  /**
   * shininess
   */
  public int nShininess;

  /**
   * constructor
   * 
   * @param kd
   * @param ks
   * @param kr
   * @param kt
   * @param shininess
   */
  public Material(double kd, double ks, double kr, double kt, int shininess) {
    this.kD = kd;
    this.kS = ks;
    this.kR = kr;
    this.kT = kt;
    this.nShininess = shininess;
  }

  /**
   * default constructor
   */
  public Material() {
    this(0, 0, 0, 0, 0);
  }

  /**
   * set kd
   * 
   * @param kd
   */
  public Material setKD(double kd) {
    this.kD = kd;
    return this;
  }

  /**
   * set ks
   * 
   * @param ks
   */
  public Material setKS(double ks) {
    this.kS = ks;
    return this;
  }

  /**
   * set kr
   * 
   * @param kr
   */
  public Material setKR(double kr) {
    this.kR = kr;
    return this;
  }

  /**
   * set kt
   * 
   * @param kt
   */
  public Material setKT(double kt) {
    this.kT = kt;
    return this;
  }

  /**
   * set shininess
   * 
   * @param shininess
   */
  public Material setShininess(int shininess) {
    this.nShininess = shininess;
    return this;
  }
}
