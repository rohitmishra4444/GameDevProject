package gamedev.ai;

import gamedev.objects.AnimatedObject;
import gamedev.objects.AnimatedObject.GameState;
import com.badlogic.gdx.math.Vector2;

public class SimpleMoveStrategy extends MoveStrategy {
	
	protected boolean reachedGoal = false;
	
	/** Time needed to walk to goal position */
	protected float duration = 0;
	
	/**
	 * Move to a position
	 * @param object
	 * @param moveTo
	 * @param state
	 */
	public SimpleMoveStrategy(AnimatedObject object, Vector2 moveTo, GameState state) {
		super(object);
		Vector2 bodyPos = this.object.getBody().getPosition();
		Vector2 moveTmp = null;
		if (this.coordinates == COORDINATES_PIXEL) {
			moveTmp = new Vector2(moveTo.x/32, moveTo.y/32);
		} else {
			moveTmp = moveTo;
		}
		this.object.moveTo(moveTmp, state);
		// Calculate the duration needed for movement
		this.duration = calculateDuration(bodyPos, moveTmp, this.object.getBody().getLinearVelocity());
//		System.out.println("Move from " + bodyPos + " to point " + moveTo + ", needed time is " + this.duration);
	}
	
	@Override
	public boolean update(float time) {
		if (reachedGoal) {
			return false;
		}
		this.time += time;
		if (this.time >= this.duration) {
			this.reachedGoal = true;
			// TODO Maybe make this state configurable?
			this.object.setState(GameState.LOOKING, -1);
			return false;
		}
		return true;
	}
	
}
