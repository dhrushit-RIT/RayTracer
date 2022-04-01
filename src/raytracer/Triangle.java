package raytracer;

import org.ejml.simple.SimpleMatrix;

public class Triangle extends Entity {

    public Point wPosition;
    public Point[] verticePoints;
    public Point[] cVerticePoints;

    private Point[] textureCoordinates;

    private Vector e1;
    private Vector e2;
    private Vector T;
    private Vector P;
    private Vector Q;

    public Triangle(MyColor baseColor, Point position, Point[] verticePoints) {
        super(baseColor, position);
        this.wPosition = position;
        this.verticePoints = verticePoints;

        // compute the vertices in camera space
        this.cVerticePoints = new Point[3];
        this.cVerticePoints[0] = Camera.toCameraSpace(this.verticePoints[0]);
        this.cVerticePoints[1] = Camera.toCameraSpace(this.verticePoints[1]);
        this.cVerticePoints[2] = Camera.toCameraSpace(this.verticePoints[2]);

        this.e1 = Util.subtract(cVerticePoints[1], cVerticePoints[0]);
        this.e2 = Util.subtract(cVerticePoints[2], cVerticePoints[0]);

        this.computeBoundingBox();
    }

    /**
     * u,v will give barycentric coords of intersection point
     * ⍵ will be distance along ray of intersection point
     * e1 x e2 will give you the normal.
     * 
     * f(u,v)=(1−u−v)p0 +up1 +vp2
     */
    @Override
    public IntersectionDetails<Entity> intersect(Ray ray) {

        // if (this.T == null) {
            this.T = Util.subtract(ray.origin, cVerticePoints[0]);
            this.Q = Util.cross(T, e1);
        // }

        Vector P = Util.cross(ray.direction, e2);
        IntersectionDetails<Entity> intersection = new IntersectionDetails<>(this);
        double Pe1 = Util.dot(P, e1);

        if (Pe1 == 0) {
            intersection.distance = -1;
        }

        double QdotE2 = Util.dot(Q, e2);
        double PdotT = Util.dot(P, T);
        double QdotD = Util.dot(Q, ray.direction);
        double w = QdotE2 / Pe1;
        double u = PdotT / Pe1;
        double v = QdotD / Pe1;

        if (w < 0) {
            w = -1;
        } else if (u < 0 || v < 0 || u + v > 1) {
            w = -1;
        }

        intersection.distance = w;
        if (w > 0) {

            // f(u,v)=(1−u−v)p0 + up1 + vp2
            Point camCenter = new Point(0, 0, 0, Point.Space.CAMERA);
            Vector p0c = Util.subtract(cVerticePoints[0], ray.origin);
            Vector p1c = Util.subtract(cVerticePoints[1], ray.origin);
            Vector p2c = Util.subtract(cVerticePoints[2], ray.origin);
            Vector pointVec = Util.add(Util.scale(1 - u - v, p0c), Util.scale(u, p1c), Util.scale(v, p2c));

            intersection.intersectionPoint = new Point(pointVec.x, pointVec.y, pointVec.z, Point.Space.CAMERA);
            intersection.normalAtIntersection = Util.cross(e1, e2).normalize();

        }
        return intersection;
    }

    public Point[] getTextureCoordinates() {
        return textureCoordinates;
    }

    public void setTextureCoordinates(Point[] textureCoordinates) {
        this.textureCoordinates = textureCoordinates;
    }

    @Override
    public void computeBoundingBox() {
        double xMin = Math.min(this.cVerticePoints[0].x, Math.min(this.cVerticePoints[1].x, this.cVerticePoints[2].x));
        double yMin = Math.min(this.cVerticePoints[0].y, Math.min(this.cVerticePoints[1].y, this.cVerticePoints[2].y));
        double zMin = Math.min(this.cVerticePoints[0].z, Math.min(this.cVerticePoints[1].z, this.cVerticePoints[2].z));
        double xMax = Math.max(this.cVerticePoints[0].x, Math.max(this.cVerticePoints[1].x, this.cVerticePoints[2].x));
        double yMax = Math.max(this.cVerticePoints[0].y, Math.max(this.cVerticePoints[1].y, this.cVerticePoints[2].y));
        double zMax = Math.max(this.cVerticePoints[0].z, Math.max(this.cVerticePoints[1].z, this.cVerticePoints[2].z));
        this.boundingBox = new BoundingBox(xMin, xMax, yMin, yMax, zMin, zMax);
    }

}
