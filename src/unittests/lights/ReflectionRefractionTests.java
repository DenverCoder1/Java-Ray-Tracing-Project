/**
 * 
 */
package unittests.lights;

import org.junit.Test;

import elements.*;
import geometries.Sphere;
import geometries.Triangle;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 * 
 * @author dzilb
 */
public class ReflectionRefractionTests {
	private Scene scene = new Scene("Test scene").setSupersamplingEnabled(false);

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void twoSpheres() {
		Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(150, 150).setDistance(1000);

		scene.geometries.add( //
				new Sphere(new Point3D(0, 0, -50), 50) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)),
				new Sphere(new Point3D(0, 0, -50), 25) //
						.setEmission(new Color(java.awt.Color.RED)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)));
		scene.lights.add( //
				new SpotLight(new Color(1000, 600, 0), new Point3D(-100, -100, 500), new Vector(-1, -1, -2)) //
						.setKL(0.0004).setKQ(0.0000006));
		scene.setCamera(camera);

		Render render = new Render() //
				.setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500)) //
				.setRayTracer(new BasicRayTracer(scene));
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void twoSpheresOnMirrors() {
		Camera camera = new Camera(new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(2500, 2500).setDistance(10000); //

		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

		scene.geometries.add( //
				new Sphere(new Point3D(-950, -900, -1000), 400) //
						.setEmission(new Color(0, 0, 100)) //
						.setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20).setKT(0.5)),
				new Sphere(new Point3D(-950, -900, -1000), 200) //
						.setEmission(new Color(100, 20, 20)) //
						.setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500), new Point3D(670, 670, 3000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(1)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500), new Point3D(-1500, -1500, -2000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(0.5)));

		scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point3D(-750, -750, -150), new Vector(-1, -1, -4)) //
				.setKL(0.00001).setKQ(0.000005));

		scene.setCamera(camera);

		ImageWriter imageWriter = new ImageWriter("reflectionTwoSpheresMirrored", 500, 500);
		Render render = new Render() //
				.setImageWriter(imageWriter) //
				.setRayTracer(new BasicRayTracer(scene));

		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light with a partially
	 * transparent Sphere producing partial shadow
	 */
	@Test
	public void trianglesTransparentSphere() {
		Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(200, 200).setDistance(1000);

		scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

		scene.geometries.add( //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(150, -150, -135), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)), //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(-70, 70, -140), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)), //
				new Sphere(new Point3D(60, 50, -50), 30) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));

		scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(60, 50, 0), new Vector(0, 0, -1)) //
				.setKL(4E-5).setKQ(2E-7));

		scene.setCamera(camera);

		ImageWriter imageWriter = new ImageWriter("refractionShadow", 600, 600);
		Render render = new Render() //
				.setImageWriter(imageWriter) //
				.setRayTracer(new BasicRayTracer(scene));

		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a bird-like figure
	 */
	@Test
	public void customScene() {
		Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(150, 150).setDistance(1000);

		scene.geometries.add( //
				new Sphere(new Point3D(0, 0, -50), 50) //
						.setEmission(new Color(java.awt.Color.GREEN)) //
						.setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)),
				new Sphere(new Point3D(-25, 5, -20), 25) //
						.setEmission(new Color(java.awt.Color.WHITE)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100).setKT(0.9)),
				new Sphere(new Point3D(-30, 5, 1), 5) //
						.setEmission(new Color(java.awt.Color.BLACK)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),
				new Sphere(new Point3D(25, 5, -20), 25) //
						.setEmission(new Color(java.awt.Color.WHITE)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100).setKT(0.5)),
				new Sphere(new Point3D(30, 5, 1), 5) //
						.setEmission(new Color(java.awt.Color.BLACK)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),
				new Sphere(new Point3D(30, 5, 1), 5) //
						.setEmission(new Color(java.awt.Color.BLACK)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),
				new Triangle(new Point3D(-10, -18, -5), new Point3D(10, -18, -5), new Point3D(0, -38, 5)) //
						.setEmission(new Color(java.awt.Color.ORANGE)) //
						.setMaterial(new Material().setKR(0.3)));

		scene.lights.add( //
				new SpotLight(new Color(1000, 600, 0), new Point3D(-100, -100, 500), new Vector(-1, -1, -2)) //
						.setKL(0.0004).setKQ(0.000006));

		scene.setCamera(camera);

		Render render = new Render() //
				.setImageWriter(new ImageWriter("customScene", 500, 500)) //
				.setRayTracer(new BasicRayTracer(scene));
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a bird like figure inside two triangular mirrors
	 */
	@Test
	public void customScene2() {
		Camera camera = new Camera(new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(2500, 2500).setDistance(10000); //

		scene.geometries.add( //
				new Sphere(new Point3D(-500.0 + 0, -500.0 + 0, -500.0 - 50), 50) //
						.setEmission(new Color(java.awt.Color.GREEN)) //
						.setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)),
				new Sphere(new Point3D(-500.0 - 25, -500.0 + 5, -500.0 - 20), 25) //
						.setEmission(new Color(java.awt.Color.WHITE)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100).setKT(0.9)),
				new Sphere(new Point3D(-1030, -500.0 + 5, -500.0 + 1), 5) //
						.setEmission(new Color(java.awt.Color.BLACK)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),
				new Sphere(new Point3D(-500.0 + 25, -500.0 + 5, -500.0 - 20), 25) //
						.setEmission(new Color(java.awt.Color.WHITE)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100).setKT(0.5)),
				new Sphere(new Point3D(-500.0 + 30, -500.0 + 5, -500.0 + 1), 5) //
						.setEmission(new Color(java.awt.Color.BLACK)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),
				new Sphere(new Point3D(-500.0 + 30, -500.0 + 5, -500.0 + 1), 5) //
						.setEmission(new Color(java.awt.Color.BLACK)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),
				new Triangle(new Point3D(-500.0 - 10, -500.0 - 18, -500.0 - 5),
						new Point3D(-500.0 + 10, -500.0 - 18, -500.0 - 5), new Point3D(-500.0 + 0, -500.0 - 38, -500.0 + 5)) //
								.setEmission(new Color(java.awt.Color.ORANGE)) //
								.setMaterial(new Material().setKR(0.3)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500), new Point3D(670, 670, 3000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(1)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500), new Point3D(-1500, -1500, -2000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(0.5)));

		scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point3D(-750, -750, -150), new Vector(-1, -1, -4)) //
				.setKL(0.00001).setKQ(0.000005));

		scene.lights.add( //
				new SpotLight(new Color(1000, 600, 0), new Point3D(-100, -100, 500), new Vector(-1, -1, -2)) //
						.setKL(0.0004).setKQ(0.000006));

		scene.setCamera(camera);

		Render render = new Render() //
				.setImageWriter(new ImageWriter("customScene2", 500, 500)) //
				.setRayTracer(new BasicRayTracer(scene));
		render.renderImage();
		render.writeToImage();
	}

}
