package com.celestial.SinglePlayer.Physics;

import java.util.Iterator;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Inventory.InventoryDrop;
import com.celestial.SinglePlayer.Inventory.InventoryManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;

/**
 * Physics Collision Listener
 *
 * @author kevint.
 *         Created Jun 24, 2013.
 */
public class Listener implements PhysicsCollisionListener {

	CelestialPortal portal;

	public Listener(CelestialPortal portal) {
		this.portal = portal;
	}

	@Override
	public void collision(PhysicsCollisionEvent e) {
		InventoryManager invmanager = portal.getInventoryManager();
		if(!invmanager.getDropItems().isEmpty()) {
			Iterator<InventoryDrop> itr = invmanager.getDropItems().iterator();
			while(itr.hasNext()) {
				InventoryDrop drop = itr.next();
				if((e.getObjectA().equals(drop.getCollisionBox()) && e.getObjectB().equals(this.portal.player.getCollisionBox())) || (e.getObjectB().equals(drop.getCollisionBox()) && e.getObjectA().equals(this.portal.player.getCollisionBox()))) {
					invmanager.pickupDrop(drop, 1);
					invmanager.updateAll();
					invmanager.removeDropItem(drop, itr);
				}
			}
		}

	}
}
