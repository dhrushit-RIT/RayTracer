public class Application {

    public Application() {
        this.world = new World();
        // Setups.setup0(this);
        // Setups.setup1(this);
        // Setups.setup2(this);
        // Setups.setup3(this);
        // Setups.setup4(this);
        Setups.setup5(this);
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
