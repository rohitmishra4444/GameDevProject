package gamedev.quests;

import gamedev.scenes.GameMapScene;

public abstract class Quest {

	protected String title;
	protected String description;
	public static boolean isCompleted = false;
	protected boolean isActive = false;
	protected String status;
	protected GameMapScene map;

	// TODO: Give rewards!

	public Quest(GameMapScene map) {
		this.map = map;
	}

	public abstract void onFinish();

	public abstract String getStatus();

	public abstract boolean isCompleted();

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return this.description;
	}

}
