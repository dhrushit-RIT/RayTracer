package raytracer.kdtree;

import raytracer.Point;

public class AAPlane {

    enum Alignment {
        XY,
        YZ,
        ZX
    }

    Alignment alignment;
    Point pointOnPlane;

    public AAPlane() {
        this.alignment = Alignment.XY;
        this.pointOnPlane = new Point(0, 0, 0, Point.Space.CAMERA);
    }

    public AAPlane(Point pointOnPlane) {
        this.alignment = Alignment.XY;
        this.pointOnPlane = new Point(pointOnPlane);
    }

    public AAPlane(Alignment alignment, Point pointOnPlane) {
        this.alignment = alignment;
        this.pointOnPlane = new Point(pointOnPlane);
    }

    public void setPointOnPlane(Point pointOnPlane){
        this.pointOnPlane = pointOnPlane;
    }

}
