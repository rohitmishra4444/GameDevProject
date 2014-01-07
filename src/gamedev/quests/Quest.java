package gamedev.quests;

public abstract class Quest {
	
	protected String title;
	protected String description;	
	public static boolean isCompleted = false;
	protected boolean isActive = false;
	protected String status;
	
	// TODO: Give rewards!
	
	
	public abstract void onFinish();
		
	public abstract String getStatus();
	
	public abstract boolean isCompleted();

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
