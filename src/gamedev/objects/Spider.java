package gamedev.objects;

import gamedev.ai.RandomMoveStrategy;
import gamedev.game.ResourcesManager;

public class Spider extends AnimatedObject {

	private final static int TILES_PER_LINE = 8;
	private final static long[] ANIMATION_DURATION = { 60, 60, 60, 60, 60, 60,60, 60 };
	private final static int FRAMES_PER_ANIMATION = ANIMATION_DURATION.length;

	public Spider(float x, float y) {
		super(x, y, ResourcesManager.getInstance().spiderRegion);
		this.velocity = 4f;
		this.factorRunning = 1.5f;
		this.getBody().setUserData(this);
		this.setCullingEnabled(true);
	}

	@Override
	public void setInitialState() {
		this.setState(GameState.IDLE, 0);
	}

	@Override
	public void setState(GameState state, int direction) {
		if (state == this.state
				&& (direction == -1 || direction == this.direction)) {
			return;
		}

		this.state = state;
		if (direction != -1)
			this.direction = direction;
		int rowIndex = 0;

		switch (state) {
		case IDLE:
			this.body.setLinearVelocity(0, 0);
			this.stopAnimation();
			break;
		default:
			break;
		}
		
		if (this.state != GameState.IDLE) {
			int startTile = rowIndex * TILES_PER_LINE + this.direction
					* FRAMES_PER_ANIMATION;
			this.animate(ANIMATION_DURATION, startTile, startTile
					+ FRAMES_PER_ANIMATION - 1, true);			
		}
	}

	@Override
	public boolean onCustomUpdate(float pSecondsElapsed) {
		if (!super.onCustomUpdate(pSecondsElapsed)) {
			return false;
		} else {
			if (this.moveStrategy != null) this.moveStrategy.update(pSecondsElapsed);
			return true;
		}
	}

}
