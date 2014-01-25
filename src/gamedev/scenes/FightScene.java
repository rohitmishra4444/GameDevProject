package gamedev.scenes;

import gamedev.game.GameActivity;
import gamedev.game.GameActivity.GameMode;
import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.objects.AnimatedObject;
import gamedev.objects.AnimatedObject.GameState;
import gamedev.objects.Dinosaur;
import gamedev.objects.Target;

import java.util.ArrayList;
import java.util.Random;

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
	protected AnimatedObject dinosaur;
	protected ResourcesManager resourcesManager;
	protected ArrayList<Target> targets = new ArrayList<Target>();
	protected ArrayList<Target> targetsToRemove = new ArrayList<Target>();
	protected float fightDuration = 0; // Total duration of fight
	protected float lastTargetCreated = 0; // Last target created (passed
											// seconds)
	protected float frequencyOfTargets = 0.2f; // How often are new targets
												// created in seconds
	private static FightScene instance;
	
	protected Random r = new Random();
	
	private FightScene() {
		super(ResourcesManager.getInstance().camera);
		this.resourcesManager = ResourcesManager.getInstance();
		this.fightDino = new Sprite(0, 0,
				this.resourcesManager.fightDinoRegion,
				this.resourcesManager.vbom);
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
							if (target.getDamageOpponent() > 0) {
								dinosaur.attack(target.getDamageOpponent());
								resourcesManager.hit.play();
							}
							if (target.getDamageAvatar() > 0) {
								resourcesManager.avatar.attack(target
										.getDamageAvatar());
							}
							target.setRemovable(true);
						}
					}
					if (dinosaur.getState() == GameState.DEAD) {
						resourcesManager.avatar.setState(GameState.IDLE, -1);
						GameActivity.mode = GameMode.EXPLORING;
						onClose();
						SceneManager.getInstance().getCurrentGameMapScene()
								.clearChildScene();
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

	public void setObject(Dinosaur d) {
		this.dinosaur = d;
		this.frequencyOfTargets = (d.getDinoColor() == Dinosaur.COLOR_RED) ? 0.1f : 0.2f; 
		if (ResourcesManager.getInstance().avatar.isPoisened()) {
			this.frequencyOfTargets = this.frequencyOfTargets * 0.5f;
		}
		if (this.dinosaur.getScaleX() > 1) {
			this.frequencyOfTargets = this.frequencyOfTargets * 0.8f;
		}
	}

	public void onManagedUpdate(float seconds) {
		if (this.dinosaur == null)
			return;
		this.fightDuration += seconds;
		this.lastTargetCreated += seconds;

		// First step: Check for dismissed targets!
		for (Target t : this.targets) {
			if ((this.fightDuration - t.getTimeAlive()) > t.getTimeCreated()) {
				// Missed!
				if (t.getDamageMiss() > 0)
					resourcesManager.avatar.attack(t.getDamageMiss());
				t.setRemovable(true);
				// System.out.println("You dismissed target [actual time: "+
				// fightDuration + ", created: " + t.getTimeCreated() +
				// ", timeAlive: " + t.getTimeAlive()+"]");
			}
			if (t.isRemovable())
				targetsToRemove.add(t);
		}

		// Second step: Remove targets!
		for (Target t : targetsToRemove) {
			this.targets.remove(t);
			this.detachChild(t);
			t.dispose();
		}
		this.targetsToRemove.clear();

		// Third step: Check how much new target should be created.
		// Note that this depends on the time how often this method can be
		// called - therefore we maybe must create multiple targets on slower
		// devices
		if (this.lastTargetCreated > this.frequencyOfTargets) {
			int nTargets = (int) (this.lastTargetCreated / this.frequencyOfTargets);
			this.lastTargetCreated = 0;
			for (int i = 0; i < nTargets; i++) {
				Target t = this.generateTarget();
				this.targets.add(t);
				this.attachChild(t);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	protected Target generateTarget() {
		// Percentage to generate a "good" target that attacks the object when
		// hit ;)
		// 1-p is the percentage of a "bad" target, that should not be hit
		float pGoodTarget = 0;
		Random r = new Random();
		Vector2 position = this.getRandomPosition();
		Target t = null;

		// I know it's horrible this way, but who cares... we're in a hurry
		if (this.dinosaur instanceof Dinosaur) {
			Dinosaur d = (Dinosaur) this.dinosaur;
			pGoodTarget = (d.getDinoColor() == Dinosaur.COLOR_GREEN) ? 0.7f
					: 0.9f;
			if (r.nextFloat() < pGoodTarget) {
				// Good target
				if (d.getDinoColor() == Dinosaur.COLOR_GREEN) {
					t = new Target(position.x, position.y, TARGET_RADIUS,
							Color.BLACK, this.fightDuration, 0.75f);
					t.setDamageOpponent(10);
					t.setDamageMiss(10);
				} else {
					t = new Target(position.x, position.y, TARGET_RADIUS,
							Color.BLACK, this.fightDuration, 0.5f);
					t.setDamageOpponent(5);
					t.setDamageMiss(15);
				}
			} else {
				// Bad target
				if (d.getDinoColor() == Dinosaur.COLOR_GREEN) {
					t = new Target(position.x, position.y, TARGET_RADIUS,
							Color.RED, this.fightDuration, 0.75f);
					t.setDamageAvatar(10);
				} else {
					t = new Target(position.x, position.y, TARGET_RADIUS,
							Color.RED, this.fightDuration, 0.5f);
					t.setDamageAvatar(15);
				}
			}
		}
		return t;
	}

	/**
	 * Get a random position so that none of the targets are overlapping
	 * 
	 * @return
	 */
	protected Vector2 getRandomPosition() {
		float x = this.fightDino.getX() + 50 + r.nextFloat() * FIGHTBAR_WIDTH;
		float y = this.fightDino.getY() + 50 + r.nextFloat() * FIGHTBAR_HEIGHT;
		boolean intersects = false;
		do {
			for (Target t : this.targets) {
				if (MathUtils.distance(t.getX(), t.getY(), x, y) < t
						.getRadius() * 2) {
					intersects = true;
					x = this.fightDino.getX() + 50 + r.nextFloat()
							* FIGHTBAR_WIDTH;
					y = this.fightDino.getY() + 50 + r.nextFloat()
							* FIGHTBAR_HEIGHT;
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
		this.dinosaur = null;
		this.fightDuration = 0;
		this.lastTargetCreated = 0;
		this.resourcesManager.loadHUDResources();
	}

}
