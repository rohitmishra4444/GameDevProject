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
				} else {
					if (quest.getStatus() != null) {
						ResourcesManager.getInstance().activity.toastOnUIThread(quest.getStatus(), Toast.LENGTH_SHORT);						
					}
				}
			} else {
				quest.onFinish();
				// TODO: Removing same object in on ManagedUpdate does not work, we need to find a way to safely remove objects...
//				this.setIgnoreUpdate(true);
//				this.clearUpdateHandlers();
//				ResourcesManager.getInstance().removeSpriteFromScene(this);
			}
		}
	}
	
}
