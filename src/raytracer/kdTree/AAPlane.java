package raytracer.kdTree;

import raytracer.IntersectionDetails;
import raytracer.Point;
import raytracer.Ray;

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

    public Point intersect(Ray ray) {
        // Ax + By + Cz + F = 0
        // XY : Cz + F = 0
        // YZ : Ax + F = 0
        // ZX : By + F = 0
        // w = -1 * (Axo + Byo + Czo + F) / (Adx + Bdy + Cdz)
        double A = 0;
        double B = 0;
        double C = 0;
        double F = 0;

        switch (alignment) {
            case XY:
                C = 1;
                F = -pointOnPlane.z;
                break;
            case YZ:
                A = 1;
                F = -pointOnPlane.x;
                break;
            case ZX:
                B = 1;
                F = -pointOnPlane.y;
                break;
        }

        double w = -1 * (A * ray.origin.x + B * ray.origin.y + C * ray.origin.z + F)
                / (A * ray.direction.x + B * ray.direction.y + C * ray.direction.z);

        IntersectionDetails<AAPlane> intersectionDetails = new IntersectionDetails<>(w);
        intersectionDetails.entity = this;
        intersectionDetails.intersectionPoint = new Point(
                ray.origin.x + w * ray.direction.x,
                ray.origin.y + w * ray.direction.y,
                ray.origin.z + w * ray.direction.z,
                Point.Space.CAMERA);
        // if(w > 0) {
        return intersectionDetails.intersectionPoint;
        // } else {
        // return null;
        // }

    }

    public String toString() {
        return alignment + " | " + this.pointOnPlane;
    }
}
