package raytracer;

public class Light extends Entity {

    Irradiance color;
    double irradiance;

    public Light(Irradiance baseColor, Point position, double irradiance) {
        super(baseColor, position);
        this.color = baseColor;
        this.irradiance = irradiance;
    }

    @Override
    public IntersectionDetails<Entity> intersect(Ray ray) {
        return new IntersectionDetails<Entity>(this);
    }

    @Override
    protected void computeBoundingBox() {

    }

}
