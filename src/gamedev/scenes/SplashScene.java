package gamedev.scenes;

import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

public class SplashScene extends BaseScene {

	private Sprite splash;

	@Override
	public void createScene() {

		float centerX = camera.getCenterX()
				- resourcesManager.splash_region.getWidth() / 2;
		float centerY = camera.getCenterY()
				- resourcesManager.splash_region.getHeight() / 2;

		splash = new Sprite(centerX, centerY,
				resourcesManager.splash_region.getWidth(),
				resourcesManager.splash_region.getHeight(),
				resourcesManager.splash_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		splash.setScale(1.5f);
		attachChild(splash);
	}

	@Override
	public void onBackKeyPressed() {
		// Do nothing.
		return;
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene() {
		this.detachSelf();
		this.dispose();
		splash.detachSelf();
		splash.dispose();
	}
}
