package gamedev.objects;

import java.util.Random;

import gamedev.game.Direction;
import gamedev.game.ResourcesManager;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;

import com.badlogic.gdx.physics.box2d.Body;

public class Dinosaur extends AnimatedSprite {
	
	public final static long[] ANIMATION_DURATION = { 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
	public final static int FRAMES_PER_ANIMATION = 13;
	public final static int TILES_PER_LINE = 26;
	
	//TODO create body and connect to physicsworld
	public Body body;
	public PhysicsHandler physicsHandler;

	protected ResourcesManager resourcesManager;
	protected DinosaurState currentState;
	protected float animationElapsedTime = 0;
	protected float animationTime = 10;
	
	protected int direction = Direction.WEST;
	protected int life = 100;
	//TODO Add velocity as attribute
	
	public enum DinosaurState {
		WALKING,
		RUNNING,
		BEEN_HIT,
		TIPPING_OVER,
		ATTACK,
		ROARING,
		PAUSED,
		LOOKING,
	}
	
	public Dinosaur(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().dinosaurGreenRegion, ResourcesManager.getInstance().vbom);
		this.resourcesManager = ResourcesManager.getInstance();
		this.setState(DinosaurState.PAUSED);
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
		if (state == DinosaurState.RUNNING) rowIndex = 8;
		if (state == DinosaurState.ROARING) rowIndex = 12;
		if (state == DinosaurState.PAUSED) rowIndex = 16;
		if (state == DinosaurState.LOOKING) rowIndex = 20;
		if (state == DinosaurState.BEEN_HIT) rowIndex = 24;
		if (state == DinosaurState.ATTACK) rowIndex = 28;			
		int startTile = rowIndex*TILES_PER_LINE + this.direction*FRAMES_PER_ANIMATION;
		boolean animate = (state == DinosaurState.TIPPING_OVER || state == DinosaurState.BEEN_HIT) ? false : true;
		this.animate(ANIMATION_DURATION, startTile, startTile+12, animate);			

	}
	
	@Override
    protected void onManagedUpdate(float pSecondsElapsed) {
            super.onManagedUpdate(pSecondsElapsed);
            
            // Set a random state after a random time. If the state is walking or running, set a random position where the dino walks.
            // TODO This must be connected to the physics and not the sprite. Also when running, velocity must be faster
            this.animationElapsedTime += pSecondsElapsed;
            if (this.animationElapsedTime > this.animationTime) {
            	this.animationElapsedTime = 0;
            	Random r = new Random();
            	// Set a random animation time [10...20] for the next animation seconds
            	this.animationTime = 10 + (r.nextFloat() * 10 + 1);
            	// Pick a random state, exclude some states
            	DinosaurState randomState = DinosaurState.values()[r.nextInt(7)];
            	while (randomState == DinosaurState.ATTACK || randomState == DinosaurState.BEEN_HIT || randomState == DinosaurState.TIPPING_OVER) {
            		randomState = DinosaurState.values()[r.nextInt(7)];
            	}
            	// If the state is walking, calculate a new random position
            	if (randomState == DinosaurState.WALKING || randomState == DinosaurState.RUNNING) {
            		// The new Position should be in Range [-250...250] from the current position
            		float rX = this.getX() + (-250 + (r.nextFloat() * 500 + 1));
            		float rY = this.getY() + (-250 + (r.nextFloat() * 500 + 1));            		
            		int direction = Direction.getDirection(this.getX(), rX, this.getY(), rY);
            		this.setDirection(direction);
            		//TODO Calculate the animation time based on current velocity and distance to go... ;)
            		this.registerEntityModifier(new MoveModifier(this.animationTime, this.getX(), rX, this.getY(), rY));
            	}
            	this.setState(randomState);
            }
    }
	
	
}
