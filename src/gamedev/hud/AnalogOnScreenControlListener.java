package gamedev.hud;

import gamedev.game.ResourcesManager;
import gamedev.objects.AnimatedObject.GameState;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;

public class AnalogOnScreenControlListener implements
		IAnalogOnScreenControlListener {

	private ResourcesManager resourcesManager = ResourcesManager.getInstance();

	@Override
	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl,
			final float pValueX, final float pValueY) {

		// Avoid nullpointer
		if (resourcesManager.avatar == null) {
			return;
		}

		if (pValueX == 0 && pValueY == 0) {
			// Only set the player to idle if state is not attack or animation
			// is not running. This is needed, because when the state is attack
			// x and y can also be 0.
			if (resourcesManager.avatar.getState() != GameState.ATTACK
					|| !resourcesManager.avatar.isAnimationRunning()) {
				resourcesManager.avatar.setState(GameState.IDLE, -1);
			}
		} else {
			GameState state = resourcesManager.hud.isTouchedSecondaryButton() ? GameState.RUNNING
					: GameState.WALKING;
			resourcesManager.avatar.setVelocity(pValueX, pValueY, state);
		}

	}

	@Override
	public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
		// TODO Auto-generated method stub
	}

}
