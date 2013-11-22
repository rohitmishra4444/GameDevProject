package gamedev.objects;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import gamedev.game.ResourcesManager;

public class Tree extends StaticObject {
	
	private final static float BODY_WIDTH = 10;
	private final static float BODY_HEIGHT = 10;
	
	/**
	 * 
	 * @param pX
	 * @param pY
	 * @param texturIndex Index of the texture, there are 20 tree textures available (index 0 to 19)
	 */
	public Tree(float pX, float pY, int textureIndex) {
		super(pX, pY, ResourcesManager.getInstance().treeRegions[textureIndex].deepCopy());
		this.mScaleX = this.mScaleX * 1.5f;
		this.mScaleY = this.mScaleY * 1.5f;
	}

	@Override
	protected void createPhysics() {
		// Use a invisible rectangle to define the body, because the tree sprite is too big
	    final Rectangle rect = new Rectangle(this.getX() + 128/2, this.getY()+128-BODY_HEIGHT/2, BODY_WIDTH,
				BODY_HEIGHT, this.resourcesManager.vbom);
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		this.body = PhysicsFactory.createBoxBody(this.resourcesManager.physicsWorld, rect, BodyType.StaticBody, boxFixtureDef);
		this.body.setUserData(this.bodyUserData);
		rect.setVisible(false);
		this.attachChild(rect);
	}

}
