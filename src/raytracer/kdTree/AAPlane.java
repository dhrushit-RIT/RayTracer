package raytracer.kdTree;

import raytracer.Point;
import raytracer.Ray;
import raytracer.Point.Space;

public class AAPlane {
    public enum Alignment {
        XY, YZ, ZX
    }

    private Point pointOnPlane;
    private Alignment alignment;

    public AAPlane(Point pointOnPoint, Alignment alignment) {
        this.pointOnPlane = pointOnPoint;
        this.alignment = alignment;
        if (this.alignment == null) {
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

    public Point intersectPoint(Ray cRay) {
        double w = 1;
        switch (alignment) {
            case XY:
                w = pointOnPlane.z / cRay.direction.z;
                break;
            case YZ:
                w = pointOnPlane.x / cRay.direction.x;
                break;
            case ZX:
                w = pointOnPlane.y / cRay.direction.y;
                break;
            default:
                break;
        }

        return new Point(
                w * cRay.direction.x,
                w * cRay.direction.y,
                w * cRay.direction.z,
                Point.Space.CAMERA);
    }
}
