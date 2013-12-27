package gamedev.game;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;

import gamedev.objects.AnimatedObject;
import gamedev.objects.AnimatedObject.GameState;

public class FollowPlayerStrategy extends MoveStrategy {
	
	/** Radius inside the object follows the player */
	protected float radius;
	
	public FollowPlayerStrategy(AnimatedObject object, float startTime, float radius) {
		super(object);
		this.radius = radius;
	}
	
	@Override
	public boolean update(float time) {
		Vector2 bodyPos = this.object.getBody().getPosition();
		Vector2 playerPos = this.resourcesManager.player.body.getPosition();
		float distance = MathUtils.distance(bodyPos.x, bodyPos.y, playerPos.x, playerPos.y);
		if (distance <= this.radius) {
			this.object.moveTo(playerPos, GameState.CHASE_PLAYER);
			return true;
		} else {
			this.object.setState(GameState.LOOKING, -1);
			return false;
		}
	}
		
}
