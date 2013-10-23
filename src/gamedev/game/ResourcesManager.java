package gamedev.game;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseActivity;

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

    public BitmapTextureAtlas gameTextureAtlas;
    public ITiledTextureRegion playerRegion;

    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

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

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

    	this.gameTextureAtlas = new BitmapTextureAtlas(
				getInstance().textureManager, 982, 688, TextureOptions.DEFAULT);

    	this.playerRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.gameTextureAtlas, activity,
						"caveman_walking.png", 0, 0, 10, 7);
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
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}