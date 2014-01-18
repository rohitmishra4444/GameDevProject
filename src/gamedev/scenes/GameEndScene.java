package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;

public class GameEndScene extends BaseScene {

	private Sprite gameEndSprite;

	private Text gameEndText;
	private Text gameDevelopersText;

	@Override
	public void createScene() {
		float centerX = camera.getCenterX()
				- resourcesManager.game_end_region.getWidth() / 2;
		float centerY = camera.getCenterY()
				- resourcesManager.game_end_region.getHeight() / 2;

		String gameEndString = "Congratulations! "
				+ "\n"
				+ "The portal was working properly and you returned to your era."
				+ "\n\n\n" + "Tap...";
		gameEndText = new Text(0, 0, resourcesManager.font, gameEndString,
				gameEndString.length(), vbom);
		gameEndText.setPosition(camera.getCenterX() - gameEndText.getWidth()
				/ 2, camera.getCenterY() - gameEndText.getHeight() / 2);
		gameEndText.setHorizontalAlign(HorizontalAlign.CENTER);
		this.registerTouchArea(gameEndText);
		attachChild(gameEndText);

		gameEndSprite = new Sprite(centerX, centerY,
				resourcesManager.game_end_region.getWidth(),
				resourcesManager.game_end_region.getHeight(),
				resourcesManager.game_end_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		String gameDevelopersString = "This game was designed and developed by"
				+ "\n" + "Stefan Wanzenried" + "\n" + "David Wettstein"
				+ "\n\n\n" + "Tap...";
		gameDevelopersText = new Text(0, 0, resourcesManager.font,
				gameDevelopersString, gameDevelopersString.length(), vbom);
		gameDevelopersText.setPosition(
				camera.getCenterX() - gameDevelopersText.getWidth() / 2,
				camera.getCenterY() - gameDevelopersText.getHeight() / 2);
		gameDevelopersText.setHorizontalAlign(HorizontalAlign.CENTER);

		registerAreaTouchListener();
	}

	private void registerAreaTouchListener() {
		this.setOnAreaTouchListener(new IOnAreaTouchListener() {
			long lastTouchTime = 0;
			// Wait time in ms. Needed for the touch input, otherwise it is
			// proceeding to fast.
			final long WAIT_TIME = 800;

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					ITouchArea pTouchArea, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					long touchTime = pSceneTouchEvent.getMotionEvent()
							.getEventTime();

					if (touchTime > lastTouchTime + WAIT_TIME
							&& pTouchArea.equals(gameEndText)) {
						gameEndText.detachSelf();
						unregisterTouchArea(gameEndText);

						attachChild(gameEndSprite);
						gameEndSprite
								.registerEntityModifier(new FadeInModifier(1f));
						registerTouchArea(gameEndSprite);

						lastTouchTime = touchTime;
					}

					if (touchTime > lastTouchTime + WAIT_TIME
							&& pTouchArea.equals(gameEndSprite)) {
						gameEndSprite.detachSelf();
						unregisterTouchArea(gameEndSprite);

						attachChild(gameDevelopersText);
						gameDevelopersText
								.registerEntityModifier(new FadeInModifier(1f));
						registerTouchArea(gameDevelopersText);

						lastTouchTime = touchTime;
					}

					if (touchTime > lastTouchTime + WAIT_TIME
							&& pTouchArea.equals(gameDevelopersText)) {

						gameDevelopersText.detachSelf();
						unregisterTouchArea(gameDevelopersText);

						SceneManager.getInstance().loadMenuScene(engine);
						SceneManager.getInstance().deleteCurrentGameMapScene();
						resourcesManager.avatar = null;

						lastTouchTime = touchTime;
					}
				}
				return false;
			}
		});
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME_END;
	}

	@Override
	public void disposeScene() {
		if (!gameEndText.isDisposed()) {
			gameEndText.detachSelf();
			gameEndText.dispose();
		}

		if (!gameEndSprite.isDisposed()) {
			gameEndSprite.detachSelf();
			gameEndSprite.dispose();
		}

		this.detachSelf();
		if (!this.isDisposed()) {
			this.dispose();
		}
	}
}
