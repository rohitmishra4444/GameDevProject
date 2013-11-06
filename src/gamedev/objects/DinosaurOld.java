package gamedev.objects;

import gamedev.game.Direction;
import gamedev.game.ResourcesManager;

import java.util.Random;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.*;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.modifier.IModifier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class DinosaurOld extends AnimatedSprite {
	
	public final static long[] ANIMATION_DURATION = { 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
	public final static int FRAMES_PER_ANIMATION = 13;
	public final static int TILES_PER_LINE = 26;
	
//	public Body body;
//	public PhysicsHandler physicsHandler;

	protected ResourcesManager resourcesManager;
	protected DinosaurState currentState;
	protected float animationElapsedTime = 0;
	protected float animationTime = 10;
	
	protected int direction = Direction.WEST;
	protected int life = 100;
	// Ground  speed Pixels per second when moving
	protected float speed = 3;
	// Factor for speed when running
	protected float speedFactor = 2;
	
	// If the player is in this radius, follow him
	protected int radius = 100;
	private MoveModifier currentMoveModifier;
		
	public enum DinosaurState {
		WALKING,
		RUNNING,
		BEEN_HIT,
		TIPPING_OVER,
		ATTACK,
		ROARING,
		PAUSED,
		LOOKING,
		CHASE_PLAYER,
	}
	
	public DinosaurOld(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().dinosaurGreenRegion, ResourcesManager.getInstance().vbom);
		this.resourcesManager = ResourcesManager.getInstance();
		this.direction = Direction.getRandomDirection();
		this.setState(DinosaurState.LOOKING);
//		this.createPhysic();
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void setState(DinosaurState state) {
		this.currentState = state;
		// Display the correct animation based on the state and direction
		int rowIndex = 0;
		if (state == DinosaurState.WALKING) rowIndex = 0;
		if (state == DinosaurState.TIPPING_OVER) rowIndex = 4;
		if (state == DinosaurState.RUNNING || state == DinosaurState.CHASE_PLAYER) rowIndex = 8;
		if (state == DinosaurState.ROARING) rowIndex = 12;
		if (state == DinosaurState.PAUSED) rowIndex = 16;
		if (state == DinosaurState.LOOKING) rowIndex = 20;
		if (state == DinosaurState.BEEN_HIT) rowIndex = 24;
		if (state == DinosaurState.ATTACK) rowIndex = 28;			
		int startTile = rowIndex*TILES_PER_LINE + this.direction*FRAMES_PER_ANIMATION;
		boolean animate = (state == DinosaurState.TIPPING_OVER || state == DinosaurState.BEEN_HIT) ? false : true;
		this.animate(ANIMATION_DURATION, startTile, startTile+12, animate);			

	}
	
	public Vector2 getPositionVector() {
		return new Vector2(this.getX(), this.getY());
	}
	
	@Override
    protected void onManagedUpdate(float pSecondsElapsed) {
            super.onManagedUpdate(pSecondsElapsed);
            
            // Check if we should chase the player!
//            Vector2 playerPos = this.resourcesManager.player.getPositionVector();
//            Vector2 pos = this.getPositionVector();
//            if (pos.dst(playerPos) < this.radius) {
//            	this.chasePlayer(pos, playerPos);
//            	this.animationTime = 0;
//            	return;
//            } 
            
            // Set a random state after a random time. If the state is walking or running, set a random position where the dino walks.
            this.animationElapsedTime += pSecondsElapsed;
            if (this.animationElapsedTime > this.animationTime) {
            	this.animationElapsedTime = 0;
            	Random r = new Random();
            	// Set a random animation time [10...20] for the next animation seconds
            	this.animationTime = 10 + (r.nextFloat() * 10 + 1);
            	// Pick a random state, exclude some states
            	DinosaurState randomState = this.getRandomState();
            	// If the state is walking, calculate a new random position
            	if (randomState == DinosaurState.WALKING || randomState == DinosaurState.RUNNING) {
            		// The new Position should be in Range [-1000...1000] from the current position
            		float rX = this.getX() + (-1000 + (r.nextFloat() * 2000 + 1));
            		float rY = this.getY() + (-1000 + (r.nextFloat() * 2000 + 1));            		
            		this.moveTo(rX, rY, randomState);
            	} else {
                	this.setState(randomState);            		
            	}
            }
    }
	
	protected void chasePlayer(Vector2 pos, Vector2 playerPos) {
		this.setDirection(Direction.getDirectionFromVectors(pos, playerPos));
		// Unregister random walking modifier
		if (this.currentMoveModifier != null) this.unregisterEntityModifier(this.currentMoveModifier);

		this.currentMoveModifier = new MoveModifier(10f, pos.x, playerPos.x, pos.y, playerPos.y, new IEntityModifierListener() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub
			}
		});
		this.registerEntityModifier(new MoveModifier(10f, pos.x, playerPos.x, pos.y, playerPos.y));
		this.setState(DinosaurState.CHASE_PLAYER);
	}

	protected void moveTo(float x, float y, DinosaurState state) {
		// Calculate direction
		Vector2 from = this.getPositionVector();
		Vector2 to = Vector2Pool.obtain(x,y);
		//this.setDirection(Direction.getDirection(this.getX(), x, this.getY(), y));
		this.setDirection(Direction.getDirectionFromVectors(from, to));
		// Calculate the distance
		float dist = from.dst(to);
		// Calculate time needed depending on state
		float speed = (state == DinosaurState.WALKING) ? this.speed : this.speed * this.speedFactor;
		this.animationTime = dist / speed;
		this.setState(state);
		this.currentMoveModifier = new MoveModifier(this.animationTime, this.getX(), x, this.getY(), y);
		this.registerEntityModifier(this.currentMoveModifier);
		Vector2Pool.recycle(from);
		Vector2Pool.recycle(to);
	}
	
	private DinosaurState getRandomState() {
    	Random r = new Random();
		DinosaurState randomState = DinosaurState.values()[r.nextInt(7)];
    	while (randomState == DinosaurState.ATTACK || randomState == DinosaurState.BEEN_HIT || randomState == DinosaurState.TIPPING_OVER) {
    		randomState = DinosaurState.values()[r.nextInt(7)];
    	}
		return randomState;
	}
	
	
}
