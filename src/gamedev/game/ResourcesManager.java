package gamedev.game;

import gamedev.scenes.LevelScene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseActivity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class ResourcesManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public BaseActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    public TextureManager textureManager;
    
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------

    public BitmapTextureAtlas playerAtlas;
    public ITiledTextureRegion playerRegion;
    
    
    //---------------------------------------------
    // Physic
    //---------------------------------------------
    
	public PhysicsWorld physicsWorld;

    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    public void loadPlayerGraphics() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

    	this.playerAtlas = new BitmapTextureAtlas(
				getInstance().textureManager, 768, 2400, TextureOptions.DEFAULT);

    	this.playerRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.playerAtlas, getInstance().activity,
						"player_sprite.png", 0, 0, 8, 25);
    	this.playerAtlas.load();
    }
    
    public void loadPhysics() {
    	this.physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0),false, 8, 1);
    }
    
    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
    }
    
    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }
    
    private void loadMenuGraphics()
    {
        
    }
    
    private void loadMenuAudio()
    {
        
    }

    private void loadGameGraphics()
    {

    }
    
    private void loadGameFonts()
    {
        
    }
    
    private void loadGameAudio()
    {
        
    }
    
    public void loadSplashScreen()
    {
    
    }
    
    public void unloadSplashScreen()
    {

    }
    
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, BaseActivity activity, BoundCamera camera, VertexBufferObjectManager vbom, TextureManager textureManager)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
        getInstance().textureManager = textureManager;
        
        // We also load physics and player.. //TODO Move out from here, since a Menu scene does not require them in memory
        getInstance().loadPhysics();
        getInstance().loadPlayerGraphics();
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}