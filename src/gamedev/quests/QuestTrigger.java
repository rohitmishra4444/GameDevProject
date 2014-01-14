package gamedev.quests;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import org.andengine.entity.primitive.Rectangle;

import android.widget.Toast;

public class QuestTrigger extends Rectangle {

	protected int questId;
	protected Quest quest;
	private boolean contact;

	public QuestTrigger(int questId, float pX, float pY, float pWidth,
			float pHeight) {
		super(pX, pY, pWidth, pHeight, ResourcesManager.getInstance().vbom);
		this.questId = questId;
		this.setVisible(false);
		this.contact = false;
	}

	@Override
	public void onManagedUpdate(float pSecondsElapsed) {
		if (this.collidesWith(ResourcesManager.getInstance().avatar)
				&& this.contact == false) {
			this.contact = true;

			// This method is called and executed multiple times when the avatar
			// collides, so we need a boolean to handle this. Like this it is
			// guaranteed that the following code is called only once per
			// contact (see println in console).
			System.out.println("Avatar collided with trigger.");

			Quest quest = SceneManager.getInstance().getCurrentGameMapScene()
					.getQuest(this.questId - 1);
			if (!quest.isCompleted()) {
				if (!quest.isActive) {
					quest.setActive(true);
					ResourcesManager.getInstance().activity.toastOnUIThread(
							quest.getDescription(), Toast.LENGTH_SHORT);
				} else {
					if (quest.getStatus() != null) {
						ResourcesManager.getInstance().activity
								.toastOnUIThread(quest.getStatus(),
										Toast.LENGTH_SHORT);
					}
				}
			} else {
				this.setIgnoreUpdate(true);
				quest.onFinish();
				// TODO: Removing same object in on ManagedUpdate does not work,
				// we need to find a way to safely remove objects...
				// this.clearUpdateHandlers();
				// ResourcesManager.getInstance().removeSpriteFromScene(this);
			}
		} else if (!this.collidesWith(ResourcesManager.getInstance().avatar)) {
			this.contact = false;
		}
	}

}
