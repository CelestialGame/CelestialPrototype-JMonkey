/**
@author	Mitch Talmadge
Date Created:
	Jun 26, 2013
*/

package com.celestial.SinglePlayer.Events;

import com.celestial.SinglePlayer.Components.Planet;
import com.celestial.SinglePlayer.Components.Player;
import com.cubes.Vector3Int;
import com.jme3.math.Vector3f;

/**
 * Player events are called AFTER any action. It is sort of a way of stopping it before it happens.
 * The returned boolean is whether or not this is an allowed action. This is NOT the place to perform the action.
 * Use like this: if(PlayerJumpEvent()){ player.Jump(); }
 * 
 */
public class PlayerEvents {
	
	/**
	 * This event is called whenever the player moves.
	 * @param player The player moving.
	 * @param newLocation Where the player wants to end up.
	 */
	public static void PlayerMoveEvent(Player player, Vector3f newLocation)
	{
		
	}
	
	/**
	 * This event is called whenever the player jumps.
	 * @param player The player jumping.
	 */
	public static void PlayerJumpEvent(Player player)
	{
	}
	
	/**
	 * This event is called whenever the player deletes a block.
	 * @param player The player deleting the block.
	 * @param planet The planet the block was removed from.
	 * @param collisionPoint The point where the player clicked (null if this action was caused without clicking)
	 * @param blockLocation The location of the block being deleted.
	 */
	public static void PlayerDeleteBlockEvent(Player player, Planet planet, Vector3f collisionPoint, Vector3Int blockLocation)
	{
	}
	
	/**
	 * This event is called whenever the player adds a block.
	 * @param player The player adding the block.
	 * @param planet The planet the block was added to.
	 * @param collisionPoint The point where the player clicked (null if this action was caused without clicking)
	 * @param blockLocation The location of the block being added.
	 */
	public static void PlayerAddBlockEvent(Player player, Planet planet, Vector3f collisionPoint, Vector3Int blockLocation)
	{
	}
}
