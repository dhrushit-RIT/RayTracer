package raytracer;

import java.util.ArrayList;

public class Pixel {
    public Irradiance color;
    public Irradiance reflectance;
    public int row;
    public int col;
    public static double width;
    public static double height;

    public static double maxR = -1;
    public static double maxG = -1;
    public static double maxB = -1;
    public static double overallMax = -1;

    public static double minR = 1;
    public static double minG = 1;
    public static double minB = 1;

    private double luminance = 0.0;
    private double logLuminance = 0.0;
    private double destLuminance = 0.0;

    private boolean noIntersect = false;

    // public Vector planePosition; // with respect to the origin of film plane
    public Vector cPosition; // with respect to the origin of world

    public int divisions = 1;

    public Pixel(int row, int col, Vector position) {
        this.row = row;
        this.col = col;
        this.cPosition = position;
    }

    public double getDestLuminance() {
        return destLuminance;
    }

    public void setDestLuminance(double destLuminance) {
        this.destLuminance = destLuminance;
    }

    public double getLuminance() {
        return luminance;
    }

    public void setLuminance(double luminance) {
        this.luminance = luminance;
    }

    public void setIrradiance(Irradiance value) {
        this.color = value;
        Pixel.maxR = Math.max(maxR, this.color.r);
        Pixel.maxG = Math.max(maxG, this.color.g);
        Pixel.maxB = Math.max(maxB, this.color.b);
        Pixel.overallMax = Math.max(maxB, Math.max(maxR, maxG));

        Pixel.minR = Math.min(minR, this.color.r);
        Pixel.minG = Math.min(minG, this.color.g);
        Pixel.minB = Math.min(minB, this.color.b);
    }

    public void setWidth(float width) {
        Pixel.width = width;
    }

    public void setHeight(float height) {
        Pixel.height = height;
    }

    public String toString() {
        return "Pix pos: " + cPosition + " val : " + this.color;
    }

    public ArrayList<Pixel> getSubPixels(int subpixelCount) {
        ArrayList<Pixel> subPixels = new ArrayList<>();

        double subpixelWidth = Pixel.width / subpixelCount;
        double subpixelHeight = Pixel.height / subpixelCount;

        for (int row = 0; row < subpixelCount; row++) {
            for (int col = 0; col < subpixelCount; col++) {
                Vector subPixelPosition = new Vector(
                        -Pixel.width / 2 + col * subpixelWidth + subpixelWidth / 2,
                        -Pixel.height / 2 + row * subpixelHeight + subpixelHeight / 2,
                        0);
                subPixelPosition.x += this.cPosition.x;
                subPixelPosition.y += this.cPosition.y;
                subPixelPosition.z += this.cPosition.z;
                subPixelPosition.updateMagnitude();

                subPixels.add(new Pixel(row, col, subPixelPosition));
            }
        }

        return subPixels;
    }

    public void setLogLuminance(double log) {
        this.logLuminance = log;
    }

    public void setNoIntersect(boolean noIntersect) {
        this.noIntersect = noIntersect;
    }

    public boolean hadNoIntersection() {
        return noIntersect;
    }

}
