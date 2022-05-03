package raytracer;

import raytracer.kdTree.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class World {

    public static final int NUM_RAYS_TO_CREATE = 2;
    public static long SEED = 42;
    public static boolean DEBUG_FLAG = false;
    public static double EPSILON = 0.001;
    public static int MAX_DEPTH = 10;

    private KDNode kdRoot = null;
    private Random rng = new Random(SEED);

    private boolean withoutKDTree = false;

    private Entity.BSDFTechnique techniqueToUse = Entity.BSDFTechnique.PHONG;
    BoundingBox boundingBox;
    private Camera camera;

    ArrayList<Entity> worldObjects;
    ArrayList<Light> lightSources;
    private int superSampleFactor;

    public World() {

        this.worldObjects = new ArrayList<>();
        this.lightSources = new ArrayList<>();
        this.superSampleFactor = 1;

    }

    public void setBSDFTechnique(Entity.BSDFTechnique technique) {
        this.techniqueToUse = technique;
    }

    public void setSuperSampleFactor(int factor) {
        this.superSampleFactor = factor;
    }

    public void addEntity(Entity entity) {
        this.worldObjects.add(entity);
    }

    public void addLightSource(Light light) {
        this.lightSources.add(light);
    }

    // assuming world's center is 0,0,0
    private void computeBoundingBox() {
        double xMin = Double.MAX_VALUE;
        double yMin = Double.MAX_VALUE;
        double zMin = Double.MAX_VALUE;
        double xMax = -Double.MAX_VALUE;
        double yMax = -Double.MAX_VALUE;
        double zMax = -Double.MAX_VALUE;

        for (Entity entity : this.worldObjects) {
            xMin = Math.min(xMin, entity.boundingBox.xMin);
            yMin = Math.min(yMin, entity.boundingBox.yMin);
            zMin = Math.min(zMin, entity.boundingBox.zMin);
            xMax = Math.max(xMax, entity.boundingBox.xMax);
            yMax = Math.max(yMax, entity.boundingBox.yMax);
            zMax = Math.max(zMax, entity.boundingBox.zMax);
        }

        // TODO : xmin to 0 and xmax to xmax - xmin
        this.boundingBox = new BoundingBox(xMin, xMax, yMin, yMax, zMin, zMax);
    }

    public void simulate() {
        this.computeEntityPositionInCurrentEntitySpace();
        System.out.println("entities per voxel : " + KDTree.MAX_ENTITIES_IN_VOXEL);
        long startTimeBuildKD = System.currentTimeMillis();
        System.out.println("Starting to build the kd-Tree");
        this.computeBoundingBox();
        System.out.println(this);

        this.generateKDTree();
        System.out.println("kd-Tree building complete...");
        long endTimeBuildKD = System.currentTimeMillis();

        long startTime = System.currentTimeMillis();

        this.camera.takeASnap(this.superSampleFactor);
        this.camera.applyToneMapping();
        // this.camera.denormalizeColors(); // use this as separate pass later
        this.camera.generateImage();
        long endTime = System.currentTimeMillis();

        System.out.println("Time to build kd tree : " + (endTimeBuildKD - startTimeBuildKD) + "ms");
        System.out.println("Time to render : " + (endTime - startTime) + "ms");
    }

    private void computeEntityPositionInCurrentEntitySpace() {
        for (Light light : this.lightSources)
            light.getPositionInCameraCoordinates();
        for (Entity entity : this.worldObjects)
            entity.getPositionInCameraCoordinates();
    }

    private void generateKDTree() {

        Voxel worldVoxel = new Voxel(
                this.boundingBox.xMin, this.boundingBox.xMax,
                this.boundingBox.yMin, this.boundingBox.yMax,
                this.boundingBox.zMin, this.boundingBox.zMax);

        this.kdRoot = raytracer.kdTree.KDTree.getNode(this.worldObjects, worldVoxel, AAPlane.Alignment.XY, 0);
        System.out.println(Voxel.count + " " + Voxel.leafCount);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        this.camera.setWorld(this);
    }

    public Irradiance kajiaIlluminate(Ray ray, IntersectionDetails<Entity> source, int depth) {
        Irradiance finalColor = new Irradiance(0, 0, 0, false).normalize();

        ArrayList<IntersectionDetails<Entity>> entityIntersectionDetailsList = this.checkIntersection(ray, false);

        // no entities intersected
        // return the background color
        if (entityIntersectionDetailsList.size() == 0) {
            return new Irradiance(Camera.DEFAULT_COLOR);
        }

        ArrayList<IntersectionDetails<Entity>> lightIntersectionDetailsList = this.checkLightIntersetion(ray);
        if (lightIntersectionDetailsList.size() > 0) {

            for (IntersectionDetails<Entity> lightIntersection : lightIntersectionDetailsList) {

                Light light = (Light) lightIntersection.entity;
                Irradiance colorFromLightSource = ray.originEntity
                        .getPixelIrradiance(light, camera,
                                source.intersectionPoint,
                                source.normalAtIntersection,
                                this.techniqueToUse, false);
                finalColor.addColor(colorFromLightSource);
            }
            return finalColor;
        }

        // importance sampling at the current point
        // create ray from which light is coming
        IntersectionDetails<Entity> intersectingEntityDetails = entityIntersectionDetailsList.get(0);
        Entity intersectingEntity = intersectingEntityDetails.entity;
        ArrayList<Ray> scatteredRays = new ArrayList<>();

        

        // generate scattered rays
        for (int i = 0; i < World.NUM_RAYS_TO_CREATE; i++) {
            Vector direction = new Vector(0, 0, 0);

            // decide if diffuse or specular
            double diffuseOrSpecular = rng.nextDouble();

            // diffuse
            if (diffuseOrSpecular <= intersectingEntity.kd) {
                double u1 = rng.nextDouble();
                double u2 = rng.nextDouble();

                double theta = Math.acos(Math.sqrt(u1));
                double phi = 2 * Math.PI * u2;

                double x = Math.cos(phi) * Math.sin(theta);
                double y = Math.sin(phi) * Math.sin(theta);
                double z = Math.cos(theta);

                direction = new Vector(x, y, z);

            }

            // specular
            else if (diffuseOrSpecular > intersectingEntity.kd
                    && diffuseOrSpecular <= intersectingEntity.kd + intersectingEntity.ks) {

            }

            // no reflection case. do not create any rays
            else {

            }

            scatteredRays.add(new Ray(intersectingEntityDetails.intersectionPoint, direction));
        }

        for (Ray scatteredRay : scatteredRays) {
            Irradiance receiveIrradiance = kajiaIlluminate(scatteredRay, intersectingEntityDetails, depth + 1);

            // check if the ray was fruitful, if so, then want to explore it further with a
            // little deviation
            // this might be somewhere else

            finalColor.addColor(receiveIrradiance);
        }

        return finalColor;

    }

    public Irradiance illuminate(Ray ray, int depth) {
        Irradiance finalColor = new Irradiance(0, 0, 0, false).normalize();

        ArrayList<IntersectionDetails<Entity>> entityIntersectionDetailsList = this.checkIntersection(ray, false);

        // no entities intersected
        // return the background color
        if (entityIntersectionDetailsList.size() == 0) {
            return new Irradiance(Camera.DEFAULT_COLOR);
        }

        // ray hit some entitiy
        // get irradiance from brdf
        finalColor = getColorFromIllumination(entityIntersectionDetailsList.get(0), ray, depth);

        return finalColor;

    }

    private Irradiance getColorFromIllumination(IntersectionDetails<Entity> entityIntersectionDetailsList, Ray ray,
            int depth) {
        Irradiance finalColor = new Irradiance(0, 0, 0, true);

        for (Light light : lightSources) {

            // intersectionPoint ----> light
            Vector shadowRayDir = Util.subtract(
                    Camera.toCameraSpace(light.position),
                    entityIntersectionDetailsList.intersectionPoint).normalize();
            Point intersectionPoint = new Point(entityIntersectionDetailsList.intersectionPoint);
            intersectionPoint = Sphere.addEpsilonDisplacementToIntersection(intersectionPoint,
                    entityIntersectionDetailsList.normalAtIntersection);
            Ray shadowRay = new Ray(intersectionPoint, shadowRayDir);

            // check if shadow ray hits the light or some other entity
            ArrayList<IntersectionDetails<Entity>> intersectingDetailsList = checkIntersection(shadowRay, true);

            double scaleFactor = 1.0;
            for (IntersectionDetails<Entity> id : intersectingDetailsList) {
                if (id.entity.isTransmissive() == false) {
                    scaleFactor = 0;
                    break;
                } else {
                    scaleFactor *= id.entity.kt;
                }
            }

            // if entity is hit, the irradiance does not get updated
            if (scaleFactor == 0) {
                continue;
            } else {
                // if no entities are hit then ray reaches light
                // get irradiance from brdf
                Entity entity = entityIntersectionDetailsList.entity;
                Irradiance colorFromLightSource = entityIntersectionDetailsList.entity
                        .getPixelIrradiance(light, camera, entityIntersectionDetailsList.intersectionPoint,
                                entityIntersectionDetailsList.normalAtIntersection, this.techniqueToUse, false);

                finalColor = Util.addColor(finalColor, Util.scaleColor(scaleFactor, colorFromLightSource));
                if (entity.isTransmissive()) {
                    finalColor = Util.scaleColor(1 - entity.kt, finalColor);
                }

                if (entity.isReflective()) {
                    finalColor = Util.scaleColor(1 - entity.kr, finalColor);
                }

                // if (World.DEBUG_FLAG)
                // System.out.println("origin: " + ray.origin + "\nintersection: "
                // + entityIntersectionDetails.intersectionPoint + "\nray: " + ray.direction
                // + "\nentity: " + entityIntersectionDetails.entity.name
                // + "\n\n");

                if (depth < MAX_DEPTH) {
                    // reflection
                    if (entity.isReflective()) {
                        Irradiance reflectedColor = doReflection(entityIntersectionDetailsList, ray, depth);
                        finalColor = Util.addColor(finalColor, reflectedColor);

                    }

                    // transmission
                    if (entity.isTransmissive()) {
                        Irradiance triansmittedColor = doTransmission(entityIntersectionDetailsList, ray, depth);
                        finalColor = Util.addColor(finalColor, triansmittedColor);
                    }
                }
            }
        }

        return finalColor;
    }

    private Irradiance doReflection(IntersectionDetails<Entity> entityIntersectionDetails, Ray ray, int depth) {
        Vector fromIntersectionTowardsRayOrigin = Util
                .subtract(ray.origin, entityIntersectionDetails.intersectionPoint)
                .normalize();
        Vector reflectedDirection = Util.reflect(
                fromIntersectionTowardsRayOrigin,
                entityIntersectionDetails.normalAtIntersection,
                entityIntersectionDetails.intersectionPoint);
        Ray reflectionRay = new Ray(
                entityIntersectionDetails.intersectionPoint, reflectedDirection);
        Irradiance reflectionColor = illuminate(reflectionRay, depth + 1);
        Irradiance reflectedColor = Util.scaleColor(entityIntersectionDetails.entity.kr, reflectionColor);
        return reflectedColor;
    }

    private Irradiance doTransmission(IntersectionDetails<Entity> entityIntersectionDetails, Ray ray, int depth) {

        // firstTerm = ni * (d - n * (d.n)) / nt
        // second term = n * (sqrt( 1 - (ni*ni*(1 - (d.n)^2))/nt/nt ))
        // if second term D > 0
        // t = first term + second term
        // otherwise
        // t = reflected ray

        double ni = 1.0;
        double nt = 1.0;

        Vector normal = new Vector(entityIntersectionDetails.normalAtIntersection);
        Ray d = new Ray(ray);

        Entity originEntity = ray.originEntity;
        if (originEntity != null && originEntity != entityIntersectionDetails.entity) {
            ni = originEntity.getRefractiveIndex();
            nt = entityIntersectionDetails.entity.getRefractiveIndex();
        } else if (originEntity != null) {
            ni = originEntity.getRefractiveIndex();
            nt = originEntity.nParent;
        } else {
            ni = 1.0;
            nt = entityIntersectionDetails.entity.getRefractiveIndex();
        }

        double dDotN = Util.dot(d.direction, normal);

        if (dDotN < 0) {
            normal = Util.scale(normal, -1);
            dDotN = Util.dot(d.direction, normal);
            // System.out.println("TIR");
        }

        double scaleFactor = ni / nt;
        double D = 1 - (scaleFactor * scaleFactor * (1 - (dDotN * dDotN)));

        Vector transmittedVector;
        Ray transmittedRay;

        if (D < 0) {
            // total internal reflection
            // normal = Util.scale(normal, -1);
            transmittedVector = Util.reflect2(d.direction, normal,
                    entityIntersectionDetails.intersectionPoint).normalize();
            if (World.DEBUG_FLAG)
                System.out.println("TIR\n" + "origin: " + ray.origin + "\nintersection: "
                        + entityIntersectionDetails.intersectionPoint + "\nray: " + ray.direction + "\ntransmitted: "
                        + transmittedVector
                        + "\nnormal: " + entityIntersectionDetails.normalAtIntersection
                        + "\nentity: " + entityIntersectionDetails.entity.name
                        + "\n\n");
            transmittedRay = new Ray(entityIntersectionDetails.intersectionPoint, transmittedVector,
                    ray.originEntity);
        } else {

            D = Math.sqrt(D);
            Vector secondTerm = Util.scale(normal, D);
            Vector firstTerm = Util.scale(scaleFactor, Util.subtract(d.direction, Util.scale(normal, dDotN)));

            transmittedVector = Util.add(firstTerm, secondTerm).normalize();
            if (World.DEBUG_FLAG)
                System.out.println("Refraction\n" + "origin: " + ray.origin + "\nintersection: "
                        + entityIntersectionDetails.intersectionPoint + "\nft: " + firstTerm
                        + "\nst: " + secondTerm + "\nray: " + ray.direction + "\ntransmitted: " + transmittedVector
                        + "\nentity: " + entityIntersectionDetails.entity.name
                        + "\n\n");
            Point intersectionPoint = new Point(entityIntersectionDetails.intersectionPoint);
            transmittedRay = new Ray(intersectionPoint, transmittedVector,
                    entityIntersectionDetails.entity);
        }

        Irradiance transmittedColor = illuminate(transmittedRay, depth + 1);
        Irradiance finalTransmittedColor = Util.scaleColor(entityIntersectionDetails.entity.kt, transmittedColor);

        return finalTransmittedColor;
    }

    public ArrayList<IntersectionDetails<Entity>> checkLightIntersetion(Ray cRay) {

        ArrayList<IntersectionDetails<Entity>> intersectionList = new ArrayList<>();

        for (Entity entity : lightSources) {
            IntersectionDetails<Entity> intersection = entity.intersect(cRay); // TODO : implement intersect for light
            if (intersection.distance > EPSILON) {
                intersectionList.add(intersection);
            }
        }

        return intersectionList;
    }

    public ArrayList<IntersectionDetails<Entity>> checkIntersection(Ray cRay, boolean ignoreTransparentObjects) {

        ArrayList<IntersectionDetails<Entity>> intersectionList = new ArrayList<>();
        Entity nearestEntity = null;
        // double nearestDistance = Double.MAX_VALUE;
        IntersectionDetails<Entity> bestIntersection = new IntersectionDetails<>(Double.MAX_VALUE);
        ArrayList<Entity> voxelEntities = this.getVoxelEntities(cRay);

        if (voxelEntities != null && !voxelEntities.isEmpty()) {

            for (Entity entity : voxelEntities) {
                IntersectionDetails<Entity> intersection = entity.intersect(cRay);
                if (intersection.distance > EPSILON) {
                    intersectionList.add(intersection);
                }
            }
        }

        intersectionList.sort(new Comparator<IntersectionDetails<Entity>>() {

            @Override
            public int compare(IntersectionDetails<Entity> o1, IntersectionDetails<Entity> o2) {
                double result = o1.distance - o2.distance;
                if (result < 0)
                    return -1;
                else if (result > 0)
                    return 1;
                return 0;
            }

        });
        bestIntersection.entity = nearestEntity;

        return intersectionList;
        // if (bestIntersection.distance > 0 && bestIntersection.entity != null) {
        // return bestIntersection;
        // } else {
        // return null;
        // }
    }

    private ArrayList<Entity> getVoxelEntities(Ray cRay) {
        if (this.withoutKDTree) {
            return this.worldObjects;
        }
        if (this.kdRoot == null) {
            return this.worldObjects;
        }
        // return this.worldObjects;
        ArrayList<Entity> intersectingEntities = KDTree.getEntityList(this.kdRoot,
                cRay);
        return intersectingEntities;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------\n");
        sb.append("World:\n" + "Bounds:\n" + this.boundingBox + "\n");
        sb.append("----------------------------\n");
        return sb.toString();
    }
}
