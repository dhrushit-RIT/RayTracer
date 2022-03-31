package raytracer;
public class IntersectionDetails<E> {
    public double distance;
    public Point intersectionPoint;
    public Vector normalAtIntersection;
    public E entity;

    public IntersectionDetails(double distance){
        this.distance = distance;
    }

    public IntersectionDetails(E entity) {
        this.distance = -1;
        this.intersectionPoint = null;
        this.normalAtIntersection = null;
        this.entity = entity;
    }
}
