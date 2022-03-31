package raytracer;

import java.util.ArrayList;

import raytracer.kdtree.AAPlane;
import raytracer.kdtree.KDNode;
import raytracer.kdtree.KDTree;
import raytracer.kdtree.Voxel;

public class World {
    public static double EPSILON = 0.01;

    private Entity.BSDFTechnique techniqueToUse = Entity.BSDFTechnique.PHONG;
    BoundingBox boundingBox;
    private Camera camera;

    ArrayList<Entity> worldObjects;
    ArrayList<Light> lightSources;
    private int superSampleFactor;

    private Voxel worldVoxel;

    private KDNode kdRoot;

    public World() {
        this.worldObjects = new ArrayList<>();
        this.lightSources = new ArrayList<>();
        this.superSampleFactor = 1;
    }

    private Voxel computeWorldVoxel() {
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

        return new Voxel(xMin, xMax, yMin, yMax, zMin, zMax);
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
        this.worldVoxel = this.computeWorldVoxel();
        this.kdRoot = KDTree.getNode(this.worldObjects, this.worldVoxel, AAPlane.Alignment.XY);
        System.out.println("total voxels : " + Voxel.count + " leaf : " + Voxel.leaf);
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
        IntersectionDetails<Entity> entityIntersectionDetails = this.checkIntersection(ray);
        if (entityIntersectionDetails == null || entityIntersectionDetails.entity == null
                || entityIntersectionDetails.intersectionPoint == null) {
            return new MyColor(Camera.DEFAULT_COLOR);
        }
        for (Light light : lightSources) {
            Ray shadowRay = new Ray(entityIntersectionDetails.intersectionPoint,
                    // TODO: create a to Entity space and to World space in util and use that during
                    // subtraction of points
                    Util.subtract(Camera.toCameraSpace(light.position), entityIntersectionDetails.intersectionPoint));
            IntersectionDetails<Entity> intersectingEntity = checkIntersection(shadowRay);
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

    public IntersectionDetails<Entity> checkIntersection(Ray cRay) {
        Entity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;
        // ArrayList<Entity> intersectingEntities = this.worldObjects;
        ArrayList<Entity> intersectingEntities = KDTree.getEntities(this.kdRoot,
                cRay);
        IntersectionDetails<Entity> bestIntersection = new IntersectionDetails<>(Double.MAX_VALUE);
        for (Entity entity : intersectingEntities) {
            IntersectionDetails<Entity> intersection = entity.intersect(cRay);
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
