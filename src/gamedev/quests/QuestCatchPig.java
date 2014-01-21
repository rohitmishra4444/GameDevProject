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

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class QuestCatchPig extends Quest {

	protected Pig pig;
	protected Pig pig2;
	protected ArrayList<String> conversation = new ArrayList<String>();
	protected OldCaveman caveman;
	protected Axe axe;

	public QuestCatchPig(GameMapScene map) {
		super(map);
		this.title = "Catch the pigs and bring them back to the cave man";
		this.description = "Save them from the dinosaurs";

		this.conversation
				.add("Hi, my name is Detlef. I need to pass the canyon, can you help me?");
		this.conversation.add("Hmm... My back hurts, I'm so old!");
		this.conversation
				.add("And even worse, two of my pigs broke out this morning...");
		this.conversation
				.add("I'm scared that the dinosaurs will kill them... Can you catch them for me?");
		this.conversation
				.add("I will give you my axe so you can destroy the trees and go trought the canyon.");

		// Create the pig :-D
		this.pig = new Pig(500, 500);
		RandomMoveStrategy rm = new RandomMoveStrategy(pig, 100, 300, 0,
				42 * 32, 65 * 32, 11 * 32, 43 * 32);
		pig.setMoveStrategy(rm);
		map.attachChild(pig);

		this.pig2 = new Pig(500, 500);
		pig2.setMoveStrategy(new RandomMoveStrategy(pig2, 100, 300, 0, 42 * 32,
				65 * 32, 11 * 32, 43 * 32));
		map.attachChild(pig2);

		// Caveman
		this.caveman = new OldCaveman(48 * 32, 37 * 32);
		map.attachChild(caveman);

		this.axe = new Axe(59 * 32, 41 * 32);
		map.attachChild(axe);
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			for (String s : this.conversation) {
				ResourcesManager.getInstance().activity.toastOnUIThread(s,
						Toast.LENGTH_SHORT);
			}
		}
	}

	@Override
	public void onFinish() {
		ResourcesManager.getInstance().activity.toastOnUIThread(
				"Thank you very much young man! I'm so lucky...",
				Toast.LENGTH_SHORT);
		ResourcesManager.getInstance().activity.toastOnUIThread(
				"Go grab your axe!", Toast.LENGTH_SHORT);
		ResourcesManager.getInstance().activity.toastOnUIThread("Be safe...",
				Toast.LENGTH_SHORT);
		Body b = caveman.getBody();
		Vector2 v = Vector2Pool.obtain(b.getPosition());
		b.setTransform(v.x - 3f, v.y - 2f, b.getAngle());
		pig.setMoveStrategy(new SimpleMoveStrategy(pig, new Vector2(58 * 32,
				43 * 32), GameState.WALKING));
		pig2.setMoveStrategy(new SimpleMoveStrategy(pig, new Vector2(55 * 32,
				44 * 32), GameState.WALKING));
	}

	@Override
	public String getStatus() {
		return "Bring me my pigs please! Both!";
	}

	@Override
	public boolean isCompleted() {
		return (pig.isCatched() && pig2.isCatched());
		// return true;
	}

	public Axe getAxe() {
		return this.axe;
	}

}
