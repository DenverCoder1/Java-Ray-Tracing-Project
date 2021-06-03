package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.MissingResourceException;

/**
 * Generate the picture according to a scene
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Render {

  /**
   * image writer
   */
  private ImageWriter imageWriter;

  /**
   * camera
   */
  private Camera camera;

  /**
   * ray tracer base with scene
   */
  private RayTracerBase rayTracer;

  /**
   * write image writer to image file
   * 
   * @throws UnsupportedOperationException if image writer is missing
   */
  public void writeToImage() {
    try {
      if (imageWriter == null)
        throw new MissingResourceException("image writer is missing", ImageWriter.class.getName(), "");
    } catch (MissingResourceException e) {
      throw new UnsupportedOperationException("Image writer not implemented");
    }
    imageWriter.writeToImage();
  }

  /**
   * Renders image to imageWriter buffer and checks that objects exist first
   * 
   * @throws UnsupportedOperationException if any objects are missing
   */
  public void renderImage() {
    try {
      if (imageWriter == null) {
        throw new MissingResourceException("missing image writer", ImageWriter.class.getName(), "");
      }
      if (camera == null) {
        throw new MissingResourceException("missing camera", Camera.class.getName(), "");
      }
      if (rayTracer == null) {
        throw new MissingResourceException("missing ray tracer", RayTracerBase.class.getName(), "");
      }
      if (rayTracer.getScene() == null) {
        throw new MissingResourceException("missing scene", Scene.class.getName(), "");
      }
      // rendering the image
      int nX = imageWriter.getNx();
      int nY = imageWriter.getNy();
      for (int i = 0; i < nY; i++) {
        for (int j = 0; j < nX; j++) {
          Ray ray = camera.constructRayThroughPixel(nX, nY, j, i);
          Color pixelColor = rayTracer.traceRay(ray);
          imageWriter.writePixel(j, i, pixelColor);
        }
      }
    } catch (MissingResourceException e) {
      throw new UnsupportedOperationException("Missing " + e.getClassName());
    }
  }

  /**
   * prints a grid to image writer
   * 
   * @param interval
   * @param color
   * @throws UnsupportedOperationException if image writer is missing
   */
  public void printGrid(int interval, Color color) {
    try {
      if (imageWriter == null)
        throw new MissingResourceException("missing image writer", ImageWriter.class.getName(), "");
    } catch (MissingResourceException e) {
      throw new UnsupportedOperationException("Image writer not implemented");
    }
    int nX = imageWriter.getNx();
    int nY = imageWriter.getNy();
    for (int i = 0; i < nY; i++) {
      for (int j = 0; j < nX; j++) {
        if (i % interval == 0 || j % interval == 0) {
          imageWriter.writePixel(j, i, color);
        }
      }
    }
  }

  /**
   * set image writer
   * 
   * @param iw
   * @return the Render object
   */
  public Render setImageWriter(ImageWriter iw) {
    this.imageWriter = iw;
    return this;
  }

  /**
   * set camera
   * 
   * @param c
   * @return the Render object
   */
  public Render setCamera(Camera c) {
    this.camera = c;
    return this;
  }

  /**
   * set ray tracer
   * 
   * @param rayTracerBasic
   * @return the Render object
   */
  public Render setRayTracer(RayTracerBase rayTracerBasic) {
    this.rayTracer = rayTracerBasic;
    return this;
  }
}