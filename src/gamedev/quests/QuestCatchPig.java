package gamedev.quests;

import gamedev.ai.RandomMoveStrategy;
import gamedev.game.ResourcesManager;
import gamedev.objects.OldCaveman;
import gamedev.objects.Pig;
import gamedev.scenes.GameMapScene;

import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import android.widget.Toast;

public class QuestCatchPig extends Quest {
	
	private final static float RECTANGLE_X = 68 * 32;
	private final static float RECTANGLE_Y = 5 * 32;
	private final static float RECTANGLE_HEIGHT = 175;
	private final static float RECTANGLE_WIDTH = 10;
	
	protected Pig pig;
	protected ArrayList<String> conversation = new ArrayList<String>();
	protected OldCaveman caveman;
	protected Body body;
	protected Rectangle rectangle;
	
	public QuestCatchPig(GameMapScene map) {
		super(map);
		this.title = "Find a way through the canyon";
		this.description = "Catch the pig and bring it back to the cave man";

		this.conversation
				.add("Hi, my name is Detlef. I need to pass the canyon, can you help me?");
		this.conversation.add("Hmm... My back hurts, I'm so old!");
		this.conversation.add("And even worse, one of my pigs broke out this morning...");
		this.conversation
				.add("I'm scared that the dinosaurs will kill it... Can you catch it for me?");
		this.conversation
				.add("I will give you an axe so you can destroy the trees and go trought the canyon.");
		this.conversation.add("OK! Let's find this pig...");

		// Create the pig :-D
		this.pig = new Pig(500, 500);
		RandomMoveStrategy rm = new RandomMoveStrategy(pig, 100, 300, 0, 42*32, 65*32, 11*32, 43*32);
		pig.setMoveStrategy(rm);
		map.attachChild(pig);
		
		// Caveman
		this.caveman = new OldCaveman(48*32, 42*32);
		map.attachChild(caveman);
		
		// Body and trees...
		ResourcesManager res = ResourcesManager.getInstance();
		this.rectangle = new Rectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, res.vbom);
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		this.body = PhysicsFactory.createBoxBody(res.physicsWorld,
				this.rectangle, BodyType.StaticBody, boxFixtureDef);
		res.physicsWorld.registerPhysicsConnector(new PhysicsConnector(
				rectangle, body, false, false));
		map.attachChild(rectangle);
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			for (String s : this.conversation) {
				ResourcesManager.getInstance().activity.toastOnUIThread(s,
						Toast.LENGTH_LONG);
			}
		}
	}

	@Override
	public void onFinish() {
		ResourcesManager.getInstance().activity
				.toastOnUIThread(
						"Thank you very much young man! Here is your axe, as promised. Be safe!",
						Toast.LENGTH_LONG);
		// TODO Remove the trees and the body so the player can pass the forest
		// :)
		ResourcesManager.getInstance().removeSpriteAndBody(rectangle);
	}

	@Override
	public String getStatus() {
		return "Bring me the pig and you'll receive an axe!";
	}

	@Override
	public boolean isCompleted() {
		// This flag is set to true from the BodiesContact Listener when we
		// catched the pig
//		return this.isCompleted;
		return true;
	}

}
