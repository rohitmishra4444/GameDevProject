package gamedev.levels;

import gamedev.objects.Dinosaur;
import gamedev.scenes.LevelScene;

import java.util.Random;

public class Level1 extends LevelScene {

	public Level1() {
		// Level as integer. The filename is then created with:
		// "level" + levelId + ".tmx"
		super(1);
		this.createScene();
	}

	public void createScene() {
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			this.attachChild(new Dinosaur(r.nextInt(1024), r.nextInt(1204)));
		}
		// this.attachChild(new Tree(300, 300));
	}

}
