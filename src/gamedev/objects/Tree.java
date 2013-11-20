package gamedev.objects;

import org.andengine.entity.sprite.Sprite;

import gamedev.game.ResourcesManager;

public class Tree extends Sprite {
	
	/**
	 * 
	 * @param pX
	 * @param pY
	 * @param texturIndex Index of the texture, there are 20 tree textures available (index 0 to 19)
	 */
	public Tree(float pX, float pY, int textureIndex) {
		super(pX, pY, ResourcesManager.getInstance().treeRegions[textureIndex].deepCopy(), ResourcesManager.getInstance().vbom);
		this.mScaleX = this.mScaleX * 1.5f;
		this.mScaleY = this.mScaleY * 1.5f;
	}

}
