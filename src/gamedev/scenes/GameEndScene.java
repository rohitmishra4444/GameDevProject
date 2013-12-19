package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;

public class GameEndScene extends BaseScene {

	// Level-complete window
	private Sprite gameEndSprite;

	private Text gameEndText;
	private Text gameDevelopersText;

	@Override
	public void createScene() {
		float centerX = camera.getCenterX()
				- resourcesManager.game_end_region.getWidth() / 2;
		float centerY = camera.getCenterY()
				- resourcesManager.game_end_region.getHeight() / 2;

		String gameEndString = "Congratulations! "
				+ "\n"
				+ "The portal was working properly and you returned to your era."
				+ "\n\n" + "Tap here...";

		gameEndText = new Text(0, 0, resourcesManager.font, gameEndString,
				gameEndString.length(), vbom);
		gameEndText.setPosition(camera.getCenterX() - gameEndText.getWidth()
				/ 2, camera.getCenterY() - gameEndText.getHeight() / 2);
		gameEndText.setHorizontalAlign(HorizontalAlign.CENTER);
		this.registerTouchArea(gameEndText);
		attachChild(gameEndText);

		gameEndSprite = new Sprite(centerX, centerY,
				resourcesManager.game_end_region.getWidth(),
				resourcesManager.game_end_region.getHeight(),
				resourcesManager.game_end_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		this.registerTouchArea(gameEndSprite);

		String gameDevelopersString = "This game was designed and developed by"
				+ "\n" + "Stefan Wanzenried" + "\n" + "David Wettstein"
				+ "\n\n" + "Tap here...";

		gameDevelopersText = new Text(0, 0, resourcesManager.font,
				gameDevelopersString, gameDevelopersString.length(), vbom);
		gameDevelopersText.setPosition(
				camera.getCenterX() - gameDevelopersText.getWidth() / 2,
				camera.getCenterY() - gameDevelopersText.getHeight() / 2);
		gameDevelopersText.setHorizontalAlign(HorizontalAlign.CENTER);
		this.registerTouchArea(gameDevelopersText);

		this.setOnAreaTouchListener(new IOnAreaTouchListener() {

			long lastTouchTime = 0;

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					ITouchArea pTouchArea, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

				long touchTime = pSceneTouchEvent.getMotionEvent()
						.getEventTime();

				if (touchTime > lastTouchTime + 1000
						&& pTouchArea.equals(gameEndText)) {

					detachChild(gameDevelopersText);
					attachChild(gameEndSprite);

					lastTouchTime = touchTime;
				}

				if (touchTime > lastTouchTime + 1000
						&& pTouchArea.equals(gameEndSprite)
						&& gameEndSprite.hasParent()) {

					detachChild(gameEndSprite);
					attachChild(gameDevelopersText);

					lastTouchTime = touchTime;
				}

				if (touchTime > lastTouchTime + 1000
						&& pTouchArea.equals(gameDevelopersText)
						&& gameDevelopersText.hasParent()) {
					SceneManager.getInstance().loadMenuScene(engine);

					lastTouchTime = touchTime;
				}

				return false;
			}
		});
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME_END;
	}

	@Override
	public void disposeScene() {
		this.gameEndText.detachSelf();
		this.gameEndText.dispose();
		this.gameEndSprite.detachSelf();
		this.gameEndSprite.dispose();
		this.detachSelf();
		this.dispose();
	}
}
