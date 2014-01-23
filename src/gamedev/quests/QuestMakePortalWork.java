package gamedev.quests;

import gamedev.scenes.GameMapScene;

public class QuestMakePortalWork extends Quest {
	
	
	public QuestMakePortalWork(GameMapScene map) {
		super(map);
		this.title = "Make the portal work";
		this.description = "I need to find some information how to repair the portal...";
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

}
