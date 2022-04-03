package raytracer;

import raytracer.kdTree.*;

import java.util.ArrayList;

public class World {
    public static double EPSILON = 0.001;
    public static int MAX_DEPTH = 3;

    private KDNode kdRoot = null;

    private boolean withoutKDTree = false;

    private Entity.BSDFTechnique techniqueToUse = Entity.BSDFTechnique.PHONG;
    BoundingBox boundingBox;
    private Camera camera;

    ArrayList<Entity> worldObjects;
    ArrayList<Entity> worldObjectsXSorted;
    ArrayList<Entity> worldObjectsYSorted;
    ArrayList<Entity> worldObjectsZSorted;
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

    // public MyColor getPixelIrradiance(Ray ray) {
    // MyColor finalColor = new MyColor(0, 0, 0, false).normalize();

    // boolean didGetIlluminated = false;
    // IntersectionDetails<Entity> entityIntersectionDetails =
    // this.checkIntersection(ray);
    // if (entityIntersectionDetails == null || entityIntersectionDetails.entity ==
    // null
    // || entityIntersectionDetails.intersectionPoint == null) {
    // return new MyColor(Camera.DEFAULT_COLOR);
    // }

    // for (Light light : lightSources) {
    // Ray shadowRay = new Ray(entityIntersectionDetails.intersectionPoint,
    // // TODO: create a to Entity space and to World space in util and use that
    // during
    // // subtraction of points
    // Util.subtract(Camera.toCameraSpace(light.position),
    // entityIntersectionDetails.intersectionPoint));
    // IntersectionDetails<Entity> intersectingEntity =
    // checkIntersection(shadowRay);
    // if (intersectingEntity != null || (intersectingEntity != null &&
    // intersectingEntity.entity != null)) {
    // finalColor = new MyColor(0, 0, 0, true);
    // } else {
    // didGetIlluminated = true;
    // MyColor tempColor = entityIntersectionDetails.entity.getPixelIrradiance(
    // light,
    // camera,
    // entityIntersectionDetails.intersectionPoint,
    // entityIntersectionDetails.normalAtIntersection,
    // this.techniqueToUse);

    // finalColor = Util.addColor(finalColor, tempColor);
    // }
    // }

    // return finalColor;

    // }

    public MyColor illuminate(Ray ray, int depth) {
        MyColor finalColor = new MyColor(0, 0, 0, false).normalize();

        IntersectionDetails<Entity> entityIntersectionDetails = this.checkIntersection(ray, false);

        // no entities intersected
        // return the background color
        if (IntersectionDetails.noIntersections(entityIntersectionDetails)) {
            return new MyColor(Camera.DEFAULT_COLOR);
        }

        // ray hit some entitiy
        // get irradiance from brdf
        finalColor = getColorFromIllumination(entityIntersectionDetails, ray, depth);

        return finalColor;

    }

    private MyColor getColorFromIllumination(IntersectionDetails<Entity> entityIntersectionDetails, Ray ray,
            int depth) {
        MyColor finalColor = new MyColor(0, 0, 0, true);

        for (Light light : lightSources) {

            // intersectionPoint ----> light
            Vector shadowRayDir = Util.subtract(
                    Camera.toCameraSpace(light.position),
                    entityIntersectionDetails.intersectionPoint).normalize();
            Ray shadowRay = new Ray(entityIntersectionDetails.intersectionPoint, shadowRayDir);

            // check if shadow ray hits the light or some other entity
            IntersectionDetails<Entity> intersectingDetails = checkIntersection(shadowRay, true);

            // if entity is hit, the irradiance does not get updated
            if (intersectingDetails != null && intersectingDetails.entity != null) {
                continue;
            } else {
                // if no entities are hit then ray reaches light
                // get irradiance from brdf
                Entity entity = entityIntersectionDetails.entity;
                MyColor colorFromLightSource = entityIntersectionDetails.entity
                        .getPixelIrradiance(light, camera, entityIntersectionDetails.intersectionPoint,
                                entityIntersectionDetails.normalAtIntersection, this.techniqueToUse);

                finalColor = Util.addColor(finalColor, colorFromLightSource);

                if (depth < MAX_DEPTH) {
                    // reflection
                    if (entity.isReflective()) {
                        MyColor reflectedColor = doReflection(entityIntersectionDetails, ray, depth);
                        finalColor = Util.addColor(finalColor, reflectedColor);

                    }

                    // transmission
                    if (entity.isTransmissive()) {
                        MyColor triansmittedColor = doTransmission(entityIntersectionDetails, ray, depth);
                        finalColor = Util.addColor(finalColor, triansmittedColor);
                    }
                }
            }

        }

        return finalColor;
    }

