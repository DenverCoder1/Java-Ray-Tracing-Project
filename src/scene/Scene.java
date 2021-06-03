package scene;

import elements.AmbientLight;
import geometries.Geometries;
import geometries.Intersectable;
import primitives.Color;

/**
 * Defines the picture, the geometries, lights for a scene. All attributes are
 * public since this is a passive data structure
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Scene {

  /**
   * the name of the scene
   */
  private String name;

  /**
   * the color of the background
   */
  public Color background;

  /**
   * the intensity of the ambient light of the scene
   */
  public AmbientLight ambientLight;

  /**
   * the collection of the geometries in the scene
   */
  public Geometries geometries;

  /**
   * the distance of the camera from the view plane
   */
  private double distance;

  /**
   * construct scene with defaults
   */
  public Scene(String sceneName) {
    name = sceneName;
    background = Color.BLACK;
    ambientLight = new AmbientLight(new Color(192, 192, 192), 1.d);
    geometries = new Geometries();
  }

  /**
   * @return scene name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the distance
   */
  public double getDistance() {
    return distance;
  }

  /**
   * @param newBackground the background to set
   */
  public Scene setBackground(Color newBackground) {
    this.background = newBackground;
    return this;
  }

  /**
   * @param newAmbientLight the ambientLight to set
   */
  public Scene setAmbientLight(AmbientLight newAmbientLight) {
    this.ambientLight = newAmbientLight;
    return this;
  }

  /**
   * @param newDistance the distance to set
   */
  public Scene setDistance(double newDistance) {
    this.distance = newDistance;
    return this;
  }

  /**
   * add geometries to the scene
   * 
   * @param geometries to add to the scene
   */
  public Scene addGeometries(Intersectable... newGeometries) {
    geometries.add(newGeometries);
    return this;
  }
}