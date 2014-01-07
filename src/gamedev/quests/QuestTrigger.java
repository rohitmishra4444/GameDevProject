package gamedev.quests;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import org.andengine.entity.primitive.Rectangle;

public class QuestTrigger extends Rectangle {
	
	protected int questId;
	protected Quest quest;
	
	public QuestTrigger(int questId, float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight, ResourcesManager.getInstance().vbom);
		this.questId = questId;
	}

	public void onManagedUpdate(float pSecondsElapsed) {
		if (this.collidesWith(ResourcesManager.getInstance().avatar)) {
			Quest quest = SceneManager.getInstance().getCurrentGameMapScene().getQuest(this.questId-1);
			if (!quest.isCompleted()) {
				if (!quest.isActive) {
					quest.setActive(true);
				}
				// Toast quest.getStatus();
			} else {
				quest.onFinish();
				quest.isCompleted = true;
				this.dispose();
				this.detachSelf();
			}
		}
	}
	
}
