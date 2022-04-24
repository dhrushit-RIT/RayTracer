package raytracer.ToneMapping;

import raytracer.FilmPlane;
import raytracer.Pixel;

public class AdaptiveToneMapping extends ToneCompressor {

    public double base = 0.86;

    @Override
    public void compress(FilmPlane filmPlane) {
        System.out.println("Applying Adaptive Tone Mapping Compressor...");
        this.computeLogAverageLuminance(filmPlane);
        this.computeMaxSceneLuminance(filmPlane);

        for (Pixel pixel : filmPlane) {
            double Lw = pixel.getLuminance();
            double Lwa = averageLuminance;
            double t = Lw / Lwmax;
            double numerator = 1;
            double denominator = 1;
            double newLuminance = 1;

            numerator *= Math.log(Lw / Lwa + 1);
            denominator *= Math.log10(Lwmax / Lwa + 1);
            denominator *= Math.log(2 + 8 * this.bias(t, base));

            newLuminance = numerator / denominator;

            pixel.setDestLuminance(Ldmax * newLuminance);
            pixel.color.scaleColor(newLuminance);
        }
    }

    public double bias(double t, double base) {
        double power = Math.log(base) / Math.log(0.5);
        return Math.pow(t, power);
    }

    @Override
    public double compress(double inputLuminance) {
        // TODO Auto-generated method stub
        return 0;
    }
}
