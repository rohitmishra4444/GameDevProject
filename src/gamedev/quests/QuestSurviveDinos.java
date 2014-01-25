package gamedev.quests;

import android.widget.Toast;
import gamedev.game.ResourcesManager;
import gamedev.objects.Box;
import gamedev.scenes.GameMapScene;

public class QuestSurviveDinos extends Quest {
	
	protected Box box1;
	protected Box box2;
	
	public QuestSurviveDinos(GameMapScene map) {
		super(map);
		this.title = "Survive and find something to make the portal work";
		this.description = "Damn, I need to go down here, allthought it looks dangerous...";
		this.box1 = new Box(77, 47) {
			public void setOpened(boolean opened) {
				super.setOpened(opened);
				if (opened && !this.opened) {
					ResourcesManager.getInstance().activity.toastOnUIThread("A key... could be one of the keys for the portal!", Toast.LENGTH_LONG);	
				}
			}
		};
		this.box2 = new Box(102, 47) {
			public void setOpened(boolean opened) {
				super.setOpened(opened);
				if (opened && !this.opened) {
					ResourcesManager.getInstance().activity.toastOnUIThread("A key... could be one of the keys for the portal!", Toast.LENGTH_LONG);	
				}
			}
		};
		map.attachChild(box1);
		map.attachChild(box2);
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
		return (box1.isOpened() && box2.isOpened());
	}
	
	public void setActive(boolean bool) {
		super.setActive(bool);
		ResourcesManager.getInstance().activity.toastOnUIThread("Damn those dinosaurs are big! But I need to go down here...", Toast.LENGTH_LONG);
		ResourcesManager.getInstance().activity.toastOnUIThread("I really should avoid fighting against those beasts...", Toast.LENGTH_LONG);
	}

}
