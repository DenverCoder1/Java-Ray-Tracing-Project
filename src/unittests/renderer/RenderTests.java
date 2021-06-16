package unittests.renderer;

import org.junit.Test;

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import renderer.Render.SUPERSAMPLING_TYPE;
import scene.Scene;

/**
 * Test rendering a basic image
 * 
 * @author Dan
 */
public class RenderTests {
	private Camera camera = new Camera(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
			.setDistance(100) //
			.setViewPlaneSize(500, 500);

	/**
	 * Produce a scene with basic 3D model and render it into a png image with a
	 * grid
	 */
	@Test
	public void basicRenderTwoColorTest() {
		Scene scene = new Scene("basicRenderTwoColorTest")//
				.setAmbientLight(new AmbientLight(new Color(255, 191, 191), 1)) //
				.setBackground(new Color(75, 127, 90));

		scene.geometries.add(new Sphere(new Point3D(0, 0, -100), 50),
				new Triangle(new Point3D(-100, 0, -100), new Point3D(0, 100, -100), new Point3D(-100, 100, -100)), // up
				// left
				new Triangle(new Point3D(100, 0, -100), new Point3D(0, 100, -100), new Point3D(100, 100, -100)), // up
				// right
				new Triangle(new Point3D(-100, 0, -100), new Point3D(0, -100, -100), new Point3D(-100, -100, -100)), // down
				// left
				new Triangle(new Point3D(100, 0, -100), new Point3D(0, -100, -100), new Point3D(100, -100, -100))); // down
		// right

		scene.setCamera(camera);

		ImageWriter imageWriter = new ImageWriter("base render test", 1000, 1000);
		Render render = new Render() //
				.setImageWriter(imageWriter) //
				.setRayTracer(new BasicRayTracer(scene));

		render.renderImage();
		render.printGrid(100, new Color(java.awt.Color.YELLOW));
		render.writeToImage();
	}

	/**
	 * Test for XML based scene - for bonus
	 */
	@Test
	public void basicRenderXml() {
		Scene scene = new Scene("XML Test scene");
		// enter XML file name and parse from XML file into scene object
		// ...

		scene.setCamera(camera);

		ImageWriter imageWriter = new ImageWriter("xml render test", 1000, 1000);
		Render render = new Render() //
				.setImageWriter(imageWriter) //
				.setRayTracer(new BasicRayTracer(scene));

		render.renderImage();
		render.printGrid(100, new Color(java.awt.Color.YELLOW));
		render.writeToImage();
	}

	// For stage 6 - please disregard in stage 5
	/**
	 * Produce a scene with basic 3D model - including individual lights of the
	 * bodies and render it into a png image with a grid
	 */
	@Test
	public void basicRenderMultiColorTest() {
		Scene scene = new Scene("basicRenderMultiColorTest")//
				.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.2)); //

		scene.geometries.add(new Sphere(new Point3D(0, 0, -100), 50) //
				.setEmission(new Color(java.awt.Color.CYAN)), //
				new Triangle(new Point3D(-100, 0, -100), new Point3D(0, 100, -100), new Point3D(-100, 100, -100)) // up left
						.setEmission(new Color(java.awt.Color.GREEN)),
				new Triangle(new Point3D(100, 0, -100), new Point3D(0, 100, -100), new Point3D(100, 100, -100)), // up right
				new Triangle(new Point3D(-100, 0, -100), new Point3D(0, -100, -100), new Point3D(-100, -100, -100)) // down left
						.setEmission(new Color(java.awt.Color.RED)),
				new Triangle(new Point3D(100, 0, -100), new Point3D(0, -100, -100), new Point3D(100, -100, -100)) // down right
						.setEmission(new Color(java.awt.Color.BLUE)));

		scene.setCamera(camera);

		ImageWriter imageWriter = new ImageWriter("color render test", 1000, 1000);
		Render render = new Render() //
				.setImageWriter(imageWriter) //
				.setRayTracer(new BasicRayTracer(scene));

		render.renderImage();
		render.printGrid(100, new Color(java.awt.Color.WHITE));
		render.writeToImage();
	}

	/**
	 * Test for supersampling
	 */
	@Test
	public void supersamplingSpheres() {
		Scene scene = new Scene("supersamplingSpheres");
		Camera camera1 = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(150, 150).setDistance(1000);

		scene.geometries.add( //
				new Sphere(new Point3D(0, 0, -50), 70) //
						.setEmission(new Color(java.awt.Color.RED)) //
						.setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)),
				new Sphere(new Point3D(0, 0, -50), 60) //
						.setEmission(new Color(java.awt.Color.ORANGE)) //
						.setMaterial(new Material().setKD(0.2).setKS(0.1).setShininess(100).setKT(0.4)), //
				new Sphere(new Point3D(0, 0, -50), 50) //
						.setEmission(new Color(java.awt.Color.YELLOW)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100).setKT(0.5)), //
				new Sphere(new Point3D(0, 0, -50), 40) //
						.setEmission(new Color(java.awt.Color.GREEN)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100).setKT(0.6)), //
				new Sphere(new Point3D(0, 0, -50), 30) //
						.setEmission(new Color(java.awt.Color.CYAN)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100).setKT(0.7)), //
				new Sphere(new Point3D(0, 0, -50), 20) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100).setKT(0.8)), //
				new Sphere(new Point3D(0, 0, -50), 10) //
						.setEmission(new Color(java.awt.Color.MAGENTA)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100).setKT(0.9)), //
				new Sphere(new Point3D(0, 0, -50), 5) //
						.setEmission(new Color(java.awt.Color.PINK)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)), //
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500), new Point3D(670, 670, 3000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(1)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500), new Point3D(-1500, -1500, -2000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(0.5)), //
				new Triangle(new Point3D(-30, 0, 50), new Point3D(0, 0, 50), new Point3D(0, 30, 50)) //
						.setEmission(new Color(40, 40, 40)) //
						.setMaterial(new Material().setKR(0.5)), //
				new Triangle(new Point3D(30, 0, 50), new Point3D(0, 0, 50), new Point3D(0, -30, 50)) //
						.setEmission(new Color(40, 40, 40)) //
						.setMaterial(new Material().setKR(0.5)) //
		);

		scene.lights.add( //
				new SpotLight(new Color(1000, 600, 0), new Point3D(-100, -100, 500), new Vector(-1, -1, -2)) //
						.setKL(0.0004).setKQ(0.0000006));

		scene.lights.add(new DirectionalLight(new Color(500, 300, 0), new Vector(1, 1, -1)));

		scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point3D(-750, -750, -150), new Vector(-1, -1, -4)) //
				.setKL(0.00001).setKQ(0.000005));

		scene.setCamera(camera1);

		Render render = new Render() //
				.setImageWriter(new ImageWriter("SpheresSupersamplingOn", 500, 500)) //
				.setRayTracer(new BasicRayTracer(scene)) //
				.setSupersamplingType(SUPERSAMPLING_TYPE.ADAPTIVE);
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Test for adaptive supersampling
	 */
	@Test
	public void supersamplingTwoSpheres() {
		Scene scene = new Scene("supersamplingTwoSpheres");
		Camera camera1 = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
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
		scene.setCamera(camera1);

		Render render = new Render() //
				.setImageWriter(new ImageWriter("refractionTwoSpheresAdaptiveSupersampling", 500, 500)) //
				.setRayTracer(new BasicRayTracer(scene)) //
				.setSupersamplingType(SUPERSAMPLING_TYPE.ADAPTIVE) //
				.setMultithreadingThreads(0);
		render.renderImage();
		render.writeToImage();
	}
}
