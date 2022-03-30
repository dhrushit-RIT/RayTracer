package raytracer;

import java.util.ArrayList;

public class World {
    public static double EPSILON = 0.01;
    public static double MAX_DEPTH = 3;

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

    public void simulate() {
        this.camera.takeASnap(this.superSampleFactor);
        this.camera.applyToneMapping();
        // this.camera.denormalizeColors(); // use this as separate pass later
        this.camera.generateImage();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        this.camera.setWorld(this);
    }

    public MyColor getPixelIrradiance(Ray ray) {
        // MyColor finalColor = new MyColor(128, 128, 128, false).normalize();
        MyColor finalColor = new MyColor(0, 0, 0, false).normalize();

        boolean didGetIlluminated = false;
        IntersectionDetails entityIntersectionDetails = this.checkIntersection(ray);
        if (entityIntersectionDetails == null || entityIntersectionDetails.entity == null
                || entityIntersectionDetails.intersectionPoint == null) {
            return new MyColor(Camera.DEFAULT_COLOR);
        }
        for (Light light : lightSources) {
            Ray shadowRay = new Ray(entityIntersectionDetails.intersectionPoint,
                    // TODO: create a to Entity space and to World space in util and use that during
                    // subtraction of points
                    Util.subtract(Camera.toCameraSpace(light.position), entityIntersectionDetails.intersectionPoint));
            IntersectionDetails intersectingEntity = checkIntersection(shadowRay);
            if (intersectingEntity != null || (intersectingEntity != null && intersectingEntity.entity != null)) {
                finalColor = new MyColor(0, 0, 0, true);
            } else {
                didGetIlluminated = true;
                MyColor tempColor = entityIntersectionDetails.entity.getPixelIrradiance(
                        light,
                        camera,
                        entityIntersectionDetails.intersectionPoint,
                        entityIntersectionDetails.normalAtIntersection,
                        this.techniqueToUse);

                finalColor = Util.addColor(finalColor, tempColor);
            }

        }

        return finalColor;

    }

    public MyColor illuminate(Ray ray, int depth) {
        // MyColor finalColor = new MyColor(128, 128, 128, false).normalize();
        MyColor finalColor = new MyColor(0, 0, 0, false).normalize();

        IntersectionDetails entityIntersectionDetails = this.checkIntersection(ray);
        if (IntersectionDetails.noIntersections(entityIntersectionDetails)) {
            return new MyColor(Camera.DEFAULT_COLOR);
        }

        finalColor = getColorFromIllumination(entityIntersectionDetails, ray, depth);

        return finalColor;

    }

    private MyColor getColorFromIllumination(IntersectionDetails entityIntersectionDetails, Ray ray, int depth) {
        MyColor finalColor = new MyColor(0, 0, 0, false).normalize();
        boolean didGetIlluminated = false;

        for (Light light : lightSources) {
            // TODO: create a to Entity space and to World
            // space in util and use that during
            // subtraction of points

            // intersectionPoint ----> light
            Vector shadowRayDir = Util.subtract(
                    Camera.toCameraSpace(light.position),
                    entityIntersectionDetails.intersectionPoint);
            Ray shadowRay = new Ray(entityIntersectionDetails.intersectionPoint, shadowRayDir);
            IntersectionDetails intersectingDetails = checkIntersection(shadowRay);

            if (intersectingDetails != null && intersectingDetails.entity != null) {
                // finalColor = new MyColor(0, 0, 0, true);
                continue;
            } else {
                didGetIlluminated = true;
                MyColor tempColor = entityIntersectionDetails.entity.getPixelIrradiance(
                        light,
                        camera,
                        entityIntersectionDetails.intersectionPoint,
                        entityIntersectionDetails.normalAtIntersection,
                        this.techniqueToUse);

                finalColor = Util.addColor(finalColor, tempColor);

                if (depth < MAX_DEPTH) {
                    // reflection
                    Entity entity = entityIntersectionDetails.entity;
                    if (entity.isReflective()) {
                        Vector fromIntersectionTowardsRayOrigin = Util
                                .subtract(ray.origin, entityIntersectionDetails.intersectionPoint)
                                .normalize();
                        Vector reflectedDirection = Util.reflect(
                                fromIntersectionTowardsRayOrigin,
                                entityIntersectionDetails.normalAtIntersection,
                                entityIntersectionDetails.intersectionPoint);
                        Ray reflectionRay = new Ray(
                                entityIntersectionDetails.intersectionPoint, reflectedDirection);
                        MyColor reflectedColor = Util.scaleColor(entity.kr, illuminate(reflectionRay, depth + 1));
                        finalColor = Util.addColor(finalColor, reflectedColor);
                    }

                    // transmission
                    if (entity.isTransmissive()) {

                    }
                }
            }

        }

        return finalColor;
    }

    public IntersectionDetails checkIntersection(Ray cRay) {
        Entity nearestEntity = null;
        double nearestDistance = 100000000;
        IntersectionDetails bestIntersection = new IntersectionDetails(100000000);
        for (Entity entity : worldObjects) {
            IntersectionDetails intersection = entity.intersect(cRay);
            if (intersection.distance > EPSILON) {
                if (intersection.distance < nearestDistance) {
                    nearestEntity = entity;
                    nearestDistance = intersection.distance;
                    bestIntersection = intersection;
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
}
