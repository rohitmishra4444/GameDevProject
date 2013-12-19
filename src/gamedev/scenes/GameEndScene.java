package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

public class GameEndScene extends BaseScene {

	// Level-complete window
	private Sprite gameEndSprite;

	// private Text gameEndText;

	@Override
	public void createScene() {
		float centerX = camera.getCenterX()
				- resourcesManager.complete_window_region.getWidth() / 2;
		float centerY = camera.getCenterY()
				- resourcesManager.complete_window_region.getHeight() / 2;

		// setBackground(new Background(Color.BLACK));
		// String loadingInitialString =
		// "You successfully returned to your era.";
		// gameEndText = new Text(centerX, centerY, resourcesManager.font,
		// loadingInitialString, loadingInitialString.length(), vbom);
		// attachChild(gameEndText);

		gameEndSprite = new Sprite(centerX, centerY,
				resourcesManager.complete_window_region.getWidth(),
				resourcesManager.complete_window_region.getHeight(),
				resourcesManager.complete_window_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		attachChild(gameEndSprite);
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
		this.gameEndSprite.detachSelf();
		this.gameEndSprite.dispose();
		this.detachSelf();
		this.dispose();
	}
}
