package gamedev.objects;

import gamedev.ai.FollowPlayerStrategy;
import gamedev.game.ResourcesManager;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.extension.debugdraw.primitives.Ellipse;
import org.andengine.util.color.Color;

public class Dinosaur extends AnimatedObject {

	public final static int COLOR_GREEN = 0;
	public final static int COLOR_RED = 1;

	// TODO Durations can be different per animation. E.G. we will remove
	// RUNNING and just play the same animation as WALKING but faster...
	// Handle this inside the setState method!
	public final static long[] ANIMATION_DURATION = { 120, 120, 120, 120, 120,
			120, 120, 120 };
	public final static int FRAMES_PER_ANIMATION = ANIMATION_DURATION.length;
	public final static int TILES_PER_LINE = 16;

	/** Time in seconds to remove the update-handler after the dino is killed... */
	private final static int REMOVE_UPDATEHANDLER_SECONDS = 4;
	protected float timeDeath = 0;

	/** Green or Red */
	protected int color;

	/** Radius inside the Dinosaur attacks/follows the player */
	protected float radius;

	public Dinosaur(float x, float y, int color) {
		// TODO Make dinosaurRegion an array holding green on pos 0, red on pos
		// 1
		super(x, y, ResourcesManager.getInstance().dinosaurRegion[color]);
		if (color == COLOR_GREEN) {
			this.velocity = 2f;
			this.factorRunning = 2f;
			this.radius = 5f;
		} else if (color == COLOR_RED) {
			this.velocity = 3f;
			this.factorRunning = 2f;
			this.radius = 10f;
		}
		this.moveStrategy = new FollowPlayerStrategy(this, this.radius);
		this.getBody().setUserData(this);
		Ellipse e = new Ellipse(x / 32, y / 32, this.radius * 32,
				this.radius * 32, this.resourcesManager.vbom);
		e.setColor(Color.RED);
		e.setDrawMode(DrawMode.TRIANGLE_FAN);
		e.setAlpha(0.1f);
		// this.setCullingEnabled(true);
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
			rowIndex = 12;
			break;
		case TIPPING_OVER:
		case DEAD:
			rowIndex = 8;
			this.body.setLinearVelocity(0, 0);
			loopAnimation = false;
			this.detachChildren();
			this.timeDeath = this.timeAlive;
			break;
		case RUNNING:
		case CHASE_PLAYER:
			rowIndex = 4;
			break;
		// case ROARING:
		// rowIndex = 16;
		// this.body.setLinearVelocity(0, 0);
		// break;
		// case PAUSED:
		// rowIndex = 12;
		// this.body.setLinearVelocity(0, 0);
		// break;
		case LOOKING:
			rowIndex = 0;
			this.body.setLinearVelocity(0, 0);
			break;
		// case BEEN_HIT:
		// rowIndex = 4;
		// this.body.setLinearVelocity(0, 0);
		// loopAnimation = false;
		// break;
		// case ATTACK:
		// rowIndex = 0;
		// this.body.setLinearVelocity(0, 0);
		// break;
		default:
			break;
		}

		int startTile = rowIndex * TILES_PER_LINE + this.direction
				* FRAMES_PER_ANIMATION;
		this.animate(ANIMATION_DURATION, startTile, startTile
				+ FRAMES_PER_ANIMATION - 1, loopAnimation);
	}

	@Override
	public boolean onCustomUpdate(float pSecondsElapsed) {
		if (!super.onCustomUpdate(pSecondsElapsed)) {
			if (this.state == GameState.DEAD
					&& (this.timeAlive - this.timeDeath) > REMOVE_UPDATEHANDLER_SECONDS) {
				// System.out.println("Cleared Update Handlers for Dinosaur");
				this.setIgnoreUpdate(true);
				this.clearUpdateHandlers();
			}
			return false;
		} else {
			this.moveStrategy.update(pSecondsElapsed);
			return true;
		}
	}

	/**
	 * Getters & Setters
	 */

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getDinoColor() {
		return this.color;
	}

}
