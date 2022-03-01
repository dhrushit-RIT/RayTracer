
public abstract class Entity {
    BoundingBox boundingBox;
    protected MyColor baseColor;
    protected MyColor specularColor;
    protected MyColor diffusedColor;
    protected Point position;

    public Entity(MyColor baseColor, Point position) {
        this.setBaseColor(baseColor);

        this.specularColor = baseColor;
        this.diffusedColor = baseColor;
        this.position = position;
    }

    protected void setBaseColor(MyColor baseColor) {
        if (baseColor == null) {
            this.baseColor = new MyColor(255, 255, 255);
        } else {
            this.baseColor = baseColor;
        }
    }

    public abstract IntersectionDetails intersect(Ray ray);
}
