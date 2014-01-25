package gamedev.objects;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import gamedev.game.ResourcesManager;

public class Box extends StaticObject {

	protected boolean opened = false;
	
	public Box(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().boxRegion);
	}

	@Override
	protected void createPhysics() {
	    final Rectangle rect = new Rectangle(0, 0, 25, 25, this.resourcesManager.vbom);
	    final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
	    this.body = PhysicsFactory.createBoxBody(this.resourcesManager.physicsWorld, rect, BodyType.StaticBody, boxFixtureDef);
//	    rect.setVisible(false);
	    this.attachChild(rect);
	    this.body.setUserData(this);
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	
}
