package gamedev.ai;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gamedev.game.Direction;
import gamedev.objects.AnimatedObject;
import gamedev.objects.AnimatedObject.GameState;

public class FollowPlayerStrategy extends MoveStrategy {
	
	/** Radius inside the object follows the player */
	protected float radius;
	
	/** An alternative strategy that is executed when not following the player */
	protected MoveStrategy alternateStrategy;
	
	protected boolean stopWithAvatar = false;
	
	public FollowPlayerStrategy(AnimatedObject object, float radius) {
		super(object);
		this.radius = radius;
	}
	
	public FollowPlayerStrategy(AnimatedObject object, float radius, MoveStrategy alternativeStrategy) {
		super(object);
		this.radius = radius;
		this.alternateStrategy = alternativeStrategy;
	}
	
	public FollowPlayerStrategy(AnimatedObject object, float radius, boolean stopWithAvatar) {
		super(object);
		this.radius = radius;
		this.stopWithAvatar = stopWithAvatar;
	}
	
	@Override
	public boolean update(float time) {
		Vector2 playerPos = this.resourcesManager.avatar.getBody().getPosition();
		Vector2 bodyPos = this.object.getBody().getPosition();
		float distance = MathUtils.distance(bodyPos.x, bodyPos.y, playerPos.x, playerPos.y);
		if (distance <= this.radius) {
			if (this.stopWithAvatar) {
				Vector2 velocity = this.resourcesManager.avatar.getBody().getLinearVelocity();
				if (velocity.x == 0 && velocity.y == 0) {
					this.object.setState(GameState.IDLE, -1);
				} else {
					this.object.getBody().setLinearVelocity(velocity.x, velocity.y);
					this.object.setState(GameState.WALKING, this.resourcesManager.avatar.getDirection());
				}
				return true;
			} else {
				this.object.moveTo(playerPos, GameState.CHASE_PLAYER);
				return true;
			}
		} else {
			if (this.alternateStrategy == null) {
				this.object.setState(GameState.LOOKING, -1);
				return false;
			} else {
				return this.alternateStrategy.update(time);
			}
		}
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public MoveStrategy getAlternateStrategy() {
		return alternateStrategy;
	}

	public void setAlternateStrategy(MoveStrategy alternateStrategy) {
		this.alternateStrategy = alternateStrategy;
	}

	public boolean isStopWithAvatar() {
		return stopWithAvatar;
	}

	public void setStopWithAvatar(boolean stopWithAvatar) {
		this.stopWithAvatar = stopWithAvatar;
	}
	
	
		
}
