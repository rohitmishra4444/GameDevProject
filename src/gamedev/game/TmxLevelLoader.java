package gamedev.game;

import gamedev.objects.AnimatedObject.GameState;
import gamedev.objects.BerryBush;
import gamedev.objects.Dinosaur;
import gamedev.objects.Tree;

import java.util.ArrayList;
import java.util.Random;

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
			} else if (group.getName().equals("Trees")) {
				this.createTrees(group.getTMXObjects());
			} else if (group.getName().equals("GreenDinosaurs")) {
				this.createDinosaurs(group.getTMXObjects());
			} else if (group.getName().equals("BerryBushes")) {
				this.createBerryBushes(group.getTMXObjects());
			} else if (group.getName().equals("ShopCave")) {
				this.createShopCave(group.getTMXObjects());
			}

		}

		// TODO REMOVE TEST FOR MOVE-STRATEGIES
		// Create a dinosaur with a different move strategy. the dino simply
		// walks to point 5,5 (meters = *32 for pixels)
//		Dinosaur d = new Dinosaur(600, 600, Dinosaur.COLOR_GREEN);
//		this.scene.attachChild(d);
//		d.setMoveStrategy(new SimpleMoveStrategy(d, new Vector2(100, 100), GameState.WALKING));

		Dinosaur d2 = new Dinosaur(800, 800, Dinosaur.COLOR_GREEN);
		this.scene.attachChild(d2);
		d2.setMoveStrategy(new RandomMoveStrategy(d2, 3, 5, 3));
		
		Dinosaur d3 = new Dinosaur(400, 400, Dinosaur.COLOR_GREEN);
		this.scene.attachChild(d3);
		ArrayList<Vector2> wayPoints = new ArrayList<Vector2>();
		wayPoints.add(new Vector2(400,100));
		wayPoints.add(new Vector2(600,100));
		wayPoints.add(new Vector2(600,200));
		wayPoints.add(new Vector2(400,200));		
//		d3.setMoveStrategy(new WaypointMoveStrategy(d3, wayPoints, true));		
//		d3.setMoveStrategy(new WaypointMoveStrategy(d3, wayPoints, false));
		d3.setMoveStrategy(new FollowPlayerStrategy(d3, d3.getRadius(), new WaypointMoveStrategy(d3, wayPoints, true)));
		// TODO: Create portal object from tmx map.
		// TODO: Create cave object from tmx map.
	}

	protected void createBoundaries(ArrayList<TMXObject> objects) {
		for (final TMXObject object : objects) {
			final Rectangle rect = new Rectangle(object.getX(), object.getY(),
					object.getWidth(), object.getHeight(),
					this.resourcesManager.vbom);
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

	protected void createTrees(ArrayList<TMXObject> objects) {
		Random r = new Random();
		for (final TMXObject object : objects) {
			this.scene.attachChild(new Tree(object.getX(), object.getY(), r
					.nextInt(20)));
		}
	}

	protected void createDinosaurs(ArrayList<TMXObject> objects) {
		for (final TMXObject object : objects) {
			this.scene.attachChild(new Dinosaur(object.getX(), object.getY(),
					Dinosaur.COLOR_GREEN));
		}
	}

	protected void createBerryBushes(ArrayList<TMXObject> objects) {
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
			rect.setUserData(berryBush);
			body.setUserData(berryBush);
			rect.setVisible(false);
			this.scene.attachChild(rect);
			System.out.println("BerryBush created and added to scene!");
		}
	}

	protected void createShopCave(ArrayList<TMXObject> objects) {
		for (final TMXObject object : objects) {
			final Rectangle rect = new Rectangle(object.getX(), object.getY(),
					object.getWidth(), object.getHeight(),
					this.resourcesManager.vbom);
			final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0,
					0, 0);
			Body body = PhysicsFactory.createBoxBody(
					this.resourcesManager.physicsWorld, rect,
					BodyType.StaticBody, boxFixtureDef);
			body.setUserData("ShopCave");
			rect.setVisible(false);
			this.scene.attachChild(rect);
			System.out.println("ShopCave created and added to scene!");
		}
	}

}
