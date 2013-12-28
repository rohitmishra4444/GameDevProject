package gamedev.game;

import org.andengine.util.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import gamedev.objects.AnimatedObject;
import gamedev.objects.AnimatedObject.GameState;

public class FollowPlayerStrategy extends MoveStrategy {
	
	/** Radius inside the object follows the player */
	protected float radius;
	
	/** An alternative strategy that is executed when not following the player */
	protected MoveStrategy alternateStrategy;
	
	public FollowPlayerStrategy(AnimatedObject object, float radius) {
		super(object);
		this.radius = radius;
	}
	
	public FollowPlayerStrategy(AnimatedObject object, float radius, MoveStrategy alternativeStrategy) {
		super(object);
		this.radius = radius;
		this.alternateStrategy = alternativeStrategy;
	}
	
	
	@Override
	public boolean update(float time) {
		Vector2 bodyPos = this.object.getBody().getPosition();
		Vector2 playerPos = this.resourcesManager.player.getBody().getPosition();
		float distance = MathUtils.distance(bodyPos.x, bodyPos.y, playerPos.x, playerPos.y);
		if (distance <= this.radius) {
			this.object.moveTo(playerPos, GameState.CHASE_PLAYER);
			return true;
		} else {
			if (this.alternateStrategy == null) {
				this.object.setState(GameState.LOOKING, -1);
				return false;
			} else {
				return this.alternateStrategy.update(time); 
			}
		}
	}
		
}
