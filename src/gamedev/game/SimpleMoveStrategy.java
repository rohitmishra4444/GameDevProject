package gamedev.game;

import org.andengine.util.math.MathUtils;
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
		this.object.moveTo(moveTo, state);
		// Calculate the duration needed for movement
		this.duration = MathUtils.distance(bodyPos.x, bodyPos.y, moveTo.x, moveTo.y) / this.object.getBody().getLinearVelocity().len();
	}
	
	@Override
	public boolean update(float time) {
		if (reachedGoal) {
			return false;
		}
		this.time += time;
		if (this.time > this.duration) {
			this.reachedGoal = true;
			// TODO Maybe make this state configurable?
			this.object.setState(GameState.LOOKING, -1);
			return false;
		}
		return true;
	}
	
}
