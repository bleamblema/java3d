package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		RawModel playerModel = OBJLoader.loadObjModel("player", loader);
		TexturedModel playerTexturedModel = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(playerTexturedModel, new Vector3f(70, 5, -70), 0, 100, 0, 0.6f);
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		
		// *********TERRAIN TEXTURE STUFF***********

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);

		// *****************************************

		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));


		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			float x = random.nextFloat() * 400;
			float z = random.nextFloat() * -400;
			float y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));

		}

		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);
		
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		GuiTexture shadowMap = new GuiTexture(renderer.getShadowMaptexture(), 
				new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		//guiTextures.add(shadowMap);
		GuiRenderer guiRenderer = new GuiRenderer(loader);

		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);
		
		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			
			renderer.renderShadowMap(entities, sun);
			renderer.processEntity(player);

			multisampleFbo.bindFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0f,0f,0f,0f));
			multisampleFbo.unbindFrameBuffer();
			multisampleFbo.resolveToFbo(outputFbo);
			PostProcessing.doPostProcessing(outputFbo.getColourTexture());
			
			guiRenderer.render(guiTextures);
			DisplayManager.updateDisplay();
		}

		PostProcessing.cleanUp();
		outputFbo.cleanUp();
		multisampleFbo.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	
}
