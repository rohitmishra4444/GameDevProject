package gamedev.game;

import gamedev.scenes.BaseScene;
import gamedev.scenes.GameEndScene;
import gamedev.scenes.GameMapScene;
import gamedev.scenes.IntroScene;
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
	private BaseScene gameMapScene;
	private BaseScene gameEndScene;
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
		SCENE_SPLASH, SCENE_MENU, SCENE_GAME_MAP, SCENE_GAME_END, SCENE_LOADING, SCENE_INTRO
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
		case SCENE_GAME_MAP:
			setScene(gameMapScene);
			break;
		case SCENE_GAME_END:
			setScene(gameEndScene);
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
	// Game Scene
	// ---------------------------------------------

	public void createGameMapScene(final Engine mEngine, boolean restart) {
		disposeCurrentScene(true);

		if (restart) {
			gameMapScene = null;
			resourcesManager.unloadGameResources();
		} else if (restart == false && gameMapScene != null) {
			loadGameMapScene(engine);
			return;
		}

		if (resourcesManager.areGameResourcesCreated() == false) {
			resourcesManager.loadGameResources();
		}

		gameMapScene = new GameMapScene();

		loadGameMapScene(engine);
	}

	public void disposeGameMapScene() {
		if (!gameMapScene.isDisposed()) {
			gameMapScene.disposeScene();
		}
		resourcesManager.unloadGameResources();
	}

	public void loadGameMapScene(final Engine mEngine) {
		if (!currentSceneType.equals(SceneType.SCENE_LOADING)) {
			disposeCurrentScene(true);
		}
		resourcesManager.loadGameResources();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						if (gameMapScene != null) {
							mEngine.unregisterUpdateHandler(pTimerHandler);
							setScene(gameMapScene);
							disposeLoadingScene();
						} else {
							createGameMapScene(engine, true);
						}
					}
				}));
	}

	// ---------------------------------------------
	// GameEnd Scene
	// ---------------------------------------------
	public void loadGameEndScene(final Engine mEngine) {
		disposeCurrentScene(false);
		resourcesManager.loadGameEndResources();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						if (gameEndScene == null) {
							gameEndScene = new GameEndScene();
						}
						setScene(gameEndScene);
					}
				}));
	}

	public void disposeGameEndScene() {
		if (!gameEndScene.isDisposed()) {
			gameEndScene.disposeScene();
		}
		resourcesManager.unloadGameEndResources();
	}

	/**
	 * 
	 * @param setLoadingSceneNeeded
	 *            true, if you want to show the loading scene
	 */
	public void disposeCurrentScene(boolean setLoadingSceneNeeded) {
		if (setLoadingSceneNeeded) {
			setLoadingScene();
		}

		if (currentSceneType.equals(SceneType.SCENE_GAME_MAP)) {
			disposeGameMapScene();
		} else if (currentSceneType.equals(SceneType.SCENE_GAME_END)) {
			disposeGameEndScene();
		} else if (currentSceneType.equals(SceneType.SCENE_MENU)) {
			disposeMenuScene();
		} else if (currentSceneType.equals(SceneType.SCENE_INTRO)) {
			disposeIntroScene();
		}
	}

	public void setLoadingScene() {
		if (loadingScene == null) {
			loadingScene = new LoadingScene();
		}
		setScene(loadingScene);
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

	public GameMapScene getCurrentLevelScene() {
		return (GameMapScene) gameMapScene;
	}

	public boolean isLevelSceneCreated() {
		return (gameMapScene != null && !gameMapScene.isDisposed());
	}

}