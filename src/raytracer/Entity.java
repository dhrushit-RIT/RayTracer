package raytracer;

import java.util.HashMap;

public abstract class Entity {

    public String name;

    public enum BSDFTechnique {
        PHONG,
        PHONG_BLINN,
        ASHIKHMIN_SHIRLEY,
        // COOK_TORRANCE
    }

    public static boolean SHOULD_USE_FALLOUT = false;

    protected static double EPSILON = 0.0000001;
    public BoundingBox boundingBox;
    BoundingBox cBoundingBox;
    protected Irradiance baseColor;
    protected Irradiance specularColor;
    protected Irradiance diffusedColor;
    protected Point position;

    protected Point cPosition;
    protected HashMap<String, Irradiance> entityColors;

    protected double ka = 0.1;
    protected double kd = 0.4;
    protected double ks = 0.5;
    protected double ke = 180;

    protected double kr = 0;
    protected double kt = 0;

    protected double n = 1.0;
    protected double nParent = 1.0;

    protected double nu = 1.0;
    protected double nv = 1.0;
    protected double Rd = 1.0;
    protected double Rs = 1.0;

    protected boolean hasTexture = false;

    protected BSDFTechnique shadingTechnique = BSDFTechnique.PHONG_BLINN;

    public Entity(Irradiance baseColor, Point position) {
        this.setBaseColor(baseColor);

        this.specularColor = this.baseColor;
        this.diffusedColor = this.baseColor;
        this.position = position;
    }

    public double getRefractiveIndex() {
        return this.n;
    }

    public void setRefractiveIndex(double n) {
        this.n = n;
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
        double xMin = this.boundingBox.xMin;
        double xMax = this.boundingBox.xMax;
        double yMin = this.boundingBox.yMin;
        double yMax = this.boundingBox.yMax;
        double zMin = this.boundingBox.zMin;
        double zMax = this.boundingBox.zMax;

        double oxMin = other.boundingBox.xMin;
        double oxMax = other.boundingBox.xMax;
        double oyMin = other.boundingBox.yMin;
        double oyMax = other.boundingBox.yMax;
        double ozMin = other.boundingBox.zMin;
        double ozMax = other.boundingBox.zMax;

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
    }

    protected abstract void computeBoundingBox();

    protected void setBaseColor(Irradiance baseColor) {
        if (baseColor == null) {
            this.baseColor = new Irradiance(255, 255, 255, false).normalize();
        } else {
            this.baseColor = baseColor.normalize();
        }
    }

    public Irradiance phongBlinn(Light light, Camera camera, Point intersecPoint, Vector normal, boolean onlyAmbient) {
        Vector lightDir = Util.subtract(Camera.toCameraSpace(light.position), intersecPoint);
        lightDir.normalize();
        double squaredDistance = 1;
        if (SHOULD_USE_FALLOUT) {
            squaredDistance = Util.sqDistance(intersecPoint, light.cPosition);
        }
        double actualIrradiance = light.irradiance / squaredDistance;
        double ambientFactor = ka * actualIrradiance;
        double diffuseFactor = kd * actualIrradiance * Util.dot(lightDir, normal);

        // diffuseFactor = Math.max(0.0, diffuseFactor);
        normal.normalize();

        // Vector reflectVector = Util.reflect(lightDir, normal, intersecPoint);
        Vector view = Util.subtract(camera.getcPosition(), intersecPoint);
        view.normalize();
        // double reflectDotView = Math.max(0.0, Util.dot(reflectVector, view));
        Vector halfway = Util.add(lightDir, view).normalize();

        double normalDotHalf = Math.max(0.0, Util.dot(halfway, normal));
        double specularFactor = ks * actualIrradiance * Math.pow(normalDotHalf, ke);

        Irradiance ambient = Util.multColor(ambientFactor, getBaseColor(intersecPoint));
        Irradiance diffuse = Util.multColor(diffuseFactor, getDiffuseColor(intersecPoint));
        Irradiance specular = Util.multColor(specularFactor, getSpecColor(intersecPoint));

        Irradiance finalColor = Util.addColor(ambient);
        if (onlyAmbient) {
            return finalColor;
        }

        finalColor = Util.addColor(finalColor, diffuse, specular);
        return finalColor;
    }

    public Irradiance phong(Light light, Camera camera, Point intersecPoint, Vector normal, boolean onlyAmbient) {
        Vector lightDir = Util.subtract(Camera.toCameraSpace(light.position), intersecPoint);
        lightDir.normalize();

        double squaredDistance = 1;
        if (SHOULD_USE_FALLOUT) {
            squaredDistance = Util.sqDistance(intersecPoint, light.cPosition);
        }
        double actualIrradiance = light.irradiance / squaredDistance;

        double ambientFactor = ka * actualIrradiance;
        double diffuseFactor = kd * actualIrradiance * Util.dot(lightDir, normal);

        normal.normalize();

        Vector reflectVector = Util.reflect(lightDir, normal, intersecPoint);
        Vector view = Util.subtract(camera.getcPosition(), intersecPoint);
        view.normalize();
        double reflectDotView = Math.max(0.0, Util.dot(reflectVector, view));
        double specularFactor = ks * actualIrradiance * Math.pow(reflectDotView, ke);

        Irradiance ambient = Util.multColor(ambientFactor, getBaseColor(intersecPoint));
        Irradiance diffuse = Util.multColor(diffuseFactor, getDiffuseColor(intersecPoint));
        Irradiance specular = Util.multColor(specularFactor, getSpecColor(intersecPoint));

        Irradiance finalColor = Util.addColor(ambient);
        if (onlyAmbient) {
            return finalColor;
        }

        finalColor = Util.addColor(finalColor, diffuse, specular);
        return finalColor;
    }

