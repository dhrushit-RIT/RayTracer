package raytracer.kdtree;

import java.util.ArrayList;

import raytracer.Entity;
import raytracer.Point;
import raytracer.Ray;
import raytracer.kdtree.AAPlane.Alignment;

public class KDTree {

    public static int MAX_ENTITIES_IN_VOXEL = 100;

    public static KDNode getNode(ArrayList<Entity> L, Voxel V, AAPlane.Alignment divisionAlignment) {
        if (isTerminal(L, V)) {
            return new KDNode(L, V);
        }

        AAPlane P = findPartitionPlane(V, divisionAlignment);
        // System.out.println("partition: " + P);

        Voxel left = getLeftVoxel(V, P);
        Voxel right = getRightVoxel(V, P);
        // System.out.println("left : \n" + left + "\n");
        // System.out.println("right : \n" + right + "\n");

        ArrayList<Entity> leftEntities = partitionEntitiesToVoxel(L, left);
        ArrayList<Entity> rightEntities = partitionEntitiesToVoxel(L, right);

        KDNode leftNode = getNode(leftEntities, left, nextAlignment(P.alignment));
        KDNode rightNode = getNode(rightEntities, right, nextAlignment(P.alignment));
        return new KDNode(P, leftNode, rightNode, V);
    }

    private static Voxel getLeftVoxel(Voxel parent, AAPlane partition) {
        double xmax = parent.xMax;
        double ymax = parent.yMax;
        double zmax = parent.zMax;

        switch (partition.alignment) {
            case XY:
                zmax = (parent.zMax + parent.zMin) / 2;
                break;
            case YZ:
                xmax = (parent.xMin + parent.xMax) / 2;
                break;
            case ZX:
                ymax = (parent.yMin + parent.yMax) / 2;
                break;
        }

        return new Voxel(
                parent.xMin, xmax,
                parent.yMin, ymax,
                parent.zMin, zmax);
    }

    private static Voxel getRightVoxel(Voxel parent, AAPlane partition) {
        double xmin = parent.xMin;
        double ymin = parent.yMin;
        double zmin = parent.zMin;

        switch (partition.alignment) {
            case XY:
                zmin = (parent.zMax + parent.zMin) / 2;
                break;
            case YZ:
                xmin = (parent.xMin + parent.xMax) / 2;
                break;
            case ZX:
                ymin = (parent.yMin + parent.yMax) / 2;
                break;
        }

        return new Voxel(
                xmin, parent.xMax,
                ymin, parent.yMax,
                zmin, parent.zMax);
    }

    private static ArrayList<Entity> partitionEntitiesToVoxel(ArrayList<Entity> l, Voxel voxel) {
        ArrayList<Entity> entitiesInVoxel = new ArrayList<>();
        for (Entity entity : l) {
            if (voxel.intersect(entity.boundingBox)) {
                entitiesInVoxel.add(entity);
            }
        }
        return entitiesInVoxel;
    }

    private static AAPlane findPartitionPlane(Voxel v, AAPlane.Alignment alignment) {
        double x = v.xMin;
        double y = v.yMin;
        double z = v.zMin;

        switch (alignment) {
            case XY:
                z += (v.zMax - v.zMin) / 2;
                break;
            case YZ:
                x += (v.xMax - v.xMin) / 2;
                break;
            case ZX:
                y += (v.yMax - v.yMin) / 2;
                break;
        }

        Point pointOnPlane = new Point(x, y, z, Point.Space.CAMERA);

        return new AAPlane(alignment, pointOnPlane);
    }

    private static boolean isTerminal(ArrayList<Entity> l, Voxel v) {
        if (l.size() > MAX_ENTITIES_IN_VOXEL)
            return false;
        return true;
    }

    private static AAPlane.Alignment nextAlignment(AAPlane.Alignment currAlignment) {
        switch (currAlignment) {
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

    public static ArrayList<Entity> getEntities(KDNode root, Ray ray) {
        if (root.isTerminal()) {
            return root.entities;
        }

        // System.out.println("here");
        ArrayList<Point> intersections = root.voxel.intersect(ray);

        if (intersections.size() < 1) {
            return new ArrayList<Entity>();
        }
        // get intersections with voxel
        // get intersection with partition plane
        Point A = null;
        Point B = null;

        if (intersections.size() == 1) {
            A = intersections.get(0);
            B = intersections.get(0);
        } else {
            A = intersections.get(0);
            B = intersections.get(1);
        }

        if (A == null || B == null) {
            return new ArrayList<Entity>();
        }
        Point S = root.partition.intersect(ray).intersectionPoint;
        double a = Double.MAX_VALUE;
        double b = Double.MAX_VALUE;
        double s = Double.MAX_VALUE;
        // decide a, b, s
        switch (root.partition.alignment) {
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
                break;
        }

        // the logic form the ppt

        if (a == Double.MAX_VALUE || b == Double.MAX_VALUE || s == Double.MAX_VALUE) {
            System.err.println("traversal error");
        }
        if (a <= s) {
            if (b < s) {
                return getEntities(root.left, ray);
            } else {
                if (b == s) {
                    return getEntities(root.right, ray);
                    // return getEntities(root.left, ray);
                } else {
                    ArrayList<Entity> entities = new ArrayList<>();
                    ArrayList<Entity> entitiesLeft = getEntities(root.left, ray);
                    ArrayList<Entity> entitiesRight = getEntities(root.right, ray);

                    entities.addAll(entitiesLeft);
                    entities.addAll(entitiesRight);
                    return entities;

                }
            }
        } else {
            if (b > s) {
                return getEntities(root.right, ray);
            } else {
                ArrayList<Entity> entities = new ArrayList<>();
                ArrayList<Entity> entitiesRight = getEntities(root.right, ray);
                ArrayList<Entity> entitiesLeft = getEntities(root.left, ray);

                entities.addAll(entitiesLeft);
                entities.addAll(entitiesRight);
                return entities;
            }
        }

    }

}
