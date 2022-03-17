package raytracer;
public class MyColor {
    public double r;
    public double g;
    public double b;

    public boolean normalized = false;

    public MyColor(double r, double g, double b, boolean normalized) {
        this.r = r;
        this.g = g;
        this.b = b;

        this.normalized = normalized;
    }

    public MyColor(MyColor myColor) {
        this.r = myColor.r;
        this.g = myColor.g;
        this.b = myColor.b;
        this.normalized = myColor.normalized;
    }

    public MyColor normalize() {
        if (!normalized) {
            normalized = true;
            this.r /= 255;
            this.g /= 255;
            this.b /= 255;
        }
        return this;
    }

    public MyColor denormalize() {
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

    public MyColor getNormalized() {
        if (normalized) {
            return this;
        } else {
            MyColor color = new MyColor(this);
            color.normalize();
            return color;
        }
    }

    public MyColor getColor() {
        if (!normalized) {
            return this;
        } else {
            MyColor color = new MyColor(this);
            color.denormalize();
            return color;
        }
    }

    public String toString() {
        return "C:" + r + "," + g + "," + b;
    }

    public MyColor addColor(MyColor otherColor) {
        this.normalize();
        MyColor other = otherColor.getNormalized();
        this.r += other.r;
        this.g += other.g;
        this.b += other.b;
        return this;
    }

    public MyColor addColor(MyColor... otherColors) {
        this.normalize();
        for (MyColor other : otherColors) {
            MyColor otherNorm = other.getNormalized();
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
