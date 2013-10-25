package gamedev.game;

import com.badlogic.gdx.math.Vector2;

public class Direction {

	public static final int WEST = 0;
	public static final int SOUTH_WEST = 1;
	public static final int SOUTH_EAST = 2;
	public static final int SOUTH = 3;
	public static final int NORTH_WEST = 4;
	public static final int NORTH_EAST = 5;
	public static final int NORTH = 6;
	public static final int EAST = 7;

	/**
	 * Calculate the direction of from the given vector
	 * @param v1 Start-Position
	 * @param v3 End-Position
	 * @return
	 */
	public static int getDirectionFromVectors(Vector2 v1, Vector2 v3) {
		//TODO Implement
		return 0;
	}
	
	/**
	 * Get a direction based on degree
	 * @param degree
	 * @return
	 */
	public static int getDirectionFromDegree(float degree) {
		if (-22.5 <= degree && degree <= 22.5
				&& degree != 0) {
			// Direction: S
			return SOUTH;
		} else if (22.5 <= degree && degree <= 67.5) {
			// Direction: SE
			return SOUTH_EAST;
		} else if (67.5 <= degree && degree <= 112.5) {
			// Direction: E
			return EAST;
		} else if (112.5 <= degree && degree <= 157.5) {
			// Direction: NE
			return NORTH_EAST;
		} else if (157.5 <= degree || degree <= -157.5) {
			// Direction: N
			return NORTH;
		} else if (-157.5 <= degree && degree <= -112.5) {
			// Direction: NW
			return NORTH_WEST;
		} else if (-112.5 <= degree && degree <= -67.5) {
			// Direction: W
			return WEST;
		} else if (-67.5 <= degree && degree <= -22.5) {
			// Direction: SW
			return SOUTH_WEST;
		}
		return 0;
	}
	
}
