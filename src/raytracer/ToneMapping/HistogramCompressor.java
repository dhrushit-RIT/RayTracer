package raytracer.ToneMapping;

import java.util.ArrayList;
import java.util.HashMap;

import raytracer.FilmPlane;
import raytracer.Pixel;

public class HistogramCompressor extends ToneCompressor {

    public int numBins = 256;

    private int[] freq;
    private int[] cFreq;

    public HistogramCompressor() {
        super();
    }

    @Override
    public double compress(double inputLuminance) {
        return 0;
    }

    @Override
    public void compress(FilmPlane filmPlane) {
        this.computeLogAverageLuminance(filmPlane);
        this.computeLMinMax(filmPlane);
        this.computeFrequency(filmPlane);
        this.computeCFrequency();
        this.equalize(filmPlane);
    }

    private void equalize(FilmPlane filmPlane) {
        double displayMin = Math.log(this.Lwmin + DELTA);
        double displayRange = Math.log(this.Lwmax) - Math.log(Lwmin + DELTA);
        for (Pixel pixel : filmPlane) {
            double Lw = pixel.getLuminance();
            if (pixel.hadNoIntersection()) {
                Lw = averageLuminance;
            }
            double destLogLum = displayMin
                    + displayRange * this.cFreq[this.getIndex(Lw)] / filmPlane.getSize();
            double destLuminance = Math.exp(destLogLum);

            pixel.setDestLuminance(destLuminance);
            pixel.color.scaleColor(destLuminance / Lw);
        }
    }

    private void computeCFrequency() {
        if (this.cFreq == null) {
            this.cFreq = new int[numBins];
        }

        this.cFreq[0] = this.freq[0];
        for (int i = 1; i < numBins; i++) {
            this.cFreq[i] = this.cFreq[i - 1] + this.freq[i];
        }
    }

    private void computeFrequency(FilmPlane filmPlane) {
        if (this.freq == null) {
            this.freq = new int[numBins];
        }
        for (Pixel pixel : filmPlane) {
            this.freq[this.getIndex(pixel.getLuminance())]++;
        }
    }

    private int getIndex(double luminance) {
        double range = this.Lwmax - 0.0001;

        return (int) ((numBins - 1) * luminance / range);
    }

    private void computeLMinMax(FilmPlane filmPlane) {
        Lwmax = -Double.MAX_VALUE;
        Lwmin = Double.MAX_VALUE;
        for (Pixel pixel : filmPlane) {
            Lwmax = Math.max(Lwmax, pixel.getLuminance());
            Lwmin = Math.min(Lwmin, pixel.getLuminance());
        }
    }

}
