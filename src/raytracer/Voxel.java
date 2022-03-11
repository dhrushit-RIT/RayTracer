package raytracer;
import java.util.ArrayList;

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

    public AAPlane getDividingPlane() {
        return this.divisionPlane;
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

    

}
