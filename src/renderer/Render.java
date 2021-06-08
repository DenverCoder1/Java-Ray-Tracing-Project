package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

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
      if (rayTracer.scene == null) {
        throw new MissingResourceException("missing scene", Scene.class.getName(), "");
      }
      if (rayTracer.scene.getCamera() == null) {
        throw new MissingResourceException("missing camera", Camera.class.getName(), "");
      }
      Camera camera = rayTracer.scene.getCamera();
      // rendering the image
      int nX = imageWriter.getNx();
      int nY = imageWriter.getNy();
      for (int row = 0; row < nY; row++) {
        for (int col = 0; col < nX; col++) {
          Ray ray = camera.constructRayThroughPixel(nX, nY, col, row);
          Color pixelColor = rayTracer.traceRay(ray);
          imageWriter.writePixel(col, row, pixelColor);
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