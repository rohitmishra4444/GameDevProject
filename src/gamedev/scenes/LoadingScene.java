package gamedev.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

import gamedev.game.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{
    @Override
    public void createScene()
    {
        setBackground(new Background(Color.WHITE));
//        attachChild(new Text(400, 240, resourcesManager.font, "Loading...", vbom));
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