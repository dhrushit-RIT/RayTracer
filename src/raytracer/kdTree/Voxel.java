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

public class Voxel extends BoundingBox implements ISpaceTransferable {

    private Point position;
    private Point cPosition;

    private AAPlane divisionPlane;
    private ArrayList<Entity> entities;
    private ArrayList<AAPlane> components;
    private HashMap<String, Point> voxelTrianglePoints = new HashMap<>();

    private static TerminalCondition terminalCondition = TerminalCondition.COUNT_ENTITIES;
    public static int count = 0;
    public static int leafCount = 0;

    /**
     * 
     * left : left, front, top
     * right : right, back, bottom
     * 
     * @param division
     * @param entities
     */
    public Voxel(ArrayList<Entity> entities, Point position) {
        super(0, 0, 0, 0, 0, 0);
        this.position = position;
        this.entities = entities;
        this.divisionPlane = null;
        leafCount += 1;
        this.components = new ArrayList<>();
    }

    public Voxel(AAPlane division, Voxel left, Voxel right, ArrayList<Entity> entities, Point position) {
        super(0, 0, 0, 0, 0, 0);
        this.position = position;
        this.divisionPlane = division;
        this.entities = entities;
        count += 1;
        this.components = new ArrayList<>();
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
        for (AAPlane plane : this.components) {
            Point intersectionPoint = plane.intersectPoint(cRay);

            boolean isPointInVoxel = this.checkIntersectionIsInsideVoxel(intersectionPoint, plane);
            if (isPointInVoxel) {
                intersections.add(intersectionPoint);
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
        switch (plane.getAlignment()) {
            case XY:
                xOutOfRange = intersectionPoint.x < this.xMin || intersectionPoint.x > this.xMax;
                if (xOutOfRange)
                    return false;
                yOutOfRange = intersectionPoint.y < this.yMin || intersectionPoint.y > this.yMax;
                if (yOutOfRange)
                    return false;
                break;
            case YZ:
                zOutOfRange = intersectionPoint.z < this.zMin || intersectionPoint.z > this.zMax;
                if (zOutOfRange)
                    return false;
                yOutOfRange = intersectionPoint.y < this.yMin || intersectionPoint.y > this.yMax;
                if (yOutOfRange)
                    return false;
                break;
            case ZX:
                zOutOfRange = intersectionPoint.z < this.zMin || intersectionPoint.z > this.zMax;
                if (zOutOfRange)
                    return false;
                xOutOfRange = intersectionPoint.x < this.xMin || intersectionPoint.x > this.xMax;
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
        this.components.clear();
        // udpate the planes that we have
    }

    // ==========================================================
    // Accessors
    // ==========================================================

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setBounds(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;

    }

    private void computeCPosition() {
        this.cPosition = Camera.toCameraSpace(position);
    }

    @Override
    public Point getPositionInCameraCoordinates() {
        if (this.cPosition == null) {
            this.computeCPosition();
        }
        return this.cPosition;
    }

    public boolean isEntityInsideVoxel(Entity entity) {

        this.getPositionInCameraCoordinates();
        Point entityCamPos = entity.getPositionInCameraCoordinates();

        boolean xNotIntersecting = this.xMin + this.cPosition.x > entity.boundingBox.xMin + entityCamPos.x
                || entity.boundingBox.xMin + this.cPosition.x > this.xMax + entityCamPos.x;
        if (xNotIntersecting)
            return false;
        boolean yNotIntersecting = this.yMin + this.cPosition.y > entity.boundingBox.yMax + entityCamPos.y
                || entity.boundingBox.yMin + entityCamPos.y > this.yMax + this.cPosition.y;
        if (yNotIntersecting)
            return false;
        boolean zNotIntersecting = this.zMin + this.cPosition.z > entity.boundingBox.zMax + entityCamPos.z
                || entity.boundingBox.zMin + entityCamPos.z > this.zMax + this.cPosition.z;
        if (zNotIntersecting)
            return false;

        return true;
    }

}
