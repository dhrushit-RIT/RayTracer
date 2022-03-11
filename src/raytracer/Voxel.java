package raytracer;
import java.util.ArrayList;
import java.util.Comparator;

public class Voxel extends Entity {

    private AAPlane divisionPlane;
    private ArrayList<Entity> entities;

    private static TerminalCondition terminalCondition = TerminalCondition.COUNT_ENTITIES;
    public static int count = 0;
    public static int leafCount = 0;


    /**
     * 
     * left : left, front, top
     * right : right, back, bottom
     * 
     * @param division
     * @param entities
     */
    public Voxel(ArrayList<Entity> entities, Point position) {
        super(null, position);
        this.entities = entities;
        this.divisionPlane = null;
        leafCount += 1;
    }

    public Voxel(AAPlane division, Voxel left, Voxel right, ArrayList<Entity> entities, Point position) {
        super(null, position);
        this.divisionPlane = division;
        this.entities = entities;
        count += 1;
    }


    public static enum TerminalCondition {
        COUNT_ENTITIES,
    }

    public static enum Division {
        XY, YZ, ZX, NONE
    }

    private static boolean isTerminal(ArrayList<Entity> entities) {
        switch (terminalCondition) {
            case COUNT_ENTITIES:
                return entities.size() < 2;
            default:
                System.err.println("no terminal condition!");
                return true;
        }
    }

    private static AAPlane findPartitionPlane(AAPlane.Alignment myAlignment, ArrayList<Entity> entities) {
        Point pointOnPlane;

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;

        for (Entity entity : entities) {
            Point cEntityPosition = entity.getPositionInCameraCoordinates();

            minX = Math.min(minX, cEntityPosition.x + entity.boundingBox.xMin);
            minY = Math.min(minY, cEntityPosition.y + entity.boundingBox.yMin);
            minZ = Math.min(minZ, cEntityPosition.z + entity.boundingBox.zMin);
            maxX = Math.max(maxX, cEntityPosition.x + entity.boundingBox.xMax);
            maxY = Math.max(maxY, cEntityPosition.y + entity.boundingBox.yMax);
            maxZ = Math.max(maxZ, cEntityPosition.z + entity.boundingBox.zMax);
        }

        switch (myAlignment) {
            case YZ:
                pointOnPlane = new Point((maxX + minX) / 2, 0, 0, Point.Space.CAMERA);
                break;
            case ZX:
                pointOnPlane = new Point(0, (maxY + minY) / 2, 0, Point.Space.CAMERA);
                break;
            case XY:
            default:
                pointOnPlane = new Point(0, 0, (maxZ + minZ) / 2, Point.Space.CAMERA);
                break;
        }

        return new AAPlane(pointOnPlane, myAlignment);
    }

    public AAPlane getDividingPlane() {
        return this.divisionPlane;
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

    private static ArrayList<Entity> getLeftEntities(ArrayList<Entity> entities, AAPlane dividingPlane) {
        ArrayList<Entity> list = new ArrayList<>();

        for (Entity e : entities) {
            Point entityPosition = e.getPositionInCameraCoordinates();
            double entityBound;
            double planeBound;
            switch (dividingPlane.getAlignment()) {
                case XY:
                    entityBound = entityPosition.z + e.boundingBox.zMin;
                    planeBound = dividingPlane.getPointOnPlane().z;
                    if (entityBound < planeBound)
                        list.add(e);
                    break;
                case YZ:
                    entityBound = entityPosition.x + e.boundingBox.xMin;
                    planeBound = dividingPlane.getPointOnPlane().x;
                    if (entityBound < planeBound)
                        list.add(e);
                    break;
                case ZX:
                    entityBound = entityPosition.y + e.boundingBox.yMin;
                    planeBound = dividingPlane.getPointOnPlane().y;
                    if (entityBound < planeBound)
                        list.add(e);
                    break;
                default:
                    System.out.println("invalid addition to list");
                    list.add(e);
                    break;
            }
        }

        return list;
    }

    private static ArrayList<Entity> getRightEntities(ArrayList<Entity> entities, AAPlane dividingPlane) {
        ArrayList<Entity> list = new ArrayList<>();
        double entityBound;
        double planeBound;

        for (Entity e : entities) {
            Point entityPosition = e.getPositionInCameraCoordinates();
            switch (dividingPlane.getAlignment()) {
                case XY:
                    entityBound = entityPosition.z + e.boundingBox.zMax;
                    planeBound = dividingPlane.getPointOnPlane().z;
                    if (entityBound >= planeBound)
                        list.add(e);
                    break;
                case YZ:
                    entityBound = entityPosition.x + e.boundingBox.xMax;
                    planeBound = dividingPlane.getPointOnPlane().x;
                    if (entityBound >= planeBound)
                        list.add(e);
                    break;
                case ZX:
                    entityBound = entityPosition.y + e.boundingBox.yMax;
                    planeBound = dividingPlane.getPointOnPlane().y;
                    if (entityBound >= planeBound)
                        list.add(e);
                    break;
                default:
                    System.out.println("invalid addition to list");
                    list.add(e);
                    break;
            }
        }
        return list;
    }

    @Override
    protected void computeBoundingBox() {
        // should not override this
        // this method should go when making a common parent of Entity and Voxel
    }

    @Override
    public IntersectionDetails intersect(Ray ray) {
        // TODO Auto-generated method stub
        return null;
    }

    
    // private static Comparator<Entity> comparatorXY = new Comparator<Entity>() {
    //     @Override
    //     public int compare(Entity e1, Entity e2) {
    //         double diff = e1.boundingBox.zMin - e2.boundingBox.zMin;
    //         if (diff < 0)
    //             return -1;
    //         else if (diff > 0)
    //             return 1;
    //         else
    //             return 0;

    //     }
    // };
    // private static Comparator<Entity> comparatorYZ = new Comparator<Entity>() {
    //     @Override
    //     public int compare(Entity e1, Entity e2) {
    //         double diff = e1.boundingBox.yMin - e2.boundingBox.yMin;
    //         if (diff < 0)
    //             return -1;
    //         else if (diff > 0)
    //             return 1;
    //         else
    //             return 0;

    //     }
    // };
    // private static Comparator<Entity> comparatorZX = new Comparator<Entity>() {
    //     @Override
    //     public int compare(Entity e1, Entity e2) {
    //         double diff = e1.boundingBox.xMin - e2.boundingBox.xMin;
    //         if (diff < 0)
    //             return -1;
    //         else if (diff > 0)
    //             return 1;
    //         else
    //             return 0;

    //     }
    // };

}
