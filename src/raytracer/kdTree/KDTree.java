package raytracer.kdTree;

import java.util.ArrayList;

import raytracer.Entity;
import raytracer.Point;
import raytracer.Ray;

public class KDTree {
    KDNode root;

    private static int MAX_DEPTH = 500;
    public static int MAX_ENTITIES_IN_VOXEL = 100;

    public static KDNode getNode(ArrayList<Entity> L, Voxel V, AAPlane.Alignment divisionAlignment, int depth) {
        if (isTerminal(L, V) || depth > MAX_DEPTH) {
            return new KDNode(L, V);
        }

        AAPlane P = findPartitionPlane(V, divisionAlignment); // plane wrt camera

        Voxel leftVoxel = new Voxel(V);
        Voxel rightVoxel = new Voxel(V);
        // set the boundaries of the left and right voxels
        setVoxelBounds(P, V, leftVoxel, rightVoxel);

        ArrayList<Entity> leftEntities = new ArrayList<>();
        ArrayList<Entity> rightEntities = new ArrayList<>();
        // populate left and right subvoxels
        setEntitiesForSubVoxel(leftVoxel, leftEntities, rightVoxel, rightEntities, L);

        AAPlane.Alignment nextAlignment = getNextAlignment(divisionAlignment);
        KDNode leftNode = getNode(leftEntities, leftVoxel, nextAlignment, depth + 1);
        KDNode rightNode = getNode(rightEntities, rightVoxel, nextAlignment, depth + 1);
        return new KDNode(P, leftNode, rightNode, V);
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

    private static Voxel getDefaultVoxel(AAPlane.Alignment nextAlignment, Voxel V) {
        return new Voxel(V.xMin, V.xMax, V.yMin, V.yMax, V.zMin, V.zMax);

    }

    private static void setVoxelBounds(AAPlane P, Voxel V, Voxel leftVoxel, Voxel rightVoxel) {
        switch (P.getAlignment()) {
            case XY:

                leftVoxel.zMin = V.zMin;
                leftVoxel.zMax = (V.zMax + V.zMin) / 2;

                rightVoxel.zMin = (V.zMax + V.zMin) / 2;
                rightVoxel.zMax = V.zMax;
                break;
            case YZ:

                leftVoxel.xMin = V.xMin;
                leftVoxel.xMax = (V.xMax + V.xMin) / 2;

                rightVoxel.xMin = (V.xMax + V.xMin) / 2;
                rightVoxel.xMax = V.xMax;

                break;
            case ZX:

                leftVoxel.yMin = V.yMin;
                leftVoxel.yMax = (V.yMax + V.yMin) / 2;

                rightVoxel.yMin = (V.yMax + V.yMin) / 2;
                rightVoxel.yMax = V.yMax;

                break;
            default:
                System.err.println("wrong voxel child");
        }

        leftVoxel.updateComponents();
        rightVoxel.updateComponents();
    }

    private static AAPlane findPartitionPlane(Voxel v, AAPlane.Alignment alignment) {

        double x = v.xMin;
        double y = v.yMin;
        double z = v.zMin;
        switch (alignment) {
            case YZ:
                x = (v.xMax + v.xMin) / 2;
                break;
            case ZX:
                y = (v.yMax + v.yMin) / 2;
                break;
            case XY:
            default:
                z = (v.zMax + v.zMin) / 2;
                break;
        }

        Point pointOnPlane = new Point(x, y, z, Point.Space.CAMERA);
        AAPlane dividingPlane = new AAPlane(pointOnPlane, alignment);
        dividingPlane.setPointOnPlane(pointOnPlane);

        return dividingPlane;
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
        if (l.size() < MAX_ENTITIES_IN_VOXEL) {
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

        // Point A;
        // Point B;
        // ArrayList<Point> intersectionDetails = root.getVoxel().intersectVoxel(cRay);
        // if (intersectionDetails.size() < 1) {
        // // System.err.println("Error parsing the kd tree");
        // return new ArrayList<>();
        // } else if (intersectionDetails.size() < 1) {
        // A = intersectionDetails.get(0);
        // B = intersectionDetails.get(0);
        // } else {

        // A = intersectionDetails.get(0);
        // B = intersectionDetails.get(1);
        // }

        ArrayList<Point> intersectionDetails = root.getVoxel().intersectVoxel(cRay);
        if (intersectionDetails.size() != 2) {
            // System.err.println("Error parsing the kd tree");
            return new ArrayList<>();
        }

        Point A = intersectionDetails.get(0);
        Point B = intersectionDetails.get(1);

        Point S = root.getPartitioningPlane().intersect(cRay);

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
        if (a <= s) {
            if (b < s) {
                return getEntityList(root.getLeft(), cRay);
                // left node : N1, N2, N3, P5, Z3
            } else {
                if (b == s) {
                    return getEntityList(root.getLeft(), cRay);
                    // left or right node : Z2
                } else {
                    ArrayList<Entity> intersectingEntities = new ArrayList<>();
                    intersectingEntities.addAll(getEntityList(root.getLeft(), cRay));
                    intersectingEntities.addAll(getEntityList(root.getRight(), cRay));
                    return intersectingEntities;
                    // left and right : N4
                }
            }
        } else {
            if (b > s) {
                // right node : P1, P2, P3, N5, Z1
                return getEntityList(root.getRight(), cRay);
            } else {
                // right and left : P4
                ArrayList<Entity> intersectingEntities = new ArrayList<>();
                intersectingEntities.addAll(getEntityList(root.getRight(), cRay));
                intersectingEntities.addAll(getEntityList(root.getLeft(), cRay));
                return intersectingEntities;
            }
        }
    }

}
