package xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lighting.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import primitives.*;
import geometries.*;
import renderer.ImageWriter;
import scene.Scene;

import java.io.IOException;
import java.util.LinkedList;

/** functional interface for parsing xml documents
 * @author Yoav Babayoff and Avishai Sachor
 */
public interface XMLParser {
    /**
     * parses scene from xml file
     *
     * @param filename xml file path
     * @param scene    scene object to initialize
     * @throws ParserConfigurationException if parser configuration went wrong
     * @throws IOException                  if parsing xml file went wrong
     * @throws SAXException                 if parsing xml file went wrong
     */
    public static void parseXMLScene(String filename, Scene scene) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(filename);
        Element sceneElement = (Element) document.getElementsByTagName("scene").item(0);
        double x, y, a, b, c, d, e, f;
        if (sceneElement == null) return;
        String attribute;
        String[] coords;
        // background color
        scene.setBackground(getColor(sceneElement, "background-color"));

        // ambient light
        Element ambientLight = (Element) sceneElement.getElementsByTagName("ambient-light").item(0);
        if (ambientLight.hasAttribute("Ka")) {
            try {
                scene.setAmbientLight(new AmbientLight(
                        getColor(ambientLight, "color"),
                        Double.parseDouble(ambientLight.getAttribute("Ka"))
                ));
            } catch (Exception ignored1) {
                try {
                    scene.setAmbientLight(new AmbientLight(
                            getColor(ambientLight, "color"),
                            getDouble3(ambientLight, "Ka")
                    ));
                } catch (Exception ignored2) {
                    throw new IllegalArgumentException("must receive 3, 1 or no number(s)");
                }
            }
        } else scene.setAmbientLight(new AmbientLight(getColor(ambientLight, "color"), 1));

        Element geometry;
        // geometries
        Element geometries = (Element) sceneElement.getElementsByTagName("geometries").item(0);
        if (geometries != null) {
            // spheres
            NodeList geometryNodes = geometries.getElementsByTagName("sphere");
            for (int i = 0; i < geometryNodes.getLength(); i++) {
                geometry = (Element) geometryNodes.item(i);
                scene.geometries.add(new Sphere(
                        getPoint(geometry, "center"),
                        Double.parseDouble(geometry.getAttribute("radius")))
                        .setMaterial(getMaterial(geometry))
                        .setEmission(getEmission(geometry))
                );
            }
            // triangles
            geometryNodes = geometries.getElementsByTagName("triangle");
            for (int i = 0; i < geometryNodes.getLength(); i++) {
                geometry = (Element) geometryNodes.item(i);
                try {
                    scene.geometries.add(new Triangle(
                            getPoint(geometry, "p0"),
                            getPoint(geometry, "p1"),
                            getPoint(geometry, "p2"))
                            .setMaterial(getMaterial(geometry))
                            .setEmission(getEmission(geometry))
                    );
                }catch (Exception ignored) {}
            }
            // planes
            geometryNodes = geometries.getElementsByTagName("plane");
            for (int i = 0; i < geometryNodes.getLength(); i++) {
                geometry = (Element) geometryNodes.item(i);
                if (geometry.hasAttribute("p")) {
                    scene.geometries.add(new Plane(getPoint(geometry, "p"), getVector(geometry, "normal")));
                } else {
                    scene.geometries.add(new Plane(
                            getPoint(geometry, "p0"),
                            getPoint(geometry, "p1"),
                            getPoint(geometry, "p2"))
                            .setMaterial(getMaterial(geometry))
                            .setEmission(getEmission(geometry))
                    );
                }
            }
            // cylinders
            geometryNodes = geometries.getElementsByTagName("cylinder");
            for (int i = 0; i < geometryNodes.getLength(); i++) {
                geometry = (Element) geometryNodes.item(i);
                x = Double.parseDouble(geometry.getAttribute("radius"));
                y = Double.parseDouble(geometry.getAttribute("height"));
                coords = geometry.getAttribute("axis-ray").split("\\s+");
                if (coords.length != 8) throw new IllegalArgumentException("must receive 6 numbers");
                a = Double.parseDouble(coords[1]);
                b = Double.parseDouble(coords[2]);
                c = Double.parseDouble(coords[3]);
                d = Double.parseDouble(coords[5]);
                e = Double.parseDouble(coords[6]);
                f = Double.parseDouble(coords[7]);
                scene.geometries.add(new Cylinder(
                        new Ray(
                                new Point(a, b, c),
                                new Vector(d, e, f)),
                        x,
                        y)
                        .setMaterial(getMaterial(geometry))
                        .setEmission(getEmission(geometry))
                );
            }
            // tubes
            geometryNodes = geometries.getElementsByTagName("tube");
            for (int i = 0; i < geometryNodes.getLength(); i++) {
                geometry = (Element) geometryNodes.item(i);
                x = Double.parseDouble(geometry.getAttribute("radius"));
                attribute = geometry.getAttribute("axis-ray");
                coords = attribute.split("\\s+");
                if (coords.length != 8) throw new IllegalArgumentException("must receive 6 numbers");
                a = Double.parseDouble(coords[1]);
                b = Double.parseDouble(coords[2]);
                c = Double.parseDouble(coords[3]);
                d = Double.parseDouble(coords[5]);
                e = Double.parseDouble(coords[6]);
                f = Double.parseDouble(coords[7]);
                scene.geometries.add(new Tube(new Ray(
                        new Point(a, b, c),
                        new Vector(d, e, f)),
                        x)
                        .setMaterial(getMaterial(geometry))
                        .setEmission(getEmission(geometry))
                );
            }
            // polygons
            geometryNodes = geometries.getElementsByTagName("polygon");
            for (int i = 0; i < geometryNodes.getLength(); i++) {
                geometry = (Element) geometryNodes.item(i);
                String count = "0";
                LinkedList<Point> points = new LinkedList<>();
                while (geometry.hasAttribute("s" + count)) {
                    points.add(getPoint(geometry, "s" + count));
                }
                Point[] array = points.toArray(new Point[0]);
                scene.geometries.add(new Polygon(array)
                        .setMaterial(getMaterial(geometry))
                        .setEmission(getEmission(geometry))
                );
            }
        }

