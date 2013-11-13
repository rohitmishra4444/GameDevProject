package gamedev.scenes;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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

		levelCompleteSprite = new Sprite(0, 0, 650, 400,
				ResourcesManager.getInstance().complete_window_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		levelCompleteSprite.setVisible(false);
		levelCompleteSprite.setIgnoreUpdate(true);

		// Attach our level complete panel in the middle of camera
		setPosition(
				camera.getWidth()
						/ 2
						- ResourcesManager.getInstance().complete_window_region.getWidth()
						/ 2,
				camera.getHeight()
						/ 2
						- ResourcesManager.getInstance().complete_window_region
								.getHeight() / 2);

		attachChild(levelCompleteSprite);

		attachStars(vbom);
		display(StarsCount.TWO);
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
		this.detachSelf();
		this.dispose();
	}

	private void attachStars(VertexBufferObjectManager pSpriteVertexBufferObject) {
		star1 = new TiledSprite(150, 150,
				ResourcesManager.getInstance().complete_stars_region,
				pSpriteVertexBufferObject);
		star2 = new TiledSprite(325, 150,
				ResourcesManager.getInstance().complete_stars_region,
				pSpriteVertexBufferObject);
		star3 = new TiledSprite(500, 150,
				ResourcesManager.getInstance().complete_stars_region,
				pSpriteVertexBufferObject);

		attachChild(star1);
		attachChild(star2);
		attachChild(star3);
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
