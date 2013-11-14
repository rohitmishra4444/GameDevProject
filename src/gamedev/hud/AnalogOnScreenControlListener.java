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

		
		if (pValueX == 0 && pValueY == 0) {
			ResourcesManager.getInstance().player.setState(PlayerState.IDLE, -1);
		} else {
			PlayerState state = ResourcesManager.getInstance().hud.isTouchedSecondaryButton() ? PlayerState.RUNNING: PlayerState.WALKING;
			ResourcesManager.getInstance().player.setVelocity(pValueX, pValueY,state);
		}

	}

	@Override
	public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
		// TODO Auto-generated method stub
	}

}
