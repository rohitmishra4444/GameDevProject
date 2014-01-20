package gamedev.quests;

import gamedev.game.ResourcesManager;
import gamedev.objects.Avatar;
import gamedev.objects.Inventory;
import gamedev.objects.Wood;
import gamedev.scenes.GameMapScene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import android.widget.Toast;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class QuestPassCanyon extends Quest {

	private final static float RECTANGLE_X = 68 * 32;
	private final static float RECTANGLE_Y = 5 * 32;
	private final static float RECTANGLE_HEIGHT = 175;
	private final static float RECTANGLE_WIDTH = 10;
	
	protected Body body;
	protected Rectangle rectangle;
	
	public QuestPassCanyon(GameMapScene map) {
		super(map);
		this.title = "Find a way through the canyon";
		this.description = "I need something to destroy those trees!";
		
		ResourcesManager res = ResourcesManager.getInstance();
		
		// Body and trees
		this.rectangle = new Rectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, res.vbom);
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		this.body = PhysicsFactory.createBoxBody(res.physicsWorld,
				this.rectangle, BodyType.StaticBody, boxFixtureDef);
		res.physicsWorld.registerPhysicsConnector(new PhysicsConnector(
				rectangle, body, false, false));
		map.attachChild(rectangle);
	
	}

	public void setActive(boolean active) {
		super.setActive(active);
		ResourcesManager.getInstance().activity.toastOnUIThread(
				this.description, Toast.LENGTH_SHORT);
	}

	@Override
	public void onFinish() {
		ResourcesManager.getInstance().removeSpriteAndBody(rectangle);
	}

	@Override
	public String getStatus() {
		return "I need a tool to destroy the trees... let's keep seraching!";
	}

	@Override
	public boolean isCompleted() {
		Inventory inventory = ResourcesManager.getInstance().avatar.getInventory();
//		return (inventory.contains("AXE"));
		return false;
	}

	public Rectangle getRectangle() {
		return this.rectangle;
	}
	

}
