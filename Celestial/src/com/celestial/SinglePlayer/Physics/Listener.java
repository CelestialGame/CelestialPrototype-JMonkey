package com.celestial.SinglePlayer.Physics;

import java.util.Iterator;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Inventory.InventoryDrop;
import com.celestial.SinglePlayer.Inventory.InventoryManager;
import com.celestial.util.InventoryException;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;

/**
 * Physics Collision Listener
 *
 * @author kevint.
 *         Created Jun 24, 2013.
 */
public class Listener implements PhysicsCollisionListener {

	CelestialPortal parent;
	
	public Listener(CelestialPortal parent) {
		this.parent = parent;
	}
	
	@Override
	public void collision(PhysicsCollisionEvent e) {
		//System.out.println("[A] " + e.getObjectA() + " [B] " + e.getObjectB());
		InventoryManager invmanager = parent.getInventoryManager();
		if(!invmanager.getDropItems().isEmpty()) {
			Iterator<InventoryDrop> itr = invmanager.getDropItems().iterator();
			while(itr.hasNext()) {
				InventoryDrop drop = itr.next();
				if(e.getObjectA().equals(drop.getCollisionBox())) {
					if(e.getObjectB().equals(this.parent.player)) {
						System.out.println("Collsion detected!");
						if(invmanager.getNextEmptySlot() != invmanager.EMPTY) {
							try {
								invmanager.setHotSlot(drop.getItem(), 1, invmanager.getNextEmptySlot());
								invmanager.removeDropItem(drop);
							} catch (InventoryException exception) {
								exception.printStackTrace();
							}
						}
					}
				}
				if(e.getObjectA().equals(this.parent.player)) {
					if(e.getObjectB().equals(drop.getCollisionBox())) {
						System.out.println("Collsion detected!");
						if(invmanager.getNextEmptySlot() != invmanager.EMPTY) {
							try {
								invmanager.setHotSlot(drop.getItem(), 1, invmanager.getNextEmptySlot());
								invmanager.removeDropItem(drop);
							} catch (InventoryException exception) {
								exception.printStackTrace();
							}
						}
					}
				}
				itr.remove();
			}
		}
		
	}
}
