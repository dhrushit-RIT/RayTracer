package raytracer.kdTree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import raytracer.BoundingBox;
import raytracer.Camera;
import raytracer.Entity;
import raytracer.ISpaceTransferable;
import raytracer.Point;
import raytracer.Ray;
import raytracer.Util;
import raytracer.Point.Space;
import raytracer.kdTree.AAPlane.Alignment;

public class Voxel extends BoundingBox {

    public static enum Space {
        WORLD,
        CAMERA,
        ENTITY
    }

    private ArrayList<Entity> entities;
    private AAPlane divisionPlane;
    private HashMap<ComponentPlanes, AAPlane> components;
    public static int count = 0;
    public static int leafCount = 0;

    enum ComponentPlanes {
        XY1,
        XY2,
        YZ1,
        YZ2,
        ZX1,
        ZX2
    }

    /**
     * 
     * left : left, front, top
     * right : right, back, bottom
     * 
     * @param division
     * @param entities
     */

    public Voxel(Voxel other) {
        super(other.xMin, other.xMax, other.yMin, other.yMax, other.zMin, other.zMax);
        Voxel.count += 1;
        this.computePlanes();

    }

    public Voxel(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        super(xMin, xMax, yMin, yMax, zMin, zMax);
        Voxel.count += 1;
        this.computePlanes();
    }

    // public Voxel(ArrayList<Entity> entities) {
    // super(0, 0, 0, 0, 0, 0);
    // this.divisionPlane = null;
    // leafCount += 1;
    // this.computePlanes();
    // // System.out.println(this);

    // }

    // public Voxel(AAPlane division, Voxel left, Voxel right, ArrayList<Entity>
    // entities) {
    // super(0, 0, 0, 0, 0, 0);
    // this.divisionPlane = division;
    // this.entities = entities;
    // count += 1;
    // this.computePlanes();
    // System.out.println(this);
    // }

