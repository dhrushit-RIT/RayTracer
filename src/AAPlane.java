public class AAPlane {
    enum Alignment {
        XY, YZ, ZX
    }

    private Point pointOnPlane;
    private Alignment alignment;

    public AAPlane(Point pointOnPoint, Alignment alignment) {
        this.pointOnPlane = pointOnPoint;
        this.alignment = alignment;
    }

    public Point getPointOnPlane() {
        return pointOnPlane;
    }

    public void setPointOnPlane(Point pointOnPlane) {
        this.pointOnPlane = pointOnPlane;
    }

    public Alignment getAlignment() {
        return this.alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }
}
