package gamedev.objects;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

public class OldCaveman extends StaticObject {

	public OldCaveman(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().oldCavemanRegion);
	}

	@Override
	protected void createPhysics() {
	}

}
