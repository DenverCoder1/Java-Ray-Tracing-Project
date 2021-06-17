package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;
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
  private SUPERSAMPLING_TYPE supersamplingType = SUPERSAMPLING_TYPE.ADAPTIVE;

  /**
   * number of rows and columns for supersampling
   */
  private int supersamplingGridSize = 3;

  /**
   * maximum recursion level for adaptive supersampling
   */
  private int adaptiveMaxRecursionLevel = 3;

  /**
   * thread count for multithreading
   */
  private int threadsCount = 0;

  /**
   * spare threads if trying to use all the cores
   */
  private static final int SPARE_THREADS = 2;

  /**
   * printing progress percentage
   */
  private boolean print = false;

  /**
   * image writer
   */
  private ImageWriter imageWriter;

  /**
   * ray tracer base with scene
   */
  private RayTracerBase rayTracer;

  /**
   * Constant strings
   */
  private static final String RESOURCE_ERROR = "Renderer resource not set";
  private static final String RENDER_CLASS = "Render";
  private static final String IMAGE_WRITER_COMPONENT = "Image writer";
  private static final String CAMERA_COMPONENT = "Camera";
  private static final String RAY_TRACER_COMPONENT = "Ray tracer";

  /**
   * Pixel is an internal helper class whose objects are associated with a Render
   * object that they are generated in scope of. It is used for multithreading in
   * the Renderer and for follow up its progress.<br/>
   * There is a main follow up object and several secondary objects - one in each
   * thread.
   * 
   * @author Dan
   *
   */
  private class Pixel {
    private long maxRows = 0;
    private long maxCols = 0;
    private long pixels = 0;
    public volatile int row = 0;
    public volatile int col = -1;
    private long counter = 0;
    private int percents = 0;
    private long nextCounter = 0;

    /**
     * The constructor for initializing the main follow up Pixel object
     * 
     * @param maxRows the amount of pixel rows
     * @param maxCols the amount of pixel columns
     */
    public Pixel(int maxRows, int maxCols) {
      this.maxRows = maxRows;
      this.maxCols = maxCols;
      this.pixels = (long) maxRows * maxCols;
      this.nextCounter = this.pixels / 100;
      if (Render.this.print)
        System.out.printf("\r %02d%%", this.percents);
    }

    /**
     * Default constructor for secondary Pixel objects
     */
    public Pixel() {
    }

    /**
     * Internal function for thread-safe manipulating of main follow up Pixel object
     * - this function is critical section for all the threads, and main Pixel
     * object data is the shared data of this critical section.<br/>
     * The function provides next pixel number each call.
     * 
     * @param target target secondary Pixel object to copy the row/column of the
     *               next pixel
     * @return the progress percentage for follow up: if it is 0 - nothing to print,
     *         if it is -1 - the task is finished, any other value - the progress
     *         percentage (only when it changes)
     */
    private synchronized int nextP(Pixel target) {
      ++col;
      ++this.counter;
      if (col < this.maxCols) {
        target.row = this.row;
        target.col = this.col;
        if (Render.this.print && this.counter == this.nextCounter) {
          ++this.percents;
          this.nextCounter = this.pixels * (this.percents + 1) / 100;
          return this.percents;
        }
        return 0;
      }
      ++row;
      if (row < this.maxRows) {
        col = 0;
        target.row = this.row;
        target.col = this.col;
        if (Render.this.print && this.counter == this.nextCounter) {
          ++this.percents;
          this.nextCounter = this.pixels * (this.percents + 1) / 100;
          return this.percents;
        }
        return 0;
      }
      return -1;
    }

    /**
     * Public function for getting next pixel number into secondary Pixel object.
     * The function prints also progress percentage in the console window.
     * 
     * @param target target secondary Pixel object to copy the row/column of the
     *               next pixel
     * @return true if the work still in progress, -1 if it's done
     */
    public boolean nextPixel(Pixel target) {
      int percent = nextP(target);
      if (Render.this.print && percent > 0)
        synchronized (this) {
          notifyAll();
        }
      if (percent >= 0)
        return true;
      if (Render.this.print)
        synchronized (this) {
          notifyAll();
        }
      return false;
    }

    /**
     * Debug print of progress percentage - must be run from the main thread
     */
    public void print() {
      if (Render.this.print)
        while (this.percents < 100)
          try {
            synchronized (this) {
              wait();
            }
            System.out.printf("\r %02d%%", this.percents);
            System.out.flush();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
    }
  }

  /**
   * write image writer to image file
   * 
   * @throws MissingResourceException if image writer is missing
   */
  public File writeToImage() {
    if (imageWriter == null)
      throw new MissingResourceException(RESOURCE_ERROR, RENDER_CLASS, IMAGE_WRITER_COMPONENT);
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
   * This function renders image's pixel color map from the scene included with
   * the Renderer object - with multi-threading
   */
  private void renderImageThreaded() {
    Camera camera = rayTracer.scene.getCamera();
    final int nX = imageWriter.getNx();
    final int nY = imageWriter.getNy();
    final double pixelWidth = camera.getWidth() / imageWriter.getNx();
    final double pixelHeight = camera.getHeight() / imageWriter.getNy();
    final Pixel thePixel = new Pixel(nY, nX);
    // Generate threads
    Thread[] threads = new Thread[threadsCount];
    for (int i = threadsCount - 1; i >= 0; --i) {
      threads[i] = new Thread(() -> {
        Pixel pixel = new Pixel();
        while (thePixel.nextPixel(pixel))
          castRay(nX, nY, pixelWidth, pixelHeight, pixel.col, pixel.row);
      });
    }
    // Start threads
    for (Thread thread : threads)
      thread.start();

    // Print percents on the console
    thePixel.print();

    // Ensure all threads have finished
    for (Thread thread : threads)
      try {
        thread.join();
      } catch (Exception e) {
        Thread.currentThread().interrupt();
      }

    if (print)
      System.out.println("\nFinished");
  }

  /**
   * This function renders image's pixel color map from the scene included with
   * the Renderer object
   */
  public void renderImage() {
    if (imageWriter == null)
      throw new MissingResourceException(RESOURCE_ERROR, RENDER_CLASS, IMAGE_WRITER_COMPONENT);
    if (rayTracer == null)
      throw new MissingResourceException(RESOURCE_ERROR, RENDER_CLASS, RAY_TRACER_COMPONENT);
    if (rayTracer.scene.getCamera() == null)
      throw new MissingResourceException(RESOURCE_ERROR, RENDER_CLASS, CAMERA_COMPONENT);

    long startTime = System.currentTimeMillis();

    if (threadsCount == 0) {
      Camera camera = rayTracer.scene.getCamera();
      final int nX = imageWriter.getNx();
      final int nY = imageWriter.getNy();
      final double pixelWidth = camera.getWidth() / imageWriter.getNx();
      final double pixelHeight = camera.getHeight() / imageWriter.getNy();
      for (int i = 0; i < nY; ++i)
        for (int j = 0; j < nX; ++j)
          castRay(nX, nY, pixelWidth, pixelHeight, j, i);
    } else {
      renderImageThreaded();
    }

    long endTime = System.currentTimeMillis();
    System.out.println((endTime - startTime) / 1000.0 + " seconds");
  }

  /**
   * Cast ray from camera in order to color a pixel
   * 
   * @param nX          resolution on X axis (number of pixels in row)
   * @param nY          resolution on Y axis (number of pixels in column)
   * @param pixelWidth  camera width / number of pixels wide
   * @param pixelHeight camera height / number of pixels high
   * @param col         pixel's column number (pixel index in row)
   * @param row         pixel's row number (pixel index in column)
   */
  private void castRay(int nX, int nY, double pixelWidth, double pixelHeight, int col, int row) {
    Camera camera = rayTracer.scene.getCamera();
    Ray ray = camera.constructRayThroughPixel(nX, nY, col, row);
    Color pixelColor;
    // adaptive supersampling is enabled
    if (supersamplingType == SUPERSAMPLING_TYPE.ADAPTIVE) {
      pixelColor = getAdaptiveSupersamplingColor(ray, pixelWidth, pixelHeight);
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
  }

  /**
   * Get the color for adaptive supersampling
   * 
   * @param center      ray for original pixel location
   * @param pixelWidth  camera width / number of pixels wide
   * @param pixelHeight camera height / number of pixels high
   * @return supersampling average color
   */
  private Color getAdaptiveSupersamplingColor(Ray center, double pixelWidth, double pixelHeight) {
    Scene scene = rayTracer.scene;
    Camera camera = scene.getCamera();
    return adaptiveSupersamplingRecursive(center, pixelWidth, pixelHeight, camera, adaptiveMaxRecursionLevel);
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
   * @param level        recursion level - stops when reaches 1
   * @return average color of the cell
   */
  private Color adaptiveSupersamplingRecursive(Ray center, double cellWidth, double cellHeight, Camera camera,
      int level) {

    // compute half cell width for locating rays and for recursive calls
    double halfCellWidth = cellWidth / 2;
    double halfCellHeight = cellHeight / 2;

    // calculate the centers of each quarter of the cell
    List<Ray> corners = camera.getAdaptiveSupersamplingRays(center, halfCellWidth, halfCellHeight);

    // get colors for each ray
    List<Color> cornerColors = corners.stream().map(ray -> rayTracer.traceRay(ray)).collect(Collectors.toList());

    // stop when maximum recursion level is reached
    if (level <= 1) {
      // return average of the quadrant colors
      return cornerColors.get(0).add(cornerColors.get(1), cornerColors.get(2), cornerColors.get(3)).reduce(4);
    }

    // if all centers are the same color, return any quadrant color
    if (cornerColors.get(0).same(cornerColors.get(1)) //
        && cornerColors.get(0).same(cornerColors.get(2)) //
        && cornerColors.get(0).same(cornerColors.get(3))) {
      return cornerColors.get(0);
    }

    // calculate average colors of the four quadrants
    return adaptiveSupersamplingRecursive(corners.get(0), halfCellWidth, halfCellHeight, camera, level - 1)
        .add(adaptiveSupersamplingRecursive(corners.get(1), halfCellWidth, halfCellHeight, camera, level - 1),
            adaptiveSupersamplingRecursive(corners.get(2), halfCellWidth, halfCellHeight, camera, level - 1),
            adaptiveSupersamplingRecursive(corners.get(3), halfCellWidth, halfCellHeight, camera, level - 1))
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
    double pixelWidth = camera.getWidth() / imageWriter.getNx();
    double pixelHeight = camera.getHeight() / imageWriter.getNy();
    // list for returning rays
    List<Ray> supersamplingRays = camera.getSupersamplingRays(middleRay, gridSize, pixelWidth, pixelHeight);
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
   * @throws MissingResourceException if image writer is missing
   */
  public void printGrid(int interval, Color color) {
    if (imageWriter == null)
      throw new MissingResourceException(RESOURCE_ERROR, RENDER_CLASS, IMAGE_WRITER_COMPONENT);
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

  /**
   * Set multi-threading - if the parameter is 0, number of cores less 2 is taken
   * 
   * @param threads number of threads
   * @return the Render object itself
   */
  public Render setMultithreading(int threads) {
    if (threads < 0)
      throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
    if (threads != 0)
      this.threadsCount = threads;
    else {
      int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
      this.threadsCount = cores <= 2 ? 1 : cores;
    }
    return this;
  }

  /**
   * Set debug printing on
   * 
   * @return the Render object itself
   */
  public Render setDebugPrint() {
    print = true;
    return this;
  }

}