package renderer;

import primitives.Color;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import elements.LightSource;
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
   * receives a geometry/point and returns the color at the point
   * 
   * @param geopoint intersection
   * @return Color
   */
  private Color calcColor(GeoPoint geopoint) {
    Color color = new Color(scene.ambientLight.getIntensity());
    color = color.add(geopoint.geometry.getEmission());
    Vector v = geopoint.point.subtract(scene.getCamera().getOrigin()).normalize();
    Vector n = geopoint.geometry.getNormal(geopoint.point);
    int nShininess = geopoint.geometry.getShininess();
    double kd = geopoint.geometry.material.kD;
    double ks = geopoint.geometry.material.kS;
    for (LightSource lightSource : scene.getLights()) {
      Vector l = lightSource.getL(geopoint.point);
      if (n.dotProduct(l) * n.dotProduct(v) > 0) {
        Color lightIntensity = lightSource.getIntensity(geopoint.point);
        color.add(calcDiffusive(kd, l, n, lightIntensity), calcSpecular(ks, l, n, v, nShininess, lightIntensity));
      }
    }
    return color;
  }

  /**
   * Calculate specular color
   * 
   * @param ks
   * @param l
   * @param n
   * @param v
   * @param nShininess
   * @param lightIntensity
   * @return specular color
   */
  public Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
    Vector r = l.subtract(n.scale(2 * l.dotProduct(n)));
    double factor = -v.dotProduct(r);
    return lightIntensity.scale(ks * Math.pow(factor, nShininess));
  }

  /**
   * Calculate diffusive color
   * 
   * @param kd
   * @param l
   * @param n
   * @param lightIntensity
   * @return diffuse color
   */
  public Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity) {
    double factor = kd * Math.abs(l.dotProduct(n));
    return lightIntensity.scale(factor);
  }
}