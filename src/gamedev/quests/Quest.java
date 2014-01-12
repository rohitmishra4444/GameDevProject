package gamedev.quests;

import org.andengine.entity.scene.Scene;

public abstract class Quest {
	
	protected String title;
	protected String description;	
	public static boolean isCompleted = false;
	protected boolean isActive = false;
	protected String status;
	protected Scene map;
	// TODO: Give rewards!
	
	public Quest(Scene map) {
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
