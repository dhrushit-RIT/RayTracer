public class Light extends Entity {

    MyColor color;
    double irradiance;

    public Light(MyColor baseColor, Point position, double irradiance) {
        super(baseColor, position);
        this.color = baseColor;
        this.irradiance = irradiance;
    }

    @Override
    public IntersectionDetails intersect(Ray ray) {
        return new IntersectionDetails(this);
    }

}
