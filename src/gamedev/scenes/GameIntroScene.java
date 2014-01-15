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

public class GameIntroScene extends BaseScene {

	private Sprite gameIntroSprite;
	private Text gameIntroText;

	@Override
	public void createScene() {
		float centerX = camera.getCenterX()
				- resourcesManager.game_intro_region.get(0).getWidth() / 2;
		float centerY = camera.getCenterY()
				- resourcesManager.game_intro_region.get(0).getHeight() / 2;

		String gameIntroString = "Once a day," + "\n"
				+ "in the modern world of today..." + "\n\n\n" + "Tap...";

		gameIntroText = new Text(0, 0, resourcesManager.font, gameIntroString,
				gameIntroString.length(), vbom);
		gameIntroText.setPosition(
				camera.getCenterX() - gameIntroText.getWidth() / 2,
				camera.getCenterY() - gameIntroText.getHeight() / 2);
		gameIntroText.setHorizontalAlign(HorizontalAlign.CENTER);
		this.registerTouchArea(gameIntroText);
		attachChild(gameIntroText);

		gameIntroSprite = new Sprite(centerX, centerY,
				resourcesManager.game_intro_region.get(0).getWidth(),
				resourcesManager.game_intro_region.get(0).getHeight(),
				resourcesManager.game_intro_region.get(0), vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		this.registerTouchArea(gameIntroSprite);
		gameIntroSprite.setVisible(false);
		attachChild(gameIntroSprite);

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

				long touchTime = pSceneTouchEvent.getMotionEvent()
						.getEventTime();

				if (touchTime > lastTouchTime + WAIT_TIME
						&& pTouchArea.equals(gameIntroText)
						&& gameIntroText.isVisible()) {
					System.out.println("GameIntroText touched.");

					gameIntroText.setVisible(false);
					gameIntroSprite.setVisible(true);
					gameIntroSprite.registerEntityModifier(new FadeInModifier(
							5f));

					lastTouchTime = touchTime;
				}

				if (touchTime > lastTouchTime + WAIT_TIME
						&& pTouchArea.equals(gameIntroSprite)
						&& gameIntroSprite.isVisible()) {
					System.out.println("GameIntroSprite touched.");

					gameIntroSprite.setVisible(false);
					SceneManager.getInstance()
							.createGameMapScene(engine, false);

					lastTouchTime = touchTime;
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
		return SceneType.SCENE_INTRO;
	}

	@Override
	public void disposeScene() {
		this.detachSelf();
		if (!this.isDisposed()) {
			this.dispose();
		}
	}

}
