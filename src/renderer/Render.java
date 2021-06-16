package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
   * Choices for the supersampling type
   */
  public enum SUPERSAMPLING_TYPE {
    NONE, SUPERSAMPLING, ADAPTIVE
  }

  /**
   * whether or not supersampling is enabled
   */
  public SUPERSAMPLING_TYPE supersamplingType = SUPERSAMPLING_TYPE.ADAPTIVE;

  /**
   * number of rows and columns for supersampling
   */
  public int supersamplingGridSize = 9;

  /**
   * maximum recursion level for adaptive supersampling
   */
  public int adaptiveMaxRecursionLevel = 3;

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
   * @throws InterruptedException
   * @throws ExecutionException
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
      // create thread pool
      ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
      List<Callable<Void>> tasks = new ArrayList<>();
      for (int i = 0; i < nY; i++) {
        for (int j = 0; j < nX; j++) {
          final int row = i;
          final int col = j;
          tasks.add(() -> {
            Ray ray = camera.constructRayThroughPixel(nX, nY, col, row);
            Color pixelColor;
            // adaptive supersampling is enabled
            if (supersamplingType == SUPERSAMPLING_TYPE.ADAPTIVE) {
              pixelColor = getAdaptiveSupersamplingColor(ray);
            }
            // supersampling is enabled
            else if (supersamplingType == SUPERSAMPLING_TYPE.SUPERSAMPLING) {
              pixelColor = getSupersamplingColor(ray, supersamplingGridSize);
            }
            // no supersampling
            else {
              pixelColor = rayTracer.traceRay(ray);
            }
            imageWriter.writePixel(col, row, pixelColor);
            return null;
          });
        }
      }
      long startTime = System.currentTimeMillis();
      exec.invokeAll(tasks);
      long endTime = System.currentTimeMillis();
      System.out.println((endTime - startTime) / 1000.0 + " seconds");
      exec.shutdown();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (MissingResourceException e) {
      throw new UnsupportedOperationException("Missing " + e.getClassName());
    }
  }

  /**
   * Get the color for adaptive supersampling
   * 
   * @param ray ray for original pixel location
   * @return supersampling average color
   */
  private Color getAdaptiveSupersamplingColor(Ray ray) {
    Scene scene = rayTracer.scene;
    Camera camera = scene.getCamera();
    Point3D pc = ray.getPoint(camera.getDistance());
    double pixelWidth = camera.getWidth() / imageWriter.getNx();
    double pixelHeight = camera.getHeight() / imageWriter.getNy();
    Map<Point3D, Color> memo = new HashMap<>();
    return adaptiveSupersamplingRecursive(pc, pixelWidth, pixelHeight, camera, memo, adaptiveMaxRecursionLevel);
  }

  /**
   *
   * Recursive function to sample cell colors and recursively sample smaller cells
   * when there is color variance
   * 
   * @param pc           center of the pixel
   * @param cellWidth    width of cell that is being sampled
   * @param cellHeight   height of cell that is being sampled
   * @param cameraOrigin location of the camera
   * @param memo         hash map of previously computed ray colors
   * @param level        recursion level - stops when reaches 1
   * @return average color of the cell
   */
  private Color adaptiveSupersamplingRecursive(Point3D pc, double cellWidth, double cellHeight, Camera camera,
      Map<Point3D, Color> memo, int level) {
    Vector vRight = camera.getVRight();
    Vector vUp = camera.getVUp();

    double halfCellWidth = cellWidth / 2;
    double halfCellHeight = cellHeight / 2;

    // find points of the four corners
    Point3D topLeft = pc.add(vRight.scale(-halfCellWidth)).add(vUp.scale(halfCellHeight));
    Point3D topRight = topLeft.add(vRight.scale(cellWidth));
    Point3D bottomLeft = topLeft.add(vUp.scale(-cellWidth));
    Point3D bottomRight = topRight.add(vUp.scale(-cellWidth));

    // calculate the colors of the new rays from the camera to the corners
    Color topLeftColor, topRightColor, bottomLeftColor, bottomRightColor;
    if (memo.containsKey(topLeft)) {
      topLeftColor = memo.get(topLeft);
    } else {
      topLeftColor = rayTracer.traceRay(camera.constructRayThroughPoint(topLeft));
      memo.put(topLeft, topLeftColor);
    }
    if (memo.containsKey(topRight)) {
      topRightColor = memo.get(topRight);
    } else {
      topRightColor = rayTracer.traceRay(camera.constructRayThroughPoint(topRight));
      memo.put(topRight, topRightColor);
    }
    if (memo.containsKey(bottomLeft)) {
      bottomLeftColor = memo.get(bottomLeft);
    } else {
      bottomLeftColor = rayTracer.traceRay(camera.constructRayThroughPoint(bottomLeft));
      memo.put(bottomLeft, bottomLeftColor);
    }
    if (memo.containsKey(bottomRight)) {
      bottomRightColor = memo.get(bottomRight);
    } else {
      bottomRightColor = rayTracer.traceRay(camera.constructRayThroughPoint(bottomRight));
      memo.put(bottomRight, bottomRightColor);
    }

    // stop when maximum recursion level
    if (level <= 1) {
      // return average of the corner colors
      return topLeftColor.add(topRightColor, bottomLeftColor, bottomRightColor).reduce(4);
    }

    // if all corners are the same color, return any corner color
    if (topLeftColor.equals(topRightColor) && topLeftColor.equals(bottomLeftColor)
        && topLeftColor.equals(bottomRightColor)) {
      return topLeftColor;
    }

    // calculate the centers of each quarter of the cell
    Point3D topLeftPC = pc.add(vRight.scale(-halfCellWidth / 2)).add(vUp.scale(halfCellWidth / 2));
    Point3D topRightPC = topLeftPC.add(vRight.scale(halfCellWidth));
    Point3D bottomLeftPC = topLeftPC.add(vUp.scale(-halfCellWidth));
    Point3D bottomRightPC = topRightPC.add(vUp.scale(-halfCellWidth));

    // calculate average colors of the four quarters
    return adaptiveSupersamplingRecursive(topLeftPC, halfCellWidth, halfCellHeight, camera, memo, level - 1)
        .add(adaptiveSupersamplingRecursive(bottomLeftPC, halfCellWidth, halfCellHeight, camera, memo, level - 1),
            adaptiveSupersamplingRecursive(topRightPC, halfCellWidth, halfCellHeight, camera, memo, level - 1),
            adaptiveSupersamplingRecursive(bottomRightPC, halfCellWidth, halfCellHeight, camera, memo, level - 1))
        .reduce(4);
  }

  /**
   * Get the average color of rays for supersampling
   * 
   * @param middleRay ray for original pixel location
   * @param gridSize  number of rows and columns for dividing pixel
   * @return supersampling average color
   */
  private Color getSupersamplingColor(Ray middleRay, int gridSize) {
    Scene scene = rayTracer.scene;
    Camera camera = scene.getCamera();
    int nX = imageWriter.getNx();
    int nY = imageWriter.getNy();
    // list for returning rays
    List<Ray> supersamplingRays = camera.getSupersamplingRays(middleRay, gridSize, nX, nY);
    // add the intersected colors together
    Color pixelColor = Color.BLACK;
    for (Ray r : supersamplingRays) {
      pixelColor = pixelColor.add(rayTracer.traceRay(r));
    }
    // divide by the number of rays
    return pixelColor.reduce(supersamplingRays.size());
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

  /**
   * set supersampling to NONE, SUPERSAMPLING, or ADAPTIVE
   * 
   * @param type supersampling type
   * @return Render object
   */
  public Render setSupersamplingType(SUPERSAMPLING_TYPE type) {
    this.supersamplingType = type;
    return this;
  }

  /**
   * set supersampling grid size
   * 
   * @param gridSize number of rows/cols
   * @return Render object
   */
  public Render setSupersamplingGridSize(int gridSize) {
    this.supersamplingGridSize = gridSize;
    return this;
  }

  /**
   * set adaptive supersampling max recursion level
   * 
   * @param maxLevel maximum level of recursion
   * @return Render object
   */
  public Render setAdaptiveMaxRecursionLevel(int maxLevel) {
    this.adaptiveMaxRecursionLevel = maxLevel;
    return this;
  }

}