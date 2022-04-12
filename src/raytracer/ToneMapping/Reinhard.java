package raytracer.ToneMapping;

import raytracer.FilmPlane;
import raytracer.Irradiance;
import raytracer.Pixel;

public class Reinhard extends ToneCompressor {

    MidToneLuminanceFrom midToneLuminanceFrom = MidToneLuminanceFrom.AVG_LUMINANCE;

    public enum MidToneLuminanceFrom {
        PIXEL,
        CONSTANT,
        AVG_LUMINANCE
    }

    int midToneRow = 0;
    int midToneCol = 0;

    double midToneLuminance = -1.0;

    @Override
    public double compress(double inputLuminance) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void compress(FilmPlane filmPlane) {
        this.computeMidToneLuminance(filmPlane);

        for (Pixel pixel : filmPlane) {
            pixel.color.scaleColor(0.18 / this.midToneLuminance);
            Irradiance reflectance = new Irradiance(
                    pixel.color.r / (pixel.color.r + 1),
                    pixel.color.g / (pixel.color.g + 1),
                    pixel.color.b / (pixel.color.b + 1),
                    true);

            Irradiance targetLuminance = reflectance.scaleColor(Ldmax);
            pixel.setIrradiance(targetLuminance);
        }
    }

    private void computeMidToneLuminance(FilmPlane filmPlane) {
        switch (midToneLuminanceFrom) {
            case AVG_LUMINANCE:
                this.computeLogAverageLuminance(filmPlane);
                this.midToneLuminance = this.averageLuminance;
                break;
            case CONSTANT:
                break;
            case PIXEL:
                this.midToneLuminance = filmPlane.getPixelAt(midToneRow, midToneCol).getLuminance();

        }
    }

    public void setMidtoneLuminanceValue(double midToneLuminance) {
        this.midToneLuminance = midToneLuminance;
    }

    public void setMidToneCoords(int row, int col) {
        this.midToneRow = row;
        this.midToneCol = col;
    }

    public void setMidToneLuminanceFrom(MidToneLuminanceFrom midToneLuminanceFrom) {
        this.midToneLuminanceFrom = midToneLuminanceFrom;
    }

}
