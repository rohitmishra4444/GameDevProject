package gamedev.game;

import gamedev.levels.Level1;
import gamedev.scenes.BaseScene;
import gamedev.scenes.IntroScene;
import gamedev.scenes.LevelCompleteScene;
import gamedev.scenes.LevelScene;
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
	private BaseScene introScene;

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final SceneManager INSTANCE = new SceneManager();

	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private BaseScene currentScene;

	private ResourcesManager resourcesManager = ResourcesManager.getInstance();
	private Engine engine = resourcesManager.engine;

	public enum SceneType {
		SCENE_SPLASH, SCENE_MENU, SCENE_LEVEL, SCENE_LEVEL_COMPLETE, SCENE_LOADING, SCENE_INTRO
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
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		case SCENE_INTRO:
			setScene(introScene);
			break;
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_LEVEL:
			setScene(levelScene);
			break;
		case SCENE_LEVEL_COMPLETE:
			setScene(levelCompleteScene);
			break;
		default:
			break;
		}
	}

	// ---------------------------------------------
	// Creating and Disposing of the different scenes
	// ---------------------------------------------

	// ---------------------------------------------
	// Splash Scene
	// ---------------------------------------------
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		resourcesManager.loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	private void disposeSplashScene() {
		resourcesManager.unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}

	public void disposeLoadingScene() {
		if (!loadingScene.isDisposed()) {
			loadingScene.disposeScene();
		}
	}

	// ---------------------------------------------
	// Menu Scene
	// ---------------------------------------------
	public void createMenuScene() {
		resourcesManager.loadMenuResources();
		menuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		setScene(menuScene);
		disposeSplashScene();
	}

	public void disposeMenuScene() {
		if (!menuScene.isDisposed()) {
			menuScene.disposeScene();
		}
		resourcesManager.unloadMenuResources();
	}

	public void loadMenuScene(final Engine mEngine) {
		disposeCurrentScene(true);

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						resourcesManager.loadMenuResources();
						setScene(menuScene);
						disposeLoadingScene();
					}
				}));
	}

	// ---------------------------------------------
	// Intro Scene
	// ---------------------------------------------
	public void createIntroScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		resourcesManager.loadIntroResources();
		introScene = new IntroScene();
		currentScene = introScene;
		pOnCreateSceneCallback.onCreateSceneFinished(introScene);
	}

	public void disposeIntroScene() {
		if (!introScene.isDisposed()) {
			introScene.disposeScene();
		}
		resourcesManager.unloadIntroResources();
	}

	// TODO: Probably its better for memory, when we create the intro new and
	// delete it after disposing.
	public void loadIntroScene(final Engine mEngine) {
		disposeCurrentScene(true);
		resourcesManager.loadIntroResources();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						if (introScene != null) {
							mEngine.unregisterUpdateHandler(pTimerHandler);
							setScene(introScene);
							disposeLoadingScene();
						} else {
							introScene = new IntroScene();
						}
					}
				}));
	}

	// ---------------------------------------------
	// Level Scene
	// ---------------------------------------------

	public void createLevelScene(final Engine mEngine, int levelId,
			boolean restart) {
		if (restart) {
			levelScene = null;
			resourcesManager.unloadGameResources();
		} else if (restart == false && levelScene != null) {
			loadLevelScene(engine);
			return;
		}

		if (resourcesManager.areGameResourcesCreated() == false) {
			resourcesManager.loadGameResources();
		}

		switch (levelId) {
		case 1:
			levelScene = new Level1();
			break;
		case 2:
			// TODO: More levels ;)
			break;
		default:
			break;
		}

		loadLevelScene(engine);
	}

	public void disposeLevelScene() {
		if (!levelScene.isDisposed()) {
			levelScene.disposeScene();
		}
		resourcesManager.unloadGameResources();
	}

	public void loadLevelScene(final Engine mEngine) {
		disposeCurrentScene(true);
		resourcesManager.loadGameResources();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						if (levelScene != null) {
							mEngine.unregisterUpdateHandler(pTimerHandler);
							setScene(levelScene);
							disposeLoadingScene();
						} else {
							createLevelScene(engine, 1, true);
						}
					}
				}));
	}

	// ---------------------------------------------
	// LevelComplete Scene
	// ---------------------------------------------
	public void loadLevelCompleteScene(final Engine mEngine) {
		if (levelCompleteScene == null) {
			levelCompleteScene = new LevelCompleteScene();
		}

		disposeCurrentScene(false);

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						resourcesManager.loadLevelCompleteResources();
						setScene(levelCompleteScene);
					}
				}));
	}

	public void disposeLevelCompleteScene() {
		if (!levelCompleteScene.isDisposed()) {
			levelCompleteScene.disposeScene();
		}
		resourcesManager.unloadLevelCompleteResources();
	}

	/**
	 * 
	 * @param setLoadingScene
	 *            true, if you want to show the loading scene
	 */
	public void disposeCurrentScene(boolean setLoadingScene) {
		if (setLoadingScene) {
			setScene(loadingScene);
		}

		if (currentSceneType.equals(SceneType.SCENE_LEVEL)) {
			disposeLevelScene();
		} else if (currentSceneType.equals(SceneType.SCENE_LEVEL_COMPLETE)) {
			disposeLevelCompleteScene();
		} else if (currentSceneType.equals(SceneType.SCENE_MENU)) {
			disposeMenuScene();
		}

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

	public LevelScene getCurrentLevelScene() {
		return (LevelScene) levelScene;
	}

	public boolean isLevelSceneCreated() {
		return levelScene != null;
	}

}