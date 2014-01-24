package gamedev.quests;

import android.widget.Toast;
import gamedev.game.ResourcesManager;
import gamedev.scenes.GameMapScene;

public class QuestSurviveDinos extends Quest {
	
	
	public QuestSurviveDinos(GameMapScene map) {
		super(map);
		this.title = "Survive!";
		this.description = "Damn, I need to go down here, allthought it looks dangerous...";
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
		ResourcesManager.getInstance().activity.toastOnUIThread("Damn those dinosaurs are big!", Toast.LENGTH_SHORT);
		ResourcesManager.getInstance().activity.toastOnUIThread("Although it looks very dangerous to gow down here...", Toast.LENGTH_SHORT);
		ResourcesManager.getInstance().activity.toastOnUIThread("I must try it, maybe I find something useful!!", Toast.LENGTH_SHORT);
		ResourcesManager.getInstance().activity.toastOnUIThread("But I should avoid fighting against those beasts...", Toast.LENGTH_SHORT);
	}

}
