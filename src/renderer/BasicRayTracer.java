package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.List;

import geometries.Intersectable.GeoPoint;

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
    List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
    if (intersections != null) {
      GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
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
  private Color calcColor(GeoPoint point) {
    return scene.ambientLight.getIntensity();
  }
}