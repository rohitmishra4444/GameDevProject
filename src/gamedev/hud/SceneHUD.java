package gamedev.hud;

import gamedev.game.ResourcesManager;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

public class SceneHUD extends HUD {
	
	protected Rectangle life;
	protected Rectangle energy;
	final protected Rectangle primaryButton;
	final protected Rectangle secondaryButton;
	final protected ResourcesManager resourcesManager;
	protected boolean isTouchedPrimary = false;
	protected boolean isTouchedSecondary = false;
	
	public SceneHUD() {
		// TODO Refactor
		super();
		this.resourcesManager = ResourcesManager.getInstance();
		float cameraWidth = this.resourcesManager.camera.getWidth();
		float cameraHeight = this.resourcesManager.camera.getHeight();
		this.life = new Rectangle(cameraWidth - 150, 20, 100, 20, this.resourcesManager.vbom);
		this.life.setColor(Color.RED);
		this.life.setAlpha(0.6f);
		this.energy = new Rectangle(cameraWidth - 150, 50, 100, 20, this.resourcesManager.vbom);
		this.energy.setColor(Color.BLUE);
		this.energy.setAlpha(0.6f);
		
		this.primaryButton = new Rectangle(cameraWidth - 120, cameraHeight - 100, 80, 80, this.resourcesManager.vbom) {
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	        {
				if (touchEvent.isActionUp())
	            {
					if (!resourcesManager.player.getAttackers().isEmpty()) {
						// Attack the first dinosaur
						resourcesManager.player.getAttackers().get(0).underAttack(50);
					}
	            }
	            return true;
	        };
		};
		this.primaryButton.setColor(Color.BLACK);
		this.primaryButton.setAlpha(0.7f);
		
		this.secondaryButton = new Rectangle(cameraWidth - 120, cameraHeight - 200, 80, 80, this.resourcesManager.vbom) {
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
				if (touchEvent.isActionDown()) {
	                isTouchedSecondary = true;
	                System.out.println("Touched=true");
	            } else if (touchEvent.isActionUp()) {
	            	isTouchedSecondary = false;
	                System.out.println("Touched=false");
	            }
	            return true;
	        }
		};
		this.secondaryButton.setColor(Color.WHITE);
		this.attachChild(life);
		this.attachChild(energy);
		this.attachChild(primaryButton);
		this.attachChild(secondaryButton);
		this.registerTouchArea(primaryButton);
		this.registerTouchArea(secondaryButton);
	}
	
	/**
	 * Update the energy bar
	 * @param percent value between {0..100}
	 */
	public void setEnergy(int percent) {
		this.energy.setWidth(percent);
	}
	
	/**
	 * Update the life bar
	 * @param percent value between {0..100}
	 */
	public void setLife(int percent) {
		this.life.setWidth(percent);
	}
	
	public boolean isTouchedSecondaryButton() {
		return this.isTouchedSecondary;
	}
	
	
}
