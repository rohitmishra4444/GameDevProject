package gamedev.game;

import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;

public class Direction {
	
	// 8 possible directions. Note that the order is important, it is the same order as used
	// for the animation in the tilesets
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
	 * @param v2 End-Position
	 * @return
	 */
	public static int getDirectionFromVectors(Vector2 v1, Vector2 v2) {
		float x = v2.x - v1.x;
		float y = v2.y - v1.y;		
		float degree = MathUtils.radToDeg((float) Math.atan2(x, y));
		return getDirectionFromDegree(degree);
	}
	
	public static int getDirection(float xFrom, float xTo, float yFrom, float yTo) {
		float x = xTo - xFrom;
		float y = yTo - yFrom;
		float degree = MathUtils.radToDeg((float) Math.atan2(x, y));
		return getDirectionFromDegree(degree);
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
