package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		TextMaster.init(loader);

		FontType font = new FontType(loader.loadTextureAltlas("candara"), 
					new File("res/candara.fnt"));
		GUIText text = new GUIText("This is a test text!", 3, font, new Vector2f(0f,0.5f), 1, true);
		text.setColour(0.1f, 0.1f, 0.1f);
		
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
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));

		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		fernTexture.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTexture);

		//TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));

		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);

		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
//			if (i % 7 == 0) {
//				float x = random.nextFloat() * 800 - 400;
//				float z = random.nextFloat() * -600;
//				float y = terrain.getHeightOfTerrain(x, z);
//				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
//
//				x = random.nextFloat() * 800 - 400;
//				z = random.nextFloat() * -600;
//				y = terrain.getHeightOfTerrain(x, z);
//				entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1.8f));
//
//				x = random.nextFloat() * 800 - 400;
//				z = random.nextFloat() * -600;
//				y = terrain.getHeightOfTerrain(x, z);
//				entities.add(new Entity(flower, new Vector3f(x, y, z), 0, 0, 0, 2.3f));
//			}
//
//			if (i % 3 == 0) {
//				float x = random.nextFloat() * 800 - 400;
//				float z = random.nextFloat() * -600;
//				float y = terrain.getHeightOfTerrain(x, z);
//				entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
//
//				x = random.nextFloat() * 800 - 400;
//				z = random.nextFloat() * -600;
//				y = terrain.getHeightOfTerrain(x, z);
//				entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
//			}
			float x = random.nextFloat() * 400;
			float z = random.nextFloat() * -400;
			float y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));

		}

		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(1f, 1f, 1f)));
		lights.add(new Light(new Vector3f(185, getHeight(random, terrain, 185, -293)+10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370, getHeight(random, terrain, 370, -300)+10, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(293, getHeight(random, terrain, 293, -305)+10, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));

		entities.add(new Entity(lamp, new Vector3f(185, getHeight(random, terrain, 185, -293), -293), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(370, getHeight(random, terrain, 370, -300), -300), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(293, getHeight(random, terrain, 293, -305), -305), 0, 0, 0, 1));

		MasterRenderer renderer = new MasterRenderer(loader);

		RawModel playerModel = OBJLoader.loadObjModel("player", loader);
		TexturedModel playerTexturedModel = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("playerTexture")));

		Player player = new Player(playerTexturedModel, new Vector3f(70, 5, -70), 0, 100, 0, 0.6f);
		Camera camera = new Camera(player);

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture health = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.74f, 0.925f), new Vector2f(0.25f, 0.25f));
		guis.add(health);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

//		Entity lampEntity = new Entity(lamp, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1);
//		entities.add(lampEntity);
//
//		Light light = new Light(new Vector3f(293, 7, -305), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f));
//		lights.add(light);
		
		Vector4f clipPlane = new Vector4f(0f,0f,0f,0f);
		
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setShineDamper(20);
		barrelModel.getTexture().setReflectivity(1);
		normalMapEntities.add(new Entity(barrelModel, new Vector3f(75,15,-75),0,0,0,1f));
		
		

		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			picker.update();
			renderer.processEntity(player);

			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, clipPlane);
			//guiRenderer.render(guis);
			TextMaster.render();
			DisplayManager.updateDisplay();
		}

		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	
	private static float getHeight(Random random, Terrain terrain,float x, float z){
		float y = terrain.getHeightOfTerrain(x, z);
		return y;
	}
}
