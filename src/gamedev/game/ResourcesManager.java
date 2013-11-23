package gamedev.game;

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

	private boolean gameResourcesCreated = false;

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
	// Splash resources
	// ---------------------------------------------

	public void loadSplashScreen() {
		if (splashTextureAtlas == null) {
			createSplashScreen();
		}
		if (!splashTextureAtlas.isLoadedToHardware()) {
			splashTextureAtlas.load();
		}
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		// The splash screen is no more used after the game has started.
		splash_region = null;
	}

	private void createSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				getInstance().textureManager, 257, 25, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash_edited.jpg", 0, 0);
	}

	// ---------------------------------------------
	// Menu resources
	// ---------------------------------------------

	public void loadMenuResources() {
		loadMenuGraphics();
		// loadMenuAudio();
		loadMenuFonts();
	}

	public void unloadMenuResources() {
		unloadMenuGraphics();
		// unloadMenuAudio();
		unloadMenuFonts();
	}

	private void createMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");

		// Menu background
		this.menuBackgroundTextureAtlas = new BitmapTextureAtlas(
				textureManager, 800, 600, TextureOptions.DEFAULT);
		this.menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.menuBackgroundTextureAtlas,
						getInstance().activity, "menubackground.png", 0, 0, 1,
						1);

		// Menu buttons
		this.menuButtonsTextureAtlas = new BitmapTextureAtlas(textureManager,
				800, 600, TextureOptions.DEFAULT);
		this.menu_buttons_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.menuButtonsTextureAtlas,
						getInstance().activity, "menubuttons.png", 0, 0, 1, 3);
	}

	private void loadMenuGraphics() {
		if (menuButtonsTextureAtlas == null) {
			createMenuGraphics();
		}
		if (!menuButtonsTextureAtlas.isLoadedToHardware()) {
			menuButtonsTextureAtlas.load();
		}
		if (!menuBackgroundTextureAtlas.isLoadedToHardware()) {
			menuBackgroundTextureAtlas.load();
		}
	}

	private void unloadMenuGraphics() {
		menuButtonsTextureAtlas.unload();
		menuBackgroundTextureAtlas.unload();
	}

	private void createMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				getInstance().textureManager, 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createFromAsset(
				((GameActivity) activity).getFontManager(), mainFontTexture,
				activity.getAssets(), "font.ttf", 30f, true, Color.WHITE);
	}

	private void loadMenuFonts() {
		if (font == null) {
			createMenuFonts();
		}
		font.load();
	}

	private void unloadMenuFonts() {
		font.unload();
	}

	// ---------------------------------------------
	// Game resources
	// ---------------------------------------------

	public void loadGameResources() {
		loadGameGraphics();
		// TODO:
		// loadGameFonts();
		// loadGameAudio();
		loadHUDResources();
		loadLevelCompleteResources();

		// TODO: Refactor. This should not be created here, rather in
		// LevelScene.
		physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false,
				8, 1);
		physicsWorld.setContactListener(new BodiesContactListener());
		player = new Player();
	}

	public void unloadGameResources() {
		unloadGameGraphics();
		// TODO:
		// unloadGameFonts();
		// unloadGameAudio();
		unloadHUDResources();
	}

	private void createGameGraphics() {
		createPlayerGraphics();
		createDinoGraphics();
		createTreeGraphics();
		gameResourcesCreated = true;
	}

	private void createPlayerGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.playerAtlas = new BitmapTextureAtlas(textureManager, 1056, 960,
				TextureOptions.DEFAULT);

		this.playerRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.playerAtlas, activity,
						"grey_caveman_0.5_asc.png", 0, 0, 22, 20);
	}

	private void createDinoGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.dinosaurGreenAtlas = new BitmapTextureAtlas(textureManager, 1664,
				2048, TextureOptions.DEFAULT);

		this.dinosaurGreenRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dinosaurGreenAtlas, activity,
						"green_dino_0.5_asc.png", 0, 0, 26, 32);
	}

	private void createTreeGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.treesAtlas = new BitmapTextureAtlas(textureManager, 512, 640);
		BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.treesAtlas,
				activity, "trees.png", 0, 0);
		int x = 0;
		int y = 0;
		for (int i = 1; i <= 20; i++) {
			// this.treeRegions[i-1] =
			// BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.treesAtlas,
			// activity, "trees.png", x, y);
			this.treeRegions[i - 1] = TextureRegionFactory.extractFromTexture(
					this.treesAtlas, x, y, 128, 128);
			x = x + 128;
			if (i % 4 == 0) {
				x = 0;
				y = y + 128;
			}
		}
	}

	private void loadGameGraphics() {
		if (playerAtlas == null || dinosaurGreenAtlas == null
				|| treesAtlas == null) {
			createGameGraphics();
		}

		if (!playerAtlas.isLoadedToHardware()) {
			playerAtlas.load();
		}
		if (!dinosaurGreenAtlas.isLoadedToHardware()) {
			dinosaurGreenAtlas.load();
		}
		if (!treesAtlas.isLoadedToHardware()) {
			treesAtlas.load();
		}
	}

	private void unloadGameGraphics() {
		playerAtlas.unload();
		dinosaurGreenAtlas.unload();
		treesAtlas.unload();
	}

	// ---------------------------------------------
	// HUD resources
	// ---------------------------------------------

	public void loadHUDResources() {
		loadHUDGraphics();
		this.hud = new SceneHUD();
		this.camera.setHUD(this.hud);
	}

	public void unloadHUDResources() {
		camera.setHUD(null);
		unloadHUDGraphics();
	}

	private void createHUDGraphics() {
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

	private void loadHUDGraphics() {
		if (controlTexture == null) {
			createHUDGraphics();
		}
		if (!controlTexture.isLoadedToHardware()) {
			controlTexture.load();
		}
	}

	private void unloadHUDGraphics() {
		controlTexture.unload();
	}

	// ---------------------------------------------
	// LevelComplete resources
	// ---------------------------------------------

	public void loadLevelCompleteResources() {
		loadLevelCompletedTextures();
	}

	public void unloadLevelCompleteResources() {
		unloadLevelCompletedTextures();
	}

	private void createLevelCompletedGraphics() {
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

	private void loadLevelCompletedTextures() {
		if (complete_window_atlas == null || complete_stars_atlas == null) {
			createLevelCompletedGraphics();
		}
		if (!complete_window_atlas.isLoadedToHardware()) {
		complete_window_atlas.load(); }
		if (!complete_stars_atlas.isLoadedToHardware()) {
		complete_stars_atlas.load();}
	}

	private void unloadLevelCompletedTextures() {
		complete_window_atlas.unload();
		complete_stars_atlas.unload();
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

	public boolean areGameResourcesCreated() {
		return gameResourcesCreated;
	}
}