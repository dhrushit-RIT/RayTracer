package raytracer;

import raytracer.ToneMapping.AdaptiveToneMapping;
import raytracer.ToneMapping.HistogramCompressor;
import raytracer.ToneMapping.Reinhard;
import raytracer.ToneMapping.ToneCompressor;
import raytracer.ToneMapping.Ward;

public class Application {

    public ToneCompressor toneCompressor;
    public static ToneCompressor.Type compressorType;

    public Application() {
        this.world = new World();

        // Setups.KDTree.setup1(this);
        // Setups.DefaultSetups.setup7(this);
        Setups.Refraction.setup0(this);
        // Setups.AshikhiminShirley.setup0(this);
        // Setups.LightFallout.setup0(this);
        // Setups.ToneMapping.setup5(this);
    }

    public ToneCompressor getToneCompressor() {
        switch(compressorType) {
            case WARD:
                return new Ward();
            case REINHARD:
                return new Reinhard();
            case ADAPTIVE:
                return new AdaptiveToneMapping();
            case HIST:
                return new HistogramCompressor();
            default:
                return new Ward();
        }
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
