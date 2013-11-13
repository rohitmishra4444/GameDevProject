package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;
import gamedev.levels.LevelCompleteWindow;
import gamedev.levels.LevelCompleteWindow.StarsCount;

public class LevelCompleteScene extends BaseScene {

	// Level-complete window
	private LevelCompleteWindow levelCompleteWindow;

	@Override
	public void createScene() {
		levelCompleteWindow = new LevelCompleteWindow(vbom);
		levelCompleteWindow.display(StarsCount.TWO, LevelCompleteScene.this,
				camera);
		this.setVisible(false);
		this.setIgnoreUpdate(true);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LEVEL_COMPLETE;
	}

	@Override
	public void disposeScene() {
		this.detachSelf();
		this.dispose();
	}

}
