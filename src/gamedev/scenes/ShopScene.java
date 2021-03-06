package gamedev.scenes;

import gamedev.game.GameActivity;
import gamedev.game.GameActivity.GameMode;
import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

public class ShopScene extends CameraScene {

	private static ShopScene instance;
	private ResourcesManager resourcesManager = ResourcesManager.getInstance();

	private Sprite background;

	private ShopScene() {
		super(ResourcesManager.getInstance().camera);
		this.setBackgroundEnabled(false);

		background = new Sprite(0, 0, resourcesManager.shopRegion,
				resourcesManager.vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		centerShapeInCamera(background);
		attachChild(background);

		// TODO: Instantiate the shop. Remove the onSceneTouchListener and add
		// touch areas.
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown()) {
					closeShopScene();
				}
				return true;
			}
		});
	}

	public void openShopScene() {
		resourcesManager.unloadHUDResources();
		SceneManager.getInstance().getCurrentGameMapScene().setChildScene(this);
		GameActivity.mode = GameMode.POPUP;
	}

	public void closeShopScene() {
		SceneManager.getInstance().getCurrentGameMapScene().clearChildScene();
		resourcesManager.loadHUDResources();
		GameActivity.mode = GameMode.EXPLORING;
		if (!this.isDisposed()) {
			this.dispose();
		}
	}

	public static ShopScene getInstance() {
		if (instance == null) {
			instance = new ShopScene();
		}
		return instance;
	}

}
