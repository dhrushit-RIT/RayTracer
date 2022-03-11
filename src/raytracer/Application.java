package raytracer;
public class Application {

    public Application() {
        this.world = new World();
        // Setups.setup0(this); // should not see anything
        // Setups.setup1(this); // sphere at the center
        // Setups.setup2(this); // // should not see anything
        // Setups.setup3(this);
        // Setups.setup4(this);
        // Setups.setup5(this); // two light sources
        // Setups.setup6(this); // supersampling turned on
        Setups.setup7(this); // phong blinn
    }

    private World world;

    public World getWorld() {
        return this.world;
    }

    public void start() {
        this.world.simulate();
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.start();
    }

}
