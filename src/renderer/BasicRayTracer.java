package renderer;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;

import static primitives.Util.*;

/**
 * BasicRayTracer inherits from the abstract class RayTracerBase
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class BasicRayTracer extends RayTracerBase {

  private static final int MAX_CALC_COLOR_LEVEL = 5;
  private static final double MIN_CALC_COLOR_K = 0.1;

  /**
   * constructor
   * 
   * @param scene
   */
  public BasicRayTracer(Scene scene) {
    super(scene);
  }

  /**
   * Finds the color at the closest intersection point or returns the background
   * color if no intersection points are found
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
    // no intersections
    return scene.background;
  }

  /**
   * Calculate the color for a pixel in the view plane
   * 
   * @param geopoint the intersection point of the geometry and point
   * @param ray      the ray from which the point is viewed
   * @param level    recursion level before stopping
   * @param k        effect factor
   * @return the color for the pixel
   */
  private Color calcColor(GeoPoint geopoint, Ray ray, int level, double k) {
    if (level <= 1 || k < MIN_CALC_COLOR_K) {
      return Color.BLACK;
    }
    // diffuse, specular, shadow rays
    Color result = geopoint.geometry.getEmission().add(calcLocalEffects(geopoint, ray, k));
    // reflection rays
    double kr = geopoint.geometry.getMaterial().kR;
    double kkr = k * kr;
    Vector n = geopoint.geometry.getNormal(geopoint.point);
    if (kkr > MIN_CALC_COLOR_K) {
      Ray reflectedRay = constructReflectedRay(geopoint.point, ray, n);
      if (reflectedRay != null) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(reflectedRay);
        GeoPoint reflectedPoint = reflectedRay.findClosestGeoPoint(intersections);
        if (reflectedPoint != null)
          result = result.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
      }
    }
    // transparency (refraction rays)
    double kt = geopoint.geometry.getMaterial().kT;
    double kkt = k * kt;
    if (kkt > MIN_CALC_COLOR_K) {
      Ray refractedRay = new Ray(geopoint.point, ray.getDirection(), n);
      List<GeoPoint> intersections = scene.geometries.findGeoIntersections(refractedRay);
      GeoPoint refractedPoint = refractedRay.findClosestGeoPoint(intersections);
      if (refractedPoint != null)
        result = result.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
    }
    return result;
  }

  /**
   * calcColor with defaults
   * 
   * @param geopoint the intersection point of the geometry and point
   * @param ray      the ray from which the point is viewed
   * @return the color for the pixel
   */
  private Color calcColor(GeoPoint geopoint, Ray ray) {
    return calcColor(geopoint, ray, MAX_CALC_COLOR_LEVEL, 1.0).add(scene.ambientLight.getIntensity());
  }

  /**
   * calculate lighting effects
   * 
   * @param geopoint the intersection point of the geometry and point
   * @param ray      the ray from which the point is viewed
   * @param k        effect factor
   * @return the color to add
   */
  private Color calcLocalEffects(GeoPoint geopoint, Ray ray, double k) {
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
        double ktr = transparency(l, n, geopoint, lightSource);
        if (ktr * k > MIN_CALC_COLOR_K) {
          Color lightIntensity = lightSource.getIntensity(geopoint.point).scale(ktr);
          color = color.add(calcDiffusive(kd, l, n, lightIntensity),
              calcSpecular(ks, l, n, v, nShininess, lightIntensity));
        }
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

  /**
   * get transparency of light intersection
   * 
   * @param l           light direction
   * @param n           normal
   * @param geopoint    intersection point
   * @param lightSource light source
   * @return transparency
   */
  private double transparency(Vector l, Vector n, GeoPoint geopoint, LightSource lightSource) {
    Vector lightDirection = l.scale(-1);
    Ray lightRay = new Ray(geopoint.point, lightDirection, n);
    double lightDistance = lightSource.getDistance(geopoint.point);
    var intersections = scene.geometries.findGeoIntersections(lightRay);
    if (intersections == null)
      return 1.0;
    double ktr = 1.0;
    for (GeoPoint gp : intersections) {
      if (alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0) {
        ktr *= gp.geometry.getMaterial().kT;
        if (ktr < MIN_CALC_COLOR_K)
          return 0.0;
      }
    }
    return ktr;
  }

  /**
   * construct reflected ray
   * 
   * @param point
   * @param ray
   * @param n
   * @return reflected ray
   */
  private Ray constructReflectedRay(Point3D point, Ray ray, Vector n) {
    Vector v = ray.getDirection();
    double vn = v.dotProduct(n);
    if (vn == 0) {
      return null;
    }
    Vector r = v.subtract(n.scale(2 * vn));
    return new Ray(point, r, n);
  }
}