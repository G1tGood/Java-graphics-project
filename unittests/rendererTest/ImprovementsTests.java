package rendererTest;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.RayTracerBasic;
import scene.Scene;

import static java.awt.Color.*;

/** test improvements of ray tracing
 * @author Yoav Babayoff and Avishai Sachor
 */
public class ImprovementsTests {
    private Scene scene = new Scene("Test scene");

    /** produces one image with anti aliasing and one without */
    @Test
    public void testAntiAliasing() {
        Camera camera =
                new Camera(
                        new Point(500,0,0),
                        new Vector(-1,0,0),
                        new Vector(0,0,1))
                        .setVPSize(200,200)
                        .setVPDistance(200);

        scene.setAmbientLight(
                        new AmbientLight(new Color(WHITE),
                                0.15)
                )
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

        ImageWriter imageWriter = new ImageWriter("test 1", 600, 600);
        camera.setAntiAliasing(25);
        camera.setDOF(25,300,20)
                .setMultiThreading(3,100).setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();

        camera.setASS();
        imageWriter = new ImageWriter("test 2", 600, 600);
        camera.setAntiAliasing(25);
        camera.setDOF(25,300,20)
                .setMultiThreading(3,100).setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }
}
