package gamedev.game;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class FollowedSprite extends AnimatedSprite {

	private IEntity follower;
	private float maxDistance;
	private float minDistance;

	public FollowedSprite(float pX, float pY, float pWidth, float pHeight,
			ITiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion,
				pVertexBufferObjectManager);
	}

	public FollowedSprite(float centerX, float centerY,
			TiledTextureRegion textureRegion,
			VertexBufferObjectManager vertexBufferObjectManager) {
		super(centerX, centerY, textureRegion, vertexBufferObjectManager);
	}

	@Override
	public void setPosition(final float pX, final float pY) {
		super.setPosition(pX, pY);

		//
		// Do some funky math and maybe used some random values but stay within
		// min/max distance
		// (hint cardinal spline calculator in andengine can be used to generate
		// nice smooth movement).
		// class org.andengine.entity.modifier.CardinalSplineMoveModifier
		int funkyValueX = 10;
		int funkyValueY = 10;

		final float superFunkyNewX = pX + funkyValueX;
		final float superFunkyNewY = pY + funkyValueY;

		if (follower != null) {
			CardinalSplineMoveModifierConfig splineMoveConfig = new CardinalSplineMoveModifierConfig(
					4, 1);
			CardinalSplineMoveModifier splineMove = new CardinalSplineMoveModifier(
					10, splineMoveConfig);
			follower.registerEntityModifier(splineMove);

			// follower.setPosition(splineMove.cardinalSplineX(pX0, pX1, pX2,
			// pX3,
			// pT, pTension), splineMove.cardinalSplineY(pY0, pY1, pY2,
			// pY3, pT, pTension));

			follower.setPosition(superFunkyNewX, superFunkyNewY);
		}
	}

	public void setFollowedEntity(IEntity pFollower, float pMaxDistance,
			float pMinDistance) {
		follower = pFollower;
		maxDistance = pMaxDistance;
		minDistance = pMinDistance;
	}

}
