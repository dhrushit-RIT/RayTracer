package raytracer;
public class IntersectionDetails {
    public double distance;
    public Point intersectionPoint;
    public Vector normalAtIntersection;
    public Entity entity;

    public IntersectionDetails(double distance){
        this.distance = distance;
    }

    public IntersectionDetails(Entity entity) {
        this.distance = -1;
        this.intersectionPoint = null;
        this.normalAtIntersection = null;
        this.entity = entity;
    }

    public static boolean noIntersections(IntersectionDetails entityIntersectionDetails){
        return entityIntersectionDetails == null || entityIntersectionDetails.entity == null
        || entityIntersectionDetails.intersectionPoint == null;
    }
}
