
public abstract class Entity {

    public enum BSDFTechnique {
        PHONG,
        PHONG_BLINN,
        ASHIKHMIN_SHIRLEY,
        // COOK_TORRANCE
    }

    protected static double EPSILON = 0.000001;
    BoundingBox boundingBox;
    protected MyColor baseColor;
    protected MyColor specularColor;
    protected MyColor diffusedColor;
    protected Point position;

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

    protected void setBaseColor(MyColor baseColor) {
        if (baseColor == null) {
            this.baseColor = new MyColor(255, 255, 255, false).normalize();
        } else {
            this.baseColor = baseColor.normalize();
        }
    }

    public MyColor ashikhiminShirley(Light light, Camera camera, Point intersecPoint, Vector normalDir) {
        normalDir.normalize();

        double nu = 10.0;
        double nv = 10.0;

        Vector lightDir = Util.subtract(Camera.toCameraSpace(light.position), intersecPoint).normalize();
        Vector view = Util.subtract(camera.getcPosition(), intersecPoint).normalize();
        Vector halfway = Util.add(lightDir, view).normalize();

        Vector u = Util.cross(Util.subtract(camera.getcPosition(), intersecPoint), normalDir).normalize();
        Vector v = Util.cross(normalDir, u);

        double constantCoeff = Math.sqrt((nu + 1) * (nv + 1)) / 8 / Math.PI;
        double hDotU = Util.dot(halfway, u);
        double hDotV = Util.dot(halfway, v);
        double hDotN = Util.dot(halfway, normalDir.normalize());
        double kDotH = Util.dot(halfway, lightDir);

        double power = (nu * hDotU * hDotU + nv * hDotV * hDotV) / (1 - hDotN * hDotN);
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

    public MyColor phongBlinn(Light light, Camera camera, Point intersecPoint, Vector normal) {
        Vector lightDir = Util.subtract(Camera.toCameraSpace(light.position), intersecPoint).normalize();

        double ambientFactor = ka * light.irradiance;
        double diffuseFactor = kd * light.irradiance * Util.dot(lightDir, normal);

        normal.normalize();

        // Vector reflectVector = Util.reflect(lightDir, normal, intersecPoint);
        Vector view = Util.subtract(camera.getcPosition(), intersecPoint).normalize();
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
        double reflectDotView = Math.abs(Util.dot(reflectVector, view));
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
            case ASHIKHMIN_SHIRLEY:
                retColor = this.ashikhiminShirley(light, camera, intersection, normal);
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

    public abstract IntersectionDetails intersect(Ray ray);
}
