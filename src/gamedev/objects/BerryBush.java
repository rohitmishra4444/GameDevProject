package gamedev.objects;

import java.util.ArrayList;

public class BerryBush {

	private float x;
	private float y;
	private ArrayList<Berry> berries;
	private long lastRipenTime;
	// First number is time to wait in seconds (1s = 1000000000ns):
	private final static long WAIT_TIME_FOR_RIPEN_IN_NS = 180 * 1000000000l;

	public BerryBush(float pX, float pY) {
		this.x = pX;
		this.y = pY;
		this.berries = new ArrayList<Berry>();
		addNewBerry();
	}

	private void addNewBerry() {
		Berry berry = new Berry();
		this.berries.add(berry);
	}

	public boolean isEmpty() {
		long currentTime = System.nanoTime();
		long timeSinceLastRipen = currentTime - lastRipenTime;

		if (timeSinceLastRipen > WAIT_TIME_FOR_RIPEN_IN_NS
				&& this.berries.isEmpty()) {
			addNewBerry();
		}

		return this.berries.isEmpty();
	}

	public Berry getBerry() {
		if (this.berries.isEmpty()) {
			return null;
		} else {
			lastRipenTime = System.nanoTime();
			return this.berries.remove(0);
		}
	}

	public String toString() {
		return "BerryBush";
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

}
