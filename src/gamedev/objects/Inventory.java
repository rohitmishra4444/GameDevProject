package gamedev.objects;

import gamedev.game.ResourcesManager;

import java.util.ArrayList;


/**
 * Inventory of the avatar
 *
 */
public class Inventory {
	
	/** Static objects from world */
	protected ArrayList<CollectableObject> objects = new ArrayList<CollectableObject>();
	
	/** Berries collected */
	protected ArrayList<Berry> berries = new ArrayList<Berry>();
	
	public void addObject(CollectableObject object) {
		this.objects.add(object);
	}
	
	public boolean contains(CollectableObject object) {
		return this.objects.contains(object);
	}
	
	public void addBerry(Berry berry) {
		this.berries.add(berry);
		ResourcesManager.getInstance().hud.berryCounter.onUpdate(0);
	}

	/**
	 * Removes a berry from the berry inventory if not empty.
	 * 
	 * @return true, if berry successfully removed false, if inventory is empty
	 */
	public boolean removeBerry() {
		if (!this.berries.isEmpty()) {
			this.berries.remove(0);
			return true;
		} else {
			return false;
		}
	}

	public int getNumberNumberOfBerries() {
		return this.berries.size();
	}

	
}
