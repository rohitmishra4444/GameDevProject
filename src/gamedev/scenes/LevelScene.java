package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;
import gamedev.game.TmxLevelLoader;
import gamedev.objects.Player;

import java.io.IOException;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.SAXUtils;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.xml.sax.Attributes;

/**
 * Base class for all levels
 * 
 */
public class LevelScene extends BaseScene {

	// TMX Map containing the Level
	protected TMXTiledMap mTMXTiledMap;
	protected String tmxFileName;

	// Load level by xml. Remove if not used.
	private static final String TAG_LEVEL = "level";
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";

	private static final String TAG_COMPLETE_RULE = "completeRule";
	private static final String TAG_COMPLETE_RULE_ATTRIBUTE_KILL_DINOS = "killDinos";

	// Player. Each level has to create the Player and its position in the world
	protected Player player;
	private int levelId;

	private int dinosToKillPerRule = 0;
	private int dinosKilled = 0;

	public LevelScene(int levelId) {
		// Call BaseScene without calling createScene because here we need some
		// stuff initialized before
		super(false);

		this.player = this.resourcesManager.player;
		this.levelId = levelId;

		// Load map from tmx-file.
		this.tmxFileName = "level" + levelId + ".tmx";

		// CreateScene creates the world and its objects defined in the TMX-Map.
		// TODO We need to check which method to use. Here, we have a
		// "graphical Editor" to place objects which is very easy.
		this.createScene();
	}

	@Override
	public void createScene() {
		this.createMap();
		this.connectPhysics();

		// Load level-rules from xml.
		this.loadLevel(this.levelId);

		// This is done by LevelLoader:
		// this.attachChild(this.player);
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
		return SceneType.SCENE_LEVEL;
	}

	@Override
	public void disposeScene() {
		this.detachSelf();
		this.dispose();
	}

	public void killedDino() {
		this.dinosKilled++;
	}

	private boolean areLevelRulesCompleted() {
		if (dinosKilled >= dinosToKillPerRule) {
			return true;
		}
		return false;
	}

	/**
	 * LoadLevel is used to load level specific things like the
	 * level-complete-rules, the starting position of the player or the place of
	 * the level-complete-region.
	 * 
	 * @see {@link http://www.matim-dev.com/full-game-tutorial---part-11.html }
	 * 
	 * @param levelID
	 *            level id respectively file to load from
	 */
	private void loadLevel(int levelID) {
		final LevelLoader levelLoader = new LevelLoader();

		levelLoader.registerEntityLoader(TAG_LEVEL, new IEntityLoader() {

			@Override
			public IEntity onLoadEntity(String pEntityName,
					Attributes pAttributes) {
				// Do nothing. Map is loaded from tmx-file.
				return LevelScene.this;
			}
		});

		levelLoader.registerEntityLoader(TAG_COMPLETE_RULE,
				new IEntityLoader() {

					@Override
					public IEntity onLoadEntity(String pEntityName,
							Attributes pAttributes) {
						final int killDinos = SAXUtils.getIntAttributeOrThrow(
								pAttributes,
								TAG_COMPLETE_RULE_ATTRIBUTE_KILL_DINOS);
						dinosToKillPerRule = killDinos;
						return null;
					}
				});

		levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader() {

			@Override
			public IEntity onLoadEntity(String pEntityName,
					Attributes pAttributes) {

				final int x = SAXUtils.getIntAttributeOrThrow(pAttributes,
						TAG_ENTITY_ATTRIBUTE_X);
				final int y = SAXUtils.getIntAttributeOrThrow(pAttributes,
						TAG_ENTITY_ATTRIBUTE_Y);
				final String type = SAXUtils.getAttributeOrThrow(pAttributes,
						TAG_ENTITY_ATTRIBUTE_TYPE);

				final Sprite levelObject;

				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE)) {

					// TODO: Remove dependency on levelCompleteGraphics!
					levelObject = new Sprite(x, y,
							resourcesManager.levelEndRegion, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)
									&& areLevelRulesCompleted()) {
								SceneManager.getInstance()
										.loadLevelCompleteScene(engine);
							}
						}
					};
					levelObject.setAlpha(0.9f);
					levelObject.setScale(0.1f);
					levelObject.registerEntityModifier(new LoopEntityModifier(
							new ScaleModifier(2, 0.95f, 1.05f)));

				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
					assert (player != null);
					// Check if the player is already has a parent (avoid
					// assertEntityHasNoParent IllegalStateException)
					if (player.hasParent()) {
						IEntity parentEntity = player.getParent();
						parentEntity.detachChild(player);
					}
					// 32 is the PIXEL_TO_METER_RATIO_DEFAULT from AndEngine
					player.body.setTransform(x / 32, y / 32, 0);
					levelObject = player;
				}

				else {
					throw new IllegalArgumentException();
				}

				if (levelObject != null) {
					levelObject.setCullingEnabled(true);
				}
				return levelObject;
			}
		});

		try {
			levelLoader.loadLevelFromAsset(activity.getAssets(), "level/"
					+ levelID + ".lvl");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
