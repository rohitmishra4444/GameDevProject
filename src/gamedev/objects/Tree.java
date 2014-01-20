package gamedev.objects;

import gamedev.game.ResourcesManager;

public class Tree extends StaticObject {

	public Tree(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().treeRegion);
	}

	@Override
	protected void createPhysics() { }

}
