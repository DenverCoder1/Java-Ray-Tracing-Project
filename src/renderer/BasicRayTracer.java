package renderer;

import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * RayTracerBasic class inherits from RayTracerBase
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class BasicRayTracer extends RayTracerBase {
  /**
   * receives a scene and calls the base class constructor
   * 
   * @param scene
   */
  public BasicRayTracer(Scene scene) {
    super(scene);
  }

  /**
   * Find intersections between ray and scene geometries. Returns black if there
   * are no intersections or finds to closest to calculate the color
   * 
   * @param ray
   * @return Color
   */
  @Override
  public Color traceRay(Ray ray) {
    List<Point3D> intersections = scene.geometries.findIntersections(ray);
    if (intersections != null) {
      Point3D closestPoint = ray.findClosestPoint(intersections);
      return calcColor(closestPoint);
    }
    // ray did not intersect any geometrical object
    return scene.background;

  }

  /**
   * receives a point and returns the color at the point
   * 
   * @param point
   * @return Color
   */
  private Color calcColor(Point3D point) {
    return scene.ambientLight.getIntensity();
  }
}