package gamedev.scenes;

import gamedev.game.SceneManager.SceneType;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

public class LoadingScene extends BaseScene
{

	@Override
	public void createScene() {
		setBackground(new Background(Color.WHITE));
		attachChild(new Text(200, 100, resourcesManager.font, "Loading...",
				vbom));
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