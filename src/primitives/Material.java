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
   * shininess
   */
  public int shininess;

  /**
   * constructor
   * 
   * @param kd
   * @param ks
   * @param shininess
   */
  public Material(double kd, double ks, int shininess) {
    this.kD = kd;
    this.kS = ks;
    this.shininess = shininess;
  }

  /**
   * set kd
   * 
   * @param kd
   */
  public Material setKD(int kd) {
    this.kD = kd;
    return this;
  }

  /**
   * set ks
   * 
   * @param ks
   */
  public Material setKS(int ks) {
    this.kS = ks;
    return this;
  }

  /**
   * set shininess
   * 
   * @param shininess
   */
  public Material setShininess(int shininess) {
    this.shininess = shininess;
    return this;
  }
}
