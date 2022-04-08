package raytracer;
public class Ray {

    public Point origin;
    public Vector direction;

    public Entity originEntity;

    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
        this.direction.normalize();
        this.originEntity = null;
    }

    public Ray(Ray other) {
        this.origin = new Point(other.origin);
        this.direction = new Vector(other.direction);
        this.originEntity = other.originEntity;
    }

    public Ray(Point origin, Vector direction, Entity originEntity) {
        this.origin = origin;
        this.direction = direction;
        this.direction.normalize();
        this.originEntity = originEntity;
    }

    public Vector getDirection() {
        return direction;
    }

    public Point getOrigin() {
        return origin;
    }
}