    private MyColor doReflection(IntersectionDetails<Entity> entityIntersectionDetails, Ray ray, int depth) {
        Vector fromIntersectionTowardsRayOrigin = Util
                .subtract(ray.origin, entityIntersectionDetails.intersectionPoint)
                .normalize();
        Vector reflectedDirection = Util.reflect(
                fromIntersectionTowardsRayOrigin,
                entityIntersectionDetails.normalAtIntersection,
                entityIntersectionDetails.intersectionPoint);
        Ray reflectionRay = new Ray(
                entityIntersectionDetails.intersectionPoint, reflectedDirection);
        MyColor reflectionColor = illuminate(reflectionRay, depth + 1);
        MyColor reflectedColor = Util.scaleColor(entityIntersectionDetails.entity.kr, reflectionColor);
        return reflectedColor;
    }

    private MyColor doTransmission(IntersectionDetails<Entity> entityIntersectionDetails, Ray ray, int depth) {

        // firstTerm = ni * (d - n * (d.n)) / nt
        // second term = n * (sqrt( 1 - (ni*ni*(1 - (d.n)^2))/nt/nt ))
        // if second term D > 0
        // t = first term + second term
        // otherwise
        // t = reflected ray

        double ni = 1.0;
        double nt = 1.0;

        Ray d = new Ray(ray);
        Ray dOpp = new Ray(new Point(ray.origin), Util.scale(ray.direction, -1), ray.originEntity);

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

        double scaleFactor = ni / nt;
        double dDotN = Util.dot(dOpp.direction, entityIntersectionDetails.normalAtIntersection);
        double D = 1 - (scaleFactor * scaleFactor * (1 - (dDotN * dDotN)));

        Vector normal = new Vector(entityIntersectionDetails.normalAtIntersection);
        // if (dDotN < 0) {
        //     normal = Util.scale(normal, -1);
        //     // System.out.println("TIR");
        // }

        Vector transmittedVector;
        Ray transmittedRay;
        if (D < 0) {
            // total internal reflection
            normal = Util.scale(normal, -1);
            transmittedVector = Util.reflect(dOpp.direction, normal,
                    entityIntersectionDetails.intersectionPoint).normalize();
            transmittedRay = new Ray(entityIntersectionDetails.intersectionPoint, transmittedVector,
                    ray.originEntity);
        } else {
            D = Math.sqrt(D);
            Vector firstTerm = Util.scale(normal, D);
            Vector secondTerm = Util.scale(scaleFactor, Util.subtract(d.direction, Util.scale(normal, dDotN)));

            transmittedVector = Util.add(firstTerm, secondTerm).normalize();
            Point intersectionPoint = new Point(entityIntersectionDetails.intersectionPoint);
            transmittedRay = new Ray(intersectionPoint, transmittedVector,
                    entityIntersectionDetails.entity);
        }

        MyColor transmittedColor = illuminate(transmittedRay, depth + 1);
        MyColor finalTransmittedColor = Util.scaleColor(entityIntersectionDetails.entity.kt, transmittedColor);

        return finalTransmittedColor;
    }

    public IntersectionDetails<Entity> checkIntersection(Ray cRay, boolean ignoreTransparentObjects) {

        Entity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;
        IntersectionDetails<Entity> bestIntersection = new IntersectionDetails<>(Double.MAX_VALUE);
        ArrayList<Entity> intersectingEntities = this.getIntersectingEntities(cRay);

        if (intersectingEntities != null && !intersectingEntities.isEmpty()) {

            for (Entity entity : intersectingEntities) {
                IntersectionDetails<Entity> intersection = entity.intersect(cRay);
                if (intersection.distance > EPSILON) {
                    if(ignoreTransparentObjects && entity.isTransmissive()){
                        continue;
                    }
                    if (intersection.distance < nearestDistance) {
                        nearestEntity = entity;
                        nearestDistance = intersection.distance;
                        bestIntersection = intersection;
                    }
                }
            }
        }

        bestIntersection.entity = nearestEntity;

        if (bestIntersection.distance > 0 && bestIntersection.entity != null) {
            return bestIntersection;
        } else {
            return null;
        }
    }

    private ArrayList<Entity> getIntersectingEntities(Ray cRay) {
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
