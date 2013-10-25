package gamedev.objects;

import java.util.Arrays;

import gamedev.game.Direction;
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
	
	public final static long[] ANIMATION_DURATION = { 30, 30, 30, 30, 30, 30, 30, 30};
	
	public Body playerBody;
	public PhysicsHandler physicsHandler;

	protected ResourcesManager resourcesManager;
	protected PlayerState currentState = PlayerState.IDLE;
	
	protected int direction = Direction.NORTH;
	protected int life = 100;
	
	public enum PlayerState {
		IDLE,
		WALKING,
		RUNNING,
		HIT,
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
	
	public Player() {
		super(0, 0, ResourcesManager.getInstance().playerRegion, ResourcesManager.getInstance().vbom);
		this.resourcesManager = ResourcesManager.getInstance();
		this.resourcesManager.camera.setChaseEntity(this);
		this.createAndConnectPhysics(this.resourcesManager.camera, this.resourcesManager.physicsWorld);
	}

	
	public void setState(PlayerState state) {
		this.currentState = state;
		// Display animation based on current State and direction
		if (state == PlayerState.IDLE) {
			this.stopAnimation();
			return;
		}
		if (!this.isAnimationRunning()) {
			int rowIndex = 0;
			if (state == PlayerState.WALKING) rowIndex = 0;
			if (state == PlayerState.RUNNING) rowIndex = 9;
			if (state == PlayerState.HIT) rowIndex = 17;
			int startTile = rowIndex*8 + this.direction*8;
			this.animate(ANIMATION_DURATION, startTile, startTile+7, false);			
		}
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public boolean isAlive() {
		return this.life > 0;
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
