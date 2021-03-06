package particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import entities.Camera;
import entities.Player;

public class Particle {
	
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	private float elapsedTime = 0;
	private float distance;
	
	private Vector3f reusableChange = new Vector3f();
	
	private ParticleTexture texture;
	
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect,
			float lifeLength, float rotation, float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}


	private void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	
	}


	private void setTextureOffset(Vector2f offset, int index){
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float)column / texture.getNumberOfRows();
		offset.y = (float)row / texture.getNumberOfRows();
	}


	protected boolean update(Camera camera) {
		velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		reusableChange.set(velocity);
		reusableChange.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(reusableChange, position, position);
		updateTextureCoordInfo();
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}


	protected float getDistance() {
		return distance;
	}


	protected Vector2f getTexOffset1() {
		return texOffset1;
	}


	protected Vector2f getTexOffset2() {
		return texOffset2;
	}


	protected float getBlend() {
		return blend;
	}


	protected ParticleTexture getTexture() {
		return texture;
	}

	protected Vector3f getPosition() {
		return position;
	}
	protected float getRotation() {
		return rotation;
	}
	protected float getScale() {
		return scale;
	}

}
