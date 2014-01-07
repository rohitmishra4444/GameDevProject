package gamedev.quests;

public class QuestBuildBridge extends Quest {
	
	public QuestBuildBridge() {
		this.title = "Cross the River";
		this.description = "I need to find something so I can go to the other side of the river!";
	}

	
	@Override
	public void executeAction() {
		// Here we could add the action to place the wood over the bridge and remove the body, so the player can pass
		// The action is triggered on a separate object layer.
		// If the player tries to walk over the bridge, isCompleted() is still false. Therefore we will display the quest messages
		// If the player has found the wood, isCompleted()
		if (checkCondition()) {
			// Do stuff...
			
			
			
			
			this.isCompleted = true;
		}
	}

	@Override
	public boolean checkCondition() {
		// Check if wood is in inventory
		return false;
	}

}
