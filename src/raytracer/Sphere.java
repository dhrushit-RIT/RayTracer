package raytracer;

/**
 * bounding box is assumed to be centered at the center of the sphere
 * bounding box is assumed to be in context of the entity
 */
public class Sphere extends Entity {
    private double radius;

    public Sphere(Point sphereCenter, double sphereRadius, MyColor sphereColor) {
        super(sphereColor, sphereCenter);
        this.radius = sphereRadius;
        this.getPositionInCameraCoordinates();
        this.computeBoundingBox();
        System.out.println(this);
        this.name = "Sphere";
    }

    @Override
    public IntersectionDetails<Entity> intersect(Ray cRay) {
        Point rOrigin = cRay.getOrigin();
        // Point rOriginCamSpace = Camera.toCameraSpace(rOrigin);
        Point sphereCenterCamSpace = Camera.toCameraSpace(this.position);

        Vector rDirection = cRay.getDirection().normalize();
        // Vector rDirectionCamSpace = Camera.toCameraSpace(rDirection).normalize();

        double xDiff = rOrigin.x - sphereCenterCamSpace.x;
        double yDiff = rOrigin.y - sphereCenterCamSpace.y;
        double zDiff = rOrigin.z - sphereCenterCamSpace.z;

        double A = 1; // rx^2 + ry^2 + rz^2
        double B = 2 * (rDirection.x * xDiff + rDirection.y * yDiff + rDirection.z * zDiff);
        double C = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff - radius * radius;

        double D = B * B - 4 * C;
        double w = -1;

        IntersectionDetails<Entity> intersectionDetails = new IntersectionDetails<Entity>(this);

        if (D > 0) {
            D = Math.sqrt(D);
            double w0 = (-B + D) / 2; // Note : A = 1 is assumed
            double w1 = (-B - D) / 2;

            double mindiff = 10 * Entity.EPSILON;
            if (w0 < 0) {
                w = w1;
            } else if (w1 < 0) {
                w = w0;
            } else {
                if (w0 < mindiff && w1 < mindiff) {
                    w = -1;
                } else if (w1 < mindiff) {
                    w = w0;
                } else if (w0 < mindiff) {
                    w = w1;
                } else {
                    w = Math.min(w0, w1);
                }
            }
            if (w > 0) {

                intersectionDetails.intersectionPoint = new Point(
                        rOrigin.x + rDirection.x * w,
                        rOrigin.y + rDirection.y * w,
                        rOrigin.z + rDirection.z * w,
                        Point.Space.CAMERA);

                intersectionDetails.normalAtIntersection = new Vector(
                        intersectionDetails.intersectionPoint.x - sphereCenterCamSpace.x,
                        intersectionDetails.intersectionPoint.y - sphereCenterCamSpace.y,
                        intersectionDetails.intersectionPoint.z - sphereCenterCamSpace.z).normalize();

                // this.addEpsilonDisplacementToIntersection(intersectionDetails);
            } else {
                intersectionDetails.intersectionPoint = null;
                intersectionDetails.normalAtIntersection = null;
            }

        }

        intersectionDetails.distance = w;

        return intersectionDetails;
    }

    public static Point addEpsilonDisplacementToIntersection(Point intersectionPoint, Vector normalAtIntersection) {
        intersectionPoint.x += 10 * Entity.EPSILON * normalAtIntersection.x;
        intersectionPoint.y += 10 * Entity.EPSILON * normalAtIntersection.y;
        intersectionPoint.z += 10 * Entity.EPSILON * normalAtIntersection.z;
        intersectionPoint.updatePoint();
        return intersectionPoint;
    }

    @Override
    protected void computeBoundingBox() {
        this.boundingBox = new BoundingBox(
                this.cPosition.x + (-this.radius), this.cPosition.x + this.radius,
                this.cPosition.y + (-this.radius), this.cPosition.y + this.radius,
                this.cPosition.z + (-this.radius), this.cPosition.z + this.radius);

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------\n");
        sb.append("Sphere :\n" + this.position + "\n" + this.cPosition + this.boundingBox + "\n");
        sb.append("----------------------------\n");
        return sb.toString();
    }

}
