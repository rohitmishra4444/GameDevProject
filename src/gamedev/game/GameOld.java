package gamedev.game;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.*;
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
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.opengl.GLES20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga
 * 
 * @author Nicolas Gramlich
 * @since 13:58:48 - 19.07.2010
 */
public class GameOld extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static int CAMERA_WIDTH = 800;
	private static int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private BoundCamera mBoundChaseCamera;
	private AnimatedSprite player;
	private Body mPlayerBody;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTextureRegion;
	private TiledTextureRegion mNpcTextureRegion;
	private TMXTiledMap mTMXTiledMap;
	private PhysicsWorld mPhysicsWorld;
	private Scene mScene;
	protected int mCactusCount;

	// Controls
	private BitmapTextureAtlas mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		// WindowManager wm = (WindowManager)
		// getBaseContext().getSystemService(Context.WINDOW_SERVICE);
		// Display display = wm.getDefaultDisplay();
		// Point displaySize = new Point();
		// display.getSize(displaySize);
		// CAMERA_WIDTH = displaySize.x;
		// CAMERA_HEIGHT = displaySize.y;

		// Display display = getWindowManager().getDefaultDisplay();
		// CAMERA_WIDTH = display.getWidth();
		// CAMERA_HEIGHT = display.getHeight();

		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH,
				CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mBoundChaseCamera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// Load Caveman from tiled asset.
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 982, 688, TextureOptions.DEFAULT);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"caveman_walking.png", 0, 0, 10, 7);
		this.mBitmapTextureAtlas.load();

		// Load controls from asset.
		this.mOnScreenControlTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();

		// Load another Player - could be a dinosaur
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 72, 128, TextureOptions.DEFAULT);
		this.mNpcTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"player.png", 0, 0, 3, 4);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		mScene = new Scene();

		// Create physics world
		this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0),
				false, 8, 1);
		mScene.registerUpdateHandler(this.mPhysicsWorld);

		// ==============================TMX_MAP=======================================
		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(),
					this.mEngine.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					this.getVertexBufferObjectManager(),
					new ITMXTilePropertiesListener() {
						@Override
						public void onTMXTileWithPropertiesCreated(
								final TMXTiledMap pTMXTiledMap,
								final TMXLayer pTMXLayer,
								final TMXTile pTMXTile,
								final TMXProperties<TMXTileProperty> pTMXTileProperties) {
							/*
							 * We are going to count the tiles that have the
							 * property "cactus=true" set.
							 */
							// if(pTMXTileProperties.containsTMXProperty("cactus",
							// "true")) {
							// Game.this.mCactusCount++;
							// }
						}
					});

			// Load the TMXTiledMap from tmx asset.
			this.mTMXTiledMap = tmxLoader
					.loadFromAsset("tmx/Game_Map_Level_1.tmx");

			// this.runOnUiThread(new Runnable() {
			// @Override
			// public void run() {
			// Toast.makeText(Game.this, "Cactus count in this TMXTiledMap: " +
			// Game.this.mCactusCount, Toast.LENGTH_LONG).show();
			// }
			// });
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

		// Attach the first layer from the TMXTiledMap.
		final TMXLayer tmxLayerZero = this.mTMXTiledMap.getTMXLayers().get(0);
		mScene.attachChild(tmxLayerZero);

		// Attach other layers from the TMXTiledMap, if it has more than one.
		for (int i = 1; i < this.mTMXTiledMap.getTMXLayers().size(); i++) {
			TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(i);
			// Only add non-object layers
			if (!tmxLayer.getTMXLayerProperties().containsTMXProperty(
					"walkable", "false"))
				mScene.attachChild(tmxLayer);
		}

		this.createUnwalkableObjects(this.mTMXTiledMap);
		// ==========================END_TMX_MAP========================================

		// Make the camera not exceed the bounds of the TMXEntity.
		this.mBoundChaseCamera.setBounds(0, 0, tmxLayerZero.getWidth(),
				tmxLayerZero.getHeight());
		this.mBoundChaseCamera.setBoundsEnabled(true);

		// Calculate the coordinates for the face, so its centered on the
		// camera.
		final float centerX = (CAMERA_WIDTH - this.mPlayerTextureRegion
				.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mPlayerTextureRegion
				.getHeight()) / 2;

		// ==============================PLAYER========================================
		// Create the player sprite and add it to the scene.
		player = new AnimatedSprite(centerX, centerY,
				this.mPlayerTextureRegion, this.getVertexBufferObjectManager());
		this.mBoundChaseCamera.setChaseEntity(player);

		// Connect our player to the PhysicsWorld
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(0,
				0, 0.2f);
		mPlayerBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, player,
				BodyType.DynamicBody, playerFixtureDef);

		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				player, mPlayerBody, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				mBoundChaseCamera.updateChaseEntity();
			}
		});

		// Add a PhysicsHandler to the player. Used for different velocities of
		// player when using the control knob.
		final PhysicsHandler physicsHandler = new PhysicsHandler(player);
		player.registerUpdateHandler(physicsHandler);

		mScene.attachChild(player);
		// ================================END_PLAYER==================================

		// =================================NPC========================================
		// Create a test NPC and add it to the scene
		final AnimatedSprite npc = new AnimatedSprite(centerX + 40, centerY,
				this.mNpcTextureRegion, this.getVertexBufferObjectManager());

		final Body mNpcBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld,
				npc, BodyType.KinematicBody, playerFixtureDef);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(npc,
				mNpcBody, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
			}
		});

		mScene.attachChild(npc);
		// ==================================END_NPC===================================

		// =================================CONTROLS===================================
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(
				0, CAMERA_HEIGHT
						- this.mOnScreenControlBaseTextureRegion.getHeight(),
				this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.1f, 200,
				this.getVertexBufferObjectManager(),
				new IAnalogOnScreenControlListener() {
					@Override
					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY) {
						// Velocity could be used to check if the animation of
						// the player should be walking or running.
						physicsHandler.setVelocity(pValueX * 5, pValueY * 5);
						mPlayerBody.setLinearVelocity(
								physicsHandler.getVelocityX(),
								physicsHandler.getVelocityY());

						// Compute direction in degree (from -180° to +180°).
						float degree = MathUtils.radToDeg((float) Math.atan2(
								pValueX, pValueY));

						// Stop animation if the controls are not used.
						if (player.isAnimationRunning() && degree == 0) {
							player.stopAnimation();
						}

						// Animate the player with respect to one of the 8
						// possible directions.
						if (!player.isAnimationRunning()) {
							if (-22.5 <= degree && degree <= 22.5
									&& degree != 0) {
								// Direction: S
								player.animate(new long[] { 30, 30, 30, 30, 30,
										30, 30, 30 }, 32, 39, false);
							} else if (22.5 <= degree && degree <= 67.5) {
								// Direction: SE
								player.animate(new long[] { 30, 30, 30, 30, 30,
										30, 30, 30 }, 40, 47, false);
							} else if (67.5 <= degree && degree <= 112.5) {
								// Direction: E
								player.animate(new long[] { 30, 30, 30, 30, 30,
										30, 30, 30 }, 0, 7, false);
							} else if (112.5 <= degree && degree <= 157.5) {
								// Direction: NE
								player.animate(new long[] { 30, 30, 30, 30, 30,
										30, 30, 30 }, 16, 23, false);
							} else if (157.5 <= degree || degree <= -157.5) {
								// Direction: N
								player.animate(new long[] { 30, 30, 30, 30, 30,
										30, 30, 30 }, 8, 15, false);
							} else if (-157.5 <= degree && degree <= -112.5) {
								// Direction: NW
								player.animate(new long[] { 30, 30, 30, 30, 30,
										30, 30, 30 }, 24, 31, false);
							} else if (-112.5 <= degree && degree <= -67.5) {
								// Direction: W
								player.animate(new long[] { 30, 30, 30, 30, 30,
										30, 30, 30 }, 56, 63, false);
							} else if (-67.5 <= degree && degree <= -22.5) {
								// Direction: SW
								player.animate(new long[] { 30, 30, 30, 30, 30,
										30, 30, 30 }, 48, 55, false);
							}
						}
					}

					@Override
					public void onControlClick(
							final AnalogOnScreenControl pAnalogOnScreenControl) {
						// Simulate a jump when clicking on the control knob.
						player.registerEntityModifier(new SequenceEntityModifier(
								new ScaleModifier(0.25f, 1, 1.5f),
								new ScaleModifier(0.25f, 1.5f, 1)));
					}
				});

		analogOnScreenControl.getControlBase().setBlendFunction(
				GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.getControlBase().setScale(1.25f);
		analogOnScreenControl.getControlKnob().setScale(1.25f);
		analogOnScreenControl.refreshControlKnobPosition();

		mScene.setChildScene(analogOnScreenControl);
		// ===============================END_CONTROLS================================

		/*
		 * Now we are going to create a rectangle that will always highlight the
		 * tile below the feet of the pEntity.
		 */
		// final Rectangle currentTileRectangle = new Rectangle(0, 0,
		// this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(),
		// this.getVertexBufferObjectManager());
		// currentTileRectangle.setColor(1, 0, 0, 0.25f);
		// mScene.attachChild(currentTileRectangle);

		// mScene.registerUpdateHandler(new IUpdateHandler() {
		// @Override
		// public void reset() { }
		//
		// @Override
		// public void onUpdate(final float pSecondsElapsed) {
		// // Get the scene-coordinates of the players feet.
		// final float[] playerFootCordinates =
		// player.convertLocalToSceneCoordinates(12, 31);
		//
		// // Get the tile the feet of the player are currently walking on.
		// final TMXTile tmxTile =
		// mTMXTiledMap.getTMXLayers().get(1).getTMXTileAt(playerFootCordinates[Constants.VERTEX_INDEX_X],
		// playerFootCordinates[Constants.VERTEX_INDEX_Y]);
		// if(tmxTile != null) {
		// // tmxTile.setTextureRegion(null); <-- Rubber-style removing of tiles
		// =D
		// // currentTileRectangle.setPosition(tmxTile.getTileX(),
		// tmxTile.getTileY());
		//
		// TMXProperties<TMXTileProperty> tileProperties = null;
		// int globalTileID = tmxTile.getGlobalTileID();
		// if (globalTileID != 0) {
		// tileProperties = mTMXTiledMap.getTMXTileProperties(globalTileID);
		// }
		// if (tileProperties != null) {
		// if (tileProperties.containsTMXProperty("walkable", "false")) {
		// Toast.makeText(getBaseContext(), "Cannot walk here!",
		// Toast.LENGTH_LONG).show();
		// System.out.println("Cannot walk here!");
		// }
		// }
		//
		// }
		// }
		// });

		return mScene;
	}

	private void createUnwalkableObjects(TMXTiledMap map) {
		// Loop through the object groups
		for (final TMXObjectGroup group : this.mTMXTiledMap
				.getTMXObjectGroups()) {
			if (group.getTMXObjectGroupProperties().containsTMXProperty(
					"walkable", "false")) {
				// This is our "wall" layer. Create the boxes from it
				for (final TMXObject object : group.getTMXObjects()) {
					final Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight(),
							this.getVertexBufferObjectManager());
					final FixtureDef boxFixtureDef = PhysicsFactory
							.createFixtureDef(0, 0, 1f);
					PhysicsFactory.createBoxBody(this.mPhysicsWorld, rect,
							BodyType.StaticBody, boxFixtureDef);
					rect.setVisible(false);
					mScene.attachChild(rect);
				}
			}
		}
	}

}
