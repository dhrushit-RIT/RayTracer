package raytracer.kdTree;

import java.util.ArrayList;

import raytracer.Entity;

public class KDNode {
    private AAPlane partitioningPlane;

    private KDNode left;
    private KDNode right;

    private Voxel voxel;

    private ArrayList<Entity> entities = new ArrayList<>();

    public KDNode(AAPlane P, KDNode left, KDNode right, Voxel voxel) {
        this.partitioningPlane = P;
        this.left = left;
        this.right = right;
        this.voxel = voxel;
    }

    public KDNode(ArrayList<Entity> L, Voxel voxel) {
        this.entities = L;
        this.partitioningPlane = null;
        this.right = null;
        this.left = null;
        this.voxel = voxel;
    }

    public boolean isTerminal() {
        return this.left == null && this.right == null;
    }

    // ================================================================
    // Accessors
    // ================================================================
    public AAPlane getPartitioningPlane() {
        return partitioningPlane;
    }

    public void setPartitioningPlane(AAPlane partitioningPlane) {
        this.partitioningPlane = partitioningPlane;
    }

    public KDNode getLeft() {
        return left;
    }

    public void setLeft(KDNode left) {
        this.left = left;
    }

    public KDNode getRight() {
        return right;
    }

    public void setRight(KDNode right) {
        this.right = right;
    }

    public Voxel getVoxel() {
        return voxel;
    }

    public void setVoxel(Voxel voxel) {
        this.voxel = voxel;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public ArrayList<Entity> getEntities() {
        return this.entities;
    }

}
