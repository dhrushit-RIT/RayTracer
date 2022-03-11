package raytracer.kdTree;

import java.util.ArrayList;

import raytracer.AAPlane;
import raytracer.BoundingBox;
import raytracer.Entity;
import raytracer.Point;
import raytracer.Voxel;

public class KDTree {
    KDNode root;

    public static KDNode getNode(ArrayList<Entity> L, Voxel V) {
        if (isTerminal(L, V)) {
            return new KDNode(L);
        }

        AAPlane P = setPartitionPlanePoint(V); // plane wrt camera
        AAPlane.Alignment nextAlignment = getNextAlignment(V.getDividingPlane().getAlignment());

        Voxel leftVoxel = new Voxel(new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), nextAlignment), null, null,
                null, V.getPosition());
        Voxel rightVoxel = new Voxel(new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), nextAlignment), null, null,
                null, V.getPosition());

        leftVoxel.boundingBox = new BoundingBox(V.boundingBox);
        rightVoxel.boundingBox = new BoundingBox(V.boundingBox);

        // set the boundaries of the left and right voxels
        Point pointOnPlane = P.getPointOnPlane(); // wrt camera
        Point leftVoxelPosition = new Point(V.getPosition());
        Point rightVoxelPosition = new Point(V.getPosition());

        // update the voxel start position
        switch (P.getAlignment()) {
            case XY:
                rightVoxelPosition.z += (V.boundingBox.zMax - V.boundingBox.zMin) / 2;
                break;
            case YZ:
                rightVoxelPosition.x += (V.boundingBox.xMax - V.boundingBox.xMin) / 2;
                break;
            case ZX:
                rightVoxelPosition.y += (V.boundingBox.yMax - V.boundingBox.yMin) / 2;
                break;
            default:
                System.err.println("wrong voxel child");
        }

        leftVoxelPosition.updatePoint();
        rightVoxelPosition.updatePoint();
        leftVoxel.setPosition(leftVoxelPosition);
        rightVoxel.setPosition(rightVoxelPosition);

        switch (P.getAlignment()) {
            case XY:
                leftVoxel.boundingBox.zMin = 0.0;
                rightVoxel.boundingBox.zMin = 0.0;
                leftVoxel.boundingBox.zMax = (V.boundingBox.zMax - V.boundingBox.zMin) / 2;
                rightVoxel.boundingBox.zMax = (V.boundingBox.zMax - V.boundingBox.zMin) / 2;
                break;
            case YZ:
                leftVoxel.boundingBox.xMin = 0.0;
                rightVoxel.boundingBox.xMin = 0.0;
                leftVoxel.boundingBox.xMax = (V.boundingBox.xMax - V.boundingBox.xMin) / 2;
                rightVoxel.boundingBox.xMax = (V.boundingBox.xMax - V.boundingBox.xMin) / 2;

                break;
            case ZX:
                leftVoxel.boundingBox.yMin = 0.0;
                rightVoxel.boundingBox.yMin = 0.0;
                leftVoxel.boundingBox.yMax = (V.boundingBox.yMax - V.boundingBox.yMin) / 2;
                rightVoxel.boundingBox.yMax = (V.boundingBox.yMax - V.boundingBox.yMin) / 2;

                break;
            default:
                System.err.println("wrong voxel child");
        }

        ArrayList<Entity> leftEntities = new ArrayList<>();
        ArrayList<Entity> rightEntities = new ArrayList<>();

        for (Entity entity : L) {
            if (entity.intersect(leftVoxel)) {
                leftEntities.add(entity);
            }

            if (entity.intersect(rightVoxel)) {
                rightEntities.add(entity);
            }
        }

        return new KDNode(P, getNode(leftEntities, leftVoxel), getNode(rightEntities, rightVoxel));
    }

    private static AAPlane setPartitionPlanePoint(Voxel v) {
        Point cPos = v.getPositionInCameraCoordinates();
        switch (v.getDividingPlane().getAlignment()) {
            case YZ:
                v.getDividingPlane().setPointOnPlane(
                        new Point(
                                cPos.x + (v.boundingBox.xMax - v.boundingBox.xMin) / 2,
                                0.0,
                                0.0,
                                Point.Space.CAMERA));
                break;
            case ZX:
                v.getDividingPlane().setPointOnPlane(
                        new Point(
                                0.0,
                                cPos.y + (v.boundingBox.yMax - v.boundingBox.yMin) / 2,
                                0.0,
                                Point.Space.CAMERA));
                break;
            case XY:
            default:
                v.getDividingPlane().setPointOnPlane(
                        new Point(
                                0.0,
                                0.0,
                                cPos.z + (v.boundingBox.zMax - v.boundingBox.zMin) / 2,
                                Point.Space.CAMERA));
                break;
        }
        return v.getDividingPlane();
    }

    private static AAPlane.Alignment getNextAlignment(AAPlane.Alignment previousAlignment) {
        if (previousAlignment == null) {
            return AAPlane.Alignment.XY;
        }
        switch (previousAlignment) {
            case XY:
                return AAPlane.Alignment.YZ;
            case YZ:
                return AAPlane.Alignment.ZX;
            case ZX:
                return AAPlane.Alignment.XY;
            default:
                return AAPlane.Alignment.XY;
        }

    }

    private static boolean isTerminal(ArrayList<Entity> l, Voxel v) {
        if (l.size() < 3) {
            return true;
        }
        return false;
    }

}
