package engineTester;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		float[] vertices = { -0.5f, 0.5f, 0, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, 0.5f, 0f }; 
		int[] indices = { 0,1,3, 3,1,2 };
		float[] texutureCoords = { 0,0, 0,1, 1,1, 1,0 };
		
		RawModel model = loader.loadToVAO(vertices, texutureCoords, indices);
		int textureID= loader.loadTexture("image");
		ModelTexture texture = new ModelTexture(textureID);
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		
		while(!Display.isCloseRequested()){
			renderer.prepare();
			shader.start();
			renderer.render(texturedModel);
			shader.stop();

			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
}
