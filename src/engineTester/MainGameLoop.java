package engineTester;

import java.io.IOException;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		
		//RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		int textureID = 0;
		try {
			textureID = loader.loadTexture("stallTexture");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		ModelTexture texture = new ModelTexture(textureID);
		TexturedModel staticModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-50), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()){
			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
}
