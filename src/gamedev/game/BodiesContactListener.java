package gamedev.game;

import gamedev.objects.Berry;
import gamedev.objects.BerryBush;

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
		if (x1.getBody() == null || x2.getBody() == null)
			return;
		System.out.println("Contact between: " + x1.getBody().getUserData()
				+ " and " + x2.getBody().getUserData());

		if (x1.getBody().getUserData() instanceof BerryBush) {
			addBerryToAvatarInventory((BerryBush) x1.getBody().getUserData());
		} else if (x2.getBody().getUserData() instanceof BerryBush) {
			addBerryToAvatarInventory((BerryBush) x2.getBody().getUserData());
		}

		// if ((x1.getBody().getUserData().equals("Player") &&
		// x2.getBody().getUserData().equals("StaticObject"))
		// || (x2.getBody().getUserData().equals("Player") &&
		// x1.getBody().getUserData().equals("StaticObject"))) {
		// System.out.println("Contact between player and StaticObject");
		// Vector2 a = player.body.getLinearVelocity();
		// a.x = -a.x/2;
		// a.y = -a.y/2;
		// player.body.setLinearVelocity(a);
		// }
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
		}
	}
}
