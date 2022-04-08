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

    protected static double EPSILON = 0.0000001;
    public BoundingBox boundingBox;
    BoundingBox cBoundingBox;
    protected MyColor baseColor;
    protected MyColor specularColor;
    protected MyColor diffusedColor;
    protected Point position;

    protected Point cPosition;
    protected HashMap<String, MyColor> entityColors;

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

    protected boolean hasTexture = false;

    protected BSDFTechnique shadingTechnique = BSDFTechnique.PHONG_BLINN;

    public Entity(MyColor baseColor, Point position) {
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

    protected void setBaseColor(MyColor baseColor) {
        if (baseColor == null) {
            this.baseColor = new MyColor(255, 255, 255, false).normalize();
        } else {
            this.baseColor = baseColor.normalize();
        }
    }

    public MyColor phongBlinn(Light light, Camera camera, Point intersecPoint, Vector normal, boolean onlyAmbient) {
        Vector lightDir = Util.subtract(Camera.toCameraSpace(light.position), intersecPoint);
        lightDir.normalize();
        double ambientFactor = ka * light.irradiance;
        double diffuseFactor = kd * light.irradiance * Util.dot(lightDir, normal);

        // diffuseFactor = Math.max(0.0, diffuseFactor);
        normal.normalize();

        // Vector reflectVector = Util.reflect(lightDir, normal, intersecPoint);
        Vector view = Util.subtract(camera.getcPosition(), intersecPoint);
        view.normalize();
        // double reflectDotView = Math.max(0.0, Util.dot(reflectVector, view));
        Vector halfway = Util.add(lightDir, view).normalize();

        double normalDotHalf = Math.max(0.0, Util.dot(halfway, normal));
        double specularFactor = ks * light.irradiance * Math.pow(normalDotHalf, ke);

        MyColor ambient = Util.multColor(ambientFactor, getBaseColor(intersecPoint));
        MyColor diffuse = Util.multColor(diffuseFactor, getDiffuseColor(intersecPoint));
        MyColor specular = Util.multColor(specularFactor, getSpecColor(intersecPoint));

        MyColor finalColor = Util.addColor(ambient);
        if (onlyAmbient) {
            return finalColor;
        }

        finalColor = Util.addColor(finalColor, diffuse, specular);
        return finalColor;
    }

    public MyColor phong(Light light, Camera camera, Point intersecPoint, Vector normal, boolean onlyAmbient) {
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

        MyColor ambient = Util.multColor(ambientFactor, getBaseColor(intersecPoint));
        MyColor diffuse = Util.multColor(diffuseFactor, getDiffuseColor(intersecPoint));
        MyColor specular = Util.multColor(specularFactor, getSpecColor(intersecPoint));

        MyColor finalColor = Util.addColor(ambient);
        if (onlyAmbient) {
            return finalColor;
        }

        finalColor = Util.addColor(finalColor, diffuse, specular);
        return finalColor;
    }

    public MyColor ashikhiminShirley(Light light, Camera camera, Point intersecPoint, Vector normalDir) {
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

        MyColor fresnel = Util.addColor(this.specularColor,
                Util.multColor(this.specularColor.getComplement(), Math.pow((1 - kDotH), 5)));

        MyColor specular = Util.multColor(fresnel, constantCoeff * (numerator / denominator));

        //
        // Diffuse
        //

        // double constantCoeffDiffuse = 28 / 23 / Math.PI;
        // double lightConstant = (1 - Math.pow((1 - Math.abs(Util.dot(normalDir,
        // lightDir) / 2)), 5));
        // double viewConstant = (1 - Math.pow((1 - Math.abs(Util.dot(normalDir, view) /
        // 2)), 5));

        double constantCoeffDiffuse = 28 / 23 / Math.PI;
        double lightConstant = (1 - Math.pow((1 - Util.dot(normalDir, lightDir) / 2), 5));
        double viewConstant = (1 - Math.pow((1 - Util.dot(normalDir, view) / 2), 5));

        MyColor diffuse = Util.multColor(this.diffusedColor, this.specularColor.getComplement());
        diffuse.multColor(constantCoeffDiffuse * lightConstant * viewConstant);

        MyColor finalColor = Util.addColor(diffuse, specular);
        return finalColor;
    }

    public MyColor getPixelIrradiance(Light light, Camera camera, Point intersection, Vector normal,
            BSDFTechnique technique, boolean onlyAmbient) {
        MyColor retColor = new MyColor(0, 0, 0, true);

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

    public void setAshikiminShirleyCoeffs(double nu, double nv) {
        this.nu = nu;
        this.nv = nv;
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

    public MyColor getBaseColor(Point intersectionPoint) {

        if (this.hasTexture) {
            return this.computeBaseColor(intersectionPoint);
        }
        return this.baseColor;
    }

    public MyColor getDiffuseColor(Point intersectionPoint) {

        if (this.hasTexture) {
            return this.computeBaseColor(intersectionPoint);
        }
        return this.diffusedColor;
    }

    public MyColor getSpecColor(Point intersectionPoint) {

        if (this.hasTexture) {
            return this.computeBaseColor(intersectionPoint);
        }
        return this.specularColor;
    }

    // TODO: to be converted to entity space for actual working
    public MyColor computeBaseColor(Point intersectionPoint) {
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
                return new MyColor(1.0, 0.0, 0.0, true);
            } else {
                return new MyColor(1.0, 1.0, 0.0, true);
            }
        } else {
            if (col % 2 == 0) {
                return new MyColor(1.0, 1.0, 0.0, true);
            } else {
                return new MyColor(1.0, 0.0, 0.0, true);
            }
        }
        // return new MyColor(1, 0, 0, true);
    }

    public void setHasTexture(boolean hasTexture) {
        this.hasTexture = hasTexture;
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
