package gamedev.scenes;

import gamedev.game.GameActivity;
import gamedev.game.GameActivity.GameMode;
import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;

public class HelpScene extends CameraScene {

	private static HelpScene instance;
	private ResourcesManager resourcesManager = ResourcesManager.getInstance();

	private static final float FADE_IN_DURATION = 0.5f;
	private static final float FADE_OUT_DURATION = 0.3f;

	private ArrayList<Sprite> gameHelpSprites;
	private int nextSprite = 1;

	private HelpScene() {
		super(ResourcesManager.getInstance().camera);
		this.setBackgroundEnabled(false);

		gameHelpSprites = new ArrayList<Sprite>();
		resourcesManager.loadGameHelpResources();

		for (ITextureRegion region : resourcesManager.helpRegion) {
			Sprite sprite = new Sprite(0, 0, region.getWidth(),
					region.getHeight(), region, resourcesManager.vbom) {
				@Override
				protected void preDraw(GLState pGLState, Camera pCamera) {
					super.preDraw(pGLState, pCamera);
					pGLState.enableDither();
				}
			};
			centerShapeInCamera(sprite);
			gameHelpSprites.add(sprite);
		}

		// This call has to be at the end.
		registerSceneTouchListener();
	}

	private void registerSceneTouchListener() {
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown()) {
					// Show the GameMapScene if last sprite was touched, else
					// show the next sprite.
					if (gameHelpSprites.get(gameHelpSprites.size() - 1)
							.hasParent()) {
						Sprite oldSprite = gameHelpSprites.get(gameHelpSprites
								.size() - 1);
						oldSprite.detachSelf();
						closeHelpScene();
						return true;
					} else if (gameHelpSprites.get(nextSprite - 1).hasParent()) {
						final Sprite oldSprite = gameHelpSprites
								.get(nextSprite - 1);
						oldSprite.registerEntityModifier(new FadeOutModifier(
								FADE_OUT_DURATION));
						detachViaUpdateHandlerAfterTime(oldSprite,
								FADE_OUT_DURATION);

						Sprite newSprite = gameHelpSprites.get(nextSprite);
						attachChild(newSprite);
						newSprite.registerEntityModifier(new FadeInModifier(
								FADE_IN_DURATION));
						nextSprite++;
						return true;
					}
				}
				return false;
			}
		});
	}

	protected void detachViaUpdateHandlerAfterTime(final IEntity entity,
			final float time) {
		resourcesManager.engine.registerUpdateHandler(new TimerHandler(time,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						resourcesManager.engine
								.unregisterUpdateHandler(pTimerHandler);
						entity.detachSelf();
						entity.clearEntityModifiers();
					}
				}));
	}

	public void openHelpScene() {
		GameActivity.mode = GameMode.POPUP;
		resourcesManager.unloadHUDResources();
		resourcesManager.loadGameHelpResources();
		SceneManager.getInstance().getCurrentGameMapScene().setChildScene(this);

		// Attach the first sprite.
		Sprite firstSprite = gameHelpSprites.get(0);
		attachChild(firstSprite);
		firstSprite
				.registerEntityModifier(new FadeInModifier(FADE_IN_DURATION));
	}

	public void closeHelpScene() {
		SceneManager.getInstance().getCurrentGameMapScene().clearChildScene();
		resourcesManager.unloadGameHelpResources();
		resourcesManager.loadHUDResources();
		GameActivity.mode = GameMode.EXPLORING;
		if (!this.isDisposed()) {
			this.dispose();
		}
		for (Sprite sprite : gameHelpSprites) {
			sprite.detachSelf();
			sprite.clearEntityModifiers();
			if (!sprite.isDisposed()) {
				sprite.dispose();
			}
		}
		nextSprite = 1;
	}

	public static HelpScene getInstance() {
		if (instance == null) {
			instance = new HelpScene();
		}
		return instance;
	}
}
