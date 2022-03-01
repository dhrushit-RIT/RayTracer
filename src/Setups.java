public class Setups {
    public static void setup0(Application application) {

        //
        // set up Camera
        //
        Point cameraPosition = new Point(0, 0, 0, Point.Space.WORLD);
        Vector cameraUp = new Vector(0, 1, 0);
        Point cameraLookAt = new Point(0, 0, -1, Point.Space.WORLD);
        double cameraFocalLength = 0.5;
        Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
        application.getWorld().setCamera(camera);

        //
        // Light
        //
        Light light = new Light(new MyColor(1, 1, 1), new Point(2, 5, 5, Point.Space.WORLD), 0.45);
        application.getWorld().addLightSource(light);

        //
        // Entities
        //
        Point sphereCenter = new Point(0, 0, 3, Point.Space.WORLD);
        double sphereRadius = 1.0;
        MyColor sphereColor = new MyColor(0, 0, 255);
        application.getWorld().addEntity(new Sphere(sphereCenter, sphereRadius, sphereColor));

    }

    public static void setup1(Application application) {

        //
        // set up Camera
        //
        Point cameraPosition = new Point(0, 0, 4, Point.Space.WORLD);
        Vector cameraUp = new Vector(0, 1, 0);
        Point cameraLookAt = new Point(0, 0, 0, Point.Space.WORLD);
        double cameraFocalLength = 5;
        Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
        application.getWorld().setCamera(camera);

        //
        // Light
        //
        Light light = new Light(new MyColor(1, 1, 1), new Point(2, 5, 5, Point.Space.WORLD), 0.45);
        application.getWorld().addLightSource(light);

        //
        // Entities
        //
        Point sphereCenter = new Point(0, 0, 0, Point.Space.WORLD);
        double sphereRadius = 1.0;
        MyColor sphereColor = new MyColor(0, 0, 255);
        application.getWorld().addEntity(new Sphere(sphereCenter, sphereRadius, sphereColor));

    }

    // looking in opposite direction. should not see anything
    public static void setup2(Application application) {

        //
        // set up Camera
        //
        Point cameraPosition = new Point(0, 0, 4, Point.Space.WORLD);
        Vector cameraUp = new Vector(0, 1, 0);
        Point cameraLookAt = new Point(0, 0, 11, Point.Space.WORLD);
        double cameraFocalLength = 5;
        Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
        application.getWorld().setCamera(camera);

        //
        // Light
        //
        Light light = new Light(new MyColor(1, 1, 1), new Point(2, 5, 5, Point.Space.WORLD), 0.45);
        application.getWorld().addLightSource(light);

        //
        // Entities
        //
        Point sphereCenter = new Point(0, 0, 0, Point.Space.WORLD);
        double sphereRadius = 1.0;
        MyColor sphereColor = new MyColor(0, 0, 255);
        application.getWorld().addEntity(new Sphere(sphereCenter, sphereRadius, sphereColor));

    }

    public static void setup3(Application application) {

        //
        // set up Camera
        //
        Point cameraPosition = new Point(0, 0, 4, Point.Space.WORLD);
        Vector cameraUp = new Vector(0, 1, 0);
        Point cameraLookAt = new Point(0, 0, 0, Point.Space.WORLD);
        double cameraFocalLength = 5;
        Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
        application.getWorld().setCamera(camera);

        //
        // Light
        //
        Light light = new Light(new MyColor(1, 1, 1), new Point(2, 5, 5, Point.Space.WORLD), 0.45);
        application.getWorld().addLightSource(light);

        //
        // Entities
        //
        Point sphereCenter = new Point(0, 0, 0, Point.Space.WORLD);
        double sphereRadius = 1.0;
        MyColor sphereColor = new MyColor(0, 0, 255);
        application.getWorld().addEntity(new Sphere(sphereCenter, sphereRadius, sphereColor));

    }

}