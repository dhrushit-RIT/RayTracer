package raytracer;

import java.util.ArrayList;
import java.io.File;

import raytracer.util.ply_reader.PLYReader;

public class Setups {
	public class Refraction {
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
			Light light1 = new Light(new MyColor(1, 1, 1, true), new Point(0, 5, 0, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light1);

			Light light2 = new Light(new MyColor(1, 1, 1, true), new Point(2, 5, 5, Point.Space.WORLD), 0.55);
			application.getWorld().addLightSource(light2);

			// ================================================================
			// Entities
			// ================================================================

			//
			// Sphere 1
			//
			Point sphere1Center = new Point(-0.4, 1, 0.4, Point.Space.WORLD);
			double sphere1Radius = 0.755;
			MyColor basecColorS1 = new MyColor(22, 183, 187, false).normalize();
			MyColor diffuseColorS1 = new MyColor(36, 199, 203, false).normalize();
			MyColor specColorS1 = new MyColor(123, 226, 236, false).normalize();

			Sphere sphere1 = new Sphere(sphere1Center, sphere1Radius, basecColorS1);
			sphere1.setColors(basecColorS1, specColorS1, diffuseColorS1);
			sphere1.setCoeffs(0.3, 0.6, 0.1, 160);
			application.getWorld().addEntity(sphere1);

			//
			// Sphere 2
			//
			Point sphere2Center = new Point(0.6, 0.8, -0.6, Point.Space.WORLD);
			double sphere2Radius = 0.6;
			MyColor basecColorS2 = new MyColor(22, 183, 187, false).normalize();
			MyColor diffuseColorS2 = new MyColor(36, 199, 203, false).normalize();
			MyColor specColorS2 = new MyColor(123, 226, 236, false).normalize();

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
			MyColor triangle1Color = new MyColor(238, 50, 51, false).normalize();
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
			MyColor t2Color = new MyColor(238, 50, 51, false).normalize();
			Triangle triangle2 = new Triangle(t2Color, t2Position, t2Vertices);
			triangle2.setCoeffs(0.3, 0.4, 0.2, 180);
			application.getWorld().addEntity(triangle2);

			application.getWorld().setSuperSampleFactor(1);
			application.getWorld().setBSDFTechnique(Entity.BSDFTechnique.PHONG_BLINN);

		}
	}

}