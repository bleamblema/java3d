package particles;

public class ParticleTexture {
	
	private int textureID;
	private int numberOfRows;
	private boolean additive;
	
	public ParticleTexture(int textureID, int numberOfRows, boolean additive) {
		this.textureID = textureID;
		this.additive = additive;
		this.numberOfRows = numberOfRows;
	}

	protected boolean usesAdditiveBlending() {
		return additive;
	}

	protected int getTextureID() {
		return textureID;
	}

	protected int getNumberOfRows() {
		return numberOfRows;
	}
	
}
