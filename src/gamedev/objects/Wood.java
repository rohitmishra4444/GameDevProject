package gamedev.objects;

import java.util.Random;

import gamedev.game.ResourcesManager;

public class Wood extends CollectableObject {

	public Wood(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().woodRegion);
		Random r = new Random();
		this.setRotation((float) (45 + r.nextFloat() * 90));
	}
	
	public String toString() {
		return "Wood";
	}
	
}
