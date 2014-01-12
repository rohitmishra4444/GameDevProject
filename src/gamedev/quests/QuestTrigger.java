package gamedev.quests;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import org.andengine.entity.primitive.Rectangle;

import android.widget.Toast;

public class QuestTrigger extends Rectangle {
	
	protected int questId;
	protected Quest quest;
	
	public QuestTrigger(int questId, float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight, ResourcesManager.getInstance().vbom);
		this.questId = questId;
		this.setVisible(false);
	}

	public void onManagedUpdate(float pSecondsElapsed) {
		if (this.collidesWith(ResourcesManager.getInstance().avatar)) {
			Quest quest = SceneManager.getInstance().getCurrentGameMapScene().getQuest(this.questId-1);
			if (!quest.isCompleted()) {
				if (!quest.isActive) {
					quest.setActive(true);
					ResourcesManager.getInstance().activity.toastOnUIThread(quest.getDescription(), Toast.LENGTH_SHORT);
				}
				// Toast quest.getStatus();
			} else {
				quest.onFinish();
				this.dispose();
				this.detachSelf();
			}
		}
	}
	
}
