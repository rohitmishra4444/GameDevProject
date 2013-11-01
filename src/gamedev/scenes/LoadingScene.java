package gamedev.scenes;

import gamedev.game.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{

	@Override
	public void createScene() {
		// TODO Auto-generated method stub

	}

    @Override
    public void onBackKeyPressed()
    {
        return;
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {

    }

}