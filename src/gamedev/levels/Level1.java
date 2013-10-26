package gamedev.levels;
import gamedev.objects.Dinosaur;
import gamedev.scenes.LevelScene;

public class Level1 extends LevelScene {
	
	public Level1() {
		super("Game_Map_Level_1.tmx");
		this.player.setPosition(10,10);
		this.createScene();
	}
	
	public void createScene() {
		Dinosaur dino1 = new Dinosaur(200, 200);
		this.attachChild(dino1);
		Dinosaur dino2 = new Dinosaur(600,600);
		this.attachChild(dino2);
	}
	
}
