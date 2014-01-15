package gamedev.quests;

import gamedev.game.ResourcesManager;
import gamedev.scenes.GameMapScene;

import org.andengine.entity.shape.IShape;
import org.andengine.extension.physics.box2d.PhysicsConnector;

public abstract class Quest {

	protected String title;
	protected String description;
	public static boolean isCompleted = false;
	protected boolean isActive = false;
	protected String status;
	protected GameMapScene map;

	// TODO: Give rewards!

	public Quest(GameMapScene map) {
		this.map = map;
	}

	public abstract void onFinish();

	public abstract String getStatus();

	public abstract boolean isCompleted();

	public void removeShapeWithBody(final IShape shape) {
		final ResourcesManager res = ResourcesManager.getInstance();
		res.engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				final PhysicsConnector shapePhysicsConnector = res.physicsWorld
						.getPhysicsConnectorManager()
						.findPhysicsConnectorByShape(shape);
				if (shapePhysicsConnector != null) {
					res.physicsWorld
						.unregisterPhysicsConnector(shapePhysicsConnector);

					shapePhysicsConnector.getBody().setActive(false);
					res.physicsWorld.destroyBody(shapePhysicsConnector.getBody());
					shape.detachSelf();
					shape.dispose();
				}
			}
		});
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return this.description;
	}

}
