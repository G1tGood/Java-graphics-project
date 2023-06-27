package improvementTests;

import geometries.Sphere;
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
public class PictureTests {
    /** produces one image with anti aliasing and one without */
    @Test
    public void testAntiAliasing() {
        Scene scene = new Scene("Test scene")
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

        Camera aaCamera = new Camera(
                new Point(500,0,0),
                new Vector(-1,0,0),
                new Vector(0,0,1))
                .setVPSize(200,200)
                .setVPDistance(200);
        ImageWriter imageWriter = new ImageWriter("Anti aliasing off", 300, 300);
        aaCamera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();

        imageWriter = new ImageWriter("anti aliasing on", 300, 300);
        aaCamera.setAntiAliasing(36)
                .setImageWriter(imageWriter)
                .renderImage()
                .writeToImage();
    }

    @Test
    public void testDOF() {
        Scene scene = new Scene("Test scene")
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

        Camera dofCamera = new Camera(
                new Point(500,0,0),
                new Vector(-1,0,0),
                new Vector(0,0,1))
                .setVPSize(200,200)
                .setVPDistance(200);

        ImageWriter imageWriter = new ImageWriter("DOF off", 300, 300);
        dofCamera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();

        imageWriter = new ImageWriter("DOF on", 300, 300);
        dofCamera.setImageWriter(imageWriter)
                .setDOF(25,300,20)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }

    /** test for dof and AA simultaneously */
    @Test
    public void testDOF_AA() {
        Scene scene = new Scene("Test scene")
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

        Camera dof_aaCamera = new Camera(
                new Point(500,0,0),
                new Vector(-1,0,0),
                new Vector(0,0,1))
                .setVPSize(200,200)
                .setVPDistance(200);
        ImageWriter imageWriter = new ImageWriter("DOF&AA off", 300, 300);
        dof_aaCamera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();

        imageWriter = new ImageWriter("DOF&AA on", 300, 300);
        dof_aaCamera.setImageWriter(imageWriter)
                .setDOF(25,300,20)
                .setAntiAliasing(36)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }
}

