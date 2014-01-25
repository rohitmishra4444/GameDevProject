package gamedev.objects;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

public class Box extends StaticObject {

	protected boolean opened = false;
	protected Scene map;
	
	public Box(float pX, float pY, Scene map) {
		super(pX, pY, ResourcesManager.getInstance().boxRegion);
		this.map = map;
		this.createPhysics();
	}
		
	@Override
	protected void createPhysics() {
	    final Rectangle rect = new Rectangle(getX(), getY(), 25, 25, this.resourcesManager.vbom);
	    final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
	    this.body = PhysicsFactory.createBoxBody(this.resourcesManager.physicsWorld, rect, BodyType.StaticBody, boxFixtureDef);
	    rect.setVisible(false);
	    this.map.attachChild(rect);
	    this.body.setUserData(this);
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	
}
