package gamedev.quests;

import gamedev.ai.RandomMoveStrategy;
import gamedev.ai.SimpleMoveStrategy;
import gamedev.game.ResourcesManager;
import gamedev.objects.AnimatedObject.GameState;
import gamedev.objects.Axe;
import gamedev.objects.OldCaveman;
import gamedev.objects.Pig;
import gamedev.scenes.GameMapScene;

import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class QuestCatchPig extends Quest {

	protected Pig pig;
	protected Pig pig2;
	protected ArrayList<String> conversation = new ArrayList<String>();
	protected OldCaveman caveman;
	protected Axe axe;
	protected Body body;
	protected Rectangle rect;
	protected RandomMoveStrategy moveStrategyPig1;
	protected RandomMoveStrategy moveStrategyPig2;
	private final static int N_PIGS_GARDEN = 3;
	
	public QuestCatchPig(GameMapScene map) {
		super(map);
		this.title = "Catch the pigs and bring them back to the cave man";
		this.description = "Save them from the dinosaurs";

		this.conversation.add("Hey young cave man! Please help me...two of my pigs broke out this morning!");
		this.conversation
				.add("I'm scared that the dinosaurs will kill them... Can you catch them for me?");
		this.conversation
				.add("I have some tools in my garden, maybe there's something useful for you?");

		// Create the pig :-D
		this.pig = new Pig(500, 500);
		moveStrategyPig1 = new RandomMoveStrategy(pig, 75, 300, 0, 42*32, 54*32, 5*32, 17*32);
		pig.setMoveStrategy(moveStrategyPig1);
		map.attachChild(pig);

		this.pig2 = new Pig(500, 500);
		pig2.setMoveStrategy(new RandomMoveStrategy(pig2, 75, 300, 0, 40*32, 63*32, 19*32, 35*32));
		map.attachChild(pig2);
		
		// Add some pigs in the garden
		for (int i=0; i<N_PIGS_GARDEN; i++) {
			Pig pig = new Pig(49*32, 41*32);
			pig.setVelocity(0.8f);
			pig.setMoveStrategy(new RandomMoveStrategy(pig, 2*32, 5*32, 2, 44*32, 55*32, 39*32, 46*32));
			this.map.attachChild(pig);
		}
		
		// Caveman
		this.caveman = new OldCaveman(48 * 32, 37 * 32);
		map.attachChild(caveman);
		rect = new Rectangle(caveman.getX() - 25, caveman.getY(), 100, 10,
				ResourcesManager.getInstance().vbom);
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0,
				0);
		this.body = PhysicsFactory.createBoxBody(
				ResourcesManager.getInstance().physicsWorld, rect,
				BodyType.StaticBody, boxFixtureDef);
		ResourcesManager.getInstance().physicsWorld
				.registerPhysicsConnector(new PhysicsConnector(rect, body,
						false, false));
		rect.setVisible(false);
		map.attachChild(rect);
		this.axe = new Axe(59 * 32, 41 * 32);
		map.attachChild(axe);
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
		ResourcesManager.getInstance().activity.toastOnUIThread(
				"Thank you very much young man! I'm so lucky...!",
				Toast.LENGTH_LONG);
		ResourcesManager.getInstance().activity.toastOnUIThread("Be safe...",
				Toast.LENGTH_LONG);
		ResourcesManager.getInstance().removeSpriteAndBody(rect);
		this.caveman.setPosition(caveman.getX() - 32f, caveman.getY() - 32);
		pig.setMoveStrategy(new SimpleMoveStrategy(pig, new Vector2(58 * 32,
				39 * 32), GameState.WALKING));
		pig2.setMoveStrategy(new SimpleMoveStrategy(pig2, new Vector2(55 * 32,
				40 * 32), GameState.WALKING));
	}

	@Override
	public String getStatus() {
		return "Bring me my pigs please! Both!";
	}

	@Override
	public String statusForQuestScene() {
		int count = 0;
		if (pig.isCatched()) {
			count++;
		}
		if (pig2.isCatched()) {
			count++;
		}
		return Integer.toString(count) + "/2" + " pig";
	}

	@Override
	public boolean isCompleted() {
		return (pig.isCatched() && pig2.isCatched());
//		 return true;
	}

	public Axe getAxe() {
		return this.axe;
	}

	public Pig getPig1() {
		return this.pig;
	}

	public Pig getPig2() {
		return this.pig2;
	}

	public void loosePig(Pig pig) {
		pig.setCatched(false);
		if (this.moveStrategyPig1.getObject() == pig) {
			pig.setMoveStrategy(new RandomMoveStrategy(pig, 75, 300, 0, 41*32, 55*32, 4*32, 18*32));
		} else {
			pig.setMoveStrategy(new RandomMoveStrategy(pig, 75, 300, 0, 39*32, 64*32, 18*32, 36*32));
		}
	}

}
