package raytracer;
public class Irradiance {
    public double r;
    public double g;
    public double b;

    public boolean normalized = false;

    public Irradiance(double r, double g, double b, boolean normalized) {
        this.r = r;
        this.g = g;
        this.b = b;

        this.normalized = normalized;
    }

    public Irradiance(Irradiance myColor) {
        this.r = myColor.r;
        this.g = myColor.g;
        this.b = myColor.b;
        this.normalized = myColor.normalized;
    }

    public Irradiance normalize() {
        if (!normalized) {
            normalized = true;
            this.r /= 255;
            this.g /= 255;
            this.b /= 255;
        }
        return this;
    }

    public Irradiance denormalize() {
        if (normalized) {
            this.normalized = false;
            this.r *= 255;
            this.g *= 255;
            this.b *= 255;
        }

        this.r = Math.floor(this.r);
        this.r = Math.floor(this.r);
        this.r = Math.floor(this.r);
        return this;
    }

    public Irradiance getNormalized() {
        if (normalized) {
            return this;
        } else {
            Irradiance color = new Irradiance(this);
            color.normalize();
            return color;
        }
    }

    public Irradiance getColor() {
        if (!normalized) {
            return this;
        } else {
            Irradiance color = new Irradiance(this);
            color.denormalize();
            return color;
        }
    }

    public Irradiance getComplement() {
        Irradiance retColor = new Irradiance(this);
        if(!retColor.normalized) {
            retColor.normalize();
        }

        retColor.r = 1 - retColor.r;
        retColor.g = 1 - retColor.g;
        retColor.b = 1 - retColor.b;

        return retColor;
    }

    public String toString() {
        return "C:" + r + "," + g + "," + b;
    }

    public Irradiance scaleColor(double d){
        if (!this.normalized){
            this.normalize();
        }
        this.r *= d;
        this.g *= d;
        this.b *= d;

        return this;
    }

    public Irradiance addColor(Irradiance otherColor) {
        this.normalize();
        Irradiance other = otherColor.getNormalized();
        this.r += other.r;
        this.g += other.g;
        this.b += other.b;
        return this;
    }

    public Irradiance addColor(Irradiance... otherColors) {
        this.normalize();
        for (Irradiance other : otherColors) {
            Irradiance otherNorm = other.getNormalized();
            this.r += otherNorm.r;
            this.g += otherNorm.g;
            this.b += otherNorm.b;
        }

        this.r = Math.max(0, Math.min(255, this.r));
        this.g = Math.max(0, Math.min(255, this.g));
        this.b = Math.max(0, Math.min(255, this.b));
        return this;
    }

}
