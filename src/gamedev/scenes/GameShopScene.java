package gamedev.scenes;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

public class GameShopScene extends BaseScene {

	protected ResourcesManager resourcesManager;
	private Sprite background;

	@Override
	public void createScene() {
		this.resourcesManager = ResourcesManager.getInstance();
		this.setBackground(new Background(Color.WHITE));
		this.setBackgroundEnabled(true);

		// TODO: The sprite is not correctly centered yet.
		float centerX = camera.getCenterX()
				- resourcesManager.shopRegion.getWidth() / 2 + 400;
		float centerY = camera.getCenterY()
				- resourcesManager.shopRegion.getHeight() / 2;

		background = new Sprite(centerX, centerY,
				resourcesManager.shopRegion.getWidth(),
				resourcesManager.shopRegion.getHeight(),
				resourcesManager.shopRegion, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		attachChild(background);

		// this.setOnSceneTouchListener(new IOnSceneTouchListener() {
		//
		// @Override
		// public boolean onSceneTouchEvent(Scene pScene,
		// TouchEvent pSceneTouchEvent) {
		// if (pSceneTouchEvent.isActionDown()) {
		//
		// }
		// return false;
		// }
		// });
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadGameMapScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME_SHOP;
	}

	@Override
	public void disposeScene() {
		this.detachSelf();
		this.dispose();
		background.detachSelf();
		background.dispose();
	}

}
