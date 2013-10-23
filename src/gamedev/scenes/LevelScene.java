package gamedev.scenes;

import gamedev.game.GameActivity;
import gamedev.game.SceneManager.SceneType;
import gamedev.objects.Player;
import gamedev.objects.Player.PlayerState;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;

/**
 * Base class for all levels
 * 
 */
public class LevelScene extends BaseScene {

	public static PhysicsWorld physicsWorld;

	// TMX Map containing the Level
	protected TMXTiledMap mTMXTiledMap;
	protected String tmxFileName;
	
	// Controls
	protected AnalogOnScreenControl pad;
	protected BitmapTextureAtlas controlTexture;
	protected TextureRegion controlBaseTextureRegion;
	protected TextureRegion controlKnobTextureRegion;

	// Player. Each level has to create the Player and its position in the world
	protected Player player;

	public LevelScene() {
		super();
	}
		
	@Override
	public void createScene() {
		createMap();
		createPhysics();
		createControls();
	}

	@Override
	public void onBackKeyPressed() {
		// TODO GOTO Pause menu
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		// TODO Unload resources

	}

	protected void createPhysics() {
		LevelScene.physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0),
				false, 8, 1);
		this.registerUpdateHandler(physicsWorld);
	}
	
	protected void createMap() {
		try {
			final TMXLoader tmxLoader = new TMXLoader(this.resourcesManager.activity.getAssets(),
					this.resourcesManager.engine.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					this.resourcesManager.vbom,
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
			this.mTMXTiledMap = tmxLoader
					.loadFromAsset("tmx/" + this.tmxFileName);

		} catch (final TMXLoadException e) {
			Debug.e(e);
		}


		// Attach other layers from the TMXTiledMap, if it has more than one.
		for (int i=0; i<this.mTMXTiledMap.getTMXLayers().size(); i++) {
			TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(i);
			// Only add non-object layers
			if (!tmxLayer.getTMXLayerProperties().containsTMXProperty(
					"walkable", "false"))
				this.attachChild(tmxLayer);
		}
	
	}
		
	protected void createControls() {
		this.pad = new AnalogOnScreenControl(0, GameActivity.HEIGHT
				- this.controlBaseTextureRegion.getHeight(), this.camera,
				this.controlBaseTextureRegion, this.controlKnobTextureRegion,
				0.1f, 200, this.vbom, createControlListener(this.player));

		this.pad.getControlBase().setBlendFunction(
				GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.pad.getControlBase().setAlpha(0.5f);
		this.pad.getControlBase().setScaleCenter(0, 128);
		this.pad.getControlBase().setScale(1.25f);
		this.pad.getControlKnob().setScale(1.25f);
		this.pad.refreshControlKnobPosition();
		this.setChildScene(this.pad);
	}

	private IAnalogOnScreenControlListener createControlListener(final Player player) {
		
		return new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(
					final BaseOnScreenControl pBaseOnScreenControl,
					final float pValueX, final float pValueY) {
				// Velocity could be used to check if the animation of
				// the player should be walking or running.
				
				//LevelScene.setVelocity(pValueX * 5, pValueY * 5);
				//mPlayerBody.setLinearVelocity(
				//		physicsHandler.getVelocityX(),
				//		physicsHandler.getVelocityY());
				
				//player.getPhysicsHandler().setVelocity(pValueX * 5, pValueY * 5);
//				player.getBody().setLinearVelocity(pValueX * 5, pValueY * 5);
				Player.body.setLinearVelocity(pValueX * 5, pValueY * 5);
				
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
						player.setState(PlayerState.WALKING_S);
					} else if (22.5 <= degree && degree <= 67.5) {
						// Direction: SE
						player.setState(PlayerState.WALKING_SE);
					} else if (67.5 <= degree && degree <= 112.5) {
						// Direction: E
						player.setState(PlayerState.WALKING_E);
					} else if (112.5 <= degree && degree <= 157.5) {
						// Direction: NE
						player.setState(PlayerState.WALKING_NE);
					} else if (157.5 <= degree || degree <= -157.5) {
						// Direction: N
						player.setState(PlayerState.WALKING_N);
					} else if (-157.5 <= degree && degree <= -112.5) {
						// Direction: NW
						player.setState(PlayerState.WALKING_NW);
					} else if (-112.5 <= degree && degree <= -67.5) {
						// Direction: W
						player.setState(PlayerState.WALKING_W);
					} else if (-67.5 <= degree && degree <= -22.5) {
						// Direction: SW
						player.setState(PlayerState.WALKING_SW);
					}
				}
			}

			@Override
			public void onControlClick(
					AnalogOnScreenControl pAnalogOnScreenControl) {
				// TODO Auto-generated method stub
				
			}

		};
	}

	
}
