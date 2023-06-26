package finalImage;

import primitives.*;
import geometries.*;
import renderer.*;
import lighting.*;
import scene.Scene;

import static java.awt.Color.WHITE;

public class finalImage {
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
        Scene scene = new Scene("final scene");
        ImageWriter imageWriter = new ImageWriter("final image", 600, 600);
        Camera camera =
                new Camera(
                        new Point(0,0,0),
                        new Vector(0,1,0),
                        new Vector(4,0,7))
                        .setVPSize(200,200)
                        .setVPDistance(20)
                        .setMultiThreading(3,1);

        scene.setAmbientLight(
                        new AmbientLight(new Color(WHITE),
                                0.15)
                )
                .setBackground(new Color(25,25,112));

        camera.setMultiThreading(3,100)
                .setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }
}