        // lights
        if (sceneElement.hasAttribute("lights")) {
            String tagName;
            Element lights = (Element) sceneElement.getElementsByTagName("lights");
            NodeList lightNodes = lights.getElementsByTagName("*");
            for (int i = 0; i < lightNodes.getLength(); i++) {
                Element light = (Element) lightNodes.item(i);
                tagName = light.getTagName();
                switch (tagName) {
                    case "directional-light" ->
                            scene.lights.add(new DirectionalLight(getColor(light, "intensity"), getVector(light, "direction")));
                    case "point-light" -> {
                        PointLight pl = new PointLight(getColor(light, "intensity"), getPoint(light, "position"));
                        lightsAttributesHelper(light, pl);
                        scene.lights.add(pl);
                    }
                    case "spot-light" -> {
                        SpotLight sl;
                        sl = new SpotLight(getColor(light, "intensity"), getPoint(light, "position"), getVector(light, "direction"));
                        lightsAttributesHelper(light, sl);
                        scene.lights.add(sl);
                    }
                }
            }
        }
    }

    /**
     * helper function for Emission light of a geometry
     *
     * @param geometry geometry
     * @return emission light color
     */
    private static Color getEmission(Element geometry) {
        if (geometry.hasAttribute("emission-light")) {
            return getColor(geometry, "emission-light");
        } else return Color.BLACK;
    }

    /**
     * helper function for material for geometry
     *
     * @param geometry the geometry element in xml file
     * @return the material of geometry
     */
    private static Material getMaterial(Element geometry) {
        double a;
        // material
        Material mat = new Material();

        // attenuation coefficients
        String[] atenCoeffs = {"Kd", "Kt", "Ks", "Kr"};
        for (String s : atenCoeffs) {
            if (geometry.hasAttribute(s)) {
                try {
                    switch (s) {
                        case "Kd" -> mat.setKd(getDouble3(geometry, "Kd"));
                        case "Ks" -> mat.setKs(getDouble3(geometry, "Ks"));
                        case "Kr" -> mat.setKr(getDouble3(geometry, "Kr"));
                        case "Kt" -> mat.setKt(getDouble3(geometry, "Kt"));
                        default -> throw new IllegalStateException("Unexpected value: " + s);
                    }
                    continue;
                } catch (Exception ignored) {
                }
                try {
                    a = Double.parseDouble(geometry.getAttribute(s));
                    switch (s) {
                        case "Kd" -> mat.setKd(a);
                        case "Ks" -> mat.setKs(a);
                        case "Kr" -> mat.setKr(a);
                        case "Kt" -> mat.setKt(a);
                        default -> throw new IllegalStateException("Unexpected value: " + s);
                    }
                    continue;
                } catch (Exception ignored) {
                    throw new IllegalArgumentException("attenuation coefficients must have 3-number or 1-number values");
                }
            }
        }
        // shininess
        if (geometry.hasAttribute("shininess")) {
            mat.setShininess(Integer.parseInt(geometry.getAttribute("shininess")));
        }
        return mat;
    }

    /**
     * method for initializing an image writer based on a xml file
     *
     * @param filename xml file path
     * @return imageWriter object from file
     * @throws ParserConfigurationException if parser configuration went wrong
     * @throws IOException                  if parsing xml file went wrong
     * @throws SAXException                 if parsing xml file went wrong
     */
    public static ImageWriter parseXMLImageWriter(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(filename);
        Element imageWriterElement = (Element) document.getElementsByTagName("imageWriter").item(0);
        if (imageWriterElement != null) {
            String imageName = imageWriterElement.getAttribute("name");
            int Nx = Integer.parseInt(imageWriterElement.getAttribute("Nx"));
            int Ny = Integer.parseInt(imageWriterElement.getAttribute("Ny"));
            return new ImageWriter(imageName, Nx, Ny);
        }
        return null;
    }

    /**
     * help method for setting attenuation coefficients for point light and spotlight
     *
     * @param light light element in XML file
     * @param pl    light source
     */
    private static void lightsAttributesHelper(Element light, PointLight pl) {
        String[] ac = {"Kl", "Kc", "Kq"};
        String attribute;
        double a;
        for (String s : ac) {
            if (light.hasAttribute(s)) {
                attribute = light.getAttribute(s);
                a = Double.parseDouble(attribute);
                switch (s) {
                    case "Kl" -> pl.setKl(a);
                    case "Kc" -> pl.setKc(a);
                    case "Kq" -> pl.setKq(a);
                    default -> throw new IllegalStateException("Unexpected value: " + s);
                }
            }
        }
    }

    /**
     * helper function for vector of element in xml file
     *
     * @param element element in file
     * @param name    name of vector in xml file
     * @return the direction vector
     * @throws IllegalArgumentException if vector in XML file doesn't have exactly 3 coordinates
     */
    private static Vector getVector(Element element, String name) {
        String attribute = element.getAttribute(name);
        String[] coords = attribute.split("\\s+");
        if (coords.length != 3) throw new IllegalArgumentException("must receive 3 numbers");
        return (new Vector(
                Double.parseDouble(coords[0]),
                Double.parseDouble(coords[1]),
                Double.parseDouble(coords[2])
        ));
    }

    /**
     * helper function for getting color of some element from xml file
     *
     * @param element element in XML file
     * @param name    name of color in xml file
     * @return color of element
     * @throws IllegalArgumentException if color in XML file doesn't have exactly 3 numbers
     */
    private static Color getColor(Element element, String name) {
        String attribute = element.getAttribute(name);
        String[] coords = attribute.split("\\s+");
        if (coords.length != 3) throw new IllegalArgumentException("must receive 3 numbers");
        return (new Color(
                Double.parseDouble(coords[0]),
                Double.parseDouble(coords[1]),
                Double.parseDouble(coords[2])
        ));
    }

    /**
     * helper getter function for point of element in xml file
     *
     * @param element element in XML file
     * @param name    name of point in xml file
     * @return point
     * @throws IllegalArgumentException if point in XML file doesn't have exactly 3 numbers
     */
    private static Point getPoint(Element element, String name) {
        String attribute = element.getAttribute(name);
        String[] coords = attribute.split("\\s+");
        if (coords.length != 3) throw new IllegalArgumentException("must receive 3 numbers");
        return (new Point(
                Double.parseDouble(coords[0]),
                Double.parseDouble(coords[1]),
                Double.parseDouble(coords[2])
        ));
    }

    /**
     * helper getter function for a Double3 of element in xml file
     *
     * @param element element in XML file
     * @param name    name of Double3 in xml file
     * @return Double3
     * @throws IllegalArgumentException if Double3 in XML file doesn't have exactly 3 numbers
     */
    private static Double3 getDouble3(Element element, String name) {
        String attribute = element.getAttribute(name);
        String[] coords = attribute.split("\\s+");
        if (coords.length != 3) throw new IllegalArgumentException("must receive 3 numbers");
        return (new Double3(
                Double.parseDouble(coords[0]),
                Double.parseDouble(coords[1]),
                Double.parseDouble(coords[2])
        ));
    }
}