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

        // w = -1 * (cRay.origin.z + -1 * pointOnPlane.z) / cRay.direction.z;

        double w = 1;
        switch (alignment) {
            case XY:
                w = -1 * (cRay.origin.z + -1 * pointOnPlane.z) / cRay.direction.z;
                // w = pointOnPlane.z / cRay.direction.z;
                break;
            case YZ:
                w = -1 * (cRay.origin.x + -1 * pointOnPlane.x) / cRay.direction.x;
                // w = pointOnPlane.x / cRay.direction.x;
                break;
            case ZX:
                w = -1 * (cRay.origin.y + -1 * pointOnPlane.y) / cRay.direction.y;
                // w = pointOnPlane.y / cRay.direction.y;
                break;
            default:
                break;
        }

        // if (w > 0) {

            return new Point(
                    cRay.origin.x + w * cRay.direction.x,
                    cRay.origin.y + w * cRay.direction.y,
                    cRay.origin.z + w * cRay.direction.z,
                    Point.Space.CAMERA);
        // } else {
            // return null;
        // }
    }

    public String toString() {
        return alignment + " | " + this.pointOnPlane;
    }
}
