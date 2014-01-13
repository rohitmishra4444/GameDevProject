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

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.debugdraw.primitives.Ellipse;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FightSceneOld extends CameraScene implements SensorEventListener {

	public final static int FIGHTBAR_WIDTH = 626;

	protected Sprite fightDino;
	protected AnimatedObject object;
	protected ResourcesManager resourcesManager;
	protected SensorManager sensorManager;
	protected Ellipse e;
	protected ArrayList<Ellipse> targets;
	private static FightSceneOld instance;
	protected float roundTime = 0;
	protected float roundTimeMax = 5;
	
	private FightSceneOld() {
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
					for (Ellipse target : targets) {
						if (collidesWithTarget(target)) {
							targets.remove(target);
							target.detachSelf();
							target.dispose();
							object.attack(33);
							break;
						}
					}
					if (object.getState() == GameState.DEAD) {
						resourcesManager.avatar.setState(GameState.IDLE, -1);
						GameActivity.mode = GameMode.EXPLORING;
						onClose();
						SceneManager.getInstance().getCurrentGameMapScene().clearChildScene();
					}
				}
				return false;
			}
		});
		
		sensorManager = (SensorManager)resourcesManager.activity.getSystemService(Context.SENSOR_SERVICE);
	}
	
	protected boolean collidesWithTarget(Ellipse target) {
		float distance = MathUtils.distance(e.getX(), e.getY(), target.getX(), target.getY());
		return (distance <= 20);
	}
	
	public static FightSceneOld getInstance() {
		if (instance == null) {
			instance = new FightSceneOld();
		}
		instance.enableSensor();
		ResourcesManager.getInstance().unloadHUDResources();
		return instance;
	}
	
	public void onManagedUpdate(float seconds) {
		this.roundTime += seconds;
		if (roundTime > this.roundTimeMax) {
			this.roundTime = 0;
			this.resourcesManager.avatar.attack(10);
		}
	}
	
	protected void onClose() {
		this.disableSensor();
		for (Ellipse e : this.targets) {
			this.detachChild(e);
		}
		this.detachChild(this.e);
		this.targets = null;
		this.resourcesManager.loadHUDResources();
	}
	
	
	private void enableSensor() {
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
	}
	
	private void disableSensor() {
		sensorManager.unregisterListener(this);
	}
		
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
        float posX = e.getX() + event.values[1] * 2;
        float posY = e.getY() + event.values[0] * 2;
        posX = Math.min(posX, this.fightDino.getX()+500);
        posX = Math.max(posX, this.fightDino.getX());
        posY = Math.min(posY, this.fightDino.getY()+500);
        posY = Math.max(posY, this.fightDino.getY());        
        e.setPosition(posX, posY);
	}

	public void setObject(AnimatedObject object) {
		this.object = object;
		// TODO Get difficult level from object...
		this.targets = new ArrayList<Ellipse>();
		float minX = this.fightDino.getX();
		float maxX = minX + 400;
		float minY = this.fightDino.getY();
		float maxY = minY + 355;
		Random r = new Random();
		for (int i=0; i<5; i++) {
			Ellipse e = new Ellipse(minX + r.nextFloat() * (maxX-minX) -20, minY + r.nextFloat() * (maxY-minY) -20, 20, 20, resourcesManager.vbom);
			e.setColor(Color.BLACK);
			e.setDrawMode(DrawMode.TRIANGLE_FAN);
			e.setAlpha(0.8f);
			this.targets.add(e);
			this.attachChild(e);
		}
		e = new Ellipse(this.fightDino.getX(), this.fightDino.getY(), 8, 8, this.resourcesManager.vbom);
		e.setColor(Color.RED);
		e.setDrawMode(DrawMode.TRIANGLE_FAN);
		this.attachChild(e);		
	}

}
