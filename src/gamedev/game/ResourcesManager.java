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

	private static boolean gameGraphicsCreated = false;
	private static boolean gameEndPortalAtlasLoaded = false;
	private static boolean playerAtlasLoaded = false;
	private static boolean dinosaurGreenAtlasLoaded = false;
	private static boolean treesAtlasLoaded = false;
	private static boolean controlTextureLoaded = false;
	private static boolean splashTextureAtlasLoaded = false;
	private static boolean menuBackgroundTextureAtlasLoaded = false;
	private static boolean menuButtonsTextureAtlasLoaded = false;
	private static boolean menuFontLoaded = false;
	private static boolean complete_window_atlasLoaded = false;

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

	public BitmapTextureAtlas gameEndPortalAtlas;
	public ITextureRegion gameEndPortalRegion;

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
	public BitmapTextureAtlas complete_window_atlas;

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

		if (splashTextureAtlasLoaded == false) {
			splashTextureAtlas.load();
			splashTextureAtlasLoaded = true;
		}
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splashTextureAtlasLoaded = false;
		// The splash screen is no more used after the game has started.
		splash_region = null;
	}

	private void createSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				getInstance().textureManager, 480, 320, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash_andengine.png", 0, 0);
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
		if (menuButtonsTextureAtlasLoaded == false) {
			menuButtonsTextureAtlas.load();
			menuButtonsTextureAtlasLoaded = true;
		}
		if (menuBackgroundTextureAtlasLoaded == false) {
			menuBackgroundTextureAtlas.load();
			menuBackgroundTextureAtlasLoaded = true;
		}
	}

	private void unloadMenuGraphics() {
		menuButtonsTextureAtlas.unload();
		menuButtonsTextureAtlasLoaded = false;

		menuBackgroundTextureAtlas.unload();
		menuBackgroundTextureAtlasLoaded = false;
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
		if (menuFontLoaded == false) {
			font.load();
			menuFontLoaded = true;
		}
	}

	private void unloadMenuFonts() {
		font.unload();
		menuFontLoaded = false;
	}

	// ---------------------------------------------
	// Intro resources
	// ---------------------------------------------

	public void loadIntroResources() {
		// TODO
	}

	public void unloadIntroResources() {
		// TODO
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

		// TODO: Refactor. This should not be created here, rather in
		// LevelScene.
		if (physicsWorld == null || player == null) {
			physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0),
					false, 8, 1);
			physicsWorld.setContactListener(new BodiesContactListener());
			player = new Player();
		}
	}

	public void unloadGameResources() {
		unloadGameGraphics();
		// TODO:
		// unloadGameFonts();
		// unloadGameAudio();
		unloadHUDResources();
	}

	private void createGameGraphics() {
		createGameEndPortalGraphics();
		createPlayerGraphics();
		createDinoGraphics();
		createTreeGraphics();
		gameGraphicsCreated = true;
	}

	// TODO: Create here the graphics for the portal.
	private void createGameEndPortalGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.gameEndPortalAtlas = new BitmapTextureAtlas(textureManager, 600,
				379, TextureOptions.DEFAULT);

		this.gameEndPortalRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameEndPortalAtlas, activity, "caveExit.png",
						0, 0);
	}

	private void createPlayerGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.playerAtlas = new BitmapTextureAtlas(textureManager, 1216, 1520,
				TextureOptions.DEFAULT);

		this.playerRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.playerAtlas, activity,
						"caveman.png", 0, 0, 16, 20);
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
				|| treesAtlas == null || gameGraphicsCreated == false) {
			createGameGraphics();
		}

		if (gameEndPortalAtlasLoaded == false) {
			gameEndPortalAtlas.load();
			gameEndPortalAtlasLoaded = true;
		}

		if (playerAtlasLoaded == false) {
			playerAtlas.load();
			playerAtlasLoaded = true;
		}
		if (dinosaurGreenAtlasLoaded == false) {
			dinosaurGreenAtlas.load();
			dinosaurGreenAtlasLoaded = true;
		}
		if (treesAtlasLoaded == false) {
			treesAtlas.load();
			treesAtlasLoaded = true;
		}
	}

	private void unloadGameGraphics() {
		gameEndPortalAtlas.unload();
		gameEndPortalAtlasLoaded = false;

		playerAtlas.unload();
		playerAtlasLoaded = false;

		dinosaurGreenAtlas.unload();
		dinosaurGreenAtlasLoaded = false;

		treesAtlas.unload();
		treesAtlasLoaded = false;
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
		if (controlTextureLoaded == false) {
			controlTexture.load();
			controlTextureLoaded = true;
		}
	}

	private void unloadHUDGraphics() {
		controlTexture.unload();
		controlTextureLoaded = false;
	}

	// ---------------------------------------------
	// LevelComplete resources
	// ---------------------------------------------

	public void loadGameEndResources() {
		loadGameEndTextures();
	}

	public void unloadGameEndResources() {
		unloadGameEndTextures();
	}

	private void createGameEndGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		complete_window_atlas = new BitmapTextureAtlas(textureManager, 650,
				400, TextureOptions.DEFAULT);
		complete_window_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(complete_window_atlas, activity,
						"levelCompleteWindow.png", 0, 0);
	}

	private void loadGameEndTextures() {
		if (complete_window_atlas == null) {
			createGameEndGraphics();
		}
		if (complete_window_atlasLoaded == false) {
			complete_window_atlas.load();
			complete_window_atlasLoaded = true;
		}
	}

	private void unloadGameEndTextures() {
		complete_window_atlas.unload();
		complete_window_atlasLoaded = false;
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
		return gameGraphicsCreated;
	}
}