package gamedev.objects;

import java.util.ArrayList;

public class BerryBush {

	float x;
	float y;

	ArrayList<Berry> berries;

	boolean isEmpty;

	public BerryBush(float pX, float pY) {
		this.x = pX;
		this.y = pY;

		this.berries = new ArrayList<Berry>();
		Berry berry = new Berry();
		this.berries.add(berry);
	}

	public boolean isEmpty() {
		return this.berries.isEmpty();
	}

	public Berry getBerry() {
		if (this.berries.isEmpty()) {
			return null;
		} else {
			return this.berries.remove(0);
		}
	}

	public String toString() {
		return "BerryBush";
	}

}
