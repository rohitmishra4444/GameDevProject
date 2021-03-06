package gamedev.game;

import gamedev.hud.SceneHUD;
import gamedev.objects.Avatar;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IShape;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
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
	public SoundManager soundManager;
	public Avatar avatar;
	public SceneHUD hud;

	private static boolean gameGraphicsCreated = false;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------

	// Textures for player, dinosaurs and environment
	public BitmapTextureAtlas playerAtlas;
	public ITiledTextureRegion playerRegion;
	public BitmapTextureAtlas[] dinosaurAtlas = new BitmapTextureAtlas[2];
	public ITiledTextureRegion[] dinosaurRegion = new ITiledTextureRegion[2];
	// public BitmapTextureAtlas dinosaurRedAtlas;
	// public ITiledTextureRegion dinosaurRedRegion;
	public BitmapTextureAtlas treesAtlas;
	// public ITextureRegion[] treeRegions = new ITextureRegion[20];
	public BitmapTextureAtlas pigAtlas;
	public ITiledTextureRegion pigRegion;
	public BitmapTextureAtlas spiderAtlas;
	public ITiledTextureRegion spiderRegion;

	// Textures for fight scene
	public BitmapTextureAtlas spearAtlas;
	public ITextureRegion spearRegion;
	public BitmapTextureAtlas fightDinoAtlas;
	public ITextureRegion fightDinoRegion;

	public BitmapTextureAtlas gameEndPortalAtlas;
	public ITextureRegion gameEndPortalRegion;

	// Textures for HUD and controls
	public BitmapTextureAtlas controlTextureAtlas;
	public TextureRegion controlBaseTextureRegion;
	public TextureRegion controlKnobTextureRegion;
	private BitmapTextureAtlas hudBerryAtlas;
	public TextureRegion hudBerryRegion;

	private BitmapTextureAtlas hudHelpIconAtlas;
	public TextureRegion hudHelpIconRegion;
	private BitmapTextureAtlas hudQuestListIconAtlas;
	public TextureRegion hudQuestListIconRegion;
	// private BitmapTextureAtlas hudShopIconAtlas;
	// public TextureRegion hudShopIconRegion;

	public BitmapTextureAtlas bgBarsAtlas;
	public TextureRegion bgBarsRegion;

	// Textures for splash scene
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;

	// Textures for current quest scene
	public ITextureRegion questFrameRegion;
	private BitmapTextureAtlas questFrameTextureAtlas;
	public ITextureRegion questActiveRegion;
	private BitmapTextureAtlas questActiveTextureAtlas;
	public ITextureRegion questFinishedRegion;
	private BitmapTextureAtlas questFinishedTextureAtlas;

	// Textures for game shop scene
	public ITextureRegion shopRegion;
	private BitmapTextureAtlas shopTextureAtlas;

	// Textures for game help scene
	public ArrayList<BitmapTextureAtlas> helpTextureAtlas;
	public ArrayList<ITextureRegion> helpRegion;

	// Textures for menu scene
	public ITextureRegion menu_background_region;
	public ITiledTextureRegion menu_buttons_region;
	private BitmapTextureAtlas menuBackgroundTextureAtlas;
	private BitmapTextureAtlas menuButtonsTextureAtlas;
	public Font font;
	public Font fontBig;

	// Textures for game intro scene
	public ArrayList<BitmapTextureAtlas> game_intro_atlas;
	public ArrayList<ITextureRegion> game_intro_region;

	// Textures for game end scene
	public ITextureRegion game_end_region;
	public BitmapTextureAtlas game_end_atlas;

	// Static objects
	public BitmapTextureAtlas woodAtlas;
	public ITextureRegion woodRegion;
	public BitmapTextureAtlas oldCavemanAtlas;
	public ITextureRegion oldCavemanRegion;
	public BitmapTextureAtlas bridgeAtlas;
	public ITextureRegion bridgeRegion;
	public BitmapTextureAtlas treeAtlas;
	public ITextureRegion treeRegion;
	public BitmapTextureAtlas axeAtlas;
	public ITextureRegion axeRegion;
	public BitmapTextureAtlas boxAtlas;
	public ITextureRegion boxRegion;

	// ---------------------------------------------
	// Sound and music
	// ---------------------------------------------

	public Music backgroundMusicMenu;
	public Music backgroundMusicGame;

	private boolean gameSoundsLoaded;
	public Sound hit;
	public Sound hit_false;
	public Sound collect;
	public Sound walk;
	public Sound eat;
	public Sound openChildScene;
	public Sound questFinished;
	public Sound pain;
	public Sound heartbeat;
	public Sound dinoGrowl;

	// ---------------------------------------------
	// Physic
	// ---------------------------------------------

	public PhysicsWorld physicsWorld;

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void removeSpriteAndBody(final IShape shape) {
		if (shape == null) {
			return;
		}
		final PhysicsConnector physicsConnector = physicsWorld
				.getPhysicsConnectorManager()
				.findPhysicsConnectorByShape(shape);
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				if (physicsConnector != null) {
					physicsWorld.unregisterPhysicsConnector(physicsConnector);
					physicsConnector.getBody().setActive(false);
					physicsWorld.destroyBody(physicsConnector.getBody());
				}
				shape.setIgnoreUpdate(true);
				shape.clearUpdateHandlers();
				shape.detachSelf();
				if (!shape.isDisposed()) {
					shape.dispose();
				}
			}
		});
	}

	public void removeIEntity(final IEntity entity) {
		if (entity == null) {
			return;
		}
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				entity.setIgnoreUpdate(true);
				entity.clearUpdateHandlers();
				entity.detachSelf();
				if (!entity.isDisposed()) {
					entity.dispose();
				}
			}
		});
	}

	// ---------------------------------------------
	// Splash resources
	// ---------------------------------------------

	public void loadSplashScreen() {
		if (splashTextureAtlas == null) {
			createSplashScreen();
		}

		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		// The splash screen is no more used after the game has started.
		splash_region = null;
	}

	private void createSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(textureManager, 480, 320);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash_andengine.png", 0, 0);
	}

	// ---------------------------------------------
	// Menu resources
	// ---------------------------------------------

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	public void unloadMenuResources() {
		unloadMenuGraphics();
		unloadMenuAudio();
	}

	private void createMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");

		// Menu background
		this.menuBackgroundTextureAtlas = new BitmapTextureAtlas(
				textureManager, 800, 600, BitmapTextureFormat.RGBA_8888);
		this.menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(menuBackgroundTextureAtlas, activity,
						"menubackground.jpg", 0, 0, 1, 1);

		// Menu buttons
		this.menuButtonsTextureAtlas = new BitmapTextureAtlas(textureManager,
				800, 600, TextureOptions.DEFAULT);
		this.menu_buttons_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(menuButtonsTextureAtlas, activity,
						"menubuttons.png", 0, 0, 1, 3);
	}

	private void loadMenuGraphics() {
		if (menuButtonsTextureAtlas == null) {
			createMenuGraphics();
		}
		menuButtonsTextureAtlas.load();
		menuBackgroundTextureAtlas.load();
	}

	private void unloadMenuGraphics() {
		menuButtonsTextureAtlas.unload();
		menuBackgroundTextureAtlas.unload();
	}

	private void loadMenuAudio() {
		MusicFactory.setAssetBasePath("mfx/");
		try {
			backgroundMusicMenu = MusicFactory.createMusicFromAsset(
					engine.getMusicManager(), activity, "menu_music.ogg");
			backgroundMusicMenu.setVolume(0.2f);
			backgroundMusicMenu.setLooping(true);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void unloadMenuAudio() {
		// TODO: Needed?
	}

	private void createMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(textureManager,
				256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		final ITexture texture = new BitmapTextureAtlas(textureManager, 512,
				512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createFromAsset(
				((GameActivity) activity).getFontManager(), mainFontTexture,
				activity.getAssets(), "UniversElse-Regular.ttf", 22, true,
				Color.WHITE);

		// fontBig = FontFactory.createFromAsset(
		// ((GameActivity) activity).getFontManager(), mainFontTexture,
		// activity.getAssets(), "UniversElse-Regular.ttf", 60, true,
		// Color.WHITE);

		fontBig = FontFactory.createFromAsset(
				((GameActivity) activity).getFontManager(), texture,
				activity.getAssets(), "StoneHinge.ttf", 80, true, Color.WHITE);

		// font = FontFactory.createFromAsset(
		// ((GameActivity) activity).getFontManager(), mainFontTexture,
		// activity.getAssets(), "font.ttf", 35f, true, Color.WHITE);

	}

	private void loadMenuFonts() {
		if (font == null) {
			createMenuFonts();
		}
		font.load();
		fontBig.load();
	}

	private void unloadMenuFonts() {
		font.unload();
		fontBig.unload();
	}

	// ---------------------------------------------
	// Intro resources
	// ---------------------------------------------

	public void loadGameIntroResources() {
		loadGameIntroGraphics();
		createGameMusic();
	}

	public void unloadGameIntroResources() {
		unloadGameIntroGraphics();
	}

	private void createGameIntroGraphics() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/game/game_intro/");

		game_intro_atlas = new ArrayList<BitmapTextureAtlas>();
		game_intro_region = new ArrayList<ITextureRegion>();

		for (int i = 0; i < 7; i++) {
			BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager,
					800, 480, BitmapTextureFormat.RGBA_4444);
			game_intro_atlas.add(i, atlas);

			String fileName = "picture_0" + (i + 1) + ".jpg";

			ITextureRegion region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(atlas, activity, fileName, 0, 0);
			game_intro_region.add(i, region);
		}
	}

	private void loadGameIntroGraphics() {
		if (game_intro_atlas == null) {
			createGameIntroGraphics();
		}
		for (BitmapTextureAtlas atlas : game_intro_atlas) {
			atlas.load();
		}
	}

	private void unloadGameIntroGraphics() {
		for (BitmapTextureAtlas atlas : game_intro_atlas) {
			atlas.unload();
		}
	}

	// ---------------------------------------------
	// Game resources
	// ---------------------------------------------

	public void loadGameResources() {
		loadGameGraphics();
		loadMenuFonts();
		loadGameAudio();
		loadHUDResources();
		// loadGameShopResources();

		// Avatar reacts no more after contact with berrybush, if the following
		// code is not in here.
		if (physicsWorld == null) {
			physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0),
					false, 8, 1);
			physicsWorld.setContactListener(new BodiesContactListener());
		}
		if (avatar == null) {
			avatar = new Avatar();
		}
	}

	public void unloadGameResources() {
		unloadGameGraphics();
		unloadGameAudio();
		unloadHUDResources();
		// unloadGameShopResources();
	}

	private void loadGameGraphics() {
		if (gameGraphicsCreated == false) {
			createGameGraphics();
		}
		gameEndPortalAtlas.load();
		playerAtlas.load();
		dinosaurAtlas[0].load();
		dinosaurAtlas[1].load();
		// dinosaurRedAtlas.load();
		// treesAtlas.load();
		// spearAtlas.load();
		fightDinoAtlas.load();
		woodAtlas.load();
		bridgeAtlas.load();
		pigAtlas.load();
		spiderAtlas.load();
		oldCavemanAtlas.load();
		axeAtlas.load();
		treeAtlas.load();
		boxAtlas.load();
	}

	private void unloadGameGraphics() {
		gameEndPortalAtlas.unload();
		playerAtlas.unload();
		dinosaurAtlas[0].unload();
		dinosaurAtlas[1].unload();
		// dinosaurAtlas.unload();
		// dinosaurRedAtlas.unload();
		// treesAtlas.unload();
		// spearAtlas.unload();
		fightDinoAtlas.unload();
		woodAtlas.unload();
		bridgeAtlas.unload();
		pigAtlas.unload();
		spiderAtlas.unload();
		oldCavemanAtlas.unload();
		axeAtlas.unload();
		treeAtlas.unload();
		boxAtlas.unload();
	}

	private void createGameGraphics() {
		createGameEndPortalGraphics();
		createAvatarGraphics();
		createAnimalGraphics();
		// createTreeGraphics();
		createQuestSceneGraphics();
		// createSpearGraphics();
		createFightSceneGraphics();
		createStaticObjectGraphics();
		gameGraphicsCreated = true;
	}

	private void createStaticObjectGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.woodAtlas = new BitmapTextureAtlas(textureManager, 7, 30,
				TextureOptions.DEFAULT);

		this.woodRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(woodAtlas, activity, "wood.png", 0, 0);

		this.bridgeAtlas = new BitmapTextureAtlas(textureManager, 256, 97,
				TextureOptions.DEFAULT);

		this.bridgeRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bridgeAtlas, activity, "bridge.png", 0, 0);

		this.axeAtlas = new BitmapTextureAtlas(textureManager, 24, 22,
				TextureOptions.DEFAULT);

		this.axeRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(axeAtlas, activity, "axe.png", 0, 0);

		this.boxAtlas = new BitmapTextureAtlas(textureManager, 26, 27,
				TextureOptions.DEFAULT);

		this.boxRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(boxAtlas, activity, "box.png", 0, 0);

		this.treeAtlas = new BitmapTextureAtlas(textureManager, 65, 114,
				TextureOptions.DEFAULT);

		this.treeRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(treeAtlas, activity, "tree.png", 0, 0);

		this.oldCavemanAtlas = new BitmapTextureAtlas(textureManager, 36, 35,
				TextureOptions.DEFAULT);

		this.oldCavemanRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(oldCavemanAtlas, activity,
						"old_caveman_0.6.png", 0, 0);

	}

	private void createGameEndPortalGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.gameEndPortalAtlas = new BitmapTextureAtlas(textureManager, 90,
				49, TextureOptions.DEFAULT);

		this.gameEndPortalRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameEndPortalAtlas, activity, "portal.png", 0,
						0);
	}

	private void createAvatarGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.playerAtlas = new BitmapTextureAtlas(textureManager, 608, 760,
				TextureOptions.DEFAULT);

		this.playerRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.playerAtlas, activity,
						"caveman_0.5.png", 0, 0, 16, 20);
	}

	private void createAnimalGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.dinosaurAtlas[0] = new BitmapTextureAtlas(textureManager, 864,
				864, TextureOptions.DEFAULT);

		this.dinosaurRegion[0] = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dinosaurAtlas[0], activity,
						"dino_green_0.5.png", 0, 0, 16, 16);

		this.dinosaurAtlas[1] = new BitmapTextureAtlas(textureManager, 864,
				864, TextureOptions.DEFAULT);

		this.dinosaurRegion[1] = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dinosaurAtlas[1], activity,
						"dino_red_0.5.png", 0, 0, 16, 16);

		this.pigAtlas = new BitmapTextureAtlas(textureManager, 243, 216,
				TextureOptions.DEFAULT);

		this.pigRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.pigAtlas, activity, "pig_0.5.png",
						0, 0, 9, 8);

		this.spiderAtlas = new BitmapTextureAtlas(textureManager, 226, 226,
				TextureOptions.DEFAULT);

		this.spiderRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.spiderAtlas, activity,
						"spider_0.3.png", 0, 0, 8, 8);

	}

	// private void createTreeGraphics() {
	// BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
	//
	// this.treesAtlas = new BitmapTextureAtlas(textureManager, 512, 640);
	//
	// BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.treesAtlas,
	// activity, "trees.png", 0, 0);
	// int x = 0;
	// int y = 0;
	// for (int i = 1; i <= 20; i++) {
	// // this.treeRegions[i-1] =
	// //
	// BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.treesAtlas,
	// // activity, "trees.png", x, y);
	// this.treeRegions[i - 1] = TextureRegionFactory.extractFromTexture(
	// this.treesAtlas, x, y, 128, 128);
	// x = x + 128;
	// if (i % 4 == 0) {
	// x = 0;
	// y = y + 128;
	// }
	// }
	// }

	public void loadQuestSceneGraphics() {
		questFrameTextureAtlas.load();
		questActiveTextureAtlas.load();
		questFinishedTextureAtlas.load();
	}

	public void unloadQuestSceneGraphics() {
		questFrameTextureAtlas.unload();
		questActiveTextureAtlas.unload();
		questFinishedTextureAtlas.unload();
	}

	private void createQuestSceneGraphics() {
		// BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/game/hud/");

		// this.questFrameTextureAtlas = new BitmapTextureAtlas(textureManager,
		// 590, 480, TextureOptions.DEFAULT);
		//
		// this.questFrameRegion = BitmapTextureAtlasTextureRegionFactory
		// .createFromAsset(questFrameTextureAtlas, activity,
		// "quest_frame.png", 0, 0);

		this.questFrameTextureAtlas = new BitmapTextureAtlas(textureManager,
				577, 469, TextureOptions.DEFAULT);

		this.questFrameRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(questFrameTextureAtlas, activity,
						"bg_popup.png", 0, 0);

		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/game/hud/icons/");

		this.questActiveTextureAtlas = new BitmapTextureAtlas(textureManager,
				51, 49, TextureOptions.DEFAULT);
		this.questActiveRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(questActiveTextureAtlas, activity,
						"not_finished.png", 0, 0);

		this.questFinishedTextureAtlas = new BitmapTextureAtlas(textureManager,
				49, 49, TextureOptions.DEFAULT);
		this.questFinishedRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(questFinishedTextureAtlas, activity,
						"finished.png", 0, 0);
	}

	private void loadGameAudio() {
		if (gameSoundsLoaded == false) {
			createGameSounds();
		}
		if (backgroundMusicGame == null) {
			createGameMusic();
		}
	}

	private void unloadGameAudio() {

	}

	private void createGameMusic() {
		MusicFactory.setAssetBasePath("mfx/");
		try {
			backgroundMusicGame = MusicFactory.createMusicFromAsset(
					engine.getMusicManager(), activity, "game_music.ogg");
			backgroundMusicGame.setVolume(0.5f);
			backgroundMusicGame.setLooping(true);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createGameSounds() {
		try {
			SoundFactory.setAssetBasePath("mfx/");

			hit = SoundFactory.createSoundFromAsset(soundManager, activity,
					"hit.wav");

			hit_false = SoundFactory.createSoundFromAsset(soundManager,
					activity, "hit_false.wav");

			collect = SoundFactory.createSoundFromAsset(soundManager, activity,
					"collect.wav");

			heartbeat = SoundFactory.createSoundFromAsset(soundManager,
					activity, "heartbeat.mp3");

			walk = SoundFactory.createSoundFromAsset(soundManager, activity,
					"walk.wav");
			walk.setVolume(0.7f);

			eat = SoundFactory.createSoundFromAsset(soundManager, activity,
					"eat.ogg");

			pain = SoundFactory.createSoundFromAsset(soundManager, activity,
					"pain.wav");

			openChildScene = SoundFactory.createSoundFromAsset(soundManager,
					activity, "childscene.ogg");
			openChildScene.setVolume(0.4f);

			questFinished = SoundFactory.createSoundFromAsset(soundManager,
					activity, "quest_finished.ogg");
			questFinished.setVolume(0.2f);

			dinoGrowl = SoundFactory.createSoundFromAsset(soundManager,
					activity, "dino_growl.wav");

			gameSoundsLoaded = true;
		} catch (IOException e) {
			e.printStackTrace();
			gameSoundsLoaded = false;
		}
	}

	// ---------------------------------------------
	// Fight resources
	// ---------------------------------------------

	private void createSpearGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		this.spearAtlas = new BitmapTextureAtlas(textureManager, 6, 45);
		this.spearRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.spearAtlas, activity, "spear.png", 0, 0);

		// BitmapTextureAtlasTextureRegionFactory.createFromAsset(
		// this.spearsAtlas, activity, "spear.png", 0, 0);
		//
		// for (int i = 0; i <= 7; i++) {
		// this.spearsRegions[i] = TextureRegionFactory.extractFromTexture(
		// this.spearsAtlas, i * 64, 0, 64, 48);
		// }
	}

	private void createFightSceneGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		this.fightDinoAtlas = new BitmapTextureAtlas(textureManager, 500, 451);
		this.fightDinoRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.fightDinoAtlas, activity,
						"dino_fight.png", 0, 0);
	}

	// ---------------------------------------------
	// GameHelp resources
	// ---------------------------------------------

	public void loadGameHelpResources() {
		loadGameHelpGraphics();
	}

	public void unloadGameHelpResources() {
		unloadGameHelpGraphics();
	}

	private void createGameHelpGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/help/");

		helpTextureAtlas = new ArrayList<BitmapTextureAtlas>();
		helpRegion = new ArrayList<ITextureRegion>();

		for (int i = 0; i < 2; i++) {
			BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager,
					800, 480, BitmapTextureFormat.RGBA_4444);
			helpTextureAtlas.add(i, atlas);

			String fileName = "help_0" + (i + 1) + ".jpg";

			ITextureRegion region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(atlas, activity, fileName, 0, 0);
			helpRegion.add(i, region);
		}
	}

	private void loadGameHelpGraphics() {
		if (helpTextureAtlas == null) {
			createGameHelpGraphics();
		}
		for (BitmapTextureAtlas atlas : helpTextureAtlas) {
			atlas.load();
		}
	}

	private void unloadGameHelpGraphics() {
		for (BitmapTextureAtlas atlas : helpTextureAtlas) {
			atlas.unload();
		}
	}

	// ---------------------------------------------
	// GameShop resources
	// ---------------------------------------------

	public void loadGameShopResources() {
		loadGameShopGraphics();
	}

	public void unloadGameShopResources() {
		unloadGameShopGraphics();
	}

	private void createGameShopGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/shop/");

		this.shopTextureAtlas = new BitmapTextureAtlas(textureManager, 650,
				400, TextureOptions.DEFAULT);
		this.shopRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(shopTextureAtlas, activity,
						"shop_placeholder.png", 0, 0);
	}

	private void loadGameShopGraphics() {
		if (shopTextureAtlas == null) {
			createGameShopGraphics();
		}
		shopTextureAtlas.load();
	}

	private void unloadGameShopGraphics() {
		shopTextureAtlas.unload();
	}

	// ---------------------------------------------
	// HUD resources
	// ---------------------------------------------

	public void loadHUDResources() {
		loadHUDGraphics();
		this.hud = SceneHUD.getInstance();
		this.camera.setHUD(this.hud);
	}

	public void unloadHUDResources() {
		this.camera.setHUD(null);
		this.hud.detachSelf();
		if (!this.hud.isDisposed()) {
			this.hud.dispose();
		}
		unloadHUDGraphics();
	}

	private void createHUDGraphics() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/game/hud/");

		this.controlTextureAtlas = new BitmapTextureAtlas(textureManager, 256,
				128, TextureOptions.BILINEAR);
		this.controlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(controlTextureAtlas, activity,
						"onscreen_control_base.png", 0, 0);
		this.controlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(controlTextureAtlas, activity,
						"onscreen_control_knob.png", 128, 0);

		this.hudBerryAtlas = new BitmapTextureAtlas(textureManager, 50, 39,
				TextureOptions.BILINEAR);
		this.hudBerryRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(hudBerryAtlas, activity, "berries_small.png",
						0, 0);

		this.bgBarsAtlas = new BitmapTextureAtlas(textureManager, 134, 44,
				TextureOptions.BILINEAR);
		this.bgBarsRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bgBarsAtlas, activity, "bg_bars2.png", 0, 0);

	}

	private void createHUDButtonIconGraphics() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/game/hud/icons/");

		this.hudHelpIconAtlas = new BitmapTextureAtlas(textureManager, 50, 52,
				TextureOptions.BILINEAR);
		this.hudHelpIconRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(hudHelpIconAtlas, activity, "help.png", 0, 0);

		this.hudQuestListIconAtlas = new BitmapTextureAtlas(textureManager, 44,
				44, TextureOptions.BILINEAR);
		this.hudQuestListIconRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(hudQuestListIconAtlas, activity, "quests.png",
						0, 0);

	}

	private void loadHUDGraphics() {
		if (controlTextureAtlas == null || hudBerryAtlas == null) {
			createHUDGraphics();
			createHUDButtonIconGraphics();
		}
		controlTextureAtlas.load();
		hudBerryAtlas.load();

		hudHelpIconAtlas.load();
		hudQuestListIconAtlas.load();
		// hudShopIconAtlas.load();
		bgBarsAtlas.load();
	}

	private void unloadHUDGraphics() {
		controlTextureAtlas.unload();
		hudBerryAtlas.unload();
		hudHelpIconAtlas.unload();
		hudQuestListIconAtlas.unload();
		// hudShopIconAtlas.unload();
		bgBarsAtlas.unload();
	}

	// ---------------------------------------------
	// GameEnd resources
	// ---------------------------------------------

	public void loadGameEndResources() {
		loadGameEndGraphics();
	}

	public void unloadGameEndResources() {
		unloadGameEndGraphics();
	}

	private void createGameEndGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		game_end_atlas = new BitmapTextureAtlas(textureManager, 1024, 576,
				TextureOptions.DEFAULT);
		game_end_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(game_end_atlas, activity, "game_end.png", 0, 0);
	}

	private void loadGameEndGraphics() {
		if (game_end_atlas == null) {
			createGameEndGraphics();
		}
		game_end_atlas.load();
	}

	private void unloadGameEndGraphics() {
		game_end_atlas.unload();
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
			TextureManager textureManager, SoundManager soundManager) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
		getInstance().textureManager = textureManager;
		getInstance().soundManager = soundManager;
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