package raytracer.kdTree;

import java.util.ArrayList;

import raytracer.Entity;
import raytracer.Point;
import raytracer.Ray;
import raytracer.kdTree.AAPlane.Alignment;

public class KDTree {
    KDNode root;

    private enum PartitioningStrategy {
        HALF_DIST,
        HALF_ENTITIES
    }

    private static PartitioningStrategy partitioningStrategy = PartitioningStrategy.HALF_DIST;

    private static int MAX_DEPTH = 500;
    public static int MAX_ENTITIES_IN_VOXEL = 20;

    public static KDNode getNode(ArrayList<Entity> L, Voxel V, AAPlane.Alignment divisionAlignment, int depth) {
        if (isTerminal(L, V) || depth > MAX_DEPTH) {
            return new KDNode(L, V);
        }

        ArrayList<Entity> sortedEntities = KDTree.sortEntities(L, divisionAlignment);
        AAPlane P = findPartitionPlane(V, divisionAlignment, sortedEntities); // plane wrt camera

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

    private static ArrayList<Entity> sortEntities(ArrayList<Entity> l, Alignment divisionAlignment) {
        switch (divisionAlignment) {
            case XY:
                l.sort((Entity e1, Entity e2) -> e1.boundingBox.zMax < e2.boundingBox.zMax ? -1
                        : e1.boundingBox.zMax > e2.boundingBox.zMax ? 1 : 0);
                break;
            case YZ:
                l.sort((Entity e1, Entity e2) -> e1.boundingBox.xMax < e2.boundingBox.xMax ? -1
                        : e1.boundingBox.xMax > e2.boundingBox.xMax ? 1 : 0);
                break;
            case ZX:
                l.sort((Entity e1, Entity e2) -> e1.boundingBox.yMax < e2.boundingBox.yMax ? -1
                        : e1.boundingBox.yMax > e2.boundingBox.yMax ? 1 : 0);
                break;
        }
        return l;
    }

    private static void setEntitiesForSubVoxel(Voxel leftVoxel, ArrayList<Entity> leftEntities, Voxel rightVoxel,
            ArrayList<Entity> rightEntities, ArrayList<Entity> L) {

        switch (partitioningStrategy) {
            case HALF_ENTITIES:
                // divideEntitiesInTwoParts(leftVoxel, leftEntities, rightVoxel, rightEntities, L);
            case HALF_DIST:
            default:
                divideEntitiesByHalfDist(leftVoxel, leftEntities, rightVoxel, rightEntities, L);
                break;

        }

    }

    private static void divideEntitiesInTwoParts(Voxel leftVoxel, ArrayList<Entity> leftEntities, Voxel rightVoxel,
            ArrayList<Entity> rightEntities, ArrayList<Entity> l) {
                // leftVoxel
    }

    private static void divideEntitiesByHalfDist(Voxel leftVoxel, ArrayList<Entity> leftEntities, Voxel rightVoxel,
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

    private static AAPlane findPartitionPlane(Voxel v, AAPlane.Alignment alignment,
            ArrayList<Entity> sortedEntities) {

        switch (partitioningStrategy) {
            case HALF_DIST:
                return KDTree.divideAtHalfDist(v, alignment);
            case HALF_ENTITIES:
                return KDTree.divideHalfEntities(v, alignment, sortedEntities);
            default:
                return KDTree.divideAtHalfDist(v, alignment);
        }

    }

    private static AAPlane divideHalfEntities(Voxel v, Alignment alignment, ArrayList<Entity> sortedEntities) {
        int halfIndex = sortedEntities.size() / 2;
        Entity entityOfInterest = sortedEntities.get(halfIndex);

        double x = v.xMin;
        double y = v.yMin;
        double z = v.zMin;
        switch (alignment) {
            case YZ:
                x = entityOfInterest.boundingBox.xMax;
                break;
            case ZX:
                y = entityOfInterest.boundingBox.yMax;
                break;
            case XY:
            default:
                z = entityOfInterest.boundingBox.zMax;
                break;
        }
        Point pointOnPlane = new Point(x, y, z, Point.Space.CAMERA);
        AAPlane dividingPlane = new AAPlane(pointOnPlane, alignment);
        return dividingPlane;
    }

    private static AAPlane divideAtHalfDist(Voxel v, Alignment alignment) {
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
        // } else if (intersectionDetails.size() == 1) {
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

        double a = Double.MAX_VALUE;
        double b = Double.MAX_VALUE;
        double s = Double.MAX_VALUE;

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
