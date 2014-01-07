package gamedev.scenes;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

public class QuestScene extends CameraScene {

	private ResourcesManager resourcesManager = ResourcesManager.getInstance();

	private Sprite background;

	public QuestScene() {
		super(ResourcesManager.getInstance().camera);
		this.setBackgroundEnabled(false);

		// TODO: Instantiate the list of active and completed quests.

		// Remove the rest of code in the constructor, is only for testing
		// purposes.
		resourcesManager.loadGameShopResources();
		float centerX = resourcesManager.camera.getCenterX()
				- resourcesManager.shopRegion.getWidth() / 2;
		float centerY = resourcesManager.camera.getCenterY()
				- resourcesManager.shopRegion.getHeight() / 2;

		background = new Sprite(centerX, centerY, resourcesManager.shopRegion,
				resourcesManager.vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		centerShapeInCamera(background);

		attachChild(background);

		// Touch inputs are not handled... :(
		// See
		// http://www.andengine.org/forums/development/child-scene-touch-events-t1054.html
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionUp()) {
					closeQuestScene();
				}
				return true;
			}
		});
	}

	public void openQuestScene() {
		resourcesManager.unloadHUDResources();
		SceneManager.getInstance().getCurrentGameMapScene().setChildScene(this);
		// If we stop the engine then also the touch inputs are working no more!
		// resourcesManager.engine.stop();
	}

	public void closeQuestScene() {
		SceneManager.getInstance().getCurrentGameMapScene().clearChildScene();
		resourcesManager.loadHUDResources();
		// resourcesManager.engine.start();
		this.dispose();
	}

}
