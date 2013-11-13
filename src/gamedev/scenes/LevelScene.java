package gamedev.scenes;

import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;
import gamedev.objects.Player;

import java.io.IOException;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
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
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

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
	//

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";
	// private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 =
	// "platform1";
	// private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 =
	// "platform2";
	// private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 =
	// "platform3";
	// private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN =
	// "coin";

	// Player. Each level has to create the Player and its position in the world
	protected Player player;

	public LevelScene(int levelId) {
		super();
		this.connectPhysics();
		this.player = new Player();
		this.resourcesManager.player = player;
		this.tmxFileName = "level" + levelId + ".tmx";
		this.createMap();

		// This is done by LevelLoader
		// this.attachChild(this.player);
		this.loadLevel(levelId);
	}

	@Override
	public void createScene() {
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
		// TODO: Code responsible for disposing scene removing all game scene
		// objects.
	}

	protected void connectPhysics() {
		this.registerUpdateHandler(this.resourcesManager.physicsWorld);
	}

	protected void createMap() {
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

			// Load the TMXTiledMap from tmx asset.
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/"
					+ this.tmxFileName);

		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

		TMXLayer tmxLayerZero = null;

		// Attach other layers from the TMXTiledMap, if it has more than one.
		for (int i = 0; i < this.mTMXTiledMap.getTMXLayers().size(); i++) {
			TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(i);
			if (i == 0)
				tmxLayerZero = tmxLayer;
			// Only add non-object layers
			if (!tmxLayer.getTMXLayerProperties().containsTMXProperty(
					"boundaries", "true")) {
				this.attachChild(tmxLayer);
			}
		}

		this.camera.setBounds(0, 0, tmxLayerZero.getWidth(),
				tmxLayerZero.getHeight());
		this.camera.setBoundsEnabled(true);
		this.createUnwalkableObjects(this.mTMXTiledMap);
	}

	protected void createUnwalkableObjects(TMXTiledMap map) {
		for (final TMXObjectGroup group : this.mTMXTiledMap
				.getTMXObjectGroups()) {
			if (group.getTMXObjectGroupProperties().containsTMXProperty(
					"boundaries", "true")) {
				// This is our "wall" layer. Create the boxes from it
				for (final TMXObject object : group.getTMXObjects()) {
					final Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight(), this.resourcesManager.vbom);
					final FixtureDef boxFixtureDef = PhysicsFactory
							.createFixtureDef(0, 0, 0);
					PhysicsFactory.createBoxBody(
							this.resourcesManager.physicsWorld, rect,
							BodyType.StaticBody, boxFixtureDef);
					rect.setVisible(false);
					this.attachChild(rect);
				}
			}
		}
	}

	// TODO: This method could be used to load trees and other objects into a
	// level from a xml-file.
	// Also look at: http://www.matim-dev.com/full-game-tutorial---part-11.html
	//
	private void loadLevel(int levelID) {
		final LevelLoader levelLoader = new LevelLoader();

		final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0,
				0.01f, 0.5f);

		levelLoader.registerEntityLoader(TAG_LEVEL, new IEntityLoader() {

			@Override
			public IEntity onLoadEntity(String pEntityName,
					Attributes pAttributes) {
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes,
						LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes,
						LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

				return LevelScene.this;
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
					levelObject = new Sprite(x, y,
							resourcesManager.complete_stars_region, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);

							if (player.collidesWith(this)) {
								SceneManager.getInstance()
										.loadLevelCompleteScene(engine);
							}
						}
					};
					levelObject.registerEntityModifier(new LoopEntityModifier(
							new ScaleModifier(1, 1, 1.3f)));
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
					player.setPosition(x, y);
					levelObject = player;
				}
				// else if (type
				// .equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1)) {
				// levelObject = new Sprite(x, y,
				// resourcesManager.platform1_region, vbom);
				// PhysicsFactory.createBoxBody(
				// resourcesManager.physicsWorld, levelObject,
				// BodyType.StaticBody, FIXTURE_DEF)
				// .setUserData("platform1");
				// } else if (type
				// .equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2)) {
				// levelObject = new Sprite(x, y,
				// resourcesManager.platform2_region, vbom);
				// final Body body = PhysicsFactory.createBoxBody(
				// resourcesManager.physicsWorld, levelObject,
				// BodyType.StaticBody, FIXTURE_DEF);
				// body.setUserData("platform2");
				// physicsWorld
				// .registerPhysicsConnector(new PhysicsConnector(
				// levelObject, body, true, false));
				// } else if (type
				// .equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3)) {
				// levelObject = new Sprite(x, y,
				// resourcesManager.platform3_region, vbom);
				// final Body body = PhysicsFactory.createBoxBody(
				// resourcesManager.physicsWorld, levelObject,
				// BodyType.StaticBody, FIXTURE_DEF);
				// body.setUserData("platform3");
				// resourcesManager.physicsWorld
				// .registerPhysicsConnector(new PhysicsConnector(
				// levelObject, body, true, false));
				// } else if (type
				// .equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN)) {
				// levelObject = new Sprite(x, y,
				// resourcesManager.coin_region, vbom) {
				// @Override
				// protected void onManagedUpdate(
				// float pSecondsElapsed) {
				// super.onManagedUpdate(pSecondsElapsed);
				//
				// /**
				// * TODO we will later check if player
				// * collide with this (coin) and if it does,
				// * we will increase score and hide coin it
				// * will be completed in next articles (after
				// * creating player code)
				// */
				// }
				// };
				// levelObject
				// .registerEntityModifier(new LoopEntityModifier(
				// new ScaleModifier(1, 1, 1.3f)));
				// }
				else {
					throw new IllegalArgumentException();
				}

				levelObject.setCullingEnabled(true);

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
