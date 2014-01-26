package gamedev.hud;

import gamedev.game.GameActivity;
import gamedev.game.ResourcesManager;
import gamedev.scenes.HelpScene;
import gamedev.scenes.QuestScene;
import gamedev.scenes.ShopScene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import android.opengl.GLES20;
import android.widget.Toast;

public class SceneHUD extends HUD {

	final protected ResourcesManager resourcesManager = ResourcesManager
			.getInstance();

	// Controls
	protected AnalogOnScreenControl pad;

	protected Rectangle life;
	protected Rectangle energy;

	protected ButtonSprite berries;
	public Text berryCounter;
	private static int lifeAdditionFromBerry = 20;
	private static int energyAdditionFromBerry = 30;

	protected boolean isTouchedPrimary = false;
	protected boolean isSprintButtonTouched = false;

	float cameraWidth = this.resourcesManager.camera.getWidth();
	float cameraHeight = this.resourcesManager.camera.getHeight();

	public SceneHUD() {
		super();

		int avatarLife = 100;
		float avatarEnergy = 100;

		if (resourcesManager.avatar != null) {
			avatarLife = resourcesManager.avatar.getLife();
			avatarEnergy = resourcesManager.avatar.getEnergy();
		}

		Sprite bgBars = new Sprite(cameraWidth - 190, 20,
				resourcesManager.bgBarsRegion, resourcesManager.vbom);
		bgBars.setAlpha(0.9f);
		this.attachChild(bgBars);

		this.life = new Rectangle(cameraWidth - 170, 30, avatarLife, 10,
				this.resourcesManager.vbom);
		this.life.setColor(Color.RED);
		this.life.setAlpha(0.8f);
		this.energy = new Rectangle(cameraWidth - 170, 43, avatarEnergy, 10,
				this.resourcesManager.vbom);
		this.energy.setColor(Color.BLUE);
		this.energy.setAlpha(0.8f);

		this.attachChild(life);
		this.attachChild(energy);

		this.setTouchAreaBindingOnActionDownEnabled(true);

		createDPadControls();
		createSprintButton();
		createQuestButton();
		createHelpButton();
		// createShopButton();
		createBerriesAndButton();
	}

	protected void createDPadControls() {
		AnalogOnScreenControlListener controlListener = new AnalogOnScreenControlListener();
		this.pad = new AnalogOnScreenControl(20, GameActivity.HEIGHT
				- resourcesManager.controlBaseTextureRegion.getHeight() - 20,
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

	private void createSprintButton() {
		ButtonSprite sprintButton = new ButtonSprite(cameraWidth - 120,
				cameraHeight - 100, resourcesManager.controlKnobTextureRegion,
				resourcesManager.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
				this.registerEntityModifier(new SequenceEntityModifier(
						new ScaleModifier(0.25f, 1.25f, 1f), new ScaleModifier(
								0.25f, 1f, 1.25f)));

				if (touchEvent.isActionDown()) {
					isSprintButtonTouched = true;
				} else if (touchEvent.isActionUp()) {
					isSprintButtonTouched = false;
				}
				return true;
			}
		};

		sprintButton.setAlpha(0.5f);
		sprintButton.setScale(1.25f);
		sprintButton.setColor(Color.WHITE);
		this.registerTouchArea(sprintButton);
		this.attachChild(sprintButton);
	}

	private void createBerriesAndButton() {
		this.berries = new ButtonSprite(cameraWidth - 115, 90,
				resourcesManager.hudBerryRegion, resourcesManager.vbom) {
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {

				if (touchEvent.isActionUp()) {
					this.registerEntityModifier(new SequenceEntityModifier(
							new ScaleModifier(0.25f, 0.75f, 1.25f),
							new ScaleModifier(0.25f, 1.25f, 0.75f)));

					if (resourcesManager.avatar.getInventory().removeBerry()) {
						int currentLife = resourcesManager.avatar.getLife();
						resourcesManager.avatar.setLife(currentLife
								+ lifeAdditionFromBerry);
						float currentEnergy = resourcesManager.avatar
								.getEnergy();
						resourcesManager.avatar.setEnergy(currentEnergy
								+ energyAdditionFromBerry);
						// Feedback:
						ResourcesManager.getInstance().eat.play();
					}

					return true;
				}
				return false;
			}
		};
		this.attachChild(berries);
		berries.setScale(0.75f);
		this.registerTouchArea(berries);

		String berryInitialString = " 0";
		this.berryCounter = new Text(cameraWidth - 130, 90,
				resourcesManager.font, berryInitialString,
				berryInitialString.length(), resourcesManager.vbom);
		this.berryCounter.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				berryCounter.setText(String.valueOf(resourcesManager.avatar
						.getInventory().getNumberNumberOfBerries()));
			}

			@Override
			public void reset() {
				berryCounter.setText(" 0");
			}

		});
		// this.berryCounter.setScale(1.5f);
		this.attachChild(berryCounter);

	}

	private void createShopButton() {
		ButtonSprite shopButton = new ButtonSprite(cameraWidth - 400, 20,
				resourcesManager.hudShopIconRegion, resourcesManager.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
				this.registerEntityModifier(new SequenceEntityModifier(
						new ScaleModifier(0.25f, 1.25f, 1f), new ScaleModifier(
								0.25f, 1f, 1.25f)));
				// Feedback:
				ResourcesManager.getInstance().openChildScene.play();

				ShopScene shopScene = ShopScene.getInstance();
				shopScene.openShopScene();
				// TODO: Remove.
				resourcesManager.activity.toastOnUIThread(
						"Sorry, the shop is not implemented yet.",
						Toast.LENGTH_LONG);

				return true;
			};
		};

		shopButton.setAlpha(0.8f);
		// shopButton.setScale(1.25f);

		this.registerTouchArea(shopButton);
		this.attachChild(shopButton);
	}

	private void createQuestButton() {
		ButtonSprite questButton = new ButtonSprite(cameraWidth - 250, 20,
				resourcesManager.hudQuestListIconRegion, resourcesManager.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
				this.registerEntityModifier(new SequenceEntityModifier(
						new ScaleModifier(0.25f, 1.25f, 1f), new ScaleModifier(
								0.25f, 1f, 1.25f)));
				// Feedback:
				ResourcesManager.getInstance().openChildScene.play();

				QuestScene questScene = QuestScene.getInstance();
				questScene.openQuestScene();
				return true;
			};
		};

		questButton.setAlpha(0.9f);
		// questButton.setScale(1.25f);

		this.registerTouchArea(questButton);
		this.attachChild(questButton);
	}

	private void createHelpButton() {
		ButtonSprite helpButton = new ButtonSprite(25, 20,
				resourcesManager.hudHelpIconRegion, resourcesManager.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
				this.registerEntityModifier(new SequenceEntityModifier(
						new ScaleModifier(0.25f, 1.25f, 1f), new ScaleModifier(
								0.25f, 1f, 1.25f)));
				// Feedback:
				ResourcesManager.getInstance().openChildScene.play();

				HelpScene helpScene = HelpScene.getInstance();
				helpScene.openHelpScene();
				return true;
			};
		};

		helpButton.setAlpha(0.9f);
		// helpButton.setScale(1.25f);

		this.registerTouchArea(helpButton);
		this.attachChild(helpButton);
	}

	/**
	 * Update the energy bar
	 * 
	 * @param percent
	 *            value between {0..100}
	 */
	public void setEnergy(float percent) {
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
		return this.isSprintButtonTouched;
	}

}
