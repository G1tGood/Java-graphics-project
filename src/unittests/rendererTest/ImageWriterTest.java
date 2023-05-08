package unittests.rendererTest;

import org.junit.jupiter.api.Test;
import primitives.Color;
import renderer.ImageWriter;

/** Testing image writer
 * @author Avishai Sachor and Yoav Babayoff */
public class ImageWriterTest {
    @Test
    void imageCreationTest() {
        ImageWriter imageWriter = new ImageWriter("image_creation_test", 801, 501);
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; ++j) {
                imageWriter.writePixel(j, i, new Color(255,255,0));
            }
        }        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; j+=50) {
                imageWriter.writePixel(j, i, new Color(255,0,0));
            }
        }
        for (int i = 0; i < nY; i+=50) {
            for (int j = 0; j < nX; ++j) {
                imageWriter.writePixel(j, i, new Color(255,0,0));
            }
        }
        imageWriter.writeToImage();
    }
}
