package com.celestial.Tools;

import com.celestial.SinglePlayer.Components.Player;
import com.cubes.Vector3i;

public class Marker extends ToolObject{
	
	Vector3i pos1;
	Vector3i pos2;
	
	boolean onFirst = true;

	@Override
	public void onActionUse(Object[] values, Player player) {
		Vector3i blockLocation = (Vector3i) values[1];
		//Vector3f blockAbsLocation = (Vector3f) values[0];
		if(blockLocation != null)
		{
			if(onFirst) {
				pos1 = blockLocation;
				System.out.println("First position set: " + pos1.toString());
				onFirst = false;
			} else {
				pos2 = blockLocation;
				System.out.println("Second position set: " + pos2.toString());
				onFirst = true;
			}
		}
		
	}
	
	public Vector3i[] getPositions() {
		if(pos1 != null && pos2 != null)
			return new Vector3i[]{pos1, pos2};
		else
			return null;
	}

}
