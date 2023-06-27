package improvementTests;

import geometries.Sphere;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.RayTracerBasic;
import scene.Scene;

import static java.awt.Color.*;
import static java.awt.Color.RED;

public class AccelerationTests {
    /** test for adaptive super-sampling */
    @Test
    public void testASS() {
        Scene scene = new Scene("ASS scene")
                .setAmbientLight(
                        new AmbientLight(
                                new Color(WHITE),
                                0.15))
                .setBackground(new Color(WHITE));

        scene.geometries.add(
                new Sphere(
                        new Point(0,0,0),
                        30
                )
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material()
                                .setKr(0.2)
                                .setKs(0.2)
                                .setShininess(60)),
                new Sphere(
                        new Point(-1000,0,0),
                        500
                )
                        .setEmission(new Color(YELLOW))
                        .setMaterial(new Material()
                                .setKr(0.2)
                                .setKs(0.2)
                                .setShininess(60)),
                new Sphere(
                        new Point(-100,120,0),
                        50
                )
                        .setEmission(new Color(RED))
                        .setMaterial(new Material()
                                .setKr(0.2)
                                .setKs(0.2)
                                .setShininess(60))


        );


        scene.lights.add(
                new SpotLight(
                        new Color(700, 400, 400),
                        new Point(800, 400, 0),
                        new Vector(-1, -2, 0)
                )
                        .setKl(4E-5)
                        .setKq(2E-7)
        );

        Camera camera = new Camera(
                new Point(500,0,0),
                new Vector(-1,0,0),
                new Vector(0,0,1))
                .setVPSize(200,200)
                .setVPDistance(200);
        ImageWriter imageWriter = new ImageWriter("ASS off", 300, 300);
        camera.setImageWriter(imageWriter)
                .setDOF(25,300,20)
                .setAntiAliasing(25)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();

        imageWriter = new ImageWriter("ASS on", 300, 300);
        camera.setImageWriter(imageWriter)
                .setASS()
                .setDOF(25,300,20)
                .setAntiAliasing(25)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }

    /** test for multi-threading */
    @Test
    public void testMTT() {
        Scene scene = new Scene("MTT scene")
                .setAmbientLight(
                        new AmbientLight(
                                new Color(WHITE),
                                0.15))
                .setBackground(new Color(WHITE));

        scene.geometries.add(
                new Sphere(
                        new Point(0,0,0),
                        30
                )
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material()
                                .setKr(0.2)
                                .setKs(0.2)
                                .setShininess(60)),
                new Sphere(
                        new Point(-1000,0,0),
                        500
                )
                        .setEmission(new Color(YELLOW))
                        .setMaterial(new Material()
                                .setKr(0.2)
                                .setKs(0.2)
                                .setShininess(60)),
                new Sphere(
                        new Point(-100,120,0),
                        50
                )
                        .setEmission(new Color(RED))
                        .setMaterial(new Material()
                                .setKr(0.2)
                                .setKs(0.2)
                                .setShininess(60))


        );


        scene.lights.add(
                new SpotLight(
                        new Color(700, 400, 400),
                        new Point(800, 400, 0),
                        new Vector(-1, -2, 0)
                )
                        .setKl(4E-5)
                        .setKq(2E-7)
        );

        Camera camera = new Camera(
                new Point(500,0,0),
                new Vector(-1,0,0),
                new Vector(0,0,1))
                .setVPSize(200,200)
                .setVPDistance(200);
        ImageWriter imageWriter = new ImageWriter("MTT off", 300, 300);
        camera.setImageWriter(imageWriter)
                .setDOF(25,300,20)
                .setAntiAliasing(25)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();

        imageWriter = new ImageWriter("MTT on", 300, 300);
        camera.setImageWriter(imageWriter)
                .setASS()
                .setDOF(25,300,20)
                .setAntiAliasing(25)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }

    /** test for adaptive super-sampling AND multi-threading*/
    @Test
    public void testASS_MTT() {
        Scene scene = new Scene("ASS_MTT scene")
                .setAmbientLight(
                        new AmbientLight(
                                new Color(WHITE),
                                0.15))
                .setBackground(new Color(WHITE));

        scene.geometries.add(
                new Sphere(
                        new Point(0,0,0),
                        30
                )
                        .setEmission(new Color(BLUE))
                        .setMaterial(new Material()
                                .setKr(0.2)
                                .setKs(0.2)
                                .setShininess(60)),
                new Sphere(
                        new Point(-1000,0,0),
                        500
                )
                        .setEmission(new Color(YELLOW))
                        .setMaterial(new Material()
                                .setKr(0.2)
                                .setKs(0.2)
                                .setShininess(60)),
                new Sphere(
                        new Point(-100,120,0),
                        50
                )
                        .setEmission(new Color(RED))
                        .setMaterial(new Material()
                                .setKr(0.2)
                                .setKs(0.2)
                                .setShininess(60))


        );


        scene.lights.add(
                new SpotLight(
                        new Color(700, 400, 400),
                        new Point(800, 400, 0),
                        new Vector(-1, -2, 0)
                )
                        .setKl(4E-5)
                        .setKq(2E-7)
        );

        Camera camera = new Camera(
                new Point(500,0,0),
                new Vector(-1,0,0),
                new Vector(0,0,1))
                .setVPSize(200,200)
                .setVPDistance(200);
        ImageWriter imageWriter = new ImageWriter("ASS&MTT off", 300, 300);
        camera.setImageWriter(imageWriter)
                .setDOF(25,300,20)
                .setAntiAliasing(25)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();

        imageWriter = new ImageWriter("ASS&MTT on", 300, 300);
        camera.setImageWriter(imageWriter)
                .setASS()
                .setMultiThreading(3,1)
                .setDOF(25,300,20)
                .setAntiAliasing(25)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }
}
