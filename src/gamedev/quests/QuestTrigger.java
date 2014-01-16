package gamedev.quests;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import org.andengine.entity.primitive.Rectangle;

import android.widget.Toast;

public class QuestTrigger extends Rectangle {

	protected int questId;
	private Quest quest;
	private boolean contact;

	public QuestTrigger(int questId, float pX, float pY, float pWidth,
			float pHeight) {
		super(pX, pY, pWidth, pHeight, ResourcesManager.getInstance().vbom);
		this.questId = questId;
		// this.setVisible(false);
		this.contact = false;
	}

	@Override
	public void onManagedUpdate(float pSecondsElapsed) {
		if (this.collidesWith(ResourcesManager.getInstance().avatar)
				&& this.contact == false) {
			this.contact = true;

			quest = SceneManager.getInstance().getCurrentGameMapScene()
					.getQuest(this.questId - 1);
			if (!quest.isCompleted()) {
				if (!quest.isActive) {
					quest.setActive(true);
					//Moving this to the quests setActive() method so we are more flexible what to print...
//					ResourcesManager.getInstance().activity.toastOnUIThread(
//							quest.getDescription(), Toast.LENGTH_SHORT);
				} else {
					if (quest.getStatus() != null) {
						ResourcesManager.getInstance().activity
								.toastOnUIThread(quest.getStatus(),
										Toast.LENGTH_SHORT);
					}
				}
			} else {
				quest.onFinish();
				this.setIgnoreUpdate(true);
				ResourcesManager.getInstance().removeSpriteAndBody(this);
				// TODO: Removing same object in on ManagedUpdate does not work,
				// we need to find a way to safely remove objects...
				// this.clearUpdateHandlers();
				// ResourcesManager.getInstance().removeSpriteFromScene(this);

				// DW: We don't need to remove the trigger, we can simply
				// setIgnoreUpdate to true. The objects can be detached in the
				// quest itself, where they were also attached.

//				final QuestTrigger self = this;
//
//				Runnable removeTrigger = new Runnable() {
//					@Override
//					public void run() {
//						SceneManager.getInstance().getCurrentGameMapScene()
//								.detachChild(self);
//					}
//				};
//				ResourcesManager.getInstance().physicsWorld
//						.postRunnable(removeTrigger);

			}
		} else if (!this.collidesWith(ResourcesManager.getInstance().avatar)) {
			this.contact = false;
		}
	}

}
