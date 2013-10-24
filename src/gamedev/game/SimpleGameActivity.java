package gamedev.game;
import gamedev.levels.Level1;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.view.KeyEvent;

public class SimpleGameActivity extends SimpleBaseGameActivity
{
	
	// Game resolution
	public final static int WIDTH = 800;
	public final static int HEIGHT = 480;
	
	// Camera chasing the player
	private BoundCamera camera;
	
	// Singleton class to load resources and get objects across different Scenes
	private ResourcesManager resourcesManager;
	
//	@Override
//	public Engine onCreateEngine(EngineOptions pEngineOptions) 
//	{
//	    // Try to run our game at 60fps
//		//return new LimitedFPSEngine(pEngineOptions, 60);
//	}
	
	@Override
	public EngineOptions onCreateEngineOptions()
    {
		 this.camera = new BoundCamera(0, 0, WIDTH, HEIGHT);
		 EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(WIDTH, HEIGHT), this.camera);
		 //engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		 //engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		 System.out.println("EngineOptions created");
		 return engineOptions;
    }
		
//	/**
//	 * Handling pressing the Back-Button.
//	 * Forward this request to the currently active Scene
//	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) 
//	{  
//	    if (keyCode == KeyEvent.KEYCODE_BACK)
//	    {
//	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
//	    }
//	    return false; 
//	}
	
//	/**
//	 * Exit game activity
//	 */
//	protected void onDestroy() {
//		super.onDestroy();
//        System.exit(0);
//	}

	@Override
	protected void onCreateResources() {
    	ResourcesManager.prepareManager(this.mEngine, this, this.camera, this.getVertexBufferObjectManager(), this.getTextureManager());
    	this.resourcesManager = ResourcesManager.getInstance();
		System.out.println("Rescources created");
	}

	@Override
	protected Scene onCreateScene() {
		return new Level1();
	}
	
}