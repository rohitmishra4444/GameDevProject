package gamedev.objects;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import gamedev.game.ResourcesManager;

public class OldCaveman extends StaticObject {

	public OldCaveman(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().oldCavemanRegion);
	}

	@Override
	protected void createPhysics() {
		
	    final Rectangle rect = new Rectangle(this.getX()-32, this.getY(), 100, 10, this.resourcesManager.vbom);
	    final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
	    this.body = PhysicsFactory.createBoxBody(this.resourcesManager.physicsWorld, rect, BodyType.StaticBody, boxFixtureDef);
//	    rect.setVisible(false);
	    this.attachChild(rect);		
	}

}
