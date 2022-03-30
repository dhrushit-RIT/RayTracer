package raytracer.kdtree;

import java.util.ArrayList;

import raytracer.Entity;
import raytracer.Point;
import raytracer.kdtree.AAPlane.Alignment;

public class KDTree {

    private static final int MAX_ENTITIES_IN_VOXEL = 0;

    public static KDNode getNode(ArrayList<Entity> L, Voxel V, AAPlane.Alignment divisionAlignment) {
        if (isTerminal(L, V)) {
            return new KDNode(L);
        }

        AAPlane P = findPartitionPlane(V, divisionAlignment);

        Voxel left = new Voxel();
        Voxel right = new Voxel();

        ArrayList<Entity> leftEntities = partitionEntitiesToVoxel(L, left);
        ArrayList<Entity> rightEntities = partitionEntitiesToVoxel(L, right);

        KDNode leftNode = getNode(leftEntities, left, nextAlignment(P.alignment));
        KDNode rightNode = getNode(rightEntities, right, nextAlignment(P.alignment));
        return new KDNode(P, leftNode, rightNode);
    }

    private static ArrayList<Entity> partitionEntitiesToVoxel(ArrayList<Entity> l, Voxel voxel) {
        ArrayList<Entity> entitiesInVoxel = new ArrayList<>();
        for(Entity entity:l){
            if(voxel.intersect(entity.boundingBox)) {
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

        return new AAPlane(pointOnPlane);
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
}
