package raytracer.kdTree;

import java.util.ArrayList;

import raytracer.BoundingBox;
import raytracer.Entity;
import raytracer.IntersectionDetails;
import raytracer.Point;
import raytracer.Ray;

public class KDTree {
    KDNode root;

    public static KDNode getNode(ArrayList<Entity> L, Voxel V) {
        if (isTerminal(L, V)) {
            return new KDNode(L, V);
        }

        AAPlane.Alignment nextAlignment = getNextAlignment(V.getDividingPlane().getAlignment());

        AAPlane P = setPartitionPlanePoint(V); // plane wrt camera

        Voxel leftVoxel = getDefaultVoxel(nextAlignment, V);
        Voxel rightVoxel = getDefaultVoxel(nextAlignment, V);

        ArrayList<Entity> leftEntities = new ArrayList<>();
        ArrayList<Entity> rightEntities = new ArrayList<>();

        // set the boundaries of the left and right voxels
        setVoxelPosition(leftVoxel, rightVoxel, P, V);
        setVoxelBounds(P, V, leftVoxel, rightVoxel);

        // populate left and right subvoxels
        setEntitiesForSubVoxel(leftVoxel, leftEntities, rightVoxel, rightEntities, L);

        return new KDNode(P, getNode(leftEntities, leftVoxel), getNode(rightEntities, rightVoxel), V);
    }

    private static void setEntitiesForSubVoxel(Voxel leftVoxel, ArrayList<Entity> leftEntities, Voxel rightVoxel,
            ArrayList<Entity> rightEntities, ArrayList<Entity> L) {

        for (Entity entity : L) {
            if (leftVoxel.isEntityInsideVoxel(entity)) {
                leftEntities.add(entity);
            }

            if (rightVoxel.isEntityInsideVoxel(entity)) {
                rightEntities.add(entity);
            }
        }
    }

    private static void setVoxelPosition(Voxel leftVoxel, Voxel rightVoxel, AAPlane P, Voxel V) {
        Point leftVoxelPosition = new Point(V.getPosition());
        Point rightVoxelPosition = new Point(V.getPosition());

        // update the voxel start position
        switch (P.getAlignment()) {
            case XY:
                rightVoxelPosition.z += (V.zMax - V.zMin) / 2;
                break;
            case YZ:
                rightVoxelPosition.x += (V.xMax - V.xMin) / 2;
                break;
            case ZX:
                rightVoxelPosition.y += (V.yMax - V.yMin) / 2;
                break;
            default:
                System.err.println("wrong voxel child");
        }

        leftVoxelPosition.updatePoint();
        rightVoxelPosition.updatePoint();
        leftVoxel.setPosition(leftVoxelPosition);
        rightVoxel.setPosition(rightVoxelPosition);
    }

    private static Voxel getDefaultVoxel(AAPlane.Alignment nextAlignment, Voxel V) {
        Voxel voxel = new Voxel(new AAPlane(new Point(0, 0, 0, Point.Space.CAMERA), nextAlignment), null, null,
                null, V.getPosition());

        voxel.setBounds(V.xMin, V.xMax, V.yMin, V.yMax, V.zMin, V.zMax);
        // voxel.boundingBox = new BoundingBox(V.boundingBox);
        return voxel;
    }

    private static void setVoxelBounds(AAPlane P, Voxel V, Voxel leftVoxel, Voxel rightVoxel) {
        switch (P.getAlignment()) {
            case XY:
                leftVoxel.zMin = 0.0;
                rightVoxel.zMin = 0.0;
                leftVoxel.zMax = (V.zMax - V.zMin) / 2;
                rightVoxel.zMax = (V.zMax - V.zMin) / 2;
                break;
            case YZ:
                leftVoxel.xMin = 0.0;
                rightVoxel.xMin = 0.0;
                leftVoxel.xMax = (V.xMax - V.xMin) / 2;
                rightVoxel.xMax = (V.xMax - V.xMin) / 2;

                break;
            case ZX:
                leftVoxel.yMin = 0.0;
                rightVoxel.yMin = 0.0;
                leftVoxel.yMax = (V.yMax - V.yMin) / 2;
                rightVoxel.yMax = (V.yMax - V.yMin) / 2;

                break;
            default:
                System.err.println("wrong voxel child");
        }

        leftVoxel.updateComponents();
        rightVoxel.updateComponents();
    }

    private static AAPlane setPartitionPlanePoint(Voxel v) {
        Point cPos = v.getPositionInCameraCoordinates();

        double x = 0;
        double y = 0;
        double z = 0;

        switch (v.getDividingPlane().getAlignment()) {
            case YZ:
                x = cPos.x + (v.xMax - v.xMin) / 2;
                break;
            case ZX:
                y = cPos.y + (v.yMax - v.yMin) / 2;
                break;
            case XY:
            default:
                z = cPos.z + (v.zMax - v.zMin) / 2;
                break;
        }

        v.getDividingPlane().setPointOnPlane(new Point(x, y, z, Point.Space.CAMERA));

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

    public static ArrayList<Entity> getEntityList(KDNode root, Ray cRay) {

        //
        // return the entities if leaf node reached
        //
        if (root.isTerminal()) {
            return root.getEntities();
        }

        ArrayList<Point> intersectionDetails = root.getVoxel().intersectVoxel(cRay);
        if (intersectionDetails.size() != 2) {
            System.err.println("Error parsing the kd tree");
        }

        Point A = intersectionDetails.get(0);
        Point B = intersectionDetails.get(1);
        Point S = root.getPartitioningPlane().intersectPoint(cRay);

        double a = 0;
        double b = 0;
        double s = 0;

        switch (root.getPartitioningPlane().getAlignment()) {
            case XY:
                a = A.z;
                b = B.z;
                s = S.z;
                break;
            case YZ:
                a = A.x;
                b = B.x;
                s = S.x;
                break;
            case ZX:
                a = A.y;
                b = B.y;
                s = S.y;
                break;
            default:
                a = A.z;
                b = B.z;
                s = S.z;
                System.out.println("default condition");
                break;
        }

        // algorithm to find the next root
        if (a < s) {
            if (b < s) {
                // left node : N1, N2, N3, P5, Z3
            } else {
                if (b == s) {
                    // left or right node : Z2
                } else {
                    // left and right : N4
                }
            }
        } else {
            if (b > s) {
                // right node : P1, P2, P3, N5, Z1
            } else {
                // right and left : P4
            }
        }
        return getEntityList(root, cRay);
    }

}
