package raytracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.Color;
import org.ejml.simple.SimpleMatrix;

public class Camera extends Entity {

    static final MyColor DEFAULT_COLOR = new MyColor(0, 0, 255, false).normalize();

    private static SimpleMatrix worldToNodeMatrix;

    public Vector n;
    public Vector u;
    public Vector v;
    public Vector cLookAtDir;

    private Point wLookAt;
    private Point cLookAt;
    private Point cPosition = new Point(0, 0, 0, Point.Space.CAMERA);

    public static int scaleRatio = 80;

    public Point getcPosition() {
        return cPosition;
    }

    public void setcPosition(Point cPosition) {
        this.cPosition = cPosition;
    }

    private Vector cameraUp;

    private double focalLength;

    private FilmPlane filmPlane;

    private World world;

    public Camera(Point cameraPosition, Vector cameraUp, Point cameraLookAt, double cameraFocalLength) {

        super(null, cameraPosition);
        this.position = cameraPosition;
        this.cameraUp = cameraUp;
        this.wLookAt = cameraLookAt;
        this.focalLength = cameraFocalLength;

        this.n = Util.subtract(position, wLookAt).normalize();
        this.u = Util.cross(cameraUp, n).normalize();
        this.v = Util.cross(n, u).normalize();

        Vector wCameraPos = new Vector(position.x, position.y, position.z);
        //
        Camera.worldToNodeMatrix = new SimpleMatrix(new double[][] {
                { u.x, v.x, n.x, 0 },
                { u.y, v.y, n.y, 0 },
                { u.z, v.z, n.z, 0 },
                { -1.0 * Util.dot(wCameraPos, u), -1.0 * Util.dot(wCameraPos, v), -1.0 *
                        Util.dot(wCameraPos, n), 1 }
        });

        this.cLookAt = toCameraSpace(wLookAt);
        this.cPosition = toCameraSpace(this.position);
        this.cLookAtDir = Util.subtract(cLookAt, cPosition).normalize();

        Vector filmPlanePosition = Util.scale(this.cLookAtDir, this.focalLength);
        // this.filmPlane = new FilmPlane(16, 10, 640, 400, filmPlanePosition);
        this.filmPlane = new FilmPlane(16, 10, 16 * scaleRatio, 10 * scaleRatio, filmPlanePosition);

        System.out.println(this);
    }

    //
    // static methods
    //

    public static Vector toCameraSpace(Vector v) {
        Vector vector = Vector.getVectorFromMatrix(v.getMatrix().copy().mult(Camera.worldToNodeMatrix));
        // vector.setSpace(Point.Space.CAMERA);
        return vector;
    }

    public static Point toCameraSpace(Point p) {
        Point point = Point.getPointFromMatrix(p.getMatrix().copy().mult(Camera.worldToNodeMatrix));
        point.setSpace(Point.Space.CAMERA);
        return point;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void takeASnap(int subpixelsCount) {
        subpixelsCount = Math.max(subpixelsCount, 1);
        int subPixelCountSquare = subpixelsCount * subpixelsCount;

        for (Pixel pixel : filmPlane) {

            ArrayList<Pixel> subPixels = pixel.getSubPixels(subpixelsCount);
            MyColor color = new MyColor(0, 0, 0, true);
            for (Pixel subPixel : subPixels) {
                Vector dir = new Vector(subPixel.cPosition);
                dir.normalize();
                Ray ray = new Ray(this.cPosition, dir);
                // color.addColor(this.world.getPixelIrradiance(ray));
                color.addColor(this.world.illuminate(ray, 0));
            }
            color.r /= subPixelCountSquare;
            color.g /= subPixelCountSquare;
            color.b /= subPixelCountSquare;
            // Vector dir = new Vector(pixel.cPosition);
            // dir.normalize();
            // Ray ray = new Ray(this.cPosition, dir);
            // MyColor color = this.world.getPixelIrradiance(ray);
            pixel.setValue(color);
        }
    }

    public void denormalizeColors() {
        for (Pixel pixel : filmPlane) {
            pixel.color.denormalize();
        }
    }

    public BufferedImage generateImage() {
        // System.out.println(this.filmPlane);
        BufferedImage rgbImage = new BufferedImage(filmPlane.numPixelsWidth,
                filmPlane.numPixelsHeight,
                BufferedImage.TYPE_3BYTE_BGR);

        for (Pixel pixel : filmPlane) {
            // System.out.println(pixel.color);

            MyColor denormColor = pixel.color.denormalize();

            Color color = new Color((int) denormColor.r, (int) denormColor.g, (int) denormColor.b);
            int actualRow = filmPlane.numPixelsHeight - pixel.row - 1;
            rgbImage.setRGB(pixel.col, actualRow, color.getRGB());
        }

        writeImgToFile(rgbImage);
        return rgbImage;
    }

    public void writeImgToFile(BufferedImage bi) {
        try {

            File outputfile = new File("saved.png");
            ImageIO.write(bi, "jpg", outputfile);
        } catch (IOException e) {
            System.out.println("error while writing image file " + e);
        }
    }

    public void capToOne() {
        for (Pixel pixel : filmPlane) {
            pixel.color.r = Math.max(0, Math.min(1, pixel.color.r));
            pixel.color.g = Math.max(0, Math.min(1, pixel.color.g));
            pixel.color.b = Math.max(0, Math.min(1, pixel.color.b));
        }
    }

    public void normalizeAcrossPixels() {
        for (Pixel pixel : filmPlane) {
            pixel.color.r = Math.max(0, Math.min(1, (pixel.color.r - Pixel.minR) / (Pixel.maxR - Pixel.minR)));
            pixel.color.g = Math.max(0, Math.min(1, (pixel.color.g - Pixel.minG) / (Pixel.maxG - Pixel.minG)));
            pixel.color.b = Math.max(0, Math.min(1, (pixel.color.b - Pixel.minB) / (Pixel.maxB - Pixel.minB)));
        }
    }

    public void applyToneMapping() {
        // this.capToOne();
        this.normalizeAcrossPixels();
    }

    @Override
    public IntersectionDetails intersect(Ray ray) {
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Vector upCam = toCameraSpace(cameraUp).normalize();
        Point centerCam = toCameraSpace(this.position);
        Point lookAtCam = toCameraSpace(wLookAt);
        Vector lookAtDir = Util.subtract(lookAtCam, new Point(0, 0, 0, Point.Space.CAMERA));
        upCam.normalize();
        lookAtDir.normalize();

        sb.append("CAMERA : \n");
        sb.append("\tposition " + position + "\n");
        sb.append("\tlookAt " + wLookAt + "\n");
        sb.append("\tup " + cameraUp + "\n");
        sb.append("\tn " + n + "\n");
        sb.append("\tu " + u + "\n");
        sb.append("\tv " + v + "\n");
        sb.append("\tcenterCam " + centerCam + "\n");
        sb.append("\tupCam " + upCam + "\n");
        sb.append("\tlookatCam " + lookAtDir + "\n");
        sb.append("\tviewmatrix " + worldToNodeMatrix + "\n");

        return sb.toString();
    }

    public void setScaleRatio(int scale) {
        this.scaleRatio = scale;
    }

}
