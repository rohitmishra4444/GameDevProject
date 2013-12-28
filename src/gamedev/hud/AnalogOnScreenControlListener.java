package gamedev.hud;

import gamedev.game.ResourcesManager;
import gamedev.objects.AnimatedObject.GameState;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;

public class AnalogOnScreenControlListener implements
		IAnalogOnScreenControlListener {

	protected ResourcesManager resourcesManager = ResourcesManager
			.getInstance();

	@Override
	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl,
			final float pValueX, final float pValueY) {

		// Avoid nullpointer, needs to be resourcesManager.player, since
		// it is final here
		if (resourcesManager.player == null) {
			return;
		}

		if (pValueX == 0 && pValueY == 0) {
			// Only set the player to idle if state is not attack or animation
			// is not running. This is needed, because when the state is attack
			// x and y can also be 0.
			if (!resourcesManager.player.getState().equals(GameState.ATTACK)
					|| !resourcesManager.player.isAnimationRunning()) {
				resourcesManager.player.setState(GameState.IDLE, -1);
			}
		} else {
			GameState state = resourcesManager.hud.isTouchedSecondaryButton() ? GameState.RUNNING: GameState.WALKING;
			resourcesManager.player.setVelocity(pValueX, pValueY, state);
		}

	}

	@Override
	public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
		// TODO Auto-generated method stub
	}

}
