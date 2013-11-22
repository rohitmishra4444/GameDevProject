package gamedev.levels;

import java.util.Random;
import gamedev.objects.Dinosaur;
import gamedev.objects.Tree;
import gamedev.scenes.LevelScene;

public class Level1 extends LevelScene {

	public Level1() {
		// Level as integer. The filename is then created with:
		// "level" + levelId + ".tmx"
		super(1);
		this.player.body.setTransform(2, 2, 0);
//		this.createScene();
	}

//	public void createScene() {
//		Random r = new Random();
//		for (int i = 0; i < 10; i++) {
//			this.attachChild(new Dinosaur(r.nextInt(1024), r.nextInt(1204)));
//		}
//		for (int i = 0; i<50; i++) {
//			this.attachChild(new Tree(r.nextInt(2048), r.nextInt(2048), r.nextInt(20)));
//		}
		
//	}

}
