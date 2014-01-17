package gamedev.scenes;

import java.util.ArrayList;
import java.util.Random;

import gamedev.game.GameActivity;
import gamedev.game.GameActivity.GameMode;
import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.objects.AnimatedObject;
import gamedev.objects.AnimatedObject.GameState;
import gamedev.objects.Dinosaur;
import gamedev.objects.Target;

import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;

public class FightScene extends CameraScene {

	private final static int FIGHTBAR_WIDTH = 400;
	private static final int FIGHTBAR_HEIGHT = 300;
	private final static float TARGET_RADIUS = 50;

	protected Sprite fightDino;
	protected AnimatedObject object;
	protected ResourcesManager resourcesManager;
	protected ArrayList<Target> targets = new ArrayList<Target>();
	protected float fightDuration = 0; // Total duration of fight
	protected float lastTargetCreated = 0; // Last target created (passed seconds)
	protected float frequencyOfTargts = 0.5f; // How often are new targets created in seconds
	private static FightScene instance;
		
	private FightScene() {
		super(ResourcesManager.getInstance().camera);
		this.resourcesManager = ResourcesManager.getInstance();
		this.fightDino = new Sprite(0, 0, this.resourcesManager.fightDinoRegion, this.resourcesManager.vbom);
		centerShapeInCamera(this.fightDino);
		this.attachChild(this.fightDino);
		this.setBackgroundEnabled(false);
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown()) {
					float pX = pSceneTouchEvent.getX();
					float pY = pSceneTouchEvent.getY();
					for (Target target : targets) {
						if (target.isHit(pX, pY)) {
							if (target.getDamageOpponent() > 0) object.attack(target.getDamageOpponent());
							if (target.getDamageAvatar() > 0) resourcesManager.avatar.attack(target.getDamageAvatar());
							target.setRemovable(true);
						}
					}
					if (object.getState() == GameState.DEAD) {
						resourcesManager.avatar.setState(GameState.IDLE, -1);
						GameActivity.mode = GameMode.EXPLORING;
						onClose();
						SceneManager.getInstance().getCurrentGameMapScene().clearChildScene();
					}
					return true;
				}
				return false;
			}
		});
		
	}
	
	
	public static FightScene getInstance() {
		if (instance == null) {
			instance = new FightScene();
		}
		ResourcesManager.getInstance().unloadHUDResources();
		return instance;
	}
	
	public void setObject(AnimatedObject object) {
		this.object = object;
	}
	
	public void onManagedUpdate(float seconds) {
		if (this.object == null) return;
		this.fightDuration += seconds;
		this.lastTargetCreated += seconds;
	
		// First step: Check for dismissed targets!
		ArrayList<Target> targetsToRemove = new ArrayList<Target>();
		for (Target t : this.targets) {
			if ((this.fightDuration - t.getTimeAlive()) > t.getTimeCreated()) {
				// Missed!
				if (t.getDamageMiss() > 0) resourcesManager.avatar.attack(t.getDamageMiss());
				t.setRemovable(true);
//				System.out.println("You dismissed target [actual time: "+ fightDuration + ", created: " + t.getTimeCreated() + ", timeAlive: " + t.getTimeAlive()+"]");
			}
			if (t.isRemovable()) targetsToRemove.add(t);
		}
		
		// Second step: Remove targets!
		for (Target t : targetsToRemove) {
			this.targets.remove(t);
			this.detachChild(t);
			t.dispose();
		}
		
		// Third step: Check if a new target should be created
		if (this.lastTargetCreated > this.frequencyOfTargts) {
			Target t = this.generateTarget();
			this.targets.add(t);
			this.attachChild(t);
			this.lastTargetCreated = 0;
		}			
	}
	
	
	/**
	 * 
	 * @return
	 */
	protected Target generateTarget() {
		// Percentage to generate a "good" target that attacks the object when hit ;)
		// 1-p is the percentage of a "bad" target, that should not be hit
		float pGoodTarget = 0;
		Random r = new Random();
		Vector2 position = this.getRandomPosition();
		Target t = null;
		
		// I know it's horrible this way, but who cares... we're in a hurry
		if (this.object instanceof Dinosaur) {
			Dinosaur d = (Dinosaur) this.object;
			pGoodTarget = (d.getDinoColor() == Dinosaur.COLOR_GREEN) ? 0.6f : 0.5f;
			if (r.nextFloat() < pGoodTarget) {
				// Good target
				if (d.getDinoColor() == Dinosaur.COLOR_GREEN) {
					t = new Target(position.x, position.y, TARGET_RADIUS, Color.BLACK, this.fightDuration, 2.5f);
					t.setDamageOpponent(10);
					t.setDamageMiss(10);
				} else {
					t = new Target(position.x, position.y, TARGET_RADIUS, Color.BLACK, this.fightDuration, 1.5f);
					t.setDamageOpponent(5);
					t.setDamageMiss(15);
				}
			} else {
				// Bad target
				if (d.getDinoColor() == Dinosaur.COLOR_GREEN) {
					t = new Target(position.x, position.y, TARGET_RADIUS, Color.RED, this.fightDuration, 2);
					t.setDamageAvatar(15);
				} else {
					t = new Target(position.x, position.y, TARGET_RADIUS, Color.RED, this.fightDuration, 2);
					t.setDamageAvatar(20);
				}
			}
		}
		return t;
	}
	
	/**
	 * Get a random position so that none of the targets are overlapping
	 * @return
	 */
	protected Vector2 getRandomPosition() {
		Random r = new Random();
		float x = this.fightDino.getX() + 50 + r.nextFloat() * FIGHTBAR_WIDTH;
		float y = this.fightDino.getY() + 50 + r.nextFloat() * FIGHTBAR_HEIGHT;
		boolean intersects = false;
		do  {
			for (Target t : this.targets) {
				if (MathUtils.distance(t.getX(), t.getY(), x, y) < t.getRadius()*2) {
					intersects = true;
					x = this.fightDino.getX() + 50 + r.nextFloat() * FIGHTBAR_WIDTH;
					y = this.fightDino.getY() + 50 + r.nextFloat() * FIGHTBAR_HEIGHT;
				} else {
					intersects = false;
				}
			}
		} while (intersects);
		return new Vector2(x, y);
	}
	
	
	
	protected void onClose() {
		for (Target t : this.targets) {
			this.detachChild(t);
			t.dispose();
		}
		this.targets.clear();
		this.object = null;
		this.fightDuration  = 0;
		this.lastTargetCreated = 0;
		this.resourcesManager.loadHUDResources();
	}
	
}
