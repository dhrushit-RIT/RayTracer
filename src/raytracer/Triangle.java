package raytracer;

import org.ejml.simple.SimpleMatrix;

public class Triangle extends Entity {

    public Point wPosition;
    public Point cPosition;
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
        this.computePositionInCameraSpace();

        this.e1 = Util.subtract(cVerticePoints[1], cVerticePoints[0]);
        this.e2 = Util.subtract(cVerticePoints[2], cVerticePoints[0]);
        
        // this.computeVerticesInCamera();
        this.computeBoundingBox();
    }

    private void computeVerticesInCamera() {
        this.getPositionInCameraCoordinates();
        this.cVerticePoints = new Point[3];

        this.cVerticePoints[0] = Camera.toCameraSpace(this.verticePoints[0]);
        this.cVerticePoints[1] = Camera.toCameraSpace(this.verticePoints[1]);
        this.cVerticePoints[2] = Camera.toCameraSpace(this.verticePoints[2]);
    }

    private void computePositionInCameraSpace() {
        this.cPosition = Camera.toCameraSpace(this.wPosition);

        this.cVerticePoints = new Point[3];

        this.cVerticePoints[0] = Camera.toCameraSpace(this.verticePoints[0]);
        this.cVerticePoints[1] = Camera.toCameraSpace(this.verticePoints[1]);
        this.cVerticePoints[2] = Camera.toCameraSpace(this.verticePoints[2]);
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
            this.T = Util.subtract(ray.origin, this.cVerticePoints[0]);///* verticePoints[0] */Camera.toCameraSpace(verticePoints[0]));
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
            Vector p0c = Util.subtract(/* Camera.toCameraSpace(verticePoints[0]) */this.cVerticePoints[0], /* camCenter */ ray.origin);
            Vector p1c = Util.subtract(/* Camera.toCameraSpace(verticePoints[1]) */this.cVerticePoints[1], /* camCenter */ ray.origin);
            Vector p2c = Util.subtract(/* Camera.toCameraSpace(verticePoints[2]) */this.cVerticePoints[2], /* camCenter */ ray.origin);
            Vector pointVec = Util.add(Util.scale(1 - u - v, p0c), Util.scale(u, p1c), Util.scale(v, p2c));

            intersection.intersectionPoint = new Point(pointVec.x, pointVec.y, pointVec.z, Point.Space.CAMERA);
            intersection.normalAtIntersection = Util.cross(e1, e2).normalize();

        } else {
            intersection.entity = null;
            intersection.intersectionPoint = null;
            intersection.normalAtIntersection = null;
        }
        return intersection;
    }

    @Override
    protected void computeBoundingBox() {
        // this is in the world coords
        // need to convert this to camera
        // try figuring out a better way to generally compute the bb
        // double xMin = Math.min(this.verticePoints[0].x - this.position.x,
        // Math.min(this.verticePoints[1].x - this.position.x, this.verticePoints[2].x -
        // this.position.x));
        // double xMax = Math.max(this.verticePoints[0].x - this.position.x,
        // Math.max(this.verticePoints[1].x - this.position.x, this.verticePoints[2].x -
        // this.position.x));
        // double yMin = Math.min(this.verticePoints[0].y - this.position.y,
        // Math.min(this.verticePoints[1].y - this.position.y, this.verticePoints[2].y -
        // this.position.y));
        // double yMax = Math.max(this.verticePoints[0].y - this.position.y,
        // Math.max(this.verticePoints[1].y - this.position.y, this.verticePoints[2].y -
        // this.position.y));
        // double zMin = Math.min(this.verticePoints[0].z - this.position.z,
        // Math.min(this.verticePoints[1].z - this.position.z, this.verticePoints[2].z -
        // this.position.z));
        // double zMax = Math.max(this.verticePoints[0].z - this.position.z,
        // Math.max(this.verticePoints[1].z - this.position.z, this.verticePoints[2].z -
        // this.position.z));
        
        double xMin = Math.min(this.cVerticePoints[0].x - this.cPosition.x,
                Math.min(this.cVerticePoints[1].x - this.cPosition.x, this.cVerticePoints[2].x - this.cPosition.x));
        double xMax = Math.max(this.cVerticePoints[0].x - this.cPosition.x,
                Math.max(this.cVerticePoints[1].x - this.cPosition.x, this.cVerticePoints[2].x - this.cPosition.x));
        double yMin = Math.min(this.cVerticePoints[0].y - this.cPosition.y,
                Math.min(this.cVerticePoints[1].y - this.cPosition.y, this.cVerticePoints[2].y - this.cPosition.y));
        double yMax = Math.max(this.cVerticePoints[0].y - this.cPosition.y,
                Math.max(this.cVerticePoints[1].y - this.cPosition.y, this.cVerticePoints[2].y - this.cPosition.y));
        double zMin = Math.min(this.cVerticePoints[0].z - this.cPosition.z,
                Math.min(this.cVerticePoints[1].z - this.cPosition.z, this.cVerticePoints[2].z - this.cPosition.z));
        double zMax = Math.max(this.cVerticePoints[0].z - this.cPosition.z,
                Math.max(this.cVerticePoints[1].z - this.cPosition.z, this.cVerticePoints[2].z - this.cPosition.z));

        this.boundingBox = new BoundingBox(xMin, xMax, yMin, yMax, zMin, zMax);
    }

    public String toString() {
        return this.verticePoints[0] + "\n" + this.verticePoints[1] + "\n" + this.verticePoints[2] + "\n\n";
    }
    public Point[] getTextureCoordinates() {
        return textureCoordinates;
    }

    public void setTextureCoordinates(Point[] textureCoordinates) {
        this.textureCoordinates = textureCoordinates;
    }

}
