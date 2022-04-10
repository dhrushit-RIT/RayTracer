package raytracer;

import java.io.File;
import java.util.ArrayList;

import raytracer.Entity.BSDFTechnique;
import raytracer.util.ply_reader.PLYReader;

public class Setups {
	public static class DefaultSetups {
		/**
		 * should not output anything
		 * 
		 * @param application
		 */
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
			Light light = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.45);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphereCenter = new Point(0, 0, 3, Point.Space.WORLD);
			double sphereRadius = 1.0;
			Irradiance sphereColor = new Irradiance(0, 0, 255, false);
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
			Light light = new Light(new Irradiance(1, 1, 1, true), cameraPosition/*
																				 * new Point(2, 5, 5, Point.Space.WORLD)
																				 */,
					0.45);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphereCenter = new Point(0, 0, 0, Point.Space.WORLD);
			double sphereRadius = 1.0;
			Irradiance sphereColor = new Irradiance(0, 0, 255, false);
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
			Light light = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.45);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphereCenter = new Point(0, 0, 0, Point.Space.WORLD);
			double sphereRadius = 1.0;
			Irradiance sphereColor = new Irradiance(0, 0, 255, false);
			application.getWorld().addEntity(new Sphere(sphereCenter, sphereRadius, sphereColor));

		}

		public static void setup3(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(2, 0, 4, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 0, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.45);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphereCenter = new Point(0, 0, 0, Point.Space.WORLD);
			double sphereRadius = 1.0;
			Irradiance sphereColor = new Irradiance(0, 0, 255, false);
			application.getWorld().addEntity(new Sphere(sphereCenter, sphereRadius, sphereColor));

		}

		// introducing initial setup for checkpoint
		public static void setup4(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.65);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.5, 0.4, 0.1, 60);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.5, 0.4, 0.1, 80);
			application.getWorld().addEntity(sphere2);

			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.5, 0.4, 0.1, 80);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.5, 0.4, 0.1, 80);
			application.getWorld().addEntity(triangle2);

		}

		public static void setup5(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light2);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.2, 0.65, 0.15, 60);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.2, 0.55, 0.25, 80);
			application.getWorld().addEntity(sphere2);

			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 80);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 80);
			application.getWorld().addEntity(triangle2);

		}

		// super sampling turned on
		public static void setup6(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light2);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 60);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 80);
			application.getWorld().addEntity(sphere2);

			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 80);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 80);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(3);

		}

		// use phong blinn
		public static void setup7(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.2, 0.5, 0.3, 180);
			application.getWorld().addEntity(sphere2);

			//
			// Triangle 1
			//
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);

			application.getWorld().addEntity(triangle1);

			//
			// Triangle 2
			//
			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		// z is up instead of y - yet to be set up
		public static void setup7_z_up(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere2);

			//
			// Triangle 1
			//
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);

			application.getWorld().addEntity(triangle1);

			//
			// Triangle 2
			//
			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}
	}

	public static class Refraction {
		public static void setup0(Application application) {
			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(-2, 5, -5, Point.Space.WORLD), 0.65);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.65);
			application.getWorld().addLightSource(light2);

			Light light3 = new Light(new Irradiance(1, 1, 1, true), new Point(5, 4, -3, Point.Space.WORLD), 0.65);
			application.getWorld().addLightSource(light3);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(-0.4, 1, 0, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(0, 0, 0, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(0, 0, 0, false).normalize();
			Irradiance specColorS1 = new Irradiance(0, 0, 0, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.1, 0.65, 0.25, 160);
			sphere1.setTransmissiveCoeff(0.75);
			sphere1.setRefractiveIndex(0.98);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.85, 0.8, -0.8, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			sphere2.setReflectiveCoeff(0.5);
			application.getWorld().addEntity(sphere2);

			//
			// Triangle 1
			//
			double sideLength = 3;
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(sideLength, 0, sideLength, Point.Space.WORLD),
					new Point(sideLength, 0, -sideLength, Point.Space.WORLD),
					new Point(-sideLength, 0, sideLength, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);
			triangle1.setHasTexture(true);

			application.getWorld().addEntity(triangle1);

			//
			// Triangle 2
			//
			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-sideLength, 0, sideLength, Point.Space.WORLD),
					new Point(sideLength, 0, -sideLength, Point.Space.WORLD),
					new Point(-sideLength, 0, -sideLength, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			triangle2.setHasTexture(true);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		public static void setup1(Application application) {
			//
			// set up Camera
			//
			Point cameraPosition = new Point(-0.4, 3, 0, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 0.65);
			// application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.65);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(0, 1, 0, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			sphere1.setTransmissiveCoeff(1);
			sphere1.setRefractiveIndex(1);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.5;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			sphere2.setReflectiveCoeff(0.5);
			// application.getWorld().addEntity(sphere2);

			//
			// Triangle 1
			//
			double sidelen = 2;
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(sidelen, 0, sidelen, Point.Space.WORLD),
					new Point(sidelen, 0, -sidelen, Point.Space.WORLD),
					new Point(-sidelen, 0, sidelen, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);
			triangle1.setHasTexture(true);

			application.getWorld().addEntity(triangle1);

			//
			// Triangle 2
			//
			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-sidelen, 0, sidelen, Point.Space.WORLD),
					new Point(sidelen, 0, -sidelen, Point.Space.WORLD),
					new Point(-sidelen, 0, -sidelen, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			triangle2.setHasTexture(true);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		public static void setup2(Application application) {
			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 0, 5, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 0, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 0.65);
			// application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.65);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(0, 0, 0, Point.Space.WORLD);
			double sphere1Radius = 1;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			sphere1.setTransmissiveCoeff(1);
			sphere1.setRefractiveIndex(1);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.5;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			sphere2.setReflectiveCoeff(0.5);
			// application.getWorld().addEntity(sphere2);

			//
			// Triangle 1
			//
			double sidelen = 2;
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(sidelen, 0, sidelen, Point.Space.WORLD),
					new Point(sidelen, 0, -sidelen, Point.Space.WORLD),
					new Point(-sidelen, 0, sidelen, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);
			triangle1.setHasTexture(true);

			// application.getWorld().addEntity(triangle1);

			//
			// Triangle 2
			//
			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-sidelen, 0, sidelen, Point.Space.WORLD),
					new Point(sidelen, 0, -sidelen, Point.Space.WORLD),
					new Point(-sidelen, 0, -sidelen, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			triangle2.setHasTexture(true);
			// application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}
	}

	public static class KDTree {
		public static void setup0(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere2);

			//
			// Triangle 1
			//
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);

			application.getWorld().addEntity(triangle1);

			//
			// Triangle 2
			//
			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		public static void setup1(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 0.17, 0.17, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 0, 0, Point.Space.WORLD);
			double cameraFocalLength = 3;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD),
					0.35);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.35);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// set other configs
			//
			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

			//
			// Add Bunny
			// "C:\dev\RayTracer\src\raytracer\resources\ply_files\bunny_high_res.ply"
			// src\raytracer\resources\ply_files\bun_zipper_res2.ply
			String path = "src/raytracer/resources/ply_files/bun_zipper_res2.ply";
			// String path = "src/raytracer/resources/ply_files/bunny_high_res.ply";
			File file = new File(path);
			ArrayList<Triangle> entities = PLYReader.loadModel(file.getAbsolutePath());

			for (Entity entity : entities) {

				application.getWorld().addEntity(entity);
			}
		}

		// 4 spheres z up
		public static void setup2(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(10, 10, 10, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 0, 1);
			Point cameraLookAt = new Point(0, 0, -2, Point.Space.WORLD);
			double cameraFocalLength = 20;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(4, 1, 6, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			// Light light2 = new Light(new MyColor(1, 1, 1, true), new Point(2, 5, 5,
			// Point.Space.WORLD),
			// 0.55);
			// application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(0, 0, 0, Point.Space.WORLD);
			double sphere1Radius = 1;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(3, 0, 0, Point.Space.WORLD);
			double sphere2Radius = 1;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere2);

			//
			// Sphere 3
			//
			Point sphere3Center = new Point(0, 3, 0, Point.Space.WORLD);
			double sphere3Radius = 1;
			Irradiance basecColorS3 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS3 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS3 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere3 = new Sphere(sphere3Center, sphere3Radius, basecColorS3);
			sphere3.setColors(basecColorS1, specColorS3, diffuseColorS3);
			sphere3.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere3);

			//
			// Sphere 4
			//
			Point sphere4Center = new Point(3, 3, 0, Point.Space.WORLD);
			double sphere4Radius = 1;
			Irradiance basecColorS4 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS4 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS4 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere4 = new Sphere(sphere4Center, sphere4Radius, basecColorS4);
			sphere4.setColors(basecColorS2, specColorS4, diffuseColorS4);
			sphere4.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere4);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		// 4 spheres y up
		public static void setup3(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(10, 10, 10, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 0, -2, Point.Space.WORLD);
			double cameraFocalLength = 20;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(4, 1, 6, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			// Light light2 = new Light(new MyColor(1, 1, 1, true), new Point(2, 5, 5,
			// Point.Space.WORLD),
			// 0.55);
			// application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(0, 0, 0, Point.Space.WORLD);
			double sphere1Radius = 1;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(3, 0, 0, Point.Space.WORLD);
			double sphere2Radius = 1;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere2);

			//
			// Sphere 3
			//
			Point sphere3Center = new Point(0, 0, 3, Point.Space.WORLD);
			double sphere3Radius = 1;
			Irradiance basecColorS3 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS3 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS3 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere3 = new Sphere(sphere3Center, sphere3Radius, basecColorS3);
			sphere3.setColors(basecColorS1, specColorS3, diffuseColorS3);
			sphere3.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere3);

			//
			// Sphere 4
			//
			Point sphere4Center = new Point(3, 0, 3, Point.Space.WORLD);
			double sphere4Radius = 1;
			Irradiance basecColorS4 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS4 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS4 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere4 = new Sphere(sphere4Center, sphere4Radius, basecColorS4);
			sphere4.setColors(basecColorS2, specColorS4, diffuseColorS4);
			sphere4.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere4);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		// 2 spheres z up
		public static void setup4(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(5, 5, 5, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 0, 1);
			Point cameraLookAt = new Point(0, 0, -2, Point.Space.WORLD);
			double cameraFocalLength = 10;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(4, 1, 6, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			// Light light2 = new Light(new MyColor(1, 1, 1, true), new Point(2, 5, 5,
			// Point.Space.WORLD),
			// 0.55);
			// application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(0, 0, 0, Point.Space.WORLD);
			double sphere1Radius = 1;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(2, 0.5, 1.5, Point.Space.WORLD);
			double sphere2Radius = 1;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere2);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		// 2 spheres z up
		public static void setup5(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(10, 0, .5, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 0, 1);
			Point cameraLookAt = new Point(0, 0, 0.5, Point.Space.WORLD);
			double cameraFocalLength = 10;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(4, 1, 6, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			// Light light2 = new Light(new MyColor(1, 1, 1, true), new Point(2, 5, 5,
			// Point.Space.WORLD),
			// 0.55);
			// application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(0, 0, 0, Point.Space.WORLD);
			double sphere1Radius = 1;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(2.5, 0, 0, Point.Space.WORLD);
			double sphere2Radius = 1;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere2);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		public static void setup6(Application application) {

			raytracer.kdTree.KDTree.MAX_ENTITIES_IN_VOXEL = 3;
			// ================================================================
			// set up Camera
			// ================================================================
			Point cameraPosition = new Point(0, 0, -5, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 0, 1, Point.Space.WORLD);
			double cameraFocalLength = 10;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			// ================================================================
			// Light
			// ================================================================
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(7, 7, -6, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			// ================================================================
			// Entities
			// ================================================================

			sphereCube(application, 2);

			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		private static void sphereCube(Application application, int sideLen) {
			Irradiance basecColor = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColor = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColor = new Irradiance(123, 226, 236, false).normalize();
			double gap = 0.5;
			double sphereRadius = 0.5;
			for (int i = -sideLen / 2; i < sideLen / 2; i++) {
				for (int j = -sideLen / 2; j < sideLen / 2; j++) {
					for (int k = -sideLen / 2; k < sideLen / 2; k++) {
						Point center = new Point(
								(gap + 2 * sphereRadius) * i + sphereRadius,
								(gap + 2 * sphereRadius) * j + sphereRadius,
								(gap + 2 * sphereRadius) * k + sphereRadius,
								Point.Space.WORLD);
						Sphere sphere = new Sphere(
								center,
								sphereRadius, null);
						// System.out.println("S" + i + j + k + center);
						sphere.setCoeffs(0.3, 0.5, 0.2, 180);
						sphere.setColors(basecColor, specColor, diffuseColor);

						application.getWorld().addEntity(sphere);
					}
				}
			}
		}

		public static void setup7(Application application) {

			//
			// set up Camera
			//
			raytracer.Camera.SCALE_RATIO = 40;
			raytracer.kdTree.KDTree.MAX_ENTITIES_IN_VOXEL = 3;

			Point cameraPosition = new Point(0, 0, -5, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 0, 1, Point.Space.WORLD);
			double cameraFocalLength = 10;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(7, 7, -6, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			// ================================================================
			// Entities
			// ================================================================

			triangleCube(application, 4);

			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		private static void triangleCube(Application application, int sideLen) {

			double gap = 0.5;
			double triangleSide = 1;
			for (int i = -sideLen / 2; i < sideLen / 2; i++) {
				for (int j = -sideLen / 2; j < sideLen / 2; j++) {
					for (int k = -sideLen / 2; k < sideLen / 2; k++) {
						Irradiance basecColor = new Irradiance(22 * k / 10, 183 * k / 10, 187 * k / 10, false).normalize();
						Irradiance diffuseColor = new Irradiance(36 * k / 10, 199 * k / 10, 203 * k / 10, false).normalize();
						Irradiance specColor = new Irradiance(123 * k / 10, 226 * k / 10, 236 * k / 10, false).normalize();

						double x = i * (gap + triangleSide);
						double y = j * (gap + triangleSide);
						double z = k * gap;
						Triangle triangle = new Triangle(null,
								new Point(x, y, z, Point.Space.WORLD),
								new Point[] {
										new Point(x, y, z, Point.Space.WORLD),
										new Point(x, y + triangleSide, z, Point.Space.WORLD),
										new Point(x + triangleSide, y + triangleSide, z, Point.Space.WORLD)
								});
						triangle.setCoeffs(0.3, 0.5, 0.2, 180);
						triangle.setColors(basecColor, specColor, diffuseColor);

						application.getWorld().addEntity(triangle);
					}
				}
			}
		}
	}

	static class TextureCoordinates {
		public static void setup1(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light2);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere2);

			Point triangle1Position = new Point(0, 0, 0, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Point[] t1TexCoords = new Point[] {
					new Point(1, 0, 1, Point.Space.ENTITY),
					new Point(1, 0, 0, Point.Space.ENTITY),
					new Point(0, 0, 1, Point.Space.ENTITY)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);
			triangle1.setTextureCoordinates(t1TexCoords);
			triangle1.setHasTexture(true);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(0, 0, 0, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Point[] t2texCoords = new Point[] {
					new Point(0, 0, 1, Point.Space.WORLD),
					new Point(1, 0, 0, Point.Space.WORLD),
					new Point(0, 0, 0, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			triangle2.setTextureCoordinates(t2texCoords);
			triangle2.setHasTexture(true);

			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}
	}

	public static class Checkpoint1 {
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
			Light light = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.45);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphereCenter = new Point(0, 0, 3, Point.Space.WORLD);
			double sphereRadius = 1.0;
			Irradiance sphereColor = new Irradiance(0, 0, 255, false);
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
			Light light = new Light(new Irradiance(1, 1, 1, true), cameraPosition/*
																				 * new Point(2, 5, 5,
																				 * Point.Space.WORLD)
																				 */,
					0.45);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphereCenter = new Point(0, 0, 0, Point.Space.WORLD);
			double sphereRadius = 1.0;
			Irradiance sphereColor = new Irradiance(0, 0, 255, false);
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
			Light light = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.45);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphereCenter = new Point(0, 0, 0, Point.Space.WORLD);
			double sphereRadius = 1.0;
			Irradiance sphereColor = new Irradiance(0, 0, 255, false);
			application.getWorld().addEntity(new Sphere(sphereCenter, sphereRadius, sphereColor));

		}

		public static void setup3(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(2, 0, 4, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 0, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.45);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphereCenter = new Point(0, 0, 0, Point.Space.WORLD);
			double sphereRadius = 1.0;
			Irradiance sphereColor = new Irradiance(0, 0, 255, false);
			application.getWorld().addEntity(new Sphere(sphereCenter, sphereRadius, sphereColor));

		}

		// introducing initial setup for checkpoint
		public static void setup4(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.65);
			application.getWorld().addLightSource(light);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.5, 0.4, 0.1, 60);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.5, 0.4, 0.1, 80);
			application.getWorld().addEntity(sphere2);

			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.5, 0.4, 0.1, 80);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.5, 0.4, 0.1, 80);
			application.getWorld().addEntity(triangle2);

		}

		public static void setup5(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light2);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 60);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 80);
			application.getWorld().addEntity(sphere2);

			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 80);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 80);
			application.getWorld().addEntity(triangle2);

		}

		// super sampling turned on
		public static void setup6(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light2);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 60);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 80);
			application.getWorld().addEntity(sphere2);

			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 80);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 80);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(3);

		}

		// use phong blinn
		public static void setup7(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light2);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere2);

			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

	}

	static class Reflection {
		public static void setup0(Application application) {
			//
			// set up Camera
			//
			Camera.SCALE_RATIO = 80;
			Point cameraPosition = new Point(3, 1, 3, Point.Space.WORLD);
			// Point cameraPosition = new Point(0, 2.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			// Light light2 = new Light(new MyColor(1, 1, 1, true), new Point(2, 5, 5,
			// Point.Space.WORLD),
			// 0.55);
			// application.getWorld().addLightSource(light2);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setReflectiveCoeff(0.5);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			// sphere2.setReflectiveCoeff(0.2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			// application.getWorld().addEntity(sphere2);

			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);
			triangle1.setHasTexture(true);
			// triangle1.setReflectiveCoeff(0.5);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			triangle2.setHasTexture(true);
			// triangle2.setReflectiveCoeff(0.5);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		public static void setup1(Application application) {
			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			// Point cameraPosition = new Point(0, 2.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD),
					0.4);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.4);
			application.getWorld().addLightSource(light2);

			//
			// Entities
			//
			Point sphere1Center = new Point(-0.4, 1.5, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			// sphere1.setReflectiveCoeff(0.5);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setReflectiveCoeff(0.2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			application.getWorld().addEntity(sphere2);

			double triangleSide = 5;
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(triangleSide, 0, triangleSide, Point.Space.WORLD),
					new Point(triangleSide, 0, -triangleSide, Point.Space.WORLD),
					new Point(-triangleSide, 0, triangleSide, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);
			triangle1.setHasTexture(true);
			// triangle1.setReflectiveCoeff(0.5);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-triangleSide, 0, triangleSide, Point.Space.WORLD),
					new Point(triangleSide, 0, -triangleSide, Point.Space.WORLD),
					new Point(-triangleSide, 0, -triangleSide, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			triangle2.setHasTexture(true);
			// triangle2.setReflectiveCoeff(0.5);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		public static void setup2(Application application) {
			//
			// set up Camera
			//
			Camera.SCALE_RATIO = 80;
			Point cameraPosition = new Point(5, 0, 0, Point.Space.WORLD);
			// Point cameraPosition = new Point(0, 2.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 0, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD),
					0.55);
			application.getWorld().addLightSource(light2);

			//
			// Entities
			//
			Point sphere1Center = new Point(0, 0, 0, Point.Space.WORLD);
			double sphere1Radius = 1;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setReflectiveCoeff(0.5);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			// sphere2.setReflectiveCoeff(0.2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.3, 0.5, 0.2, 180);
			// application.getWorld().addEntity(sphere2);

			double triangleSide = 5;

			Point triangle1Position = new Point(1, -1.5, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(triangleSide, -1.5, triangleSide, Point.Space.WORLD),
					new Point(triangleSide, -1.5, -triangleSide, Point.Space.WORLD),
					new Point(-triangleSide, -1.5, triangleSide, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);
			triangle1.setHasTexture(true);
			// triangle1.setReflectiveCoeff(0.5);

			application.getWorld().addEntity(triangle1);

			Point t2Position = new Point(1, -1.5, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-triangleSide, -1.5, triangleSide, Point.Space.WORLD),
					new Point(triangleSide, -1.5, -triangleSide, Point.Space.WORLD),
					new Point(-triangleSide, -1.5, -triangleSide, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			triangle2.setHasTexture(true);
			// triangle2.setReflectiveCoeff(0.5);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

	}

	static class AshikiminShirley {
		public static void setup0(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			sphere1.setShadingTechnique(BSDFTechnique.ASHIKHMIN_SHIRLEY);
			sphere1.setAshikiminShirleyCoeffs(100, 10);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.2, 0.5, 0.3, 180);
			sphere2.setAshikiminShirleyCoeffs(100, 10);
			sphere2.setShadingTechnique(BSDFTechnique.ASHIKHMIN_SHIRLEY);
			application.getWorld().addEntity(sphere2);

			//
			// Triangle 1
			//
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);
			triangle1.setShadingTechnique(BSDFTechnique.PHONG_BLINN);

			application.getWorld().addEntity(triangle1);

			//
			// Triangle 2
			//
			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			triangle2.setShadingTechnique(BSDFTechnique.PHONG_BLINN);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(3);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.ASHIKHMIN_SHIRLEY);

		}
	}

	public static class LightFallout {
		public static void setup0(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 65);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 65);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.2, 0.5, 0.3, 180);
			application.getWorld().addEntity(sphere2);

			//
			// Triangle 1
			//
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);

			application.getWorld().addEntity(triangle1);

			//
			// Triangle 2
			//
			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			application.getWorld().addEntity(triangle2);

			Entity.SHOULD_USE_FALLOUT = true;
			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}

		public static void setup1(Application application) {

			//
			// set up Camera
			//
			Point cameraPosition = new Point(0, 1.15, 3, Point.Space.WORLD);
			Vector cameraUp = new Vector(0, 1, 0);
			Point cameraLookAt = new Point(0, 1.15, 0, Point.Space.WORLD);
			double cameraFocalLength = 8;
			Camera camera = new Camera(cameraPosition, cameraUp, cameraLookAt, cameraFocalLength);
			application.getWorld().setCamera(camera);

			//
			// Light
			//
			Light light1 = new Light(new Irradiance(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 165);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new Irradiance(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 65);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			Irradiance basecColorS1 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS1 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS1 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			sphere1.setTransmissiveCoeff(0.8);
			sphere1.setRefractiveIndex(1.03);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			Irradiance basecColorS2 = new Irradiance(22, 183, 187, false).normalize();
			Irradiance diffuseColorS2 = new Irradiance(36, 199, 203, false).normalize();
			Irradiance specColorS2 = new Irradiance(123, 226, 236, false).normalize();

			Sphere sphere2 = new Sphere(sphere2Center, sphere2Radius, basecColorS2);
			sphere2.setColors(basecColorS2, specColorS2, diffuseColorS2);
			sphere2.setCoeffs(0.2, 0.5, 0.3, 180);
			sphere2.setReflectiveCoeff(0.6);
			application.getWorld().addEntity(sphere2);

			//
			// Triangle 1
			//
			Point triangle1Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t1Vertices = new Point[] {
					new Point(1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, 1.5, Point.Space.WORLD)
			};
			Irradiance triangle1Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle1 = new Triangle(triangle1Color, triangle1Position, t1Vertices);
			triangle1.setCoeffs(0.3, 0.4, 0.3, 180);
			triangle1.setHasTexture(true);
			application.getWorld().addEntity(triangle1);

			//
			// Triangle 2
			//
			Point t2Position = new Point(1, 0, 1, Point.Space.WORLD);
			Point[] t2Vertices = new Point[] {
					new Point(-1.5, 0, 1.5, Point.Space.WORLD),
					new Point(1.5, 0, -1.5, Point.Space.WORLD),
					new Point(-1.5, 0, -1.5, Point.Space.WORLD)
			};
			Irradiance t2Color = new Irradiance(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			triangle2.setHasTexture(true);
			application.getWorld().addEntity(triangle2);

			Entity.SHOULD_USE_FALLOUT = true;
			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}
	}
}