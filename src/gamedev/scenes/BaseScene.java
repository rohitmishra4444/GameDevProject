package gamedev.scenes;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

public abstract class BaseScene extends Scene {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	protected Engine engine;
	protected Activity activity;
	protected ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected BoundCamera camera;
	protected TextureManager textureManager;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public BaseScene() {
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
		this.activity = resourcesManager.activity;
		this.vbom = resourcesManager.vbom;
		this.camera = resourcesManager.camera;
		this.textureManager = resourcesManager.textureManager;
		this.createScene();
	}

	public BaseScene(boolean callCreateScene) {
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
		this.activity = resourcesManager.activity;
		this.vbom = resourcesManager.vbom;
		this.camera = resourcesManager.camera;
		this.textureManager = resourcesManager.textureManager;
		if (callCreateScene) {
			this.createScene();
		}
	}

	protected void detachViaUpdateHandlerAfterTime(final IEntity entity,
			final float time) {
		resourcesManager.engine.registerUpdateHandler(new TimerHandler(time,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						resourcesManager.engine
								.unregisterUpdateHandler(pTimerHandler);
						entity.detachSelf();
						entity.clearEntityModifiers();
					}
				}));
	}

	// ---------------------------------------------
	// ABSTRACTION
	// ---------------------------------------------

	public abstract void createScene();

	public abstract void onBackKeyPressed();

	public abstract SceneType getSceneType();

	public abstract void disposeScene();
}