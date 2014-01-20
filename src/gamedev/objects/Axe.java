package gamedev.objects;

import gamedev.game.ResourcesManager;

public class Axe extends CollectableObject {

	public Axe(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().axeRegion);
	}
	
	public String toString() {
		return "Axe";
	}
	
}
