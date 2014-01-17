package gamedev.scenes;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.game.SceneManager.SceneType;
import gamedev.game.TmxLevelLoader;
import gamedev.objects.Avatar;
import gamedev.quests.Quest;
import gamedev.quests.QuestBuildBridge;
import gamedev.quests.QuestCatchPig;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
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
import org.andengine.util.debug.Debug;

import android.widget.Toast;

/**
 * Base class for all levels
 * 
 */
public class GameMapScene extends BaseScene {

	// TMX Map containing the Level
	protected TMXTiledMap mTMXTiledMap;
	protected String tmxFileName;

	private Sprite gameEndPortal;
	private boolean gameEndPortalContact = false;

	protected ArrayList<Quest> quests = new ArrayList<Quest>();

	public GameMapScene() {
		// Call BaseScene without calling createScene because here we need some
		// stuff initialized before
		super(false);
		this.tmxFileName = "final_map.tmx";
		this.createScene();
	}

	@Override
	public void createScene() {
		this.createMap();
		this.connectPhysics();

		if (resourcesManager.avatar == null) {
			System.out.println("Avatar is null!");
			resourcesManager.avatar = new Avatar();
		}

		// Check if the player is already has a parent (avoid
		// assertEntityHasNoParent IllegalStateException)
		// This is needed for the back button during gameplay (for restarting a
		// new game).
		if (resourcesManager.avatar.hasParent()) {
			IEntity parentEntity = resourcesManager.avatar.getParent();
			parentEntity.detachChild(resourcesManager.avatar);
		}
		resourcesManager.avatar.getBody().setTransform(200 / 32, 200 / 32, 0);
		this.attachChild(resourcesManager.avatar);
		createQuests();
		createGameEndPortal();
	}

	protected void connectPhysics() {
		this.registerUpdateHandler(this.resourcesManager.physicsWorld);
	}

	protected void createMap() {
		// Try to load the tmx file
		try {
			final TMXLoader tmxLoader = new TMXLoader(
					this.activity.getAssets(), this.engine.getTextureManager(),
					TextureOptions.DEFAULT, this.vbom,
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

	public void createQuests() {
		this.quests.add(new QuestBuildBridge(this));
		this.quests.add(new QuestCatchPig(this));
	}

	private void createGameEndPortal() {
		// TODO: Game end portal should be created in TmxLevelLoader class. This
		// will be done by dwettstein.
		// This is only for testing the GameEndScene.
		gameEndPortal = new Sprite(1500, 300,
				resourcesManager.gameEndPortalRegion, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (resourcesManager.avatar.collidesWith(this)
						&& gameEndPortalContact == false) {
					gameEndPortalContact = true;

					int numberOfFinishedQuests = 0;
					for (Quest quest : quests) {
						if (quest.isFinished()) {
							numberOfFinishedQuests++;
						}
					}
					if (numberOfFinishedQuests == quests.size()) {
						SceneManager.getInstance().loadGameEndScene(engine);
					} else {
						ResourcesManager.getInstance().activity
								.toastOnUIThread(
										"Hmm... the portal is not working. Did I complete all quests?",
										Toast.LENGTH_SHORT);
					}
				} else if (!this
						.collidesWith(ResourcesManager.getInstance().avatar)) {
					gameEndPortalContact = false;
				}
			}
		};
		gameEndPortal.setAlpha(0.8f);
		gameEndPortal.setScale(0.5f);
		this.attachChild(gameEndPortal);
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
		if (!this.isDisposed()) {
			this.dispose();
		}
	}

	public void addQuest(Quest quest) {
		this.quests.add(quest);
	}

	public ArrayList<Quest> getQuests() {
		return quests;
	}

	public void setQuests(ArrayList<Quest> quests) {
		this.quests = quests;
	}

	public Quest getQuest(int i) {
		return this.quests.get(i);
	}

}
