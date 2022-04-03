package raytracer;

public class Application {

    public Application() {
        this.world = new World();
        Setups.Reflection.setup1(this);
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
