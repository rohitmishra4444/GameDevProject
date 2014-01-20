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

	private static final String string0 = "Once a day," + "\n"
			+ "in the modern world of today..." + "\n\n\n" + "Tap...";
	private static final String string1 = "Text1";
	private static final String string2 = "Text2";
	private static final String string3 = "Text3";
	private static final String string4 = "Text4";
	private static final String string5 = "Text5";
	private static final String string6 = "Text6";

	private static final String[] gameIntroStrings = { string0, string1,
			string2, string3, string4, string5, string6 };

	private ArrayList<Text> gameIntroTexts;
	private ArrayList<Sprite> gameIntroSprites;
	private int nextText = 1;
	private int nextSprite = 0;

	@Override
	public void createScene() {

		gameIntroTexts = new ArrayList<Text>();

		for (String string : gameIntroStrings) {
			Text text = new Text(0, 0, resourcesManager.font, string,
					string.length(), vbom);
			text.setPosition(camera.getCenterX() - text.getWidth() / 2,
					camera.getCenterY() - text.getHeight() / 2);
			text.setHorizontalAlign(HorizontalAlign.CENTER);
			gameIntroTexts.add(text);
		}
		attachChild(gameIntroTexts.get(0));
		registerTouchArea(gameIntroTexts.get(0));

		float spriteCenterX = camera.getCenterX()
				- resourcesManager.game_intro_region.get(0).getWidth() / 2;
		float spriteCenterY = camera.getCenterY()
				- resourcesManager.game_intro_region.get(0).getHeight() / 2;

		gameIntroSprites = new ArrayList<Sprite>();
		for (ITextureRegion region : resourcesManager.game_intro_region) {
			Sprite sprite = new Sprite(spriteCenterX, spriteCenterY,
					region.getWidth(), region.getHeight(), region, vbom) {
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
				if (pSceneTouchEvent.isActionDown()) {
					long touchTime = pSceneTouchEvent.getMotionEvent()
							.getEventTime();

					if (nextText == 0) {
						return false;
					}

					if (touchTime > lastTouchTime + WAIT_TIME
							&& pTouchArea.equals(gameIntroTexts
									.get(nextText - 1))) {
						Text oldText = gameIntroTexts.get(nextText - 1);
						oldText.detachSelf();
						unregisterTouchArea(oldText);

						Sprite newSprite = gameIntroSprites.get(nextSprite);
						attachChild(newSprite);
						newSprite
								.registerEntityModifier(new FadeInModifier(1f));
						registerTouchArea(newSprite);

						lastTouchTime = touchTime;
						nextSprite++;
					}

					// Show the GameMapScene if last sprite was touched, else
					// show
					// the next text.
					if (touchTime > lastTouchTime + WAIT_TIME
							&& pTouchArea.equals(gameIntroSprites
									.get(gameIntroSprites.size() - 1))) {
						Sprite oldSprite = gameIntroSprites
								.get(gameIntroSprites.size() - 1);
						oldSprite.detachSelf();
						unregisterTouchArea(oldSprite);

						SceneManager.getInstance().createGameMapScene(engine,
								false);

						lastTouchTime = touchTime;
					} else if (touchTime > lastTouchTime + WAIT_TIME
							&& pTouchArea.equals(gameIntroSprites
									.get(nextSprite - 1))) {
						Sprite oldSprite = gameIntroSprites.get(nextSprite - 1);
						oldSprite.detachSelf();
						unregisterTouchArea(oldSprite);

						Text newText = gameIntroTexts.get(nextText);
						attachChild(newText);
						registerTouchArea(newText);

						lastTouchTime = touchTime;
						nextText++;
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
