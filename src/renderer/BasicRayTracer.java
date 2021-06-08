package renderer;

import primitives.Color;
import primitives.Material;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;

import static primitives.Util.*;

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
   * are no intersections or finds to closest to calculate the color.
   * 
   * @param ray
   * @return Color
   */
  @Override
  public Color traceRay(Ray ray) {
    List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
    if (intersections != null) {
      GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
      return calcColor(closestPoint, ray);
    }
    // ray did not intersect any geometrical object
    return scene.background;
  }

  /**
   * Calculate the color for a pixel in the view plane
   * 
   * @param geopoint the intersection point of the geometry and point
   * @param ray      the ray from which the point is viewed
   * @return the color for the pixel
   */
  private Color calcColor(GeoPoint geopoint, Ray ray) {
    return scene.ambientLight.getIntensity().add(geopoint.geometry.getEmission().add(calcLocalEffects(geopoint, ray)));
  }

  /**
   * calculate lighting effects
   * 
   * @param geopoint the intersection point of the geometry and point
   * @param ray      the ray from which the point is viewed
   * @return the color to add
   */
  private Color calcLocalEffects(GeoPoint geopoint, Ray ray) {
    // viewing direction
    Vector v = ray.getDirection().normalized();
    // normal
    Vector n = geopoint.geometry.getNormal(geopoint.point);
    // dot product of direction and normal
    double nv = alignZero(n.dotProduct(v));
    if (isZero(nv)) {
      return Color.BLACK;
    }
    // material
    Material material = geopoint.geometry.getMaterial();
    // shininess
    int nShininess = material.nShininess;
    // diffuse
    double kd = material.kD;
    // specular
    double ks = material.kS;
    // add lights
    Color color = Color.BLACK;
    for (LightSource lightSource : scene.lights) {
      Vector l = lightSource.getL(geopoint.point);
      double nl = alignZero(n.dotProduct(l));
      if ((nl > 0 && nv > 0 || nl < 0 && nv < 0)) {
        Color lightIntensity = lightSource.getIntensity(geopoint.point);
        color = color.add(calcDiffusive(kd, l, n, lightIntensity),
            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
      }
    }
    return color;
  }

  /**
   * Calculate specular effects
   * 
   * @param ks             specular
   * @param l              light direction
   * @param n              normal
   * @param v              view direction
   * @param nShininess     shininess
   * @param lightIntensity intensity color
   * @return specular color to add
   */
  private Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
    Vector r = l.subtract(n.scale(alignZero(2 * l.dotProduct(n))));
    double vr = alignZero(v.dotProduct(r));
    return lightIntensity.scale(ks * Math.pow(-vr, nShininess));
  }

  /**
   * Calculate diffuse effects
   * 
   * @param kd             diffuse
   * @param l              light direction
   * @param n              normal
   * @param lightIntensity intensity color
   * @return diffuse color to add
   */
  private Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity) {
    double factor = Math.abs(alignZero(l.dotProduct(n)));
    return lightIntensity.scale(kd * factor);
  }
}