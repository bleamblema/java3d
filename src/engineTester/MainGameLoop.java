package engineTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		RawModel model = OBJLoader.loadObjModel("box", loader);
		
		//RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		int textureID = 0;
		try {
			textureID = loader.loadTexture("image");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		
		ModelTexture texture = new ModelTexture(textureID);
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		TexturedModel staticModel = new TexturedModel(model, texture);
		
//		Entity entity = new Entity(staticModel, new Vector3f(0,0,-50), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		Random random = new Random();
		List<Entity> allCubes = new ArrayList<Entity>();
		
		for(int i = 0; i< 200; i++){
			float x = random.nextFloat() * 100 - 50;
			float y = random.nextFloat() * 100 - 50;
			float z = random.nextFloat() * -300;
			allCubes.add(new Entity(staticModel, new Vector3f(x,y,z), random.nextFloat() * 180f, 
					random.nextFloat() * 180f, 0f, 1f));
			
 		}
		
		MasterRenderer renderer = new MasterRenderer();
		while(!Display.isCloseRequested()){
			
			for(Entity cube : allCubes){
				renderer.precessEntity(cube);
			}
			
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
}
