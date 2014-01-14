package gamedev.objects;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.widget.Toast;

abstract public class CollectableObject extends Sprite {

	protected boolean removeable = false;

	public CollectableObject(float pX, float pY, ITextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion, ResourcesManager.getInstance().vbom);
	}

	private void init() {
		SceneManager.getInstance().getCurrentGameMapScene()
				.addCollectableObject(this);
	}

	@Override
	public void onManagedUpdate(float seconds) {
		super.onManagedUpdate(seconds);
		if (this.collidesWith(ResourcesManager.getInstance().avatar)) {
			ResourcesManager.getInstance().avatar.getInventory()
					.addObject(this);

			// TODO: This does not work, we need to find a way to safely remove
			// objects from the map...
			// ResourcesManager.getInstance().removeSpriteFromScene(this);

			// A possible solution to the removing problem: Here we only set the
			// object invisible and ignore further update. Then in the
			// onFinish() method of the quest we detach the object.
			// this.setVisible(false);

			this.setIgnoreUpdate(true);
			this.removeable = true;

			// Better solution: Use RunnableHandler like described in javadoc of
			// detachChild method.
			// This way we don't need the removeable boolean and the
			// collectableObject list.
			final CollectableObject self = this;

			Runnable removeObject = new Runnable() {
				@Override
				public void run() {
					SceneManager.getInstance().getCurrentGameMapScene()
							.detachChild(self);
				}
			};
			SceneManager.getInstance().getCurrentGameMapScene().runnableHandler
					.postRunnable(removeObject);

			// Give feedback:
			ResourcesManager.getInstance().activity.toastOnUIThread(
					"Item collected.", Toast.LENGTH_SHORT);
		}
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public void setRemoveable(boolean removeable) {
		this.removeable = removeable;
	}

}
