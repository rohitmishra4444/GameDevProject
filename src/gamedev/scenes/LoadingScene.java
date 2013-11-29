package gamedev.scenes;

import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;
import org.andengine.util.progress.IProgressListener;

public class LoadingScene extends BaseScene implements IProgressListener {

	private Text loadingText;

	@Override
	public void createScene() {
		setBackground(new Background(Color.BLACK));
		String loadingInitialString = "Loading: 0%       ";

		float centerX = camera.getCenterX();
		float centerY = camera.getCenterY();

		loadingText = new Text(centerX, centerY, resourcesManager.font,
				loadingInitialString, loadingInitialString.length(), vbom);
		attachChild(loadingText);

		// TODO: Improve loading progress. Is not displayed right at the moment.
		registerUpdateHandler(new TimerHandler(1 / 20.0f, true,
				new ITimerCallback() {
					// Starts a timer for updating out progress
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						int seconds = (int) engine.getSecondsElapsedTotal();
						if (seconds > 100)
							// ProgressListeners require the number to be within
							// 0 and 100.
							return;
						else
							onProgressChanged(seconds);
					}
				}));
	}

	@Override
	public void onBackKeyPressed() {
		// Do nothing.
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

	@Override
	public void onProgressChanged(int pProgress) {
		if (loadingText == null) {
			return;
		}
		if (pProgress == 100)
			loadingText.setText("Loading: Complete");
		else
			loadingText.setText("Loading: " + pProgress + "%");
	}

}