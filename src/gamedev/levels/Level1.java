package gamedev.levels;

import gamedev.objects.Player;
import gamedev.scenes.LevelScene;

public class Level1 extends LevelScene {

	
	public Level1() {
		super();
		this.player = new Player(10, 10);
		this.tmxFileName = "Game_Map_Level_1.tmx";
	}
	
}
