package raytracer.kdTree;
import java.util.ArrayList;

import raytracer.AAPlane;
import raytracer.Entity;

public class KDNode {
    private AAPlane partitioningPlane;
    private KDNode left;
    private KDNode right;

    private ArrayList<Entity> entities = new ArrayList<>();

    public KDNode(AAPlane P, KDNode left, KDNode right) {
        this.partitioningPlane = P;
        this.left = left;
        this.right = right;
    }

    public KDNode(ArrayList<Entity> L) {
        this.entities = L;
        this.partitioningPlane = null;
        this.right = null;
        this.left = null;
    }

}
