package gamedev.hud;

import gamedev.game.Direction;
import gamedev.game.ResourcesManager;
import gamedev.objects.Player.PlayerState;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.util.math.MathUtils;

public class AnalogOnScreenControlListener implements
		IAnalogOnScreenControlListener {

	@Override
	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl,
			final float pValueX, final float pValueY) {

		// Avoid nullpointer, needs to be resourcesManager.player, since
		// it is final here
		if (ResourcesManager.getInstance().player == null) {
			return;
		}

		// Compute direction in degree (from -180° to +180°).
		float degree = MathUtils.radToDeg((float) Math.atan2(pValueX, pValueY));

		// Set the direction and State
		int direction = Direction.getDirectionFromDegree(degree);
		if (degree == 0) {
			ResourcesManager.getInstance().player.setState(PlayerState.IDLE,
					direction);
		} else {
			PlayerState state = ResourcesManager.getInstance().hud
					.isTouchedSecondaryButton() ? PlayerState.RUNNING
					: PlayerState.WALKING;
			ResourcesManager.getInstance().player.setVelocity(pValueX, pValueY,
					state, direction);
		}

		// else if (Math.abs(pValueX) > 0.75 || Math.abs(pValueY) >
		// 0.75) {
		// player.setVelocity(pValueX, pValueY, PlayerState.RUNNING);
		// } else {
		// player.setVelocity(pValueX, pValueY, PlayerState.WALKING);
		// }

	}

	@Override
	public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
		// TODO Auto-generated method stub
	}

}
