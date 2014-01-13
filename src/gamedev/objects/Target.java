package gamedev.objects;

import gamedev.game.ResourcesManager;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.extension.debugdraw.primitives.Ellipse;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

public class Target extends Ellipse {
	
	/**
	 * Amount of damage given to the (animal) fighting against
	 */
	protected int damageOpponent = 0;

	/**
	 * Amount of damage given to the avatar
	 */
	protected int damageAvatar = 0;
	
	/**
	 * Amount of damage given to the avatar when the player misses this target
	 */
	protected int damageMiss = 10;
	
	/**
	 * Time in seconds this target is showed
	 */
	protected float timeShowed; // In seconds
	
	/**
	 * Timestamp when this target was created
	 */
	protected float timeCreated; // In seconds
	
	protected float radius;
	
	/**
	 * Set to true when the Scene can remove this target...
	 */
	protected boolean isRemovable;
	
	public Target(float pX, float pY, float radius, Color color, float created, float showed) {
		super(pX, pY, radius, radius, ResourcesManager.getInstance().vbom);
		this.radius = radius;
		this.timeCreated = created;
		this.timeShowed = showed;
		this.setColor(color);
		this.setDrawMode(DrawMode.TRIANGLE_FAN);
		this.setAlpha(0.8f);
	}
	
	
	public boolean isHit(float x, float y) {
		float distance = MathUtils.distance(x, y, this.getX(), this.getY());
		if (distance <= this.radius) {
			this.isRemovable = true;
			return true;
		}
		return false;
	}
	
	
	public boolean isRemovable() {
		return isRemovable;
	}


	public void setRemovable(boolean isRemovable) {
		this.isRemovable = isRemovable;
	}


	public int getDamageObject() {
		return damageOpponent;
	}

	public void setDamageObject(int damageObject) {
		this.damageOpponent = damageObject;
	}

	public int getDamageAvatar() {
		return damageAvatar;
	}

	public void setDamageAvatar(int damageAvatar) {
		this.damageAvatar = damageAvatar;
	}

	public float getTimeAlive() {
		return timeShowed;
	}

	public void setTimeShowed(float timeShowed) {
		this.timeShowed = timeShowed;
	}

	public int getDamageMiss() {
		return damageMiss;
	}

	public void setDamageMiss(int damageMiss) {
		this.damageMiss = damageMiss;
	}


	public float getTimeCreated() {
		return timeCreated;
	}


	public void setTimeCreated(float timeCreated) {
		this.timeCreated = timeCreated;
	}
	
	public float getRadius() {
		return this.radius;
	}
	
	
}
