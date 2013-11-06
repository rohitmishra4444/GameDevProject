package gamedev.hud;

import gamedev.game.ResourcesManager;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;

public class SceneHUD extends HUD {
	
	public Rectangle life;
	public Rectangle energy;
	protected ResourcesManager resourcesManager;
	
	public SceneHUD() {
		super();
		this.resourcesManager = ResourcesManager.getInstance();
		this.life = new Rectangle(this.resourcesManager.camera.getWidth() - 150, 20, 100, 20, this.resourcesManager.vbom);
		this.life.setColor(Color.RED);
		this.life.setAlpha(0.6f);
		this.energy = new Rectangle(this.resourcesManager.camera.getWidth() - 150, 50, 100, 20, this.resourcesManager.vbom);
		this.energy.setColor(Color.BLUE);
		this.energy.setAlpha(0.6f);
		this.attachChild(life);
		this.attachChild(energy);
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
	
	
}
