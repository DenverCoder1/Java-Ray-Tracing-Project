package unittests.renderer;

import org.junit.Test;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import primitives.Color;
import renderer.ImageWriter;

/**
 * Test the ImageWriter by writing grid to each pixel in the image
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 * 
 */
public class ImageWriterTest {

  /**
   * {@link ImageWriter.writeToImage#writeToImage()}.
   */
  @Test
  public void testWriteToImage() {
    int nX = 800;
    int nY = 500;
    ImageWriter imageWriter = new ImageWriter("test1", nX, nY);
    for (int col = 0; col < nX; col++) {
      for (int row = 0; row < nY; row++) {
        if (col % 50 == 0 || row % 50 == 0) {
          imageWriter.writePixel(col, row, new Color(java.awt.Color.ORANGE));
        } else {
          imageWriter.writePixel(col, row, new Color(java.awt.Color.BLUE));
        }
      }
    }
    File file = imageWriter.writeToImage();
    // open in image viewer
    try {
      Desktop.getDesktop().open(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}