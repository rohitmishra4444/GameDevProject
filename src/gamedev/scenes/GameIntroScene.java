package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;

public class GameIntroScene extends BaseScene {

	private static final float FADE_IN_DURATION = 1f;
	private static final float FADE_OUT_DURATION = 0.5f;

	private static final String string0 = "Once a beautiful day in our modern world,"
			+ "\n\n\n" + "Tap...";
	private static final String string1 = "a bad accident messed up your life.";
	private static final String string2 = "With a big flash, you were thrown back to the stoneage.";
	private static final String string3 = "Fortunately, some kind cavemen have taken you to their living cave.";
	private static final String string4 = "On a fascinating mural painting, you could recognise something.";
	private static final String string5 = "There has to be a portal somewhere, which could bring you back to home.";
	private static final String string6 = "Furthermore, on a small map you could see where this portal should have to be.";

	private static final String[] gameIntroStrings = { string0, string1,
			string2, string3, string4, string5, string6 };

	private ArrayList<Text> gameIntroTexts;
	private ArrayList<Sprite> gameIntroSprites;
	private ArrayList<IEntity> gameIntroEntities;

	private int nextEntity = 1;

	// private int nextText = 0;
	// private int nextSprite = 1;

	@Override
	public void createScene() {
		// Create the text list.
		gameIntroTexts = new ArrayList<Text>();
		for (String string : gameIntroStrings) {
			Text text = new Text(0, 0, resourcesManager.font, string,
					string.length(), vbom);
			text.setPosition(camera.getCenterX() - text.getWidth() / 2,
					camera.getCenterY() - text.getHeight() / 2);
			text.setHorizontalAlign(HorizontalAlign.CENTER);
			gameIntroTexts.add(text);
		}

		// Create the picture list.
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

		gameIntroEntities = new ArrayList<IEntity>();
		// Add the entities alternately, beginning with the pictures.
		for (int i = 0; i < 7; i++) {
			gameIntroEntities.add(gameIntroSprites.get(i));
			gameIntroEntities.add(gameIntroTexts.get(i));
		}
		// Attach the first entity to the scene.
		attachChild(gameIntroEntities.get(0));

		// This call has to be at the end.
		registerSceneTouchListener();
	}

	private void registerSceneTouchListener() {
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown()) {
					// Show the GameMapScene if last entity was touched, else
					// show the next one.
					if (gameIntroEntities.get(gameIntroEntities.size() - 1)
							.hasParent()) {
						IEntity oldEntity = gameIntroEntities
								.get(gameIntroEntities.size() - 1);
						oldEntity.registerEntityModifier(new FadeOutModifier(
								FADE_OUT_DURATION));
						detachViaUpdateHandlerAfterTime(oldEntity,
								FADE_OUT_DURATION);

						SceneManager.getInstance().createGameMapScene(engine,
								false);
						return true;
					} else if (gameIntroEntities.get(nextEntity - 1)
							.hasParent()) {
						IEntity oldEntity = gameIntroEntities
								.get(nextEntity - 1);
						oldEntity.registerEntityModifier(new FadeOutModifier(
								FADE_OUT_DURATION));
						detachViaUpdateHandlerAfterTime(oldEntity,
								FADE_OUT_DURATION);

						IEntity newEntity = gameIntroEntities.get(nextEntity);
						attachChild(newEntity);
						newEntity.registerEntityModifier(new FadeInModifier(
								FADE_IN_DURATION));
						nextEntity++;
						return true;
					}

					// if (gameIntroTexts.get(nextText - 1).hasParent()) {
					// final Text oldText = gameIntroTexts.get(nextText - 1);
					// oldText.registerEntityModifier(new FadeOutModifier(
					// FADE_OUT_DURATION));
					// detachViaUpdateHandlerAfterTime(oldText,
					// FADE_OUT_DURATION);
					//
					// Sprite newSprite = gameIntroSprites.get(nextSprite);
					// attachChild(newSprite);
					// newSprite.registerEntityModifier(new FadeInModifier(
					// FADE_IN_DURATION));
					// nextSprite++;
					// return true;
					// }

					// if (gameIntroSprites.get(gameIntroSprites.size() - 1)
					// .hasParent()) {
					// final Sprite oldSprite = gameIntroSprites
					// .get(gameIntroSprites.size() - 1);
					// oldSprite.registerEntityModifier(new FadeOutModifier(
					// FADE_OUT_DURATION));
					// detachViaUpdateHandlerAfterTime(oldSprite,
					// FADE_OUT_DURATION);
					//
					// SceneManager.getInstance().createGameMapScene(engine,
					// false);
					// return true;
					// } else if (gameIntroSprites.get(nextSprite -
					// 1).hasParent()) {
					// final Sprite oldSprite = gameIntroSprites
					// .get(nextSprite - 1);
					// oldSprite.registerEntityModifier(new FadeOutModifier(
					// FADE_OUT_DURATION));
					// detachViaUpdateHandlerAfterTime(oldSprite,
					// FADE_OUT_DURATION);
					//
					// Text newText = gameIntroTexts.get(nextText);
					// attachChild(newText);
					// newText.registerEntityModifier(new FadeInModifier(
					// FADE_IN_DURATION));
					// nextText++;
					// return true;
					// }
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
		for (IEntity entity : gameIntroEntities) {
			if (entity.hasParent()) {
				entity.detachSelf();
			}
			entity.clearEntityModifiers();
			if (!entity.isDisposed()) {
				entity.dispose();
			}
		}
		// for (Text text : gameIntroTexts) {
		// text.detachSelf();
		// text.clearEntityModifiers();
		// if (!text.isDisposed()) {
		// text.dispose();
		// }
		// }
		this.detachSelf();
		if (!this.isDisposed()) {
			this.dispose();
		}
	}

}
