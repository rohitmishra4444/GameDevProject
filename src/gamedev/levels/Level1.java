package gamedev.levels;

import gamedev.scenes.LevelScene;

public class Level1 extends LevelScene {

	private static final int LEVEL_ID = 1;

	public Level1() {
		// Level as integer. The filename is then created with:
		// "level" + levelId + ".tmx"
		super(LEVEL_ID);
		// this.player.body.setTransform(2, 2, 0);
	}

	public void createScene() {
		super.createScene();
		// Now define quests or other stuff not already loaded with the tmx File
	}

	public void disposeScene() {
		super.disposeScene();
	}

}
