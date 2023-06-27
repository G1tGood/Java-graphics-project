package finalImage;

import org.xml.sax.SAXException;
import primitives.*;
import geometries.*;
import renderer.*;
import lighting.*;
import scene.Scene;
import xml.XMLParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static java.awt.Color.RED;
import static java.awt.Color.WHITE;

/** final picture class
 * @author Yoav Babayoff and Avishai Sachor
 */
public final class finalImage {
    private static Scene scene = new Scene("final scene");
    public static void main(String[] args) {
        Material monkeybodyMat = new Material()
                .setKd(0.3)
                .setKs(0.6)
                .setShininess(5)
                .setKr(0.1),
                monkeyheadMat = new Material()
                        .setKd(0.6)
                        .setKs(0.3),
                propelorMat = new Material()
                        .setKd(0.3)
                        .setKs(0.3),
                planeMat = new Material()
                        .setKs(0.6)
                        .setKd(0.2)
                        .setKr(0.2)
                        .setShininess(4),
                planepilotMat = new Material()
                        .setKt(0.8)
                        .setKr(0.1)
                        .setKs(0.1);
        Color monkeybodyColor = new Color(java.awt.Color.darkGray),
                monkeyheadColor = new Color(java.awt.Color.BLACK),
                propelorColor = new Color(java.awt.Color.GRAY),
                planeColor = new Color(java.awt.Color.RED),
                planepilotColor = new Color(java.awt.Color.WHITE);

        try {
            XMLParser.parseXMLScene("src/xml/finalimage.xml",scene);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        Plane plane = (Plane) new Plane(new Point(0,0,-40),new Vector(0,0,1))
                .setEmission(new Color(88,57,39));
        scene.geometries.add(plane);
        scene.lights.add(new PointLight(new Color(RED),new Point(-5,60.6,22.5)));
        ImageWriter imageWriter = new ImageWriter("final image", 1500, 1500);
        Camera camera =
                new Camera(
                        new Point(-4,-20.6,14),
                        new Vector(0,1,-0.1),
                        new Vector(3,1,10))
                        .setVPSize(2.5,2)
                        .setVPDistance(2.8)
                        .setAntiAliasing(17*17)
                        .setASS()
                        .setDOF(17*17,79.6,2)
                        .setMultiThreading(3,1);

        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }
}
