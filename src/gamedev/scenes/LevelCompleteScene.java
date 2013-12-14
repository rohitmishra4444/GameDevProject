package gamedev.scenes;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.util.GLState;

public class LevelCompleteScene extends BaseScene {

	// Level-complete window
	private Sprite levelCompleteSprite;
	private TiledSprite star1;
	private TiledSprite star2;
	private TiledSprite star3;

	public enum StarsCount {
		ONE, TWO, THREE
	}

	@Override
	public void createScene() {
		float centerX = camera.getBoundsWidth() - 650;
		float centerY = camera.getBoundsHeight() - 400;

		centerX = 0;
		centerY = 0;

		levelCompleteSprite = new Sprite(centerX, centerY, 650, 400,
				ResourcesManager.getInstance().complete_window_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		attachStars();

		// TODO: Define based on goals how many stars to give.
		display(StarsCount.TWO);

		attachChild(levelCompleteSprite);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LEVEL_COMPLETE;
	}

	@Override
	public void disposeScene() {
		this.levelCompleteSprite.detachSelf();
		this.levelCompleteSprite.dispose();
		this.star1.detachSelf();
		this.star1.dispose();
		this.star2.detachSelf();
		this.star2.dispose();
		this.star3.detachSelf();
		this.star3.dispose();
		this.detachSelf();
		this.dispose();
	}

	private void attachStars() {
		star1 = new TiledSprite(55, 150,
				ResourcesManager.getInstance().complete_stars_region, vbom);
		star2 = new TiledSprite(235, 150,
				ResourcesManager.getInstance().complete_stars_region, vbom);
		star3 = new TiledSprite(415, 150,
				ResourcesManager.getInstance().complete_stars_region, vbom);

		levelCompleteSprite.attachChild(star1);
		levelCompleteSprite.attachChild(star2);
		levelCompleteSprite.attachChild(star3);
	}

	/**
	 * Change star`s tile index, depends on stars count.
	 * 
	 * @param starsCount
	 */
	public void display(StarsCount starsCount) {
		// Change stars tile index, based on stars count (1-3)
		switch (starsCount) {
		case ONE:
			star1.setCurrentTileIndex(0);
			star2.setCurrentTileIndex(1);
			star3.setCurrentTileIndex(1);
			break;
		case TWO:
			star1.setCurrentTileIndex(0);
			star2.setCurrentTileIndex(0);
			star3.setCurrentTileIndex(1);
			break;
		case THREE:
			star1.setCurrentTileIndex(0);
			star2.setCurrentTileIndex(0);
			star3.setCurrentTileIndex(0);
			break;
		}
	}
}
