package gamedev.quests;

public abstract class Quest {
	
	protected String title;
	protected String description;	
	protected boolean isCompleted = false;
	
	// TODO: Give rewards!
	
	
	public abstract void executeAction();
	
	public abstract boolean checkCondition();

	public boolean isCompleted() {
		return this.isCompleted;
	}

	
}
