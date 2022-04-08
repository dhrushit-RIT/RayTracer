package raytracer;

public class Application {

    public Application() {
        this.world = new World();
        // Setups.DefaultSetups.setup7(this);
        Setups.Refraction.setup0(this);
        // Setups.AshikiminShirley.setup0(this);
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
