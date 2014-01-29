package gamedev.ai;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import gamedev.objects.AnimatedObject;
import gamedev.objects.AnimatedObject.GameState;
import com.badlogic.gdx.math.Vector2;

public class SimpleMoveStrategy extends MoveStrategy {
	
	protected boolean reachedGoal = false;
	
	/** Time needed to walk to goal position */
	protected float duration = 0;
	
	protected Vector2 moveTo;
	
	protected GameState finishedState = GameState.IDLE;
	protected GameState moveState = GameState.WALKING;
	
	/**
	 * Move to a position
	 * @param object
	 * @param moveTo
	 * @param finishedState
	 */
	public SimpleMoveStrategy(AnimatedObject object, Vector2 moveTo, GameState moveState, GameState finishedState) {
		super(object);
		this.finishedState = finishedState;
		this.moveState = moveState;
		this.moveTo = moveTo;
		init(this.moveTo, moveState);
	}

	public SimpleMoveStrategy(AnimatedObject object, Vector2 moveTo, GameState moveState) {
		super(object);
		this.moveTo = moveTo;
		this.moveState = moveState;
		init(this.moveTo, moveState);
	}
	
	protected void init(Vector2 moveTo, GameState moveState) {
		Vector2 bodyPos = this.object.getBody().getPosition();
		Vector2 moveTmp = null;
		if (this.coordinates == COORDINATES_PIXEL) {
			moveTmp = Vector2Pool.obtain(moveTo.x/32, moveTo.y/32);
		} else {
			moveTmp = moveTo;
		}
		this.object.moveTo(moveTmp, moveState);
		// Calculate the duration needed for movement
		this.duration = calculateDuration(bodyPos, moveTmp, this.object.getBody().getLinearVelocity());
		Vector2Pool.recycle(moveTmp);
//		System.out.println("Move from " + bodyPos + " to point " + moveTo + ", needed time is " + this.duration);		
	}
	
	@Override
	public boolean update(float time) {
		if (reachedGoal) {
			return false;
		}
		// If the objects state is IDLE and we did not reached our goal, this means
		// that there was opened a popup- or fight scene. We need to recalculate stuff
		// because the velocity of body is zero
		if (this.object.getState() == GameState.IDLE) {
			this.time = 0;
			this.init(this.moveTo, this.moveState);
		}
		this.time += time;
		if (this.time >= this.duration) {
			this.reachedGoal = true;
			this.object.setState(this.finishedState, -1);
			return false;
		}
		return true;
	}
	
}
