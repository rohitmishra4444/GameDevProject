package gamedev.game;

import gamedev.levels.Level1;
import gamedev.scenes.BaseScene;
import gamedev.scenes.LevelCompleteScene;
import gamedev.scenes.LoadingScene;
import gamedev.scenes.MainMenuScene;
import gamedev.scenes.SplashScene;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class SceneManager {
	// ---------------------------------------------
	// SCENES
	// ---------------------------------------------

	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene levelScene;
	private BaseScene levelCompleteScene;
	private BaseScene loadingScene;

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final SceneManager INSTANCE = new SceneManager();

	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private BaseScene currentScene;

	private Engine engine = ResourcesManager.getInstance().engine;

	public enum SceneType {
		SCENE_SPLASH, SCENE_MENU, SCENE_LEVEL, SCENE_LEVEL_COMPLETE, SCENE_LOADING,
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	public void setScene(SceneType sceneType) {
		switch (sceneType) {
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_LEVEL:
			setScene(levelScene);
			break;
		case SCENE_LEVEL_COMPLETE:
			setScene(levelCompleteScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		default:
			break;
		}
	}

	// ---------------------------------------------
	// Creating and Disposing of the different scenes
	// ---------------------------------------------

	public void createMenuScene() {
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		setScene(menuScene);
		disposeSplashScene();
	}

	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	private void disposeSplashScene() {
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}

	public void createLevelScene(final Engine mEngine, int level) {
		setScene(loadingScene);
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						ResourcesManager.getInstance().loadGameResources();
						levelScene = new Level1(); // TODO Load level dynamic
													// based on provided level
													// variable
						setScene(levelScene);
						if (!loadingScene.isDisposed()) {
							loadingScene.disposeScene();
						}
					}
				}));
	}

	public void loadMenuScene(final Engine mEngine) {

		if (currentSceneType.equals(SceneType.SCENE_LEVEL)) {
			if (!levelScene.isDisposed()) {
				levelScene.disposeScene();
			}
			ResourcesManager.getInstance().unloadHUD();
			ResourcesManager.getInstance().unloadGameTextures();
		} else if (currentSceneType.equals(SceneType.SCENE_LEVEL_COMPLETE)) {
			if (!levelCompleteScene.isDisposed()) {
				levelCompleteScene.disposeScene();
			}
			ResourcesManager.getInstance().unloadLevelCompletedTextures();
		}

		setScene(loadingScene);

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						ResourcesManager.getInstance().loadMenuTextures();
						setScene(menuScene);
						if (!loadingScene.isDisposed()) {
							loadingScene.disposeScene();
						}
					}
				}));
	}

	public void loadLevelScene(final Engine mEngine) {
		setScene(loadingScene);
		if (!menuScene.isDisposed()) {
			menuScene.disposeScene();
		}
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						ResourcesManager.getInstance().loadGameTextures();
						ResourcesManager.getInstance().loadHUD();
						setScene(levelScene);
						if (!loadingScene.isDisposed()) {
							loadingScene.disposeScene();
						}
					}
				}));
	}

	public void loadLevelCompleteScene(final Engine mEngine) {
		if (levelCompleteScene == null) {
			levelCompleteScene = new LevelCompleteScene();
		}
		ResourcesManager.getInstance().loadLevelCompletedTextures();
		if (!levelScene.isDisposed()) {
			levelScene.disposeScene();
		}
		ResourcesManager.getInstance().unloadHUD();
		ResourcesManager.getInstance().unloadGameTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						setScene(levelCompleteScene);
					}
				}));
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}

	public BaseScene getCurrentScene() {
		return currentScene;
	}

	public boolean isLevelSceneCreated() {
		return levelScene != null;
	}

}