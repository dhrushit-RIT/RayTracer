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

public class Voxel extends BoundingBox implements ISpaceTransferable {

    public static enum Space {
        WORLD,
        CAMERA,
        ENTITY
    }

    private Space voxelSpace = Space.CAMERA;
    private Point position; // in camera coordinate  TODO: try to make this independent of the context in which we are calculating
    private Point cPosition;

    private AAPlane divisionPlane;
    private ArrayList<Entity> entities;
    private HashMap<ComponentPlanes, AAPlane> components;
    private HashMap<String, Point> voxelTrianglePoints = new HashMap<>();

    private static TerminalCondition terminalCondition = TerminalCondition.COUNT_ENTITIES;
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
    public Voxel(ArrayList<Entity> entities, Point cPosition) {
        super(0, 0, 0, 0, 0, 0);
        this.position = cPosition;
        this.entities = entities;
        this.divisionPlane = null;
        leafCount += 1;
        this.setUpComponents();

    }

    public Voxel(AAPlane division, Voxel left, Voxel right, ArrayList<Entity> entities, Point position) {
        super(0, 0, 0, 0, 0, 0);
        this.position = position;
        this.divisionPlane = division;
        this.entities = entities;
        count += 1;
        this.setUpComponents();
    }

    public void setUpComponents() {
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
        // TODO: this is not generalised in the sense that if the camera is itself inside the voxel, it does not work.

        ArrayList<Point> intersections = new ArrayList<>();

        for (ComponentPlanes planeType : ComponentPlanes.values()) {
            AAPlane plane = this.components.get(planeType);
            Point intersectionPoint = plane.intersectPoint(cRay);
            if(intersectionPoint != null) {
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

        this.getPositionInCameraCoordinates();
        double voxelXmin = this.xMin + this.cPosition.x;
        double voxelXmax = this.xMax + this.cPosition.x;
        double voxelYmin = this.yMin + this.cPosition.y;
        double voxelYmax = this.yMax + this.cPosition.y;
        double voxelZmin = this.zMin + this.cPosition.z;
        double voxelZmax = this.zMax + this.cPosition.z;

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
        // this.components.clear();
        this.updateBounds();
        // udpate the planes that we have
    }

    public void updateBounds() {
        this.getPositionInCameraCoordinates();
        this.components.get(ComponentPlanes.XY1).setPointOnPlane(
                new Point(this.xMin + this.cPosition.x, this.yMin + this.cPosition.y, this.zMin + this.cPosition.z,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.XY2).setPointOnPlane(
                new Point(this.xMax + this.cPosition.x, this.yMax + this.cPosition.y, this.zMax + this.cPosition.z,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.YZ1).setPointOnPlane(
                new Point(this.xMin + this.cPosition.x, this.yMin + this.cPosition.y, this.zMin + this.cPosition.z,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.YZ2).setPointOnPlane(
                new Point(this.xMax + this.cPosition.x, this.yMax + this.cPosition.y, this.zMax + this.cPosition.z,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.ZX1).setPointOnPlane(
                new Point(this.xMin + this.cPosition.x, this.yMin + this.cPosition.y, this.zMin + this.cPosition.z,
                        Point.Space.CAMERA));
        this.components.get(ComponentPlanes.ZX2).setPointOnPlane(
                new Point(this.xMax + this.cPosition.x, this.yMax + this.cPosition.y, this.zMax + this.cPosition.z,
                        Point.Space.CAMERA));

    }

    // ==========================================================
    // Accessors
    // ==========================================================

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
        this.computeCPosition();
    }

    public void setBounds(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;

        this.getPositionInCameraCoordinates();
        this.components.get(ComponentPlanes.XY1).setPointOnPlane(
                new Point(xMin + this.cPosition.x, yMin + this.cPosition.y, zMin + this.cPosition.z, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.XY2).setPointOnPlane(
                new Point(xMax + this.cPosition.x, yMax + this.cPosition.y, zMax + this.cPosition.z, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.YZ1).setPointOnPlane(
                new Point(xMin + this.cPosition.x, yMin + this.cPosition.y, zMin + this.cPosition.z, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.YZ2).setPointOnPlane(
                new Point(xMax + this.cPosition.x, yMax + this.cPosition.y, zMax + this.cPosition.z, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.ZX1).setPointOnPlane(
                new Point(xMin + this.cPosition.x, yMin + this.cPosition.y, zMin + this.cPosition.z, Point.Space.CAMERA));
        this.components.get(ComponentPlanes.ZX2).setPointOnPlane(
                new Point(xMax + this.cPosition.x, yMax + this.cPosition.y, zMax + this.cPosition.z, Point.Space.CAMERA));

    }

    private void computeCPosition() {
        // this.cPosition = Camera.toCameraSpace(position);
        this.cPosition = this.position;// .toCameraSpace(position);
    }

    @Override
    public Point getPositionInCameraCoordinates() {
        if (this.cPosition == null) {
            this.computeCPosition();
        }
        return this.position;
    }

    public boolean isEntityInsideVoxel(Entity entity) {

        this.getPositionInCameraCoordinates();
        Point entityCamPos = entity.getPositionInCameraCoordinates();

        double voxelXmin = this.cPosition.x + this.xMin;
        double voxelXmax = this.cPosition.x + this.xMax;
        double voxelYmin = this.cPosition.y + this.yMin;
        double voxelYmax = this.cPosition.y + this.yMax;
        double voxelZmin = this.cPosition.z + this.zMin;
        double voxelZmax = this.cPosition.z + this.zMax;

        double entityXmin = entityCamPos.x + entity.boundingBox.xMin;
        double entityXmax = entityCamPos.x + entity.boundingBox.xMax;
        double entityYmin = entityCamPos.y + entity.boundingBox.yMin;
        double entityYmax = entityCamPos.y + entity.boundingBox.yMax;
        double entityZmin = entityCamPos.z + entity.boundingBox.zMin;
        double entityZmax = entityCamPos.z + entity.boundingBox.zMax;

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
        sb.append("V : c" + this.cPosition + " | (" +
                this.xMin + ", " + this.yMin + ", " + this.zMin + " - " +
                this.xMax + ", " + this.yMax + ", " + this.zMax + ")");
        return sb.toString();
    }

}
