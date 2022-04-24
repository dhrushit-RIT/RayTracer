package raytracer.ToneMapping;

import raytracer.FilmPlane;
import raytracer.Pixel;

public abstract class ToneCompressor {

    protected final static double DELTA = 0.000001;

    public enum Type {
        WARD,
        REINHARD,
        ADAPTIVE,
        HIST
    }

    protected static double Ldmax;
    protected double Lwmin;
    protected double Lwmax;

    public static void setLDMax(double Ldmax) {
        ToneCompressor.Ldmax = Ldmax;
    }

    protected double averageLuminance;

    public abstract double compress(double inputLuminance);

    public abstract void compress(FilmPlane filmPlane);

    public void setAverageLuminance(double averageLuminance) {
        this.averageLuminance = averageLuminance;
    }

    public double getLDMax() {
        return Ldmax;
    }

    protected void computeLogAverageLuminance(FilmPlane filmPlane) {
        double averageLuminance = 0.0;

        for (Pixel pixel : filmPlane) {
            averageLuminance += Math.log(DELTA + pixel.getLuminance());
        }

        averageLuminance /= filmPlane.getSize();

        averageLuminance = Math.exp(averageLuminance);
        this.averageLuminance = averageLuminance;
        System.out.println("Ward log average luminance " + this.averageLuminance);
    }

    protected void computeMaxSceneLuminance(FilmPlane filmPlane) {
        this.Lwmax = -Double.MAX_VALUE;
        for (Pixel pixel : filmPlane) {
            Lwmax = Math.max(Lwmax, pixel.getLuminance());
        }
    }

}
