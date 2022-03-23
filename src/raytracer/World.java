package raytracer;

import raytracer.kdTree.*;

import java.util.ArrayList;

public class World {
    public static double EPSILON = 0.01;

    private KDNode kdRoot = null;

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
        // TODO : optimization if you want to avoid making pointer copies al the voxels
        // this.worldObjectsXSorted = new ArrayList<>();
        // this.worldObjectsYSorted = new ArrayList<>();
        // this.worldObjectsZSorted = new ArrayList<>();
        this.lightSources = new ArrayList<>();
        this.superSampleFactor = 1;

        // Voxel.terminalCondition = Voxel.TerminalCondition.COUNT_ENTITIES;
        // this.kdRoot = new Voxel(Voxel.Division.NONE, worldObjects);
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
            // Point entityPosition = entity.position;
            xMin = Math.min(xMin, entity.getPosition().x + entity.boundingBox.xMin);
            yMin = Math.min(yMin, entity.getPosition().y + entity.boundingBox.yMin);
            zMin = Math.min(zMin, entity.getPosition().z + entity.boundingBox.zMin);
            xMax = Math.max(xMax, entity.getPosition().x + entity.boundingBox.xMax);
            yMax = Math.max(yMax, entity.getPosition().y + entity.boundingBox.yMax);
            zMax = Math.max(zMax, entity.getPosition().z + entity.boundingBox.zMax);
        }

        this.boundingBox = new BoundingBox(xMin, xMax, yMin, yMax, zMin, zMax);
    }

    private Point getWorldVoxelOrigin() {
        this.computeBoundingBox();
        return new Point(this.boundingBox.xMin, this.boundingBox.yMin, this.boundingBox.zMin, Point.Space.WORLD);
    }

    public void simulate() {
        System.out.println("Starting to build the kd-Tree");
        this.generateKDTree();
        System.out.println("kd-Tree building complete...");

        this.camera.takeASnap(this.superSampleFactor);
        this.camera.applyToneMapping();
        // this.camera.denormalizeColors(); // use this as separate pass later
        this.camera.generateImage();
    }

    private void generateKDTree() {
        Point voxelPosition = this.getWorldVoxelOrigin();
        Voxel worldVoxel = new Voxel(
                new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), null), null, null, this.worldObjects,
                voxelPosition);
        worldVoxel.setBounds(
                0.0, this.boundingBox.xMax - this.boundingBox.xMin,
                0.0, this.boundingBox.yMax - this.boundingBox.yMin,
                0.0, this.boundingBox.zMax - this.boundingBox.zMin);
        this.kdRoot = raytracer.kdTree.KDTree.getNode(this.worldObjects, worldVoxel, 0);
        System.out.println("kd leaves: " + Voxel.leafCount + " internal voxel count : " + Voxel.count);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        this.camera.setWorld(this);
    }

    public MyColor getPixelIrradiance(Ray ray) {
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

    public IntersectionDetails checkIntersection(Ray cRay) {
        Entity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;
        IntersectionDetails bestIntersection = new IntersectionDetails(Double.MAX_VALUE);
        ArrayList<Entity> intersectingEntities = this.getIntersectingEntities(cRay);

        if (intersectingEntities != null && !intersectingEntities.isEmpty()) {

            for (Entity entity : intersectingEntities) {
                IntersectionDetails intersection = entity.intersect(cRay);
                if (intersection.distance > EPSILON) {
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
        if (this.kdRoot == null) {
            return this.worldObjects;
        }
        // return this.worldObjects;
        ArrayList<Entity> intersectingEntities = KDTree.getEntityList(this.kdRoot, cRay);
        return intersectingEntities;
    }
}
