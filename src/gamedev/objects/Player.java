package gamedev.objects;

import java.util.Arrays;

import gamedev.game.ResourcesManager;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Player extends AnimatedSprite {

	protected ResourcesManager resourcesManager;
	protected PlayerState currentState = PlayerState.WALKING_N;
	public Body playerBody;
	public PhysicsHandler physicsHandler;
	
	public enum PlayerState {
		WALKING_W,
		WALKING_SW,
		WALKING_SE,
		WALKING_S,
		WALKING_NW,
		WALKING_NE,
		WALKING_N,
		WALKING_E,
//		IDLE_SE,
//		IDLE_E,
//		IDLE_NE,
//		IDLE_N,
//		IDLE_NW,
//		IDLE_W,
//		IDLE_SW,
//		IDLE_S,
		RUNNING_W,
		RUNNING_SW,
		RUNNING_SE,
		RUNNING_S,
		RUNNING_NW,
		RUNNING_NE,
		RUNNING_N,
		RUNNING_E,
		HIT_W,
		HIT_SW,
		HIT_SE,
		HIT_S,
		HIT_NW,
		HIT_NE,
		HIT_N,
		HIT_E,
	}
	
	/**
	 * Create the player
	 * @param pX x-Position in the World
	 * @param pY y-Position in the World
	 */
	public Player(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().playerRegion, ResourcesManager.getInstance().vbom);
		this.resourcesManager = ResourcesManager.getInstance();
		this.resourcesManager.camera.setChaseEntity(this);
		this.createAndConnectPhysics(this.resourcesManager.camera, this.resourcesManager.physicsWorld);
	}
	
	//TODO Remove... need to find another way. where to initialize physics? ResourcesManager?
	public Player() {
		super(0, 0, ResourcesManager.getInstance().playerRegion, ResourcesManager.getInstance().vbom);
		this.resourcesManager = ResourcesManager.getInstance();
		this.resourcesManager.camera.setChaseEntity(this);
		this.createAndConnectPhysics(this.resourcesManager.camera, this.resourcesManager.physicsWorld);
	}

	
	public void setState(PlayerState state) {
		this.currentState = state;
		// Display animation based on current State
		int index = Arrays.asList(PlayerState.values()).indexOf(state);
		this.animate(new long[] { 30, 30, 30, 30, 30, 30, 30, 30 }, index * 8, index * 8 + 7, false);
	}
		
	protected void createAndConnectPhysics(final BoundCamera camera, PhysicsWorld physicsWorld) {
		this.playerBody = PhysicsFactory.createBoxBody(this.resourcesManager.physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		this.playerBody.setUserData("player");	
		this.physicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.physicsHandler);
		this.resourcesManager.physicsWorld.registerPhysicsConnector(new PhysicsConnector(
				this, this.playerBody, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.updateChaseEntity();
			}
		});		
	}
	
}
