
public class BoundingBox {
    public double xMin = 0;
    public double xMax = 0;
    public double yMin = 0;
    public double yMax = 0;
    public double zMin = 0;
    public double zMax = 0;

    private static double EPSILON = 0.000001;

    public BoundingBox(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this.xMin = xMin - EPSILON;
        this.xMax = xMax + EPSILON;
        this.yMin = yMin - EPSILON;
        this.yMax = yMax + EPSILON;
        this.zMin = zMin - EPSILON;
        this.zMax = zMax + EPSILON;
    }

    /**
     * other bounding box assumed to be in the same coordinate system as this one
     * 
     * @param other
     * @return
     */
    public boolean intersect(BoundingBox other) {
        boolean xNotIntersecting = this.xMin > other.xMax || other.xMin > this.xMax;
        boolean yNotIntersecting = this.yMin > other.yMax || other.yMin > this.yMax;
        boolean zNotIntersecting = this.zMin > other.zMax || other.zMin > this.zMax;

        return xNotIntersecting && yNotIntersecting && zNotIntersecting;
    }
}
