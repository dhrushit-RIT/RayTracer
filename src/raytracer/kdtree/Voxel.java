package raytracer.kdtree;

import java.util.ArrayList;
import java.util.HashMap;

import raytracer.BoundingBox;
import raytracer.IntersectionDetails;
import raytracer.Point;
import raytracer.Ray;

public class Voxel extends BoundingBox {

    enum VoxelPlanesNames {
        XY1,
        XY2,
        YZ1,
        YZ2,
        ZX1,
        ZX2
    }

    HashMap<VoxelPlanesNames, AAPlane> voxelPlanes;

    public static int count = 0;
    public static int leaf = 0;

    public Voxel(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        super(xMin, xMax, yMin, yMax, zMin, zMax);
        Voxel.count += 1;
        this.computePlanes();
    }

    private void computePlanes() {
        this.voxelPlanes = new HashMap<>();
        
        this.voxelPlanes.put(VoxelPlanesNames.XY1, new AAPlane(AAPlane.Alignment.XY, new Point(xMin, yMin, zMin, Point.Space.CAMERA)));
        this.voxelPlanes.put(VoxelPlanesNames.XY2, new AAPlane(AAPlane.Alignment.XY, new Point(xMax, yMax, zMax, Point.Space.CAMERA)));

        this.voxelPlanes.put(VoxelPlanesNames.YZ1, new AAPlane(AAPlane.Alignment.YZ, new Point(xMin, yMin, zMin, Point.Space.CAMERA)));
        this.voxelPlanes.put(VoxelPlanesNames.YZ2, new AAPlane(AAPlane.Alignment.YZ, new Point(xMax, yMax, zMax, Point.Space.CAMERA)));

        this.voxelPlanes.put(VoxelPlanesNames.ZX1, new AAPlane(AAPlane.Alignment.ZX, new Point(xMin, yMin, zMin, Point.Space.CAMERA)));
        this.voxelPlanes.put(VoxelPlanesNames.ZX2, new AAPlane(AAPlane.Alignment.ZX, new Point(xMax, yMax, zMax, Point.Space.CAMERA)));
    }

    public String toString() {
        return "x: " + xMin + " - " + xMax + "\ny: " + yMin + " - " + yMax + "\nz: " + zMin + " - " + zMax;
    }

    private boolean withinBounds(Point point) {
        return point.x >= xMin && point.x <= xMax && point.y >= yMin && point.y <= yMax && point.z >= zMin && point.z <= zMax;
    }

    public ArrayList<Point> intersect(Ray ray) {
        ArrayList<IntersectionDetails<AAPlane>> intersectionsDetails = new ArrayList<>();
        ArrayList<Point> intersections = new ArrayList<>();


        for (VoxelPlanesNames vpn: VoxelPlanesNames.values()){

            AAPlane plane;
            IntersectionDetails<AAPlane> intersection;

            plane = this.voxelPlanes.get(vpn);
            intersection = plane.intersect(ray);
            if(intersection != null) {
                if(withinBounds(intersection.intersectionPoint)){
                    intersectionsDetails.add(intersection);
                }
            }
        }

        intersectionsDetails.sort((IntersectionDetails<AAPlane> o1, IntersectionDetails<AAPlane> o2) -> {
            double result = o1.distance - o2.distance;
            if(result > 0) {
                return 1;
            }else if(result == 0 ){
                return 0;
            }else{
                return -1;
            }
        });

        for(IntersectionDetails<AAPlane> id:intersectionsDetails) {
            intersections.add(id.intersectionPoint);
        }
        return intersections;
    }
}