    public Irradiance ashikhiminShirley(Light light, Camera camera, Point intersecPoint, Vector normalDir) {
        normalDir.normalize();

        Vector lightDir = Util.subtract(Camera.toCameraSpace(light.position), intersecPoint).normalize();
        Vector view = Util.subtract(camera.getcPosition(), intersecPoint).normalize();
        Vector halfway = Util.add(lightDir, view).normalize();

        Vector u = Util.cross(Util.subtract(camera.getcPosition(), intersecPoint), normalDir).normalize();
        Vector v = Util.cross(normalDir, u);

        double constantCoeff = Math.sqrt((this.nu + 1) * (this.nv + 1)) / 8 / Math.PI;
        double hDotU = Util.dot(halfway, u);
        double hDotV = Util.dot(halfway, v);
        double hDotN = Util.dot(halfway, normalDir.normalize());
        double kDotH = Util.dot(halfway, lightDir);

        double power = (this.nu * hDotU * hDotU + this.nv * hDotV * hDotV) / (1 - hDotN * hDotN);
        double numerator = Math.pow(Math.max(0, Util.dot(normalDir, halfway)), power);
        double denominator = Util.dot(halfway, lightDir)
                * Math.max(Util.dot(normalDir, lightDir), Util.dot(normalDir, view));

        Irradiance fresnel = new Irradiance(this.specularColor).scaleColor(1 - Rs * Math.pow((1 - kDotH), 5));

        Irradiance specular = Util.multColor(fresnel, constantCoeff * (numerator / denominator));

        double constantCoeffDiffuse = 28 * Rd * (1 - Rs) / 23 / Math.PI;
        double lightConstant = (1 - Math.pow((1 - Util.dot(normalDir, lightDir) / 2), 5));
        double viewConstant = (1 - Math.pow((1 - Util.dot(normalDir, view) / 2), 5));

        Irradiance diffuse = new Irradiance(this.diffusedColor);
        diffuse.scaleColor(constantCoeffDiffuse * lightConstant * viewConstant);

        Irradiance finalColor = Util.addColor(diffuse, specular);
        return finalColor;
    }

    public Irradiance getPixelIrradiance(Light light, Camera camera, Point intersection, Vector normal,
            BSDFTechnique technique, boolean onlyAmbient) {
        Irradiance retColor = new Irradiance(0, 0, 0, true);

        BSDFTechnique techniqueToUse;
        if (this.shadingTechnique == null) {
            techniqueToUse = technique;
        } else {
            techniqueToUse = this.shadingTechnique;
        }

        switch (techniqueToUse) {
            case PHONG_BLINN:
                retColor = this.phongBlinn(light, camera, intersection, normal, onlyAmbient);
                break;
            case ASHIKHMIN_SHIRLEY:
                retColor = this.ashikhiminShirley(light, camera, intersection, normal);
                break;
            default:
                retColor = this.phong(light, camera, intersection, normal, onlyAmbient);
                break;
        }
        return retColor;
    }

    public void setAshikiminShirleyCoeffs(double nu, double nv, double Rs, double Rd) {
        this.nu = nu;
        this.nv = nv;
        this.Rd = Rd;
        this.Rs = Rs;
    }

    public void setShadingTechnique(BSDFTechnique technique) {
        this.shadingTechnique = technique;
    }

    public void setCoeffs(double ka, double kd, double ks, double ke) {
        this.ka = ka;
        this.kd = kd;
        this.ks = ks;
        this.ke = ke;
    }

    // public HashMap<String, MyColor> getColors() {
    // Map<String, MyColor> entityColors = new HashMap<>();
    // }

    public Irradiance getBaseColor(Point intersectionPoint) {

        if (this.hasTexture) {
            return this.computeBaseColor(intersectionPoint);
        }
        return this.baseColor;
    }

    public Irradiance getDiffuseColor(Point intersectionPoint) {

        if (this.hasTexture) {
            return this.computeBaseColor(intersectionPoint);
        }
        return this.diffusedColor;
    }

    public Irradiance getSpecColor(Point intersectionPoint) {

        if (this.hasTexture) {
            return this.computeBaseColor(intersectionPoint);
        }
        return this.specularColor;
    }

    // TODO: to be converted to entity space for actual working
    public Irradiance computeBaseColor(Point intersectionPoint) {
        Point intersectionWorld = Camera.toWorldSpace(intersectionPoint);
        double u = intersectionWorld.z;
        double v = intersectionWorld.x;
        // if(u < 0) u = u - 1;
        u /= 2;
        v /= 2;

        int row = (int) Math.floor(u / 0.2);
        int col = (int) Math.floor(v / 0.2);
        if (row % 2 == 0) {
            if (col % 2 == 0) {
                return new Irradiance(1.0, 0.0, 0.0, true);
            } else {
                return new Irradiance(1.0, 1.0, 0.0, true);
            }
        } else {
            if (col % 2 == 0) {
                return new Irradiance(1.0, 1.0, 0.0, true);
            } else {
                return new Irradiance(1.0, 0.0, 0.0, true);
            }
        }
        // return new MyColor(1, 0, 0, true);
    }

    public void setHasTexture(boolean hasTexture) {
        this.hasTexture = hasTexture;
    }

    public void setColors(Irradiance basecColor, Irradiance speColor, Irradiance diffuseColor) {
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

    public abstract IntersectionDetails<Entity> intersect(Ray ray);

    public void setTransmissiveCoeff(double kt) {
        this.kt = kt;
    }

    public void setReflectiveCoeff(double kr) {
        this.kr = kr;
    }

    public boolean isReflective() {
        return this.kr > 0;
    }

    public boolean isTransmissive() {
        return this.kt > 0;
    }

}
