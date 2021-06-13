package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

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
   * ray tracer base with scene
   */
  private RayTracerBase rayTracer;

  /**
   * write image writer to image file
   * 
   * @throws UnsupportedOperationException if image writer is missing
   */
  public File writeToImage() {
    try {
      if (imageWriter == null)
        throw new MissingResourceException("image writer is missing", ImageWriter.class.getName(), "");
    } catch (MissingResourceException e) {
      throw new UnsupportedOperationException("Image writer not implemented");
    }
    return imageWriter.writeToImage();
  }

  /**
   * write image writer to image file and open in vscode or default image viewer
   * 
   * @throws UnsupportedOperationException if image writer is missing
   */
  public void writeToImageAndOpen() {
    // write image to file
    File file = writeToImage();
    try {
      // open in vscode
      new ProcessBuilder("code", "-r", file.getAbsolutePath()).start();
    } catch (IOException e1) {
      try {
        // open in default program
        Desktop.getDesktop().open(file);
      } catch (IOException e2) {
        // print stack trace
        e2.printStackTrace();
      }
    }
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
      if (rayTracer == null) {
        throw new MissingResourceException("missing ray tracer", RayTracerBase.class.getName(), "");
      }
      Scene scene = rayTracer.scene;
      if (scene == null) {
        throw new MissingResourceException("missing scene", Scene.class.getName(), "");
      }
      if (scene.getCamera() == null) {
        throw new MissingResourceException("missing camera", Camera.class.getName(), "");
      }
      Camera camera = scene.getCamera();
      // rendering the image
      int nX = imageWriter.getNx();
      int nY = imageWriter.getNy();
      for (int row = 0; row < nY; row++) {
        for (int col = 0; col < nX; col++) {
          Ray ray = camera.constructRayThroughPixel(nX, nY, col, row);
          Color pixelColor = Color.BLACK;
          // supersampling is enabled
          if (scene.supersamplingEnabled) {
            List<Ray> supersamplingRays = getSupersamplingRays(ray, scene.supersamplingGridSize);
            // add the intersected colors together
            for (Ray r : supersamplingRays) {
              pixelColor = pixelColor.add(rayTracer.traceRay(r));
            }
            // divide by the number of rays
            pixelColor = pixelColor.reduce(supersamplingRays.size());
          }
          // no supersampling
          else {
            pixelColor = rayTracer.traceRay(ray);
          }
          imageWriter.writePixel(col, row, pixelColor);
        }
      }
    } catch (MissingResourceException e) {
      throw new UnsupportedOperationException("Missing " + e.getClassName());
    }
  }

  /**
   * Get the rays for supersampling
   * 
   * @param ray      ray for original pixel location
   * @param gridSize number of rows and columns for dividing pixel
   * @return supersampling rays
   */
  private List<Ray> getSupersamplingRays(Ray ray, int gridSize) {
    Scene scene = rayTracer.scene;
    Camera camera = scene.getCamera();
    Point3D pixel = ray.getOrigin();
    double pixelHeight = camera.getHeight() / imageWriter.getNy();
    double pixelWidth = camera.getWidth() / imageWriter.getNx();
    Vector vRight = scene.getCamera().getVRight();
    Vector vUp = scene.getCamera().getVUp();
    Point3D cameraOrigin = scene.getCamera().getOrigin();
    // list for returning rays
    List<Ray> supersamplingRays = new ArrayList<>();
    pixel = pixel.add(vRight.scale(-pixelWidth / 2));
    pixel = pixel.add(vUp.scale(-pixelHeight / 2));
    // create grid of points for supersampling
    for (int row = 0; row < gridSize; row++) {
      for (int col = 0; col < gridSize; col++) {
        Point3D newPixel = pixel.add(vUp.scale(row * (pixelHeight / (gridSize - 1))))
            .add(vRight.scale(col * (pixelWidth / (gridSize - 1))));
        supersamplingRays.add(new Ray(newPixel, newPixel.subtract(cameraOrigin)));
      }
    }
    return supersamplingRays;
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