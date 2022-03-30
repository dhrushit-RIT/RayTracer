package raytracer.kdtree;

import raytracer.BoundingBox;

public class Voxel extends BoundingBox {

    public static int count = 0;
    public static int leaf = 0;

    public Voxel(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        super(xMin, xMax, yMin, yMax, zMin, zMax);
        Voxel.count += 1;
    }

    public String toString() {
        return "x: " + xMin + " - " + xMax + "\ny: " + yMin + " - " + yMax + "\nz: " + zMin + " - " + zMax;
    }

}
