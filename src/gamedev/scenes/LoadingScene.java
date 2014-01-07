package gamedev.scenes;

import gamedev.game.SceneManager.SceneType;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

public class LoadingScene extends BaseScene {

	private Text loadingText;

	@Override
	public void createScene() {
		setBackground(new Background(Color.BLACK));
		String loadingInitialString = "Loading...";

		float centerX = camera.getCenterX();
		float centerY = camera.getCenterY();

		loadingText = new Text(centerX, centerY, resourcesManager.font,
				loadingInitialString, loadingInitialString.length(), vbom);
		attachChild(loadingText);
	}

	@Override
	public void onBackKeyPressed() {
		// Do nothing.
		return;
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {
		loadingText.detachSelf();
		loadingText.dispose();
		this.detachSelf();
		this.dispose();
	}
}