package gamedev.objects;

import gamedev.game.Direction;
import gamedev.game.ResourcesManager;

import org.andengine.audio.sound.Sound;
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
	protected Inventory inventory = new Inventory();

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

		if (state == this.state
				&& (direction == -1 || direction == this.direction)) {
			return;
		}

		this.state = state;
		if (direction != -1)
			this.direction = direction;
		int rowIndex = 0;
		boolean loopAnimation = false;

		switch (state) {
		case IDLE:
			this.body.setLinearVelocity(0, 0);
			this.stopAnimation();
			resourcesManager.walk.stop();
			// TODO Give some amount of energy back after a certain level
			return;
		case ATTACK:
			rowIndex = 0;
			this.body.setLinearVelocity(0, 0);
			break;
		case BEEN_HIT:
			rowIndex = 4;
			this.body.setLinearVelocity(0, 0);
			break;
		case RUNNING:
			rowIndex = 8;
			loopAnimation = true;
			playSound(resourcesManager.walk, 1.2f, true);
			break;
		case TIPPING_OVER:
			rowIndex = 12;
			this.body.setLinearVelocity(0, 0);
			break;
		case WALKING:
			rowIndex = 16;
			loopAnimation = true;
			playSound(resourcesManager.walk, 1f, true);
			break;
		default:
			return;
		}

		int startTile = rowIndex * TILES_PER_LINE + this.direction
				* FRAMES_PER_ANIMATION;
		this.animate(ANIMATION_DURATION, startTile, startTile
				+ FRAMES_PER_ANIMATION - 1, loopAnimation);
	}

	private void playSound(Sound sound, float rate, boolean loop) {
		sound.stop();
		sound.setRate(rate);
		sound.setLooping(loop);
		sound.play();
	}

	public void attack(int damage) {
		super.attack(damage);
		this.resourcesManager.hud.setLife(this.life);
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
		if (energy > 100) {
			energy = 100;
		}
		this.energy = Math.max(energy, 0);
		this.resourcesManager.hud.setEnergy(this.energy);
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	protected void createPhysic() {
		this.body = PhysicsFactory.createBoxBody(resourcesManager.physicsWorld,
				this, BodyType.DynamicBody,
				PhysicsFactory.createFixtureDef(0, 0, 0));
		this.body.setUserData("Avatar");
		this.physicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.physicsHandler);
		this.resourcesManager.physicsWorld
				.registerPhysicsConnector(new PhysicsConnector(this, this.body,
						true, false) {
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
