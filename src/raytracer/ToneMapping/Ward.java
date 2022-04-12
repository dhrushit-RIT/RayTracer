package raytracer.ToneMapping;

import raytracer.FilmPlane;
import raytracer.Pixel;

public class Ward extends ToneCompressor {

    private double scaleFactor = 1.0;

    public Ward() {
        // this.computeScaleFactor();
    }

    public void computeScaleFactor() {
        double numerator = 1.219 + Math.pow(ToneCompressor.Ldmax / 2, 0.4);
        double denominator = 1.219 + Math.pow(this.averageLuminance, 0.4);

        double fraction = numerator / denominator;
        this.scaleFactor = Math.pow(fraction, 2.5);
        System.out.println("Ward scale factor " + this.scaleFactor);
    }

    public void setAverageLuminance(double averageLuminance) {
        super.setAverageLuminance(averageLuminance);
        this.computeScaleFactor();
    }

    public Ward(double Ldmax) {
        ToneCompressor.setLDMax(Ldmax);
        // this.computeScaleFactor();

    }

    @Override
    public double compress(double inputLuminance) {
        return this.scaleFactor * inputLuminance;
    }

    @Override
    public void compress(FilmPlane filmPlane) {
        this.computeLogAverageLuminance(filmPlane);
        this.computeScaleFactor();

        for (Pixel pixel : filmPlane) {
            pixel.setDestLuminance(this.scaleFactor * pixel.getLuminance());
            pixel.color.scaleColor(this.scaleFactor);
        }

    }

}
