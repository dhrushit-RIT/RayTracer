package raytracer;

public class AAPlane {
    public enum Alignment {
        XY, YZ, ZX
    }

    private Point pointOnPlane;
    private Alignment alignment;

    public AAPlane(Point pointOnPoint, Alignment alignment) {
        this.pointOnPlane = pointOnPoint;
        this.alignment = alignment;
        if(this.alignment == null){
            this.alignment = Alignment.XY;
        }
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
