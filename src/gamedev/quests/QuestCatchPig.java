package gamedev.quests;

import java.util.ArrayList;

import android.widget.Toast;

import gamedev.ai.RandomMoveStrategy;
import gamedev.game.ResourcesManager;
import gamedev.objects.Dinosaur;
import gamedev.objects.Pig;
import gamedev.scenes.GameMapScene;

public class QuestCatchPig extends Quest {
	
	protected Pig pig;
	protected ArrayList<String> conversation = new ArrayList<String>();
	
	public QuestCatchPig(GameMapScene map) {
		super(map);
		this.title = "Cross the forest";
		this.description = "Catch the pig and bring it to the cave woman";
		
		this.conversation.add("Hi, my name is Detlef. I need to go to the other side of the forest, can you help me?");
		this.conversation.add("Hmm... My back hurts, I'm too old. And even worse, I lost my pig! Can you catch it for me? I will give you an axe to destroy the trees");
		this.conversation.add("OK! Let's find this pig...");
		
		// Create the pig :-D
		this.pig = new Pig(500,500);
		RandomMoveStrategy rm = new RandomMoveStrategy(pig, 200, 800, 1, 100, 1000, 100, 1000);
		pig.setMoveStrategy(rm);
		map.attachChild(pig);
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			for (String s : this.conversation) {
				ResourcesManager.getInstance().activity.toastOnUIThread(s, Toast.LENGTH_LONG);
			}
		}
	}
	
	@Override
	public void onFinish() {
		ResourcesManager.getInstance().activity.toastOnUIThread("Thank you very much young man! Here is your axe, as promised. Be safe!", Toast.LENGTH_LONG);
		// TODO Remove the trees and the body so the player can pass the forest :)
	}

	@Override
	public String getStatus() {
		return "Bring me the pig and you'll receive an axe!";
	}

	@Override
	public boolean isCompleted() {
		// This flag is set to true from the BodiesContact Listener when we catched the pig
		return this.isCompleted;
	}

}
