package gamedev.levels;
import java.util.Random;

import gamedev.objects.Dinosaur;
import gamedev.objects.DinosaurWithPhysic;
import gamedev.objects.Tree;
import gamedev.scenes.LevelScene;

public class Level1 extends LevelScene {
	
	public Level1() {
		super("level1.tmx");
		this.player.setPosition(10,10);
		this.createScene();
	}
	
	public void createScene() {
		Random r = new Random();
		for (int i=0; i<20; i++) {
			this.attachChild(new DinosaurWithPhysic(r.nextInt(1024),r.nextInt(1204)));			
		}
	}
	
}
