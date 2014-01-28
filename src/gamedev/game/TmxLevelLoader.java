package gamedev.game;

import gamedev.ai.MoveStrategy;
import gamedev.ai.RandomMoveStrategy;
import gamedev.ai.WaypointMoveStrategy;
import gamedev.objects.AnimatedObject;
import gamedev.objects.Avatar;
import gamedev.objects.BerryBush;
import gamedev.objects.Dinosaur;
import gamedev.objects.GameEndPortal;
import gamedev.objects.Spider;
import gamedev.quests.QuestTrigger;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTiledMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class TmxLevelLoader {

	private final static String MOVE_STRATEGY_RANDOM = "RandomMoveStrategy";
	private final static String MOVE_STRATEGY_SIMPLE = "SimpleMoveStrategy";
	private final static String MOVE_STRATEGY_WAYPOINT = "WaypointMoveStrategy";

	protected TMXTiledMap map;
	protected Scene scene;
	protected ResourcesManager resourcesManager;

	public TmxLevelLoader(TMXTiledMap map, Scene scene) {
		this.map = map;
		this.scene = scene;
		this.resourcesManager = ResourcesManager.getInstance();
	}

	public void createWorldAndObjects() {
		// Loop through the layers and attach them as child scene.
		for (int i = 0; i < this.map.getTMXLayers().size(); i++) {
			TMXLayer tmxLayer = this.map.getTMXLayers().get(i);
			this.scene.attachChild(tmxLayer);
		}
		// Create objects from object layers
		for (final TMXObjectGroup group : this.map.getTMXObjectGroups()) {
			if (group.getName().equals("Walls")) {
				this.createBoundaries(group.getTMXObjects());
				// } else if (group.getName().equals("Trees")) {
				// this.createTrees(group.getTMXObjects());
			} else if (group.getName().equals("GreenDinosaurs")) {
				this.createDinosaurs(group.getTMXObjects(), 0);
			} else if (group.getName().equals("RedDinosaurs")) {
				this.createDinosaurs(group.getTMXObjects(), 1);
			} else if (group.getName().equals("Spiders")) {
				this.createSpiders(group.getTMXObjects());
			} else if (group.getName().equals("BerryBushes")) {
				this.createBerryBushes(group.getTMXObjects());
			} else if (group.getName().equals("QuestTrigger")) {
				this.createQuestTriggers(group.getTMXObjects());
			}
		}
	}

	private void createSpiders(ArrayList<TMXObject> objects) {
		for (final TMXObject object : objects) {
			Spider s = new Spider(object.getX(), object.getY());
			MoveStrategy strategy = this.getMoveStrategy(object, s);
			if (strategy != null) {
				s.setMoveStrategy(strategy);
			}
			for (int i = 0; i < object.getTMXObjectProperties().size(); i++) {
				if (object.getTMXObjectProperties().get(i).getName()
						.equals("velocity")) {
					s.setVelocity(Float.parseFloat(object
							.getTMXObjectProperties().get(i).getValue()));
				} else if (object.getTMXObjectProperties().get(i).getName()
						.equals("scale")) {
					s.setScale(Float.parseFloat(object.getTMXObjectProperties()
							.get(i).getValue()));
				}
			}
			this.scene.attachChild(s);
		}
	}

	protected void createQuestTriggers(ArrayList<TMXObject> tmxObjects) {
		for (final TMXObject object : tmxObjects) {
			if (object.getName().equals("avatar")) {
				float x = object.getX();
				float y = object.getY();

				if (resourcesManager.avatar == null) {
					System.out.println("Avatar is null!");
					resourcesManager.avatar = new Avatar();
				}
				resourcesManager.avatar.getBody().setTransform(x / 32, y / 32,
						0);
				// Check if the player is already has a parent (avoid
				// assertEntityHasNoParent IllegalStateException)
				// This is needed for the back button during gameplay (for
				// restarting a
				// new game).
				if (resourcesManager.avatar.hasParent()) {
					IEntity parentEntity = resourcesManager.avatar.getParent();
					parentEntity.detachChild(resourcesManager.avatar);
				}
				this.scene.attachChild(resourcesManager.avatar);
			} else if (object.getName().equals("portal")) {
				GameEndPortal portal = new GameEndPortal(object.getX(),
						object.getY());
				this.scene.attachChild(portal);
			} else {
				int id = Integer.parseInt(object.getName());
				// System.out.println("Quest-ID: " + object.getName());
				QuestTrigger q = new QuestTrigger(id, object.getX(),
						object.getY(), object.getWidth(), object.getHeight());
				this.scene.attachChild(q);
			}
		}
	}

	protected void createBoundaries(ArrayList<TMXObject> objects) {
		for (final TMXObject object : objects) {
			final Rectangle rect = new Rectangle(object.getX(), object.getY(),
					object.getWidth(), object.getHeight(),
					this.resourcesManager.vbom);
			if (object.getTMXObjectProperties().size() > 0) {
				for (int i = 0; i < object.getTMXObjectProperties().size(); i++) {
					if (object.getTMXObjectProperties().get(i).getName()
							.equals("rotate")) {
						rect.setRotation((-1)
								* Float.parseFloat(object
										.getTMXObjectProperties().get(i)
										.getValue()));
					}
				}
			}
			final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0,
					0, 0);
			Body body = PhysicsFactory.createBoxBody(
					this.resourcesManager.physicsWorld, rect,
					BodyType.StaticBody, boxFixtureDef);
			body.setUserData("Boundary");
			rect.setVisible(false);
			this.scene.attachChild(rect);
		}
	}

	protected void createDinosaurs(ArrayList<TMXObject> objects, int color) {
		for (final TMXObject object : objects) {
			Dinosaur d = new Dinosaur(object.getX(), object.getY(), color);
			MoveStrategy alternateStrategy = this.getMoveStrategy(object, d);
			if (alternateStrategy != null) {
				d.setAlternateMoveStrategy(alternateStrategy);
			}
			for (int i = 0; i < object.getTMXObjectProperties().size(); i++) {
				if (object.getTMXObjectProperties().get(i).getName()
						.equals("radius")) {
					d.setRadius(Float.parseFloat(object
							.getTMXObjectProperties().get(i).getValue()));
				} else if (object.getTMXObjectProperties().get(i).getName()
						.equals("velocity")) {
					d.setVelocity(Float.parseFloat(object
							.getTMXObjectProperties().get(i).getValue()));
				} else if (object.getTMXObjectProperties().get(i).getName()
						.equals("scale")) {
					d.setScale(Float.parseFloat(object.getTMXObjectProperties()
							.get(i).getValue()));
				}
			}
			this.scene.attachChild(d);
		}
	}

	/**
	 * Return a new MoveStrategy object from tmx object properties
	 * 
	 * @param object
	 * @param d
	 * @return
	 */
	protected MoveStrategy getMoveStrategy(TMXObject object, AnimatedObject d) {

		for (int i = 0; i < object.getTMXObjectProperties().size(); i++) {
			if (object.getTMXObjectProperties().get(i).getName()
					.equals("MoveStrategy")) {

				if (object.getTMXObjectProperties().get(i).getValue()
						.equals(MOVE_STRATEGY_WAYPOINT)) {
					ArrayList<Vector2> wayPoints = new ArrayList<Vector2>();
					boolean loop = true;
					for (int j = (i + 1); j < object.getTMXObjectProperties()
							.size(); j++) {
						String name = object.getTMXObjectProperties().get(j)
								.getName();
						String value = object.getTMXObjectProperties().get(j)
								.getValue();
						// Points are separated with a comma and they are in the
						// tmx coordinates (tiles)
						if (name.substring(0, 5).equals("point")) {
							String[] coord = value.split(",");
							wayPoints.add(new Vector2(Float
									.parseFloat(coord[0]) * 32, Float
									.parseFloat(coord[1]) * 32));
						} else if (name.equals("loop")) {
							loop = Boolean.getBoolean(value);
						}
					}
					return new WaypointMoveStrategy(d, wayPoints, loop);

				} else if (object.getTMXObjectProperties().get(i).getValue()
						.equals(MOVE_STRATEGY_SIMPLE)) {

					// TODO Simple... not needed until now ;)

				} else if (object.getTMXObjectProperties().get(i).getValue()
						.equals(MOVE_STRATEGY_RANDOM)) {
					float minDistance = 0;
					float maxDistance = 0;
					float waitBetweenTime = 0;
					float minX = -1;
					float maxX = -1;
					float minY = -1;
					float maxY = -1;
					for (int j = (i + 1); j < object.getTMXObjectProperties()
							.size(); j++) {
						String name = object.getTMXObjectProperties().get(j)
								.getName();
						String value = object.getTMXObjectProperties().get(j)
								.getValue();
						if (name.equals("minDistance")) {
							minDistance = Float.parseFloat(value) * 32;
						} else if (name.equals("maxDistance")) {
							maxDistance = Float.parseFloat(value) * 32;
						} else if (name.equals("waitBetweenTime")) {
							waitBetweenTime = Float.parseFloat(value);
						} else if (name.equals("minX")) {
							minX = Float.parseFloat(value) * 32;
						} else if (name.equals("maxX")) {
							maxX = Float.parseFloat(value) * 32;
						} else if (name.equals("minY")) {
							minY = Float.parseFloat(value) * 32;
						} else if (name.equals("maxY")) {
							maxY = Float.parseFloat(value) * 32;
						}
					}
					if (minX != -1 && maxX != -1 && minY != -1 && maxY != -1) {
						return new RandomMoveStrategy(d, minDistance,
								maxDistance, waitBetweenTime, minX, maxX, minY,
								maxY);
					} else {
						return new RandomMoveStrategy(d, minDistance,
								maxDistance, waitBetweenTime);
					}
				}
			}
		}

		return null;
	}

	private void createBerryBushes(ArrayList<TMXObject> objects) {
		for (final TMXObject object : objects) {
			BerryBush berryBush = new BerryBush(object.getX(), object.getY());

			final Rectangle rect = new Rectangle(object.getX(), object.getY(),
					object.getWidth(), object.getHeight(),
					this.resourcesManager.vbom);
			final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0,
					0, 0);
			Body body = PhysicsFactory.createBoxBody(
					this.resourcesManager.physicsWorld, rect,
					BodyType.StaticBody, boxFixtureDef);
			body.setUserData(berryBush);
			rect.setVisible(false);
			this.scene.attachChild(rect);
		}
	}

}
