package gamedev.quests;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class QuestBuildBridge extends Quest {
	
	private final static float RECTANGLE_X = 37*32;
	private final static float RECTANGLE_Y = 14*32;
	private final static float RECTANGLE_HEIGHT = 100;
	private final static float RECTANGLE_WIDTH = 20;
	
	protected Body body;
	protected Rectangle rectangle;
	
	public QuestBuildBridge(Scene map) {
		super(map);
		this.title = "Cross the River";
		this.description = "I need to find something so I can go to the other side of the river!";
		this.rectangle = new Rectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, ResourcesManager.getInstance().vbom);
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0,0, 0);
		this.body = PhysicsFactory.createBoxBody(ResourcesManager.getInstance().physicsWorld, this.rectangle, BodyType.StaticBody, boxFixtureDef);
		this.map.attachChild(this.rectangle);
		
		// TODO Add static objects to collect...
		
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
