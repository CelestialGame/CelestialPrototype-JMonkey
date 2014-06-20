package com.celestial.SinglePlayer.Physics;

import java.util.Iterator;

import com.celestial.SinglePlayer.SPPortal;
import com.celestial.SinglePlayer.Components.Planet;
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

	@Override
	public void collision(PhysicsCollisionEvent e) {
		InventoryManager invmanager = SPPortal.self.getInventoryManager();
		if(!invmanager.getDropItems().isEmpty()) {
			Iterator<InventoryDrop> itr = invmanager.getDropItems().iterator();
			while(itr.hasNext()) {
				InventoryDrop drop = itr.next();
				if((e.getObjectA().equals(drop.getCollisionBox()) && e.getObjectB().equals(SPPortal.self.player.getCollisionBox())) || (e.getObjectB().equals(drop.getCollisionBox()) && e.getObjectA().equals(SPPortal.self.player.getCollisionBox()))) {
					invmanager.pickupDrop(drop, 1);
					invmanager.updateAll();
					invmanager.removeDropItem(drop, itr);
				}
			}
		}

	}
}
