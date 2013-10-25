package gamedev.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

import gamedev.game.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{


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