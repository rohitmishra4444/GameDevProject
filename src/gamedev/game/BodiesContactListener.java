package gamedev.game;

import gamedev.game.GameActivity.GameMode;
import gamedev.objects.Berry;
import gamedev.objects.BerryBush;
import gamedev.objects.Dinosaur;
import gamedev.objects.AnimatedObject.GameState;
import gamedev.scenes.FightScene;

import android.widget.Toast;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class BodiesContactListener implements ContactListener {

	protected ResourcesManager resourcesManager = ResourcesManager
			.getInstance();

	// protected Avatar player = resourcesManager.avatar;

	@Override
	public void beginContact(Contact contact) {
		final Fixture x1 = contact.getFixtureA();
		final Fixture x2 = contact.getFixtureB();
		if (x1.getBody() == null || x2.getBody() == null || x1.getBody().getUserData() == null || x2.getBody().getUserData() == null)
			return;
		
		System.out.println("Contact between: " + x1.getBody().getUserData() + " and " + x2.getBody().getUserData());

		if (x1.getBody().getUserData() instanceof BerryBush) {
			addBerryToAvatarInventory((BerryBush) x1.getBody().getUserData());
		} else if (x2.getBody().getUserData() instanceof BerryBush) {
			addBerryToAvatarInventory((BerryBush) x2.getBody().getUserData());
		}
		
		if (x1.getBody().getUserData().equals("Avatar") && x2.getBody().getUserData() instanceof Dinosaur) {
			Dinosaur d = (Dinosaur) x2.getBody().getUserData();
			if (d.getState() == GameState.DEAD) return;
			showFightScene(d);
		} else if (x2.getBody().getUserData().equals("Avatar") && x1.getBody().getUserData() instanceof Dinosaur) {
			Dinosaur d = (Dinosaur) x1.getBody().getUserData();
			if (d.getState() == GameState.DEAD) return;
			showFightScene(d);			
		}
		
	}
	
	private void showFightScene(Dinosaur d) {
		GameActivity.mode = GameMode.FIGHTING;
		FightScene fight = new FightScene(d);
		this.resourcesManager.level.setChildScene(fight);
	}
	
	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	private void addBerryToAvatarInventory(BerryBush berryBush) {
		Berry berry = berryBush.getBerry();
		if (berry != null) {
			resourcesManager.avatar.addBerryToInventory(berry);
			resourcesManager.activity.toastOnUIThread("Collected berries", Toast.LENGTH_SHORT);
		}
	}
}
