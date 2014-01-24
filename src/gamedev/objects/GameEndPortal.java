package gamedev.objects;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.objects.AnimatedObject.GameState;
import gamedev.quests.Quest;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;

import android.widget.Toast;

public class GameEndPortal extends Sprite {

	private boolean gameEndPortalContact = false;
	private ResourcesManager res = ResourcesManager.getInstance();

	public GameEndPortal(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().gameEndPortalRegion,
				ResourcesManager.getInstance().vbom);
//		this.setAlpha(0.8f);
//		this.setScale(0.5f);
	}
	
	// I suggest to handle this logic with the last Quest. Since we must complete all quests to get to the last quest,
	// This is no longer necessary. If Quest 5 (Make portal work) is finished, the game is finished
	
//	@Override
//	protected void onManagedUpdate(float pSecondsElapsed) {
//		super.onManagedUpdate(pSecondsElapsed);
//		if (res.avatar.collidesWith(this) && gameEndPortalContact == false) {
//			gameEndPortalContact = true;
//
//			int numberOfFinishedQuests = 0;
//			ArrayList<Quest> quests = SceneManager.getInstance()
//					.getCurrentGameMapScene().getQuests();
//
//			for (Quest quest : quests) {
//				if (quest.isFinished()) {
//					numberOfFinishedQuests++;
//				}
//			}
//			if (numberOfFinishedQuests == quests.size()) {
//				SceneManager.getInstance().loadGameEndScene(res.engine);
//				res.avatar.setState(GameState.IDLE, -1);
//			} else {
//				res.activity
//						.toastOnUIThread(
//								"Hmm... the portal is not working. Did I complete all quests?",
//								Toast.LENGTH_SHORT);
//			}
//		} else if (!this.collidesWith(res.avatar)) {
//			gameEndPortalContact = false;
//		}
//	}
}
