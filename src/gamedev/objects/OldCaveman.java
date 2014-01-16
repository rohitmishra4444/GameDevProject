package gamedev.objects;

import gamedev.game.ResourcesManager;

public class OldCaveman extends StaticObject {

	public OldCaveman(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().oldCavemanRegion);
	}

	@Override
	protected void createPhysics() {
		// TODO Depending on position, this may be necessary...
	}

}
