package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;
import gamedev.game.TmxLevelLoader;
import gamedev.objects.Avatar;

import org.andengine.entity.IEntity;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.debug.Debug;

/**
 * Base class for all levels
 * 
 */
public class GameMapScene extends BaseScene {

	// TMX Map containing the Level
	protected TMXTiledMap mTMXTiledMap;
	protected String tmxFileName;

	// Player. Each level has to create the Player and its position in the world
	protected Avatar player;

	// private Sprite gameEndPortal;
	// private Sprite cave;

	// private static final int MIN_DINOS_TO_KILL = 1;
	// private int dinosKilled = 0;

	public GameMapScene() {
		// Call BaseScene without calling createScene because here we need some
		// stuff initialized before
		super(false);
		this.player = this.resourcesManager.avatar;
		this.tmxFileName = "level.tmx";
		this.createScene();
	}

	@Override
	public void createScene() {
		this.createMap();
		this.connectPhysics();
		assert (player != null);
		// Check if the player is already has a parent (avoid
		// assertEntityHasNoParent IllegalStateException)
		// This is needed for the back button during gameplay (for restarting a
		// new game).
		if (player.hasParent()) {
			IEntity parentEntity = player.getParent();
			parentEntity.detachChild(player);
		}
		// 32 is the PIXEL_TO_METER_RATIO_DEFAULT from AndEngine
		player.getBody().setTransform(200 / 32, 200 / 32, 0);
		this.attachChild(player);

		// TODO: Define player and portal positions as constant.
		// TODO: Game end portal should be created in TmxLevelLoader class.
		// gameEndPortal = new Sprite(1200, 300,
		// resourcesManager.gameEndPortalRegion, vbom) {
		// @Override
		// protected void onManagedUpdate(float pSecondsElapsed) {
		// super.onManagedUpdate(pSecondsElapsed);
		// if (player.collidesWith(this) && areLevelRulesCompleted()) {
		// SceneManager.getInstance().loadGameEndScene(engine);
		// }
		// }
		// };
		// gameEndPortal.setAlpha(0.9f);
		// gameEndPortal.registerEntityModifier(new LoopEntityModifier(
		// new ScaleModifier(2, 0.95f, 1.05f)));
		// this.attachChild(gameEndPortal);
	}

	protected void connectPhysics() {
		this.registerUpdateHandler(this.resourcesManager.physicsWorld);
	}

	protected void createMap() {
		// Try to load the tmx file
		try {
			final TMXLoader tmxLoader = new TMXLoader(
					this.activity.getAssets(), this.engine.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.vbom,
					new ITMXTilePropertiesListener() {
						@Override
						public void onTMXTileWithPropertiesCreated(
								final TMXTiledMap pTMXTiledMap,
								final TMXLayer pTMXLayer,
								final TMXTile pTMXTile,
								final TMXProperties<TMXTileProperty> pTMXTileProperties) {
						}
					});

			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/"
					+ this.tmxFileName);

		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

		// Set camera bounds
		TMXLayer tmxLayerZero = this.mTMXTiledMap.getTMXLayers().get(0);
		this.camera.setBounds(0, 0, tmxLayerZero.getWidth(),
				tmxLayerZero.getHeight());
		this.camera.setBoundsEnabled(true);

		// Load all the objects, boundaries of our level. This is handled in a
		// new class
		TmxLevelLoader loader = new TmxLevelLoader(this.mTMXTiledMap, this);
		loader.createWorldAndObjects();
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME_MAP;
	}

	@Override
	public void disposeScene() {
		this.camera.setHUD(null);
		this.detachSelf();
		this.dispose();
	}

	// public void killedDino() {
	// this.dinosKilled++;
	// }

	// private boolean areLevelRulesCompleted() {
	// if (dinosKilled >= MIN_DINOS_TO_KILL) {
	// return true;
	// }
	// return false;
	// }

}
