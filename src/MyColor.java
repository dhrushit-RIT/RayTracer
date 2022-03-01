
public class MyColor {
    public float r;
    public float g;
    public float b;

    public boolean normalized = false;

    public MyColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.normalized = false;
    }

    public MyColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;

        this.normalized = true;
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
            this.r *= 255;
            this.g *= 255;
            this.b *= 255;
        }
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

}
