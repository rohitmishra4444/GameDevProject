package gamedev.quests;

import android.widget.Toast;
import gamedev.game.ResourcesManager;
import gamedev.scenes.GameMapScene;

public class QuestMakePortalWork extends Quest {
	
	public QuestMakePortalWork(GameMapScene map) {
		super(map);
		this.title = "Make the portal work";
		this.description = "I need to find some information how to make the portal work...";
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
	
	public void setActive(boolean bool) {
		super.setActive(bool);
		ResourcesManager.getInstance().activity.toastOnUIThread("There it is... the portal! But it's not working...", Toast.LENGTH_SHORT);
		ResourcesManager.getInstance().activity.toastOnUIThread("But it's not working...", Toast.LENGTH_SHORT);
		ResourcesManager.getInstance().activity.toastOnUIThread("I should look around, maybe I find something?", Toast.LENGTH_SHORT);
	}
	
}
