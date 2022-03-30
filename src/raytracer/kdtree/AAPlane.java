package raytracer.kdtree;

import raytracer.Point;
import raytracer.Ray;

public class AAPlane {

    public enum Alignment {
        XY,
        YZ,
        ZX
    }

    public Alignment alignment;
    public Point pointOnPlane;

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

    public void setPointOnPlane(Point pointOnPlane) {
        this.pointOnPlane = pointOnPlane;
    }

    public String toString() {
        return "AAP " + alignment + " | " + pointOnPlane;
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
                F = pointOnPlane.z;
                break;
            case YZ:
                F = pointOnPlane.x;
                break;
            case ZX:
                F = pointOnPlane.y;
                break;
        }

        double w = -1 * (A * ray.origin.x + B * ray.origin.y + C * ray.origin.z + F)
                / (A * ray.direction.x + B * ray.direction.y + C * ray.direction.z);

        // if(w > 0) {
        return new Point(ray.origin.x + ray.direction.x * w, ray.origin.y + ray.direction.y * w,
                ray.origin.z + ray.direction.z * w, Point.Space.CAMERA);
        // } else {
        // return null;
        // }

    }

}
