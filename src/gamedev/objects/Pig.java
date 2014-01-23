package gamedev.objects;

import gamedev.game.ResourcesManager;

public class Pig extends AnimatedObject {

	private final static int TILES_PER_LINE = 9;
	private final static long[] ANIMATION_DURATION = { 60, 60, 60, 60, 60, 60, 60, 60, 60 };
	private final static int FRAMES_PER_ANIMATION = ANIMATION_DURATION.length;
	
	protected boolean catched = false;
	
	public Pig(float x, float y) {
		super(x, y, ResourcesManager.getInstance().pigRegion);
		this.velocity = 5.7f;
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
		boolean loopAnimation = true;

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
					+ FRAMES_PER_ANIMATION - 1, loopAnimation);	
		}
		
	}

	@Override
	public boolean onCustomUpdate(float pSecondsElapsed) {
		if (!super.onCustomUpdate(pSecondsElapsed)) {
			return false;
		} else {
			if (this.moveStrategy != null) {
				this.moveStrategy.update(pSecondsElapsed);				
			}
			return true;
		}
	}

	public boolean isCatched() {
		return catched;
	}

	public void setCatched(boolean catched) {
		this.catched = catched;
	}

}
