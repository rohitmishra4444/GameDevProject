package gamedev.game;

import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import gamedev.objects.AnimatedObject;

public abstract class MoveStrategy {
	
	protected ResourcesManager resourcesManager;
	
	/** Holds the animated object */
	protected AnimatedObject object;
		
	
	/** Elapsed total time. Can be used inside the update method to sum up the time... */
	protected float time = 0;
	
	
	public MoveStrategy(AnimatedObject object) {
		this.resourcesManager = ResourcesManager.getInstance();
		this.object = object;
	}
	
	/**
	 * Update the strategy
	 * This method is called from the onManagedUpdate method from the objects
	 * @param float time Elapsed time passed from onManagedUpdate method
	 * @return boolean true if this strategy is still active, false otherwise
	 */
	abstract public boolean update(float time);
	
	/**
	 * Calculate the duration of a movement in seconds from pos1 to pos2 with a given velocity
	 * @param from
	 * @param to
	 * @param velocity
	 * @return
	 */
	public static float calculateDuration(Vector2 from, Vector2 to, Vector2 velocity) {
		return MathUtils.distance(from.x, from.y, to.x, to.y) / velocity.len();
	}
	
}
