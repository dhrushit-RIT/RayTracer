package raytracer;

public abstract class Entity {

    public enum BSDFTechnique {
        PHONG,
        PHONG_BLINN,
        // ASHIKHMIN_SHIRLEY,
        // COOK_TORRANCE
    }

    protected static double EPSILON = 0.000001;
    public BoundingBox boundingBox;
    BoundingBox cBoundingBox;
    protected MyColor baseColor;
    protected MyColor specularColor;
    protected MyColor diffusedColor;
    protected Point position;

    protected Point cPosition;

    protected double ka = 0.1;
    protected double kd = 0.4;
    protected double ks = 0.5;
    protected double ke = 180;

    public Entity(MyColor baseColor, Point position) {
        this.setBaseColor(baseColor);

        this.specularColor = baseColor;
        this.diffusedColor = baseColor;
        this.position = position;
    }

    public Point getPositionInCameraCoordinates() {
        if (this.cPosition == null) {
            this.computeCPosition();
        }
        return this.cPosition;
    }

    private void computeCPosition() {
        this.cPosition = Camera.toCameraSpace(position);
    }

    // TODO: make the bounding boxes independent of the camera position
    public boolean intersect(Entity other) {
        double xMin = this.boundingBox.xMin + this.position.x;
        double xMax = this.boundingBox.xMax + this.position.x;
        double yMin = this.boundingBox.yMin + this.position.y;
        double yMax = this.boundingBox.yMax + this.position.y;
        double zMin = this.boundingBox.zMin + this.position.z;
        double zMax = this.boundingBox.zMax + this.position.z;

        double oxMin = other.boundingBox.xMin + other.position.x;
        double oxMax = other.boundingBox.xMax + other.position.x;
        double oyMin = other.boundingBox.yMin + other.position.y;
        double oyMax = other.boundingBox.yMax + other.position.y;
        double ozMin = other.boundingBox.zMin + other.position.z;
        double ozMax = other.boundingBox.zMax + other.position.z;

        boolean xNotIntersecting = xMin > oxMax || oxMin > xMax;
        if (xNotIntersecting)
            return false;
        boolean yNotIntersecting = yMin > oyMax || oyMin > yMax;
        if (yNotIntersecting)
            return false;
        boolean zNotIntersecting = zMin > ozMax || ozMin > zMax;
        if (zNotIntersecting)
            return false;

        return true;

        // this.getPositionInCameraCoordinates();
        // BoundingBox otherBoundingBoxInThisFrame = new BoundingBox(
        // other.boundingBox.xMin - this.cPosition.x,
        // other.boundingBox.xMax - this.cPosition.x,
        // other.boundingBox.yMin - this.cPosition.y,
        // other.boundingBox.yMax - this.cPosition.y,
        // other.boundingBox.zMin - this.cPosition.z,
        // other.boundingBox.zMax - this.cPosition.z);
        // return this.boundingBox.intersect(otherBoundingBoxInThisFrame);
    }

    protected abstract void computeBoundingBox();

    protected void setBaseColor(MyColor baseColor) {
        if (baseColor == null) {
            this.baseColor = new MyColor(255, 255, 255, false).normalize();
        } else {
            this.baseColor = baseColor.normalize();
        }
    }

    public MyColor phongBlinn(Light light, Camera camera, Point intersecPoint, Vector normal) {
        Vector lightDir = Util.subtract(Camera.toCameraSpace(light.position), intersecPoint);
        lightDir.normalize();
        double ambientFactor = ka * light.irradiance;
        double diffuseFactor = kd * light.irradiance * Util.dot(lightDir, normal);

        normal.normalize();

        // Vector reflectVector = Util.reflect(lightDir, normal, intersecPoint);
        Vector view = Util.subtract(camera.getcPosition(), intersecPoint);
        view.normalize();
        // double reflectDotView = Math.max(0.0, Util.dot(reflectVector, view));
        Vector halfway = Util.add(lightDir, view).normalize();
        double normalDotHalf = Math.max(0.0, Util.dot(halfway, normal));

        double specularFactor = ks * light.irradiance * Math.pow(normalDotHalf, ke);

        MyColor ambient = Util.multColor(ambientFactor, baseColor);
        MyColor diffuse = Util.multColor(diffuseFactor, diffusedColor);
        MyColor specular = Util.multColor(specularFactor, specularColor);

        MyColor finalColor = Util.addColor(ambient, diffuse, specular);
        return finalColor;
    }

    public MyColor phong(Light light, Camera camera, Point intersecPoint, Vector normal) {
        Vector lightDir = Util.subtract(Camera.toCameraSpace(light.position), intersecPoint);
        lightDir.normalize();
        double ambientFactor = ka * light.irradiance;
        double diffuseFactor = kd * light.irradiance * Util.dot(lightDir, normal);

        normal.normalize();

        Vector reflectVector = Util.reflect(lightDir, normal, intersecPoint);
        Vector view = Util.subtract(camera.getcPosition(), intersecPoint);
        view.normalize();
        double reflectDotView = Math.max(0.0, Util.dot(reflectVector, view));
        double specularFactor = ks * light.irradiance * Math.pow(reflectDotView, ke);

        MyColor ambient = Util.multColor(ambientFactor, baseColor);
        MyColor diffuse = Util.multColor(diffuseFactor, diffusedColor);
        MyColor specular = Util.multColor(specularFactor, specularColor);

        MyColor finalColor = Util.addColor(ambient, diffuse, specular);
        return finalColor;
    }

    public MyColor getPixelIrradiance(Light light, Camera camera, Point intersection, Vector normal,
            BSDFTechnique technique) {
        MyColor retColor = new MyColor(0, 0, 0, true);
        switch (technique) {
            case PHONG_BLINN:
                retColor = this.phongBlinn(light, camera, intersection, normal);
                break;
            default:
                retColor = this.phong(light, camera, intersection, normal);
                break;
        }
        return retColor;
    }

    public void setCoeffs(double ka, double kd, double ks, double ke) {
        this.ka = ka;
        this.kd = kd;
        this.ks = ks;
        this.ke = ke;
    }

    public void setColors(MyColor basecColor, MyColor speColor, MyColor diffuseColor) {
        this.baseColor = basecColor.normalize();
        this.specularColor = speColor.normalize();
        this.diffusedColor = diffuseColor.normalize();
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public abstract IntersectionDetails intersect(Ray ray);
}
