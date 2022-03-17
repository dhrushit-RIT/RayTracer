package raytracer;

import org.ejml.simple.SimpleMatrix;

public class Triangle extends Entity {

    public Point wPosition;
    public Point[] verticePoints;

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

        this.e1 = Util.subtract(verticePoints[1], verticePoints[0]);
        this.e2 = Util.subtract(verticePoints[2], verticePoints[0]);
    }

    /**
     * u,v will give barycentric coords of intersection point
     * ⍵ will be distance along ray of intersection point
     * e1 x e2 will give you the normal.
     * 
     * f(u,v)=(1−u−v)p0 +up1 +vp2
     */
    @Override
    public IntersectionDetails intersect(Ray ray) {

        if (this.T == null) {
            this.T = Util.subtract(ray.origin, /* verticePoints[0] */Camera.toCameraSpace(verticePoints[0]));
            this.Q = Util.cross(T, e1);
        }

        Vector P = Util.cross(ray.direction, e2);
        IntersectionDetails intersection = new IntersectionDetails(this);
        double Pe1 = Util.dot(P, e1);

        if (Pe1 == 0) {
            intersection.distance = -1;
        }

        SimpleMatrix wuv = new SimpleMatrix(new double[][] {
                { Util.dot(Q, e2) },
                { Util.dot(P, T) },
                { Util.dot(Q, ray.direction) } })
                .divide(Pe1);

        double w = wuv.get(0, 0);
        double u = wuv.get(1, 0);
        double v = wuv.get(2, 0);

        if (w < 0) {
            w = -1;
        } else if (u < 0 || v < 0 || u + v > 1) {
            w = -1;
        }

        intersection.distance = w;
        if (w > 0) {

            // f(u,v)=(1−u−v)p0 + up1 + vp2
            Point camCenter = new Point(0, 0, 0, Point.Space.CAMERA);
            Vector p0c = Util.subtract(Camera.toCameraSpace(verticePoints[0]), camCenter);
            Vector p1c = Util.subtract(Camera.toCameraSpace(verticePoints[1]), camCenter);
            Vector p2c = Util.subtract(Camera.toCameraSpace(verticePoints[2]), camCenter);
            Vector pointVec = Util.add(Util.scale(1 - u - v, p0c), Util.scale(u, p1c), Util.scale(v, p2c));

            intersection.intersectionPoint = new Point(pointVec.x, pointVec.y, pointVec.z, Point.Space.CAMERA);
            intersection.normalAtIntersection = Util.cross(e1, e2);
            intersection.normalAtIntersection.normalize();

        }
        return intersection;
    }

    public Point[] getTextureCoordinates() {
        return textureCoordinates;
    }

    public void setTextureCoordinates(Point[] textureCoordinates) {
        this.textureCoordinates = textureCoordinates;
    }

}
