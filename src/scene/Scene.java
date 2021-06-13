package scene;

import java.util.LinkedList;
import java.util.List;

import elements.AmbientLight;
import elements.Camera;
import elements.LightSource;
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
   * Choices for the supersampling level
   */
  public enum SUPERSAMPLING_LEVEL {
    NONE, SUPERSAMPLING, ADAPTIVE
  }

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
   * the camera
   */
  private Camera camera;

  /**
   * list of lights
   */
  public List<LightSource> lights;

  /**
   * whether or not supersampling is enabled
   */
  public SUPERSAMPLING_LEVEL supersamplingLevel;

  /**
   * number of rows and columns for supersampling
   */
  public int supersamplingGridSize;

  /**
   * construct scene with defaults
   * 
   * @param sceneName
   */
  public Scene(String sceneName) {
    name = sceneName;
    background = Color.BLACK;
    ambientLight = new AmbientLight(Color.BLACK, 0);
    geometries = new Geometries();
    lights = new LinkedList<>();
    supersamplingLevel = SUPERSAMPLING_LEVEL.ADAPTIVE;
    supersamplingGridSize = 9;
  }

  /**
   * @return scene name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the camera
   */
  public Camera getCamera() {
    return camera;
  }

  /**
   * @return the lights
   */
  public List<LightSource> getLights() {
    return lights;
  }

  /**
   * @param newBackground the background to set
   * @return scene object
   */
  public Scene setBackground(Color newBackground) {
    this.background = newBackground;
    return this;
  }

  /**
   * @param newAmbientLight the ambientLight to set
   * @return scene object
   */
  public Scene setAmbientLight(AmbientLight newAmbientLight) {
    this.ambientLight = newAmbientLight;
    return this;
  }

  /**
   * @param camera the camera to set
   * @return scene object
   */
  public Scene setCamera(Camera camera) {
    this.camera = camera;
    return this;
  }

  /**
   * add geometries to the scene
   * 
   * @param geometries to add to the scene
   * @return scene object
   */
  public Scene addGeometries(Intersectable... newGeometries) {
    geometries.add(newGeometries);
    return this;
  }

  /**
   * set lights
   * 
   * @param newLights the lights to set
   * @return scene object
   */
  public Scene setLights(List<LightSource> newLights) {
    this.lights = newLights;
    return this;
  }

  /**
   * set supersampling to NONE, SUPERSAMPLING, or ADAPTIVE
   * 
   * @param level supersampling level
   * @return scene object
   */
  public Scene setSupersamplingLevel(SUPERSAMPLING_LEVEL level) {
    this.supersamplingLevel = level;
    return this;
  }

  /**
   * set supersampling grid size
   * 
   * @param gridSize number of rows/cols
   * @return scene object
   */
  public Scene setSupersamplingGridSize(int gridSize) {
    this.supersamplingGridSize = gridSize;
    return this;
  }
}