package gamedev.scenes;

import gamedev.game.SceneManager.SceneType;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

public class LoadingScene extends BaseScene {

	@Override
	public void createScene() {
		setBackground(new Background(Color.BLACK));
		String loadingString = "Loading...";
		float centerX = camera.getWidth() / 2 - loadingString.length() * 4;
		float centerY = camera.getHeight() / 2
				- resourcesManager.font.getLineHeight() / 2;
		Text loadingText = new Text(centerX, centerY, resourcesManager.font,
				loadingString, loadingString.length(), vbom);
		attachChild(loadingText);
	}

	@Override
	public void onBackKeyPressed() {
		return;
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {
		this.detachSelf();
		this.dispose();
	}

}