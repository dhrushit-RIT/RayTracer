package raytracer;

public class BoundingBox {

    public double xMax;
    public double yMax;
    public double zMax;
    public double xMin;
    public double yMin;
    public double zMin;

    public BoundingBox(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
    }

    public boolean intersect(BoundingBox other) {
        boolean xNotIntersecting = this.xMax < other.xMin || other.xMax < this.xMin;
        if (xNotIntersecting)
            return false;

        boolean yNotIntersecting = this.yMax < other.yMin || other.yMax < this.yMin;
        if (yNotIntersecting)
            return false;

        boolean zNotIntersecting = this.zMax < other.zMin || other.zMax < this.zMin;
        if (zNotIntersecting)
            return false;

        return true;
    }

}
