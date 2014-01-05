package gamedev.game;

import gamedev.game.GameActivity.GameMode;
import gamedev.objects.AnimatedObject.GameState;
import gamedev.objects.Berry;
import gamedev.objects.BerryBush;
import gamedev.objects.Dinosaur;
import gamedev.scenes.FightScene;
import gamedev.scenes.GameMapScene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;
import org.andengine.util.progress.IProgressListener;

import android.widget.Toast;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class BodiesContactListener implements ContactListener,
		IProgressListener {

	protected ResourcesManager resourcesManager = ResourcesManager
			.getInstance();
	protected GameMapScene currentMapScene;
	private Rectangle collectingBar;
	private BerryBush berryBush;
	// Duration of collecting a berry:
	private static final float COLLECTING_BAR_WIDTH = 50;

	@Override
	public void beginContact(Contact contact) {
		currentMapScene = SceneManager.getInstance().getCurrentGameMapScene();

		final Fixture x1 = contact.getFixtureA();
		final Fixture x2 = contact.getFixtureB();

		if (x1.getBody() == null || x2.getBody() == null
				|| x1.getBody().getUserData() == null
				|| x2.getBody().getUserData() == null)
			return;

		System.out.println("Contact between: " + x1.getBody().getUserData()
				+ " and " + x2.getBody().getUserData());

		if (x1.getBody().getUserData() instanceof BerryBush
				&& x2.getBody().getUserData().equals("Avatar")) {
			berryBush = (BerryBush) x1.getBody().getUserData();
			if (!berryBush.isEmpty()) {
				addCollectingBarToBerryBush(berryBush);
			} else {
				resourcesManager.activity.toastOnUIThread(
						"No berries ripened.", Toast.LENGTH_SHORT);
			}
		} else if (x1.getBody().getUserData().equals("Avatar")
				&& x2.getBody().getUserData() instanceof BerryBush) {
			berryBush = (BerryBush) x2.getBody().getUserData();
			if (!berryBush.isEmpty()) {
				addCollectingBarToBerryBush(berryBush);
			} else {
				resourcesManager.activity.toastOnUIThread(
						"No berries ripened.", Toast.LENGTH_SHORT);
			}
		}

		if (x1.getBody().getUserData().equals("Avatar")
				&& x2.getBody().getUserData() instanceof Dinosaur) {
			Dinosaur dino = (Dinosaur) x2.getBody().getUserData();
			if (dino.getState() == GameState.DEAD)
				return;
			showFightScene(dino);
		} else if (x1.getBody().getUserData() instanceof Dinosaur
				&& x2.getBody().getUserData().equals("Avatar")) {
			Dinosaur dino = (Dinosaur) x1.getBody().getUserData();
			if (dino.getState() == GameState.DEAD)
				return;
			showFightScene(dino);
		}

		if (x1.getBody().getUserData().equals("Avatar")
				&& x2.getBody().getUserData().equals("ShopCave")) {
			SceneManager.getInstance().loadGameShopScene(
					resourcesManager.engine);
		} else if (x1.getBody().getUserData().equals("ShopCave")
				&& x2.getBody().getUserData().equals("Avatar")) {
			SceneManager.getInstance().loadGameShopScene(
					resourcesManager.engine);
		}

	}

	private void showFightScene(Dinosaur d) {
		GameActivity.mode = GameMode.FIGHTING;
		FightScene fight = new FightScene(d);
		currentMapScene.setChildScene(fight);
	}

	@Override
	public void endContact(Contact contact) {
		// Remove collecting berry bar if player runs away.
		if (collectingBar != null) {
			currentMapScene.detachChild(collectingBar);
			collectingBar = null;
			berryBush = null;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProgressChanged(int pProgress) {
		if (collectingBar == null) {
			return;
		}

		if (collectingBar.getWidth() == 0) {
			addBerryToAvatarInventory();
			currentMapScene.detachChild(collectingBar);
			collectingBar = null;
		} else {
			collectingBar.setWidth(collectingBar.getWidth() - 1);
		}
	}

	private void addCollectingBarToBerryBush(BerryBush berryBush) {
		collectingBar = new Rectangle(berryBush.getX(), berryBush.getY() - 5,
				COLLECTING_BAR_WIDTH, 10, resourcesManager.vbom);
		collectingBar.setColor(Color.GREEN);
		collectingBar.setAlpha(0.5f);

		collectingBar.registerUpdateHandler(new TimerHandler(0.05f, true,
				new ITimerCallback() {
					// Starts a timer for updating out progress
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						float timerSeconds = pTimerHandler
								.getTimerSecondsElapsed();
						if (timerSeconds > 100)
							// ProgressListeners require the number to be within
							// 0 and 100.
							return;
						else
							onProgressChanged((int) timerSeconds);
					}
				}));
		currentMapScene.attachChild(collectingBar);
	}

	private void addBerryToAvatarInventory() {
		if (berryBush != null) {
			Berry berry = berryBush.getBerry();
			if (berry != null) {
				resourcesManager.avatar.addBerryToInventory(berry);
				resourcesManager.activity.toastOnUIThread("Berry collected.",
						Toast.LENGTH_SHORT);
			}
		}
	}
}
