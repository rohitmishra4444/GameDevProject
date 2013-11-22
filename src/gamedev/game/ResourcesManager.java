package gamedev.game;

import java.util.ArrayList;

import gamedev.hud.SceneHUD;
import gamedev.objects.Player;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseActivity;

import android.graphics.Color;

import com.badlogic.gdx.math.Vector2;

public class ResourcesManager {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public BaseActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public TextureManager textureManager;
	public Player player;
	public SceneHUD hud;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------

	// Textures for player, dinosaurs and environment
	public BitmapTextureAtlas playerAtlas;
	public ITiledTextureRegion playerRegion;
	public BitmapTextureAtlas dinosaurGreenAtlas;
	public ITiledTextureRegion dinosaurGreenRegion;
	public BitmapTextureAtlas treesAtlas;
	public ITextureRegion[] treeRegions = new ITextureRegion[20];

	// Textures for controls
	public BitmapTextureAtlas controlTexture;
	public TextureRegion controlBaseTextureRegion;
	public TextureRegion controlKnobTextureRegion;

	// Textures for splash scene
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;

	// Textures for menu scene
	public ITextureRegion menu_background_region;
	public ITiledTextureRegion menu_buttons_region;
	private BitmapTextureAtlas menuBackgroundTextureAtlas;
	private BitmapTextureAtlas menuButtonsTextureAtlas;
	public Font font;

	// Textures for level-complete window
	public ITextureRegion complete_window_region;
	public ITiledTextureRegion complete_stars_region;
	public BitmapTextureAtlas complete_window_atlas;
	public BitmapTextureAtlas complete_stars_atlas;
		
	// ---------------------------------------------
	// Physic
	// ---------------------------------------------

	public PhysicsWorld physicsWorld;

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	// ---------------------------------------------
	// Splash Screen
	// ---------------------------------------------

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				getInstance().textureManager, 257, 25, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash_edited.jpg", 0, 0);
		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	// ---------------------------------------------
	// Menu Screen
	// ---------------------------------------------

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");

		// Menu background
		this.menuBackgroundTextureAtlas = new BitmapTextureAtlas(
				textureManager, 800, 600, TextureOptions.DEFAULT);
		this.menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.menuBackgroundTextureAtlas,
						getInstance().activity, "menubackground.png", 0, 0, 1,
						1);
		this.menuBackgroundTextureAtlas.load();

		// Menu buttons
		this.menuButtonsTextureAtlas = new BitmapTextureAtlas(textureManager,
				800, 600, TextureOptions.DEFAULT);
		this.menu_buttons_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.menuButtonsTextureAtlas,
						getInstance().activity, "menubuttons.png", 0, 0, 1, 3);
		this.menuButtonsTextureAtlas.load();
	}

	private void loadMenuAudio() {
		// TODO
	}

	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				getInstance().textureManager, 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createFromAsset(
				((GameActivity) activity).getFontManager(), mainFontTexture,
				activity.getAssets(), "font.ttf", 30f, true, Color.WHITE);
		font.load();
	}

	public void loadMenuTextures() {
		menuBackgroundTextureAtlas.load();
	}

	public void unloadMenuTextures() {
		menuBackgroundTextureAtlas.unload();
	}

	// ---------------------------------------------
	// Game Screen
	// ---------------------------------------------

	public void loadGameResources() {
		this.physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0),
				false, 8, 1);
		loadGameGraphics();
		loadGameTextures();
		loadGameFonts();
		loadGameAudio();
		loadHUD();
		loadLevelCompletedGraphics();
		this.player = new Player();
		this.physicsWorld.setContactListener(new BodiesContactListener());
	}

	public void loadHUD() {
		if (controlTexture == null) {
			loadHUDGraphics();
		}
		controlTexture.load();
		this.hud = new SceneHUD();
		this.camera.setHUD(this.hud);
	}

	public void unloadHUD() {
		camera.setHUD(null);
		controlTexture.unload();
	}

	public void loadHUDGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.controlTexture = new BitmapTextureAtlas(textureManager, 256, 128,
				TextureOptions.BILINEAR);
		this.controlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.controlTexture, activity,
						"onscreen_control_base.png", 0, 0);
		this.controlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.controlTexture, activity,
						"onscreen_control_knob.png", 128, 0);
	}

	public void loadPlayerGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.playerAtlas = new BitmapTextureAtlas(textureManager, 1056, 960,
				TextureOptions.DEFAULT);

		this.playerRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.playerAtlas, activity,
						"grey_caveman_0.5_asc.png", 0, 0, 22, 20);
	}

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		// Player
		loadPlayerGraphics();

		// Dinosaurs
		this.dinosaurGreenAtlas = new BitmapTextureAtlas(textureManager, 1664,
				2048, TextureOptions.DEFAULT);

		this.dinosaurGreenRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dinosaurGreenAtlas, activity,
						"green_dino_0.5_asc.png", 0, 0, 26, 32);

		// Trees
		 this.treesAtlas = new BitmapTextureAtlas(textureManager, 512, 640);
		 BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.treesAtlas, activity, "trees.png", 0, 0);
		 int x = 0;
		 int y = 0;
		 for (int i=1;i<=20;i++) {
			 //this.treeRegions[i-1] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.treesAtlas, activity, "trees.png", x, y);
			 this.treeRegions[i-1] = TextureRegionFactory.extractFromTexture(this.treesAtlas, x, y, 128, 128);
			 System.out.println("Created texture " + i);
			 x = x + 128;
			 if (i % 4 == 0) {
				 x = 0;
				 y = y + 128; 
			 }
		 }
		 this.treesAtlas.load();
		 
	}

	private void loadGameAudio() {
		// TODO
	}

	private void loadGameFonts() {
		// TODO
	}

	public void loadGameTextures() {
		playerAtlas.load();
		dinosaurGreenAtlas.load();
	}

	public void unloadGameTextures() {
		playerAtlas.unload();
		dinosaurGreenAtlas.unload();
	}

	public void loadLevelCompletedGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		complete_window_atlas = new BitmapTextureAtlas(textureManager, 650,
				400, TextureOptions.DEFAULT);
		complete_window_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(complete_window_atlas, activity,
						"levelCompleteWindow.png", 0, 0);

		complete_stars_atlas = new BitmapTextureAtlas(textureManager, 650, 400,
				TextureOptions.DEFAULT);
		complete_stars_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(complete_stars_atlas, activity,
						"star.png", 0, 0, 2, 1);
	}

	public void loadLevelCompletedTextures() {
		complete_window_atlas.load();
		complete_stars_atlas.load();
	}

	public void unloadLevelCompletedTextures() {
		complete_window_atlas.unload();
		complete_stars_atlas.unload();
	}

	public ITextureRegion getRandomTreeTexture() {
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.treesAtlas, activity, "trees.png", 0, 0, 8, 4);
	}

	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br>
	 * <br>
	 *            We use this method at beginning of game loading, to prepare
	 *            Resources Manager properly, setting all needed parameters, so
	 *            we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, BaseActivity activity,
			BoundCamera camera, VertexBufferObjectManager vbom,
			TextureManager textureManager) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
		getInstance().textureManager = textureManager;
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}
}