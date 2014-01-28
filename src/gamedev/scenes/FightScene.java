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

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;

public class FightScene extends CameraScene {

	private final static int FIGHTBAR_WIDTH = 400;
	private static final int FIGHTBAR_HEIGHT = 300;
	private final static float TARGET_RADIUS = 50;
	private final static int SECONDS_COUNTDOWN = 1;
	
	protected Sprite fightDino;
	protected Dinosaur dinosaur;
	protected ResourcesManager resourcesManager;
	protected ArrayList<Target> targets = new ArrayList<Target>();
	protected ArrayList<Target> targetsToRemove = new ArrayList<Target>();
	protected float fightDuration = 0; // Total duration of fight
	protected float lastTargetCreated = 0; // Last target created (passed
											// seconds)
	protected float frequencyOfTargets = 0.2f; // How often are new targets
												// created in seconds
	protected float countdownTime = 0;
	protected int countdownIndex = 0;
	protected boolean countdownActive = false;
	protected Text countdownText;
	protected ArrayList<String> countdownStrings = new ArrayList<String>();
	private static FightScene instance;
	
	protected Random r = new Random();
	protected Rectangle rectLifeDino;
	
	/**
	 * Target-Pool contains the inactive target objects, so GC has less work with allocate/clear memory
	 */
	protected static ArrayList<Target> targetPool = new ArrayList<Target>();
	
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
					boolean hit = false;
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
							hit = true;
						}
					}
					if (!hit) {
						resourcesManager.avatar.attack(5);	
					}
					if (dinosaur.getState() == GameState.DEAD) {
						resourcesManager.avatar.setState(GameState.IDLE, -1);
						GameActivity.mode = GameMode.EXPLORING;
						resetScene();
						SceneManager.getInstance().getCurrentGameMapScene()
								.clearChildScene();
					}
					return true;
				}
				return false;
			}
		});
		
		this.countdownStrings.add("  3   ");
		this.countdownStrings.add("  2   ");
		this.countdownStrings.add("  1   ");
		this.countdownStrings.add("FIGHT!");
		this.countdownText = new Text(0, 0, ResourcesManager.getInstance().fontBig, "      ", resourcesManager.vbom);
		this.attachChild(this.countdownText);
		this.rectLifeDino = new Rectangle(0, 0, 64, GameActivity.HEIGHT, resourcesManager.vbom);
		this.rectLifeDino.setColor(Color.RED);
		this.rectLifeDino.setAlpha(0.75f);
		this.attachChild(this.rectLifeDino);
	}

	public static FightScene getInstance() {
		if (instance == null) {
			instance = new FightScene();
		}
//		ResourcesManager.getInstance().unloadHUDResources();
		instance.resetScene();
		ResourcesManager.getInstance().hud.showBarsOnly();
		return instance;
	}

	public void setObject(Dinosaur d) {
		this.dinosaur = d;
		this.frequencyOfTargets = (d.getDinoColor() == Dinosaur.COLOR_RED) ? 0.1f : 0.2f; 
		if (this.dinosaur.getScaleX() > 1) {
			this.frequencyOfTargets = this.frequencyOfTargets * 0.7f;
		}
		this.countdownActive = true;
		this.countdownText.setHorizontalAlign(HorizontalAlign.CENTER);
		this.centerShapeInCamera(this.countdownText);
		this.countdownText.setVisible(true);
		this.rectLifeDino.setHeight(GameActivity.HEIGHT);
		this.rectLifeDino.setY(0);
	}

	public void onManagedUpdate(float seconds) {
		if (this.dinosaur == null)
			return;
		
		if (this.countdownActive) {
			this.countdownTime += seconds;
			if (this.countdownTime > SECONDS_COUNTDOWN) {
				if (this.countdownIndex == this.countdownStrings.size()) {
					this.countdownIndex = 0;
					this.countdownTime = 0;
					this.countdownActive = false;
					this.countdownText.setText("      ");
					this.countdownText.setVisible(false);
					return;
				}
				this.countdownTime = 0;
				this.countdownText.setText(this.countdownStrings.get(this.countdownIndex));					
				this.countdownIndex++;
			}
		} else {
			this.fightDuration += seconds;
			this.lastTargetCreated += seconds;

			// First step: Check for dismissed targets!
			for (Target t : this.targets) {
				if ((this.fightDuration - t.getTimeShowed()) > t.getTimeCreated()) {
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

			// Second step: Remove targets - add them back to Pool and off the screen!
			for (Target t : targetsToRemove) {
				this.targets.remove(t);
				t.resetTarget();
				targetPool.add(t);
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
				}
			}
			
			// Update the remaining life of dinosaur
			if (dinosaur.getLife() < 100) {
				this.rectLifeDino.setY(GameActivity.HEIGHT - GameActivity.HEIGHT / 100 * dinosaur.getLife());				
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
		Vector2 position = this.getRandomPosition();
		// Get Dummy target from Pool or create a new Object...
		Target t = null;
		if (targetPool.isEmpty()) {
			t = new Target(TARGET_RADIUS);
			this.attachChild(t);
		} else {
			t = targetPool.get(0);
			targetPool.remove(t);
		}
		pGoodTarget = (dinosaur.getDinoColor() == Dinosaur.COLOR_GREEN) ? 0.7f : 0.9f;
		t.setTimeCreated(this.fightDuration);
		t.setX(position.x);
		t.setY(position.y);
		if (r.nextFloat() < pGoodTarget) {
			// Good target
			t.setColor(Color.BLACK);
			if (dinosaur.getDinoColor() == Dinosaur.COLOR_GREEN) {
				t.setTimeShowed(0.75f);
				t.setDamageOpponent(resourcesManager.avatar.isPoisened() ? 7 : 10);
				t.setDamageMiss(10);
			} else {
				t.setTimeShowed(0.5f);
				t.setDamageOpponent(resourcesManager.avatar.isPoisened() ? 2 : 5);
				t.setDamageMiss(15);
			}
		} else {
			// Bad target
			t.setColor(Color.RED);
			if (dinosaur.getDinoColor() == Dinosaur.COLOR_GREEN) {
				t.setDamageAvatar(10);
				t.setTimeShowed(0.75f);
			} else {
				t.setDamageAvatar(15);
				t.setTimeShowed(0.5f);
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

	protected void resetScene() {
		for (Target t : this.targets) {
			t.resetTarget();
			targetPool.add(t);
		}
		this.targets.clear();
		this.targetsToRemove.clear();
		this.dinosaur = null;
		this.fightDuration = 0;
		this.lastTargetCreated = 0;
//		this.resourcesManager.loadHUDResources();
		this.resourcesManager.hud.setVisible(true);
	}

}
