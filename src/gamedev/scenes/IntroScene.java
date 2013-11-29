package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

public class IntroScene extends BaseScene {

	@Override
	public void createScene() {
		// TODO: Create video view and display it.

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
		this.dispose();
	}

}
