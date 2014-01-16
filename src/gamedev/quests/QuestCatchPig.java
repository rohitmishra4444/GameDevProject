package gamedev.quests;

import gamedev.ai.RandomMoveStrategy;
import gamedev.objects.Dinosaur;
import gamedev.objects.Pig;
import gamedev.scenes.GameMapScene;

public class QuestCatchPig extends Quest {
	
	protected Pig pig;
	
	public QuestCatchPig(GameMapScene map) {
		super(map);
		this.title = "Catch the pig and bring it back to the cave woman";
		this.description = "Damn this pig is fast! Let's go...";
		this.pig = new Pig(500,500);
		RandomMoveStrategy rm = new RandomMoveStrategy(pig, 200, 800, 1, 100, 1000, 100, 1000);
		pig.setMoveStrategy(rm);
		map.attachChild(pig);
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getStatus() {
		return "Bring me the pig and you'll receive an axe!";
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

}
