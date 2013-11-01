package gamedev.scenes;

import gamedev.game.SceneManager.SceneType;

public class MainMenuScene extends BaseScene {

	@Override
	public void createScene() {
		createBackground();
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

	private void createBackground() {

	}
}
