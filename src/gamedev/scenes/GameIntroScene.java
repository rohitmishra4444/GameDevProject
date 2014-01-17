package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;

public class GameIntroScene extends BaseScene {

	private Text gameIntroText;
	private ArrayList<Sprite> gameIntroSprites;
	private int nextSprite = 0;

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

		gameIntroSprites = new ArrayList<Sprite>();
		for (ITextureRegion region : resourcesManager.game_intro_region) {
			Sprite sprite = new Sprite(centerX, centerY, region.getWidth(),
					region.getHeight(), region, vbom) {
				@Override
				protected void preDraw(GLState pGLState, Camera pCamera) {
					super.preDraw(pGLState, pCamera);
					pGLState.enableDither();
				}
			};
			gameIntroSprites.add(sprite);
		}

		// This call has to be at the end.
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
						&& pTouchArea.equals(gameIntroText)) {
					gameIntroText.detachSelf();
					unregisterTouchArea(gameIntroText);

					Sprite newSprite = gameIntroSprites.get(0);
					attachChild(newSprite);
					newSprite.registerEntityModifier(new FadeInModifier(1f));
					registerTouchArea(newSprite);
					lastTouchTime = touchTime;
					nextSprite++;
				}

				// Show the GameMapScene if last sprite was touched, else show
				// the next sprite.
				if (touchTime > lastTouchTime + WAIT_TIME
						&& pTouchArea.equals(gameIntroSprites
								.get(gameIntroSprites.size() - 1))) {
					Sprite oldSprite = gameIntroSprites.get(gameIntroSprites
							.size() - 1);
					oldSprite.detachSelf();
					unregisterTouchArea(oldSprite);

					SceneManager.getInstance()
							.createGameMapScene(engine, false);

					lastTouchTime = touchTime;
				} else if (touchTime > lastTouchTime + WAIT_TIME
						&& pTouchArea.equals(gameIntroSprites
								.get(nextSprite - 1))) {
					Sprite oldSprite = gameIntroSprites.get(nextSprite - 1);
					oldSprite.detachSelf();
					unregisterTouchArea(oldSprite);

					Sprite newSprite = gameIntroSprites.get(nextSprite);
					attachChild(newSprite);
					newSprite.registerEntityModifier(new FadeInModifier(1f));
					registerTouchArea(newSprite);

					lastTouchTime = touchTime;
					nextSprite++;
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
