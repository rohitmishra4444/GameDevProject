package gamedev.ai;

import java.util.ArrayList;

import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectProperty;

import gamedev.objects.AnimatedObject;
import gamedev.objects.AnimatedObject.GameState;

import com.badlogic.gdx.math.Vector2;

public class WaypointMoveStrategy extends MoveStrategy {
	
	/** The (temporary) simple Strategy used to walk from Point A to B */
	protected SimpleMoveStrategy currentMovement;
	
	/** Holds the points to walk, needs at least 2 Points! */
	protected ArrayList<Vector2> wayPoints = new ArrayList<Vector2>();
	
	/** Loop movement (go back to first point after arrived at last waypoint) */
	protected boolean loop = true;
	
	/** Index from the current starting Point */
	protected int currentIndex = 0;
	
	public WaypointMoveStrategy(AnimatedObject object, ArrayList<Vector2> wayPoints, boolean loop) {
		super(object);
		this.wayPoints = wayPoints;
		this.loop = loop;
		this.currentMovement = new SimpleMoveStrategy(object, this.wayPoints.get(0), GameState.WALKING);
	}
	
	@Override
	public boolean update(float time) {
		// Is the movement to the point x finished? If not, just return true... SimpleMoveStrategy is handling the rest
//		System.out.println("WaypointMoveStrategy: Seconds elapsed" + time);
		if (this.currentMovement.update(time)) {
			return true;
		} else {
			// Either go to next point or if at the end, check for looping and move back to first point...
			if  (this.wayPoints.size() == (currentIndex+1)) {
				if (this.loop) {
					this.currentIndex = 0;
					this.currentMovement = new SimpleMoveStrategy(this.object, this.wayPoints.get(0), GameState.WALKING);
//					System.out.println("Reached the last point, now go back to the starting point: " + this.wayPoints.get(0));
					return true;
				} else {
					this.object.setState(GameState.LOOKING, -1);
					return false;
				}
			} else {
				this.currentIndex++;
				this.currentMovement = new SimpleMoveStrategy(this.object, this.wayPoints.get(this.currentIndex), GameState.WALKING);
				return true;
			}
		}

	}
	
}
