package gamedev.objects;

import gamedev.game.ResourcesManager;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

abstract class StaticObject extends Sprite {
	
	protected Body body;
	protected PhysicsHandler physicsHandler;
	protected ResourcesManager resourcesManager;
	
	public StaticObject(float pX, float pY, ITextureRegion textureRegion) {
		super(pX, pY, textureRegion, ResourcesManager.getInstance().vbom);
		this.resourcesManager = ResourcesManager.getInstance();
		createPhysics();
	}
	
	protected void createPhysics() {
		FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		this.body = PhysicsFactory.createBoxBody(this.resourcesManager.physicsWorld, this, BodyType.StaticBody, objectFixtureDef);
		// TODO Maybe not necessary...
//		this.physicsHandler = new PhysicsHandler(this);
//		this.registerUpdateHandler(this.physicsHandler);
	}
	
}
