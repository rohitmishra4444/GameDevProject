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
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FightScene extends CameraScene implements SensorEventListener {

	public final static int FIGHTBAR_WIDTH = 626;

	protected Sprite fightDino;
	protected AnimatedObject object;
	protected ResourcesManager resourcesManager;
	protected SensorManager sensorManager;
	protected Ellipse e;
	protected ArrayList<Ellipse> targets;
	private static FightScene instance;
	
	private FightScene() {
		super(ResourcesManager.getInstance().camera);
		this.resourcesManager = ResourcesManager.getInstance();
		this.fightDino = new Sprite(0, 0, this.resourcesManager.fightDinoRegion, this.resourcesManager.vbom);
		centerShapeInCamera(this.fightDino);
		this.attachChild(this.fightDino);
		
		e = new Ellipse(this.fightDino.getX(), this.fightDino.getY(), 8, 8, this.resourcesManager.vbom);
		e.setColor(Color.RED);
		e.setDrawMode(DrawMode.TRIANGLE_FAN);
		this.attachChild(e);		
		this.setBackgroundEnabled(false);
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown()) {
					object.attack(50);
					resourcesManager.avatar.attack(10);
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
        System.out.println("In Constructor FightScene");
	}
	
	public static FightScene getInstance() {
		if (instance == null) {
			instance = new FightScene();
		}
		instance.enableSensor();
		return instance;
	}

	protected void onClose() {
		this.disableSensor();
		for (Ellipse e : this.targets) {
			this.detachChild(e);
		}
		this.targets = null;
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
        posX = Math.min(posX, this.resourcesManager.camera.getXMax());
        posX = Math.max(posX, this.resourcesManager.camera.getXMin());
        posY = Math.min(posY, this.resourcesManager.camera.getYMax());
        posY = Math.max(posY, this.resourcesManager.camera.getYMin());        
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
			Ellipse e = new Ellipse(minX + r.nextFloat() * (maxX-minX) -10, minY + r.nextFloat() * (maxY-minY) -10, 20, 20, resourcesManager.vbom);
			e.setColor(Color.BLACK);
			e.setDrawMode(DrawMode.TRIANGLE_FAN);
			e.setAlpha(0.5f);
			this.targets.add(e);
			this.attachChild(e);
		}
	}

}
