package raytracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.Color;
import org.ejml.simple.SimpleMatrix;

import raytracer.ToneMapping.ToneCompressor;

public class Camera extends Entity {

    // static final Irradiance DEFAULT_COLOR = new Irradiance(128, 128, 128,
    // false).normalize();
    static final Irradiance DEFAULT_COLOR = new Irradiance(152, 205, 236, false).normalize();

    private static SimpleMatrix worldToNodeMatrix;
    private static SimpleMatrix nodeToWorldMatrix;

    public Vector n;
    public Vector u;
    public Vector v;
    public Vector cLookAtDir;

    private Point wLookAt;
    private Point cLookAt;
    private Point cPosition = new Point(0, 0, 0, Point.Space.CAMERA);

    private ToneCompressor toneCompressor;

    public static int SCALE_RATIO = 80;

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

        Camera.nodeToWorldMatrix = new SimpleMatrix(Camera.worldToNodeMatrix.invert());

        this.cLookAt = toCameraSpace(wLookAt);
        this.cPosition = toCameraSpace(this.position);
        this.cLookAtDir = Util.subtract(cLookAt, cPosition).normalize();

        Vector filmPlanePosition = Util.scale(this.cLookAtDir, this.focalLength);
        this.filmPlane = new FilmPlane(16, 10, 16 * SCALE_RATIO, 10 * SCALE_RATIO, filmPlanePosition);

        System.out.println(this);
        System.out.println(toWorldSpace(this.cLookAt));
    }

    //
    // static methods
    //

    public static Point toWorldSpace(Point p) {
        Point point = Point.getPointFromMatrix(p.getMatrix().copy().mult(Camera.nodeToWorldMatrix));
        point.setSpace(Point.Space.CAMERA);
        return point;
    }

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
        int prev = -1;

        for (Pixel pixel : filmPlane) {
            if (pixel.row / SCALE_RATIO > prev) {
                prev++;
                System.out.println(prev + "/" + 10);
            }
            int testrow = 200;
            int testcol = 600;

            // if (pixel.row == testrow || pixel.col == testcol) {
            // pixel.setValue(new MyColor(0, 0, 1, true));
            // continue;
            // }

            if (pixel.row == testrow && pixel.col == testcol) {
                System.out.println("here");
                World.DEBUG_FLAG = true;
            } else {
                World.DEBUG_FLAG = false;
            }

            ArrayList<Pixel> subPixels = pixel.getSubPixels(subpixelsCount);
            Irradiance color = new Irradiance(0, 0, 0, true);
            for (Pixel subPixel : subPixels) {
                Vector dir = new Vector(subPixel.cPosition).normalize();
                Ray ray = new Ray(this.cPosition, dir);
                color.addColor(this.world.illuminate(ray, 0));
            }
            color.r /= subPixelCountSquare;
            color.g /= subPixelCountSquare;
            color.b /= subPixelCountSquare;

            pixel.setIrradiance(color);

            if(color.equals(DEFAULT_COLOR)){
                pixel.setNoIntersect(true);
            }
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

            Irradiance denormColor = pixel.color.denormalize();

            if (denormColor.r > 255 || denormColor.g > 255 || denormColor.b > 255) {
                System.out.println();
            }
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
            ImageIO.write(bi, "png", outputfile);
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
            pixel.color.r = Math.max(0, Math.min(1, pixel.color.r / Pixel.overallMax));
            pixel.color.g = Math.max(0, Math.min(1, pixel.color.g / Pixel.overallMax));
            pixel.color.b = Math.max(0, Math.min(1, pixel.color.b / Pixel.overallMax));
        }
    }

    public void applyToneMapping() {

        this.computeAbsoluteLuminances();
        this.compressTone();
        this.expandToneToTargetDevice();

        // this.capToOne();
        // this.normalizeAcrossPixels();
    }

    private void expandToneToTargetDevice() {
        double LDMax = this.toneCompressor.getLDMax();
        for (Pixel pixel : this.filmPlane) {
            pixel.color.scaleColor(1 / LDMax);
        }
    }

    private void compressTone() {
        this.toneCompressor.compress(filmPlane);

    }

    private void computeAbsoluteLuminances() {
        double averageLuminance = 0.0;
        for (Pixel pixel : filmPlane) {
            double luminance = 0.27 * pixel.color.r + 0.67 * pixel.color.g + 0.06 * pixel.color.b;
            if (pixel.row == 20 && pixel.col == 600) {
                System.out.println("lum" + luminance);
            }
            pixel.setLuminance(luminance);
            pixel.setLogLuminance(Math.log(luminance));
            averageLuminance += luminance;
        }

        averageLuminance /= filmPlane.getSize();
        filmPlane.setAverageLuminance(averageLuminance);
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

    @Override
    protected void computeBoundingBox() {
        this.boundingBox = null;
    }

    public void setToneCompressor(ToneCompressor compressor) {
        this.toneCompressor = compressor;
    }

}
