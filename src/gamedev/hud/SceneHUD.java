package gamedev.hud;

import gamedev.game.GameActivity;
import gamedev.game.ResourcesManager;
import gamedev.objects.Player.PlayerState;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import android.opengl.GLES20;

public class SceneHUD extends HUD {

	final protected ResourcesManager resourcesManager = ResourcesManager
			.getInstance();

	// Controls
	protected AnalogOnScreenControl pad;

	protected Rectangle life;
	protected Rectangle energy;

	protected boolean isTouchedPrimary = false;
	protected boolean isTouchedSecondary = false;

	float cameraWidth = this.resourcesManager.camera.getWidth();
	float cameraHeight = this.resourcesManager.camera.getHeight();

	private Thread attackThread;

	public SceneHUD() {
		super();

		this.life = new Rectangle(cameraWidth - 150, 20, 100, 20,
				this.resourcesManager.vbom);
		this.life.setColor(Color.RED);
		this.life.setAlpha(0.6f);
		this.energy = new Rectangle(cameraWidth - 150, 50, 100, 20,
				this.resourcesManager.vbom);
		this.energy.setColor(Color.BLUE);
		this.energy.setAlpha(0.6f);

		this.attachChild(life);
		this.attachChild(energy);

		createControls();
		createButtons();
	}

	protected void createControls() {
		AnalogOnScreenControlListener controlListener = new AnalogOnScreenControlListener();
		this.pad = new AnalogOnScreenControl(0, GameActivity.HEIGHT
				- resourcesManager.controlBaseTextureRegion.getHeight(),
				resourcesManager.camera,
				resourcesManager.controlBaseTextureRegion,
				resourcesManager.controlKnobTextureRegion, 0.1f, 200,
				resourcesManager.vbom, controlListener);

		this.pad.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA,
				GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.pad.getControlBase().setAlpha(0.5f);
		this.pad.getControlBase().setScaleCenter(0, 128);
		this.pad.getControlBase().setScale(1.25f);
		this.pad.getControlKnob().setScale(1.25f);
		this.pad.refreshControlKnobPosition();
		this.setChildScene(this.pad);

	}

	private void createButtons() {

		ButtonSprite attackButton = new ButtonSprite(cameraWidth - 120,
				cameraHeight - 100, resourcesManager.controlKnobTextureRegion,
				resourcesManager.vbom) {
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {

				this.registerEntityModifier(new SequenceEntityModifier(
						new ScaleModifier(0.25f, 1.25f, 1f), new ScaleModifier(
								0.25f, 1f, 1.25f)));

				if (touchEvent.isActionUp()) {

					// Stop the currently animation if it is not already
					// attacking.
					if (!resourcesManager.player.getState().equals(
							PlayerState.ATTACK)) {
						resourcesManager.player.stopAnimation();
					}
					
					resourcesManager.player.setState(PlayerState.ATTACK, -1);
					if (!resourcesManager.player.getAttackers().isEmpty()) {
						// Attack the first dinosaur
						resourcesManager.player.getAttackers().get(0)
								.attack(50);
					}
										
				}

				return true;
			};
		};

		ButtonSprite sprintButton = new ButtonSprite(cameraWidth - 120,
				cameraHeight - 200, resourcesManager.controlKnobTextureRegion,
				resourcesManager.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {

				this.registerEntityModifier(new SequenceEntityModifier(
						new ScaleModifier(0.25f, 1.25f, 1f), new ScaleModifier(
								0.25f, 1f, 1.25f)));

				if (touchEvent.isActionDown()) {
					isTouchedSecondary = true;
//					System.out.println("Touched=true");
				} else if (touchEvent.isActionUp()) {
					isTouchedSecondary = false;
//					System.out.println("Touched=false");
				}
				return true;
			}
		};

		attackButton.setAlpha(0.5f);
		attackButton.setScale(1.25f);
		attackButton.setColor(Color.BLACK);
		sprintButton.setAlpha(0.5f);
		sprintButton.setScale(1.25f);
		sprintButton.setColor(Color.WHITE);

		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.registerTouchArea(attackButton);
		this.registerTouchArea(sprintButton);

		this.attachChild(attackButton);
		this.attachChild(sprintButton);
	}

	/**
	 * Update the energy bar
	 * 
	 * @param percent
	 *            value between {0..100}
	 */
	public void setEnergy(int percent) {
		this.energy.setWidth(percent);
	}

	/**
	 * Update the life bar
	 * 
	 * @param percent
	 *            value between {0..100}
	 */
	public void setLife(int percent) {
		this.life.setWidth(percent);
	}

	public boolean isTouchedSecondaryButton() {
		return this.isTouchedSecondary;
	}

}
