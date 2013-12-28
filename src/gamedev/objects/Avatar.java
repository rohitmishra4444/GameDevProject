package gamedev.objects;

import gamedev.game.Direction;
import gamedev.game.ResourcesManager;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Avatar extends AnimatedObject {

	public final static long[] ANIMATION_DURATION = { 50, 50, 50, 50, 50, 50,
			50, 50 };
	public final static int FRAMES_PER_ANIMATION = ANIMATION_DURATION.length;
	public final static int TILES_PER_LINE = 16;

	protected int energy = 100;

	public Avatar(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().playerRegion);
		this.resourcesManager.camera.setChaseEntity(this);
		this.velocity = 4f;
		this.factorRunning = 1.5f;
	}

	public Avatar() {
		super(0, 0, ResourcesManager.getInstance().playerRegion);
		this.resourcesManager.camera.setChaseEntity(this);
		this.velocity = 4f;
		this.factorRunning = 1.5f;
	}

	public void setState(GameState state, int direction) {
		
		// TODO REFACTOR!
		boolean stateHasChanged = false;
		if (this.state != state && !this.isAnimationRunning()) {
			this.state = state;
			stateHasChanged = true;
		}

		// Stop animation if state is idle.
		if (state == GameState.IDLE) {
			this.body.setLinearVelocity(0, 0);
			this.stopAnimation();
			this.setEnergy(this.energy + 1);
			return;
		}

		// Check if direction has changed.
		boolean directionHasChanged = false;
		if (direction > -1 && this.direction != direction) {
			this.direction = direction;
			directionHasChanged = true;
		}

		// Change animation if no animation is running or if the direction has
		// changed.
		if (!this.isAnimationRunning() || directionHasChanged
				|| stateHasChanged) {
			int rowIndex = 0;
			if (state == GameState.ATTACK)
				rowIndex = 0;
			if (state == GameState.BEEN_HIT)
				rowIndex = 4;
			if (state == GameState.RUNNING)
				rowIndex = 8;
			if (state == GameState.TIPPING_OVER)
				rowIndex = 12;
			if (state == GameState.WALKING)
				rowIndex = 16;

			// Do not loop animation since it will be looped automatically when
			// the animation is over.
			boolean loopAnimation = false;

			int startTile = rowIndex * TILES_PER_LINE + this.direction
					* FRAMES_PER_ANIMATION;
			this.animate(ANIMATION_DURATION, startTile, startTile
					+ FRAMES_PER_ANIMATION - 1, loopAnimation);
		}
	}


	/**
	 * Set the velocity direction. Speed is calculated based on state
	 * 
	 * @param pX
	 * @param pY
	 * @param state
	 *            WALKING|RUNNING
	 */
	public void setVelocity(float pX, float pY, GameState state) {
		// Compute direction
		float degree = MathUtils.radToDeg((float) Math.atan2(pX, pY));
		int direction = Direction.getDirectionFromDegree(degree);
		// Check if enough energy, otherwise we reset to WALKIING
		if (state == GameState.RUNNING && this.energy == 0)
			state = GameState.WALKING;
		if (state == GameState.WALKING) {
			this.body.setLinearVelocity(pX * this.velocity, pY * this.velocity);
		} else {
			this.body.setLinearVelocity(
					pX * this.velocity * this.factorRunning, pY * this.velocity
							* this.factorRunning);
			this.setEnergy(this.energy - 1); // TODO Move to constant / variable
		}
		this.setState(state, direction);
	}

	@Override
	public void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	/**
	 * Getters & Setters
	 */

	public void setLife(int life) {
		super.setLife(life);
		this.resourcesManager.hud.setLife(this.life);
		// TODO Game over when life == 0
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		if (energy > 100) energy = 100;
		this.energy = Math.max(energy, 0);
		this.resourcesManager.hud.setEnergy(this.energy);
	}
	
	@Override
	protected void createPhysic() {
		this.body = PhysicsFactory.createBoxBody(resourcesManager.physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		this.physicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.physicsHandler);
		this.resourcesManager.physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, this.body, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				resourcesManager.camera.updateChaseEntity();
			}
		});
	}

	@Override
	public void setInitialState() {
		this.setState(GameState.IDLE, Direction.SOUTH);
	}

}
