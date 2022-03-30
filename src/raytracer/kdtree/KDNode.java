package raytracer.kdtree;

import java.util.ArrayList;

import raytracer.Entity;

public class KDNode {
    AAPlane partition;
    private KDNode left;
    private KDNode right;

    public KDNode(AAPlane p, KDNode leftNode, KDNode rightNode) {
        this.partition = p;
        this.left = leftNode;
        this.right = rightNode;
    }

    public KDNode(ArrayList<Entity> l) {
        Voxel.leaf += 1;
    }

}
