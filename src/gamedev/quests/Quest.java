package gamedev.quests;

import gamedev.scenes.GameMapScene;

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

	// public void removeShapeWithBody(final IShape shape) {
	// final ResourcesManager res = ResourcesManager.getInstance();
	// Runnable removeShape = new Runnable() {
	// @Override
	// public void run() {
	// shape.setVisible(false);
	// shape.setIgnoreUpdate(true);
	//
	// final PhysicsConnector shapePhysicsConnector = res.physicsWorld
	// .getPhysicsConnectorManager()
	// .findPhysicsConnectorByShape(shape);
	// Body shapeBody = shapePhysicsConnector.getBody();
	//
	// if (shapePhysicsConnector != null) {
	// for (Fixture fix : shapeBody.getFixtureList()) {
	// shapeBody.destroyFixture(fix);
	// }
	// shapeBody.setActive(false);
	// res.physicsWorld
	// .unregisterPhysicsConnector(shapePhysicsConnector);
	// res.physicsWorld.destroyBody(shapeBody);
	// shape.detachSelf();
	// shape.dispose();
	// }
	// }
	// };
	// res.physicsWorld.postRunnable(removeShape);
	// }

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
