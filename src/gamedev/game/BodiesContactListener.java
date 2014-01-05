package gamedev.game;

import gamedev.game.GameActivity.GameMode;
import gamedev.objects.Berry;
import gamedev.objects.BerryBush;
import gamedev.scenes.BaseScene;
import gamedev.objects.Dinosaur;
import gamedev.objects.AnimatedObject.GameState;
import gamedev.scenes.FightScene;

import android.widget.Toast;


import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;
import org.andengine.util.progress.IProgressListener;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class BodiesContactListener implements ContactListener,
		IProgressListener {

	protected ResourcesManager resourcesManager = ResourcesManager
			.getInstance();
	protected BaseScene currentMapScene;
	private Rectangle collectingBar;
	private BerryBush berryBush;

	// protected Avatar player = resourcesManager.avatar;

	@Override
	public void beginContact(Contact contact) {
		final Fixture x1 = contact.getFixtureA();
		final Fixture x2 = contact.getFixtureB();
		if (x1.getBody() == null || x2.getBody() == null || x1.getBody().getUserData() == null || x2.getBody().getUserData() == null)
			return;
		
		System.out.println("Contact between: " + x1.getBody().getUserData() + " and " + x2.getBody().getUserData());

		if (x1.getBody().getUserData() instanceof BerryBush) {
			berryBush = (BerryBush) x1.getBody().getUserData();
			addCollectingBarToBody(x1.getBody());
		} else if (x2.getBody().getUserData() instanceof BerryBush) {
			berryBush = (BerryBush) x2.getBody().getUserData();
			addCollectingBarToBody(x2.getBody());
		}
		
		if (x1.getBody().getUserData().equals("Avatar") && x2.getBody().getUserData() instanceof Dinosaur) {
			Dinosaur d = (Dinosaur) x2.getBody().getUserData();
			if (d.getState() == GameState.DEAD) return;
			showFightScene(d);
		} else if (x2.getBody().getUserData().equals("Avatar") && x1.getBody().getUserData() instanceof Dinosaur) {
			Dinosaur d = (Dinosaur) x1.getBody().getUserData();
			if (d.getState() == GameState.DEAD) return;
			showFightScene(d);			
		}
		
	}
	
	private void showFightScene(Dinosaur d) {
		GameActivity.mode = GameMode.FIGHTING;
		FightScene fight = new FightScene(d);
		this.resourcesManager.level.setChildScene(fight);
	}
	
	@Override
	public void endContact(Contact contact) {
		if (collectingBar != null) {
			currentMapScene.detachChild(collectingBar);
			collectingBar = null;
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

	private void addCollectingBarToBody(Body body) {
		collectingBar = new Rectangle(body.getPosition().x / 32 - 32,
				body.getPosition().y / 32, 100, 10, resourcesManager.vbom);
		collectingBar.setColor(Color.GREEN);
		// collectingBar.setAlpha(0.6f);
		// collectingBar.setScale(0.2f);
		collectingBar.setVisible(true);
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

		currentMapScene = SceneManager.getInstance().getCurrentGameMapScene();
		IEntity berryBushEntity = currentMapScene
				.getChildByMatcher(new IEntityMatcher() {
					@Override
					public boolean matches(IEntity pEntity) {
						if (pEntity.getUserData() != null
								&& pEntity.getUserData().equals(berryBush)) {
							return true;
						} else {
							return false;
						}
					}
				});
		berryBushEntity.attachChild(collectingBar);
	}

	private void addBerryToAvatarInventory() {
		if (berryBush != null) {
			Berry berry = berryBush.getBerry();
			if (berry != null) {
				resourcesManager.avatar.addBerryToInventory(berry);
				resourcesManager.activity.toastOnUIThread("Collected berries", Toast.LENGTH_SHORT);
			}
		}
	}
}
