package gamedev.quests;

import gamedev.game.ResourcesManager;
import gamedev.objects.Inventory;
import gamedev.objects.Tree;
import gamedev.scenes.GameMapScene;

import org.andengine.entity.primitive.Rectangle;
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
	protected Tree tree;
	protected Tree tree2;

	public QuestPassCanyon(GameMapScene map) {
		super(map);
		this.title = "Find a way through the canyon";
		this.description = "I need something to destroy those trees!";

		ResourcesManager res = ResourcesManager.getInstance();

		// Body and trees
		this.rectangle = new Rectangle(RECTANGLE_X, RECTANGLE_Y,
				RECTANGLE_WIDTH, RECTANGLE_HEIGHT, res.vbom);
		this.rectangle.setVisible(false);
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0,
				0);
		this.body = PhysicsFactory.createBoxBody(res.physicsWorld,
				this.rectangle, BodyType.StaticBody, boxFixtureDef);
		res.physicsWorld.registerPhysicsConnector(new PhysicsConnector(
				rectangle, body, false, false));
		map.attachChild(rectangle);
		this.tree = new Tree(68 * 32, 4.5f * 32);
		this.tree2 = new Tree(68 * 32, 6f * 32);
		this.map.attachChild(tree);
		this.map.attachChild(tree2);
	}

	public void setActive(boolean active) {
		super.setActive(active);
		ResourcesManager.getInstance().activity.toastOnUIThread(
				this.description, Toast.LENGTH_LONG);
	}

	@Override
	public void onFinish() {
		ResourcesManager.getInstance().removeSpriteAndBody(rectangle);
		ResourcesManager.getInstance().removeSpriteAndBody(tree);
		ResourcesManager.getInstance().removeSpriteAndBody(tree2);
	}

	@Override
	public String getStatus() {
		return "I need a tool to destroy the trees... let's keep searching!";
	}

	@Override
	public String statusForQuestScene() {
		Inventory inventory = ResourcesManager.getInstance().avatar
				.getInventory();
		QuestCatchPig quest = (QuestCatchPig) this.map.getQuest(1);

		if (inventory.contains(quest.getAxe())) {
			return "I have an axe";
		} else {
			return "Tool is missing";
		}
	}

	@Override
	public boolean isCompleted() {
		Inventory inventory = ResourcesManager.getInstance().avatar
				.getInventory();
		QuestCatchPig quest = (QuestCatchPig) this.map.getQuest(1);
		return inventory.contains(quest.getAxe());
		// return true;
	}

	public Rectangle getRectangle() {
		return this.rectangle;
	}

}
