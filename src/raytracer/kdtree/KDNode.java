package raytracer.kdtree;

import java.util.ArrayList;

import raytracer.Entity;

public class KDNode {
    public KDNode left;
    public KDNode right;
    public Voxel voxel;
    public AAPlane partition;
    public ArrayList<Entity> entities;

    public KDNode(AAPlane p, KDNode leftNode, KDNode rightNode, Voxel voxel) {
        this.partition = p;
        this.left = leftNode;
        this.right = rightNode;
        this.voxel = voxel;
    }

    public KDNode(ArrayList<Entity> l, Voxel voxel) {
        Voxel.leaf += 1;
        this.entities = l;
        this.voxel = voxel;
    }

    public boolean isTerminal() {
        return this.left == null && this.right == null;
    }

}
