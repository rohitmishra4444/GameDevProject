package gamedev.objects;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.shape.IShape;
import org.andengine.extension.debugdraw.primitives.Ellipse;
import org.andengine.util.color.Color;

import gamedev.ai.FollowPlayerStrategy;
import gamedev.ai.RandomMoveStrategy;
import gamedev.game.ResourcesManager;

public class Dinosaur extends AnimatedObject {

	public final static int COLOR_GREEN = 0;
	public final static int COLOR_RED = 1;

	// TODO Durations can be different per animation. E.G. we will remove
	// RUNNING and just play the same animation as WALKING but faster...
	// Handle this inside the setState method!
	public final static long[] ANIMATION_DURATION = { 120, 120, 120, 120, 120,
			120, 120, 120, 120, 120, 120, 120, 120 };
	public final static int FRAMES_PER_ANIMATION = 13;
	public final static int TILES_PER_LINE = 26;

	/** Green or Red */
	protected int color;

	/** Radius inside the Dinosaur attacks/follows the player */
	protected float radius;
		
	public Dinosaur(float x, float y, int color) {
		// TODO Make dinosaurRegion an array holding green on pos 0, red on pos
		// 1
		super(x, y, ResourcesManager.getInstance().dinosaurGreenRegion);
		if (color == COLOR_GREEN) {
			this.velocity = 2f;
			this.factorRunning = 2f;
			this.radius = 5f;
		} else if (color == COLOR_RED) {
			this.velocity = 3f;
			this.factorRunning = 2f;
			this.radius = 10f;
		}
		this.moveStrategy = new FollowPlayerStrategy(this, this.radius, new RandomMoveStrategy(this, 4, 10, 5));
		this.getBody().setUserData(this);
		Ellipse e = new Ellipse(x/32, y/32, this.radius*32, this.radius*32, this.resourcesManager.vbom);
		e.setColor(Color.RED);
		e.setDrawMode(DrawMode.TRIANGLE_FAN);
		e.setAlpha(0.1f);
		this.attachChild(e);
	}

	@Override
	public void setInitialState() {
		this.setState(GameState.LOOKING, -1);
	}

	@Override
	public void setState(GameState state, int direction) {
		if (state == this.state
				&& (direction == -1 || direction == this.direction)) {
			return;
		}

		this.state = state;
		if (direction != -1)
			this.direction = direction;
		int rowIndex = 0;
		boolean loopAnimation = true;

		switch (state) {
		case WALKING:
			rowIndex = 28;
			break;
		case TIPPING_OVER:
		case DEAD:
			rowIndex = 24;
			this.body.setLinearVelocity(0, 0);
			loopAnimation = false;
			this.detachChildren();
			break;
		case RUNNING:
		case CHASE_PLAYER:
			rowIndex = 20;
			break;
		case ROARING:
			rowIndex = 16;
			this.body.setLinearVelocity(0, 0);
			break;
		case PAUSED:
			rowIndex = 12;
			this.body.setLinearVelocity(0, 0);
			break;
		case LOOKING:
			rowIndex = 8;
			this.body.setLinearVelocity(0, 0);
			break;
		case BEEN_HIT:
			rowIndex = 4;
			this.body.setLinearVelocity(0, 0);
			loopAnimation = false;
			break;
		case ATTACK:
			rowIndex = 0;
			this.body.setLinearVelocity(0, 0);
			break;
		default:
			break;
		}

		int startTile = rowIndex * TILES_PER_LINE + this.direction
				* FRAMES_PER_ANIMATION;
		this.animate(ANIMATION_DURATION, startTile, startTile
				+ FRAMES_PER_ANIMATION - 1, loopAnimation);
	}

//	@Override
//	public void onManagedUpdate(float pSecondsElapsed) {
//		super.onManagedUpdate(pSecondsElapsed);
//		this.moveStrategy.update(pSecondsElapsed);
//	}
	
	@Override
	public boolean onCustomUpdate(float pSecondsElapsed) {
		if (!super.onCustomUpdate(pSecondsElapsed)) {
			return false;
		} else {
			this.moveStrategy.update(pSecondsElapsed);
			return true;
		}
	}
	
	/**
	 *	Getters & Setters 
	 */
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	
	// /**
	// * Get a random state. Only state at index 0 - 4 are valid :)
	// *
	// * @return
	// */
	// private DinosaurState getRandomState() {
	// Random r = new Random();
	// DinosaurState randomState = DinosaurState.values()[r.nextInt(5)];
	// return randomState;
	// }

}
