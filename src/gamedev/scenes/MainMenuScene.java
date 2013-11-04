package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

public class MainMenuScene extends BaseScene implements
		IOnMenuItemClickListener {

	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_EXIT = 1;
	private final int MENU_BACK = 2;

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed() {
		back();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		this.detachSelf();
		this.dispose();
	}

	private void createBackground() {
		attachChild(new Sprite(0, 0,
				resourcesManager.menu_background_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}

	private void createMenuChildScene() {
		menuChildScene = new MenuScene(camera);
		// Not necessary.
		// menuChildScene.setPosition(0, 0);

		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_PLAY,
						resourcesManager.buttons_region
								.getTextureRegion(MENU_PLAY),
						vbom), 1.2f, 1);

		final IMenuItem exitMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_EXIT,
						resourcesManager.buttons_region
								.getTextureRegion(MENU_EXIT), vbom), 1.2f, 1);

		final IMenuItem backMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_BACK,
						resourcesManager.buttons_region
								.getTextureRegion(MENU_BACK), vbom), 1.2f, 1);

		menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.addMenuItem(exitMenuItem);
		menuChildScene.addMenuItem(backMenuItem);

		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY());
		exitMenuItem.setPosition(exitMenuItem.getX(), exitMenuItem.getY());
		backMenuItem.setPosition(backMenuItem.getX(), backMenuItem.getY());

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {

		switch (pMenuItem.getID()) {
		case MENU_PLAY:
			SceneManager.getInstance().createLevelScene(engine, 1);
			return true;
		case MENU_EXIT:
			System.exit(0);
			return true;
		case MENU_BACK:
			return true;
		default:
			return false;
		}
	}

}