    public void computePlanes() {
        if (this.components == null)
            this.components = new HashMap<>();
        this.components.put(ComponentPlanes.XY1, new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), Alignment.XY));
        this.components.put(ComponentPlanes.XY2, new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), Alignment.XY));
        this.components.put(ComponentPlanes.YZ1, new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), Alignment.YZ));
        this.components.put(ComponentPlanes.YZ2, new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), Alignment.YZ));
        this.components.put(ComponentPlanes.ZX1, new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), Alignment.ZX));
        this.components.put(ComponentPlanes.ZX2, new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), Alignment.ZX));
    }

    public static enum TerminalCondition {
        COUNT_ENTITIES,
    }

    public static enum Division {
        XY, YZ, ZX, NONE
    }

    public AAPlane getDividingPlane() {
        return this.divisionPlane;
    }

    public ArrayList<Point> intersectVoxel(Ray cRay) {

        ArrayList<Point> intersections = new ArrayList<>();

        for (ComponentPlanes planeType : ComponentPlanes.values()) {
            AAPlane plane = this.components.get(planeType);
            Point intersectionPoint = plane.intersect(cRay);
            if (intersectionPoint != null) {
                boolean isPointInVoxel = this.checkIntersectionIsInsideVoxel(intersectionPoint, plane);

                if (isPointInVoxel) {
                    intersections.add(intersectionPoint);
                }
            }
        }

        intersections.sort(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                double dist1 = Util.distance(cRay.origin, o1);
                double dist2 = Util.distance(cRay.origin, o2);

                if (dist1 < dist2) {
                    return -1;
                } else if (dist1 > dist2) {
                    return 1;
                } else {
                    return 0;
                }
            };
        });

        return intersections;
    }

    private boolean checkIntersectionIsInsideVoxel(Point intersectionPoint, AAPlane plane) {
        boolean xOutOfRange = false;
        boolean yOutOfRange = false;
        boolean zOutOfRange = false;

        double voxelXmin = this.xMin;
        double voxelXmax = this.xMax;
        double voxelYmin = this.yMin;
        double voxelYmax = this.yMax;
        double voxelZmin = this.zMin;
        double voxelZmax = this.zMax;

        switch (plane.getAlignment()) {
            case XY:

                xOutOfRange = intersectionPoint.x < voxelXmin || intersectionPoint.x > voxelXmax;
                if (xOutOfRange)
                    return false;

                yOutOfRange = intersectionPoint.y < voxelYmin || intersectionPoint.y > voxelYmax;
                if (yOutOfRange)
                    return false;

                break;

            case YZ:

                zOutOfRange = intersectionPoint.z < voxelZmin || intersectionPoint.z > voxelZmax;
                if (zOutOfRange)
                    return false;

                yOutOfRange = intersectionPoint.y < voxelYmin || intersectionPoint.y > voxelYmax;
                if (yOutOfRange)
                    return false;

                break;

            case ZX:

                zOutOfRange = intersectionPoint.z < voxelZmin || intersectionPoint.z > voxelZmax;
                if (zOutOfRange)
                    return false;

                xOutOfRange = intersectionPoint.x < voxelXmin || intersectionPoint.x > voxelXmax;
                if (xOutOfRange)
                    return false;

                break;

            default:
                System.out.println("alignment does not exist");
                break;

        }
        return true;
    }

    public void updateComponents() {
        this.updateBounds();
    }

    public void updateBounds() {
        this.components.get(ComponentPlanes.XY1).setPointOnPlane(
                new Point(this.xMin, this.yMin, this.zMin,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.XY2).setPointOnPlane(
                new Point(this.xMax, this.yMax, this.zMax,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.YZ1).setPointOnPlane(
                new Point(this.xMin, this.yMin, this.zMin,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.YZ2).setPointOnPlane(
                new Point(this.xMax, this.yMax, this.zMax,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.ZX1).setPointOnPlane(
                new Point(this.xMin, this.yMin, this.zMin,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.ZX2).setPointOnPlane(
                new Point(this.xMax, this.yMax, this.zMax,
                        Point.Space.CAMERA));

    }

    // ==========================================================
    // Accessors
    // ==========================================================

    public void setBounds(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;

        this.components.get(ComponentPlanes.XY1)
                .setPointOnPlane(new Point(xMin, yMin, zMin, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.XY2)
                .setPointOnPlane(new Point(xMax, yMax, zMax, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.YZ1)
                .setPointOnPlane(new Point(xMin, yMin, zMin, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.YZ2)
                .setPointOnPlane(new Point(xMax, yMax, zMax, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.ZX1)
                .setPointOnPlane(new Point(xMin, yMin, zMin, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.ZX2)
                .setPointOnPlane(new Point(xMax, yMax, zMax, Point.Space.CAMERA));

    }

    public boolean isEntityInsideVoxel(Entity entity) {

        double voxelXmin = this.xMin;
        double voxelXmax = this.xMax;
        double voxelYmin = this.yMin;
        double voxelYmax = this.yMax;
        double voxelZmin = this.zMin;
        double voxelZmax = this.zMax;

        double entityXmin = entity.boundingBox.xMin;
        double entityXmax = entity.boundingBox.xMax;
        double entityYmin = entity.boundingBox.yMin;
        double entityYmax = entity.boundingBox.yMax;
        double entityZmin = entity.boundingBox.zMin;
        double entityZmax = entity.boundingBox.zMax;

        boolean xNotIntersecting = voxelXmin > entityXmax || voxelXmax < entityXmin;
        if (xNotIntersecting)
            return false;
        boolean yNotIntersecting = voxelYmin > entityYmax || voxelYmax < entityYmin;
        if (yNotIntersecting)
            return false;
        boolean zNotIntersecting = voxelZmin > entityZmax || voxelZmax < entityZmin;
        if (zNotIntersecting)
            return false;

        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------\n");
        sb.append("Voxel : \n");
        sb.append(super.toString() + "\n");
        // sb.append("Bounds:\nx: " + xMin + " - " + xMax + "\ny: " + yMin + " - " + yMax + "\nz: " + zMin + " - " + zMax
        //         + "\n");
        sb.append("----------------------------\n");
        return sb.toString();
    }

}
