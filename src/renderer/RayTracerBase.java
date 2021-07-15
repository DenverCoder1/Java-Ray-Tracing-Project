package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract class RayTracerBase contains the scene handles ray tracing
 */
public abstract class RayTracerBase {
  protected Scene scene;

  /**
   * constructor
   * 
   * @param scene
   */
  protected RayTracerBase(Scene scene) {
    this.scene = scene;
  }

  /**
   * Generate color for a pixel given the ray
   * 
   * @param ray
   * @return Color
   */
  public abstract Color traceRay(Ray ray);

  /**
   * getter for the scene
   * 
   * @return scene
   */
  public Scene getScene() {
    return this.scene;
  }
}