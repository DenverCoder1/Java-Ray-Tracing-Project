package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * RayTracerBase class holds the scene
 */
public abstract class RayTracerBase {
  protected Scene scene;

  /**
   * RayTracerBase constructor receives a scene and apply it to the scene
   * parameter
   * 
   * @param scene
   * @return scene
   */
  protected RayTracerBase(Scene scene) {
    this.scene = scene;
  }

  /**
   * This abstract function receives a ray and return a color
   * 
   * @param ray
   * @return Color
   */
  public abstract Color traceRay(Ray ray);

  /**
   * get the scene
   * 
   * @return scene
   */
  public Scene getScene() {
    return this.scene;
  }
}