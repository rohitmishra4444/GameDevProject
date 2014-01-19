package gamedev.objects;

import gamedev.game.ResourcesManager;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.widget.Toast;

abstract public class CollectableObject extends Sprite {

	public CollectableObject(float pX, float pY, ITextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion, ResourcesManager.getInstance().vbom);
	}

	@Override
	public void onManagedUpdate(float seconds) {
		super.onManagedUpdate(seconds);
		if (this.collidesWith(ResourcesManager.getInstance().avatar)) {
			ResourcesManager res = ResourcesManager.getInstance();
			res.avatar.getInventory().addObject(this);

			res.removeSpriteAndBody(this);

			// Give feedback:
			String item = (this.toString().equals("")) ? "item" : this
					.toString();
			res.activity.toastOnUIThread("Collected " + item,
					Toast.LENGTH_SHORT);
			res.collect.play();
		}
	}

	public String toString() {
		return "";
	}

}
