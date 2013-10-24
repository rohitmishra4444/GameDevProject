package gamedev.levels;
import gamedev.scenes.LevelScene;

public class Level1 extends LevelScene {
	
	public Level1() {
		super("Game_Map_Level_1.tmx");
		this.player.setPosition(10,10);
	}
	
	public void createScene() {

	}
	
}
