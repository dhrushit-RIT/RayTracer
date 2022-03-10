import java.util.ArrayList;
import java.util.Comparator;

public class Voxel {

    private AABBPlane divisionPlane;
    private ArrayList<Entity> entities;

    private Voxel left;
    private Voxel right;

    private static TerminalCondition terminalCondition = TerminalCondition.COUNT_ENTITIES;
    public static int count = 0;
    public static int leafCount = 0;

    public BoundingBox boundingBox;

    /**
     * 
     * left : left, front, top
     * right : right, back, bottom
     * 
     * @param division
     * @param entities
     */
    public Voxel(ArrayList<Entity> entities) {
        this.divisionPlane = null;
        this.entities = entities;
        leafCount += 1;
    }

    public Voxel(AABBPlane division, Voxel left, Voxel right, ArrayList<Entity> entities) {
        this.divisionPlane = division;
        this.entities = entities;
        this.left = left;
        this.right = right;

        this.computeBounds();
        count += 1;

    }

    private static Comparator<Entity> comparatorXY = new Comparator<Entity>() {
        @Override
        public int compare(Entity e1, Entity e2) {
            double diff = e1.getPositionInCameraCoordinates().z - e2.getPositionInCameraCoordinates().z;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else
                return 0;

        }
    };
    private static Comparator<Entity> comparatorYZ = new Comparator<Entity>() {
        @Override
        public int compare(Entity e1, Entity e2) {
            double diff = e1.getPositionInCameraCoordinates().y - e2.getPositionInCameraCoordinates().y;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else
                return 0;

        }
    };
    private static Comparator<Entity> comparatorZX = new Comparator<Entity>() {
        @Override
        public int compare(Entity e1, Entity e2) {
            double diff = e1.getPositionInCameraCoordinates().x - e2.getPositionInCameraCoordinates().x;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else
                return 0;

        }
    };

    public static enum TerminalCondition {
        COUNT_ENTITIES,
    }

    private void computeBounds() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;

        for (Entity e : this.entities) {
            Point cEntityPosition = e.getPositionInCameraCoordinates();
            minX = Math.min(minX, cEntityPosition.x + e.boundingBox.xMin);
            minY = Math.min(minY, cEntityPosition.y + e.boundingBox.yMin);
            minZ = Math.min(minZ, cEntityPosition.z + e.boundingBox.zMin);
            maxX = Math.max(maxX, cEntityPosition.x + e.boundingBox.xMax);
            maxY = Math.max(maxY, cEntityPosition.y + e.boundingBox.yMax);
            maxZ = Math.max(maxZ, cEntityPosition.z + e.boundingBox.zMax);
        }

        this.boundingBox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static enum Division {
        XY, YZ, ZX, NONE
    }

    private static boolean isTerminal(ArrayList<Entity> entities) {
        switch (terminalCondition) {
            case COUNT_ENTITIES:
                return entities.size() < 1;
            default:
                System.err.println("no terminal condition!");
                return true;
        }
    }

    private static AABBPlane findPartitionPlane(AABBPlane.Alignment parentAlignment, ArrayList<Entity> entities) {
        // Entity midEntity = entities.get(entities.size() / 2);
        AABBPlane.Alignment alignment = getNextAlignment(parentAlignment);

        return new AABBPlane(midEntity.getPositionInCameraCoordinates(), alignment);
    }

    public AABBPlane getDividingPlane() {
        return this.divisionPlane;
    }

    private static AABBPlane.Alignment getNextAlignment(AABBPlane.Alignment previousAlignment) {
        if (previousAlignment == null) {
            return AABBPlane.Alignment.XY;
        }
        switch (previousAlignment) {
            case XY:
                return AABBPlane.Alignment.YZ;
            case YZ:
                return AABBPlane.Alignment.ZX;
            case ZX:
                return AABBPlane.Alignment.XY;
            default:
                return AABBPlane.Alignment.XY;
        }

    }

    public static Voxel getNode(ArrayList<Entity> entities,
            AABBPlane.Alignment parentDividingPlane) {

        // if (Terminal (L, V)) return new leaf node (L)
        if (isTerminal(entities)) {
            return new Voxel(null, null, null, entities);
            // return new Voxel(entities);
        }

        // Find partition plane P
        // AABBPlane divisionPlane = new AABBPlane(new Point(0, 0, 0,
        // Point.Space.CAMERA),
        // getNextAlignment(parentDividingPlane));
        AABBPlane divisionPlane = findPartitionPlane(parentDividingPlane, entities);
        sortEntitiesByAlignment(entities, divisionPlane.getAlignment());

        ArrayList<Entity> leftEntities = getLeftEntities(entities, divisionPlane);
        ArrayList<Entity> rightEntities = getRightEntities(entities, divisionPlane);

        // Split V with P producing VFRONT and VREAR
        // Voxel leftVoxel = new Voxel(null, null, null, leftEntities);
        // Voxel rightVoxel = new Voxel(null, null, null, rightEntities);

        // Partition elements of L producing LFRONT and LREAR return new interior node
        // (P, getNode(LFRONT, VFRONT),

        // getNode(LREAR, VREAR))
        return new Voxel(divisionPlane, getNode(leftEntities, divisionPlane.getAlignment()),
                getNode(rightEntities, divisionPlane.getAlignment()), entities);
    }

    private static void sortEntitiesByAlignment(ArrayList<Entity> entities, AABBPlane.Alignment alignment) {
        switch (alignment) {
            case XY:
                entities.sort(comparatorXY);
                break;
            case YZ:
                entities.sort(comparatorYZ);
                break;
            case ZX:
                entities.sort(comparatorZX);
                break;
            default:
                entities.sort(comparatorXY);
                System.out.println("no matching alignment");
                break;
        }
    }

    private static ArrayList<Entity> getRightEntities(ArrayList<Entity> parentEntities, AABBPlane dividingPlane) {
        ArrayList<Entity> list = new ArrayList<>();
        for (Entity e : parentEntities) {
            Point entityPosition = e.getPositionInCameraCoordinates();
            switch (dividingPlane.getAlignment()) {
                case XY:
                    if (entityPosition.z > dividingPlane.getPointOnPlane().z)
                        list.add(e);
                    break;
                case YZ:
                    if (entityPosition.x > dividingPlane.getPointOnPlane().x)
                        list.add(e);
                    break;
                case ZX:
                    if (entityPosition.y > dividingPlane.getPointOnPlane().y)
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

    private static ArrayList<Entity> getLeftEntities(ArrayList<Entity> parentEntities, AABBPlane dividingPlane) {
        ArrayList<Entity> list = new ArrayList<>();
        for (Entity e : parentEntities) {
            Point entityPosition = e.getPositionInCameraCoordinates();
            switch (dividingPlane.getAlignment()) {
                case XY:
                    if (entityPosition.z <= dividingPlane.getPointOnPlane().z)
                        list.add(e);
                    break;
                case YZ:
                    if (entityPosition.x <= dividingPlane.getPointOnPlane().x)
                        list.add(e);
                    break;
                case ZX:
                    if (entityPosition.y <= dividingPlane.getPointOnPlane().y)
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
}
