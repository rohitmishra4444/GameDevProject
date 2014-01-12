package gamedev.objects;

import gamedev.game.ResourcesManager;

public class Wood extends CollectableObject {

	public Wood(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().woodRegion);
	}

}
