package unittests.rendererTest;

import org.junit.jupiter.api.Test;
import primitives.Color;
import renderer.ImageWriter;

/** Testing image writer
 * @author Avishai Sachor and Yoav Babayoff */
public class ImageWriterTest {
    @Test
    void imageCreationTest() {
        ImageWriter imageWriter = new ImageWriter("banana", 801, 501);
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        for (int i = 0; i < nX; ++i) {
            for (int j = 0; j < nY; ++j) {
                if (i % 50 == 0 || j % 50 == 0)  imageWriter.writePixel(i, j, new Color(255,0,0));
                else imageWriter.writePixel(i, j, new Color(255,255,0));
            }
        }
        imageWriter.writeToImage();
    }
}
