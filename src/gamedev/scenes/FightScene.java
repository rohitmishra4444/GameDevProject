package gamedev.scenes;

import gamedev.game.GameActivity;
import gamedev.game.GameActivity.GameMode;
import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.objects.AnimatedObject;
import gamedev.objects.AnimatedObject.GameState;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FightScene extends Scene {

	public final static int FIGHTBAR_WIDTH = 626;

	protected Sprite fightBar;
	protected Sprite spear;
	protected AnimatedObject object;
	protected ResourcesManager resourcesManager;
	protected Body spearBody;
	protected float xMax;
	protected float xMin;

	public FightScene(final AnimatedObject object) {
		this.resourcesManager = ResourcesManager.getInstance();
		this.object = object;
		int width = (int) this.resourcesManager.avatar.getBody().getPosition().x
				* 32 - FIGHTBAR_WIDTH / 2;
		int height = (int) this.resourcesManager.avatar.getBody().getPosition().y * 32 - 50;
		this.fightBar = new Sprite(width, height,
				this.resourcesManager.fightRegion, this.resourcesManager.vbom);
		// this.fightBar.setScale(0.7f);
		this.spear = new Sprite(width, height,
				this.resourcesManager.spearRegion, this.resourcesManager.vbom);
		this.xMin = this.spear.getX() / 32;
		this.xMax = (xMin + FIGHTBAR_WIDTH) / 32;
		this.resourcesManager.engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				spearBody = PhysicsFactory.createBoxBody(
						resourcesManager.physicsWorld, spear,
						BodyType.KinematicBody,
						PhysicsFactory.createFixtureDef(0, 0, 0, true));
				resourcesManager.physicsWorld
						.registerPhysicsConnector(new PhysicsConnector(spear,
								spearBody, true, false));
				spearBody.setLinearVelocity(10, 0);
			}
		});
		this.attachChild(this.fightBar);
		this.attachChild(this.spear);
		this.setBackgroundEnabled(false);
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown()) {
					object.attack(50);
					resourcesManager.avatar.attack(10);
				}
				return false;
			}
		});
	}

	public void onManagedUpdate(float pSecondsElapsed) {
		if (this.spearBody == null)
			return;
		if (this.spearBody.getPosition().x > xMax) {
			this.spearBody.setLinearVelocity(-10, 0);
		} else if (this.spearBody.getPosition().x < xMin) {
			this.spearBody.setLinearVelocity(10, 0);
		}
		if (this.object.getState() == GameState.DEAD) {
			this.resourcesManager.avatar.setState(GameState.IDLE, -1);
			GameActivity.mode = GameMode.EXPLORING;
			SceneManager.getInstance().getCurrentGameMapScene()
					.clearChildScene();
		}
	}

	public void onClick() {

	}

}
