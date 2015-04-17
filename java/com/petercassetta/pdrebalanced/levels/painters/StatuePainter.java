/*
 * Pixel Dungeon: Rebalanced
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2015 Peter Cassetta
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.petercassetta.pdrebalanced.levels.painters;

import com.petercassetta.pdrebalanced.actors.Actor;
import com.petercassetta.pdrebalanced.actors.mobs.Statue;
import com.petercassetta.pdrebalanced.items.keys.IronKey;
import com.petercassetta.pdrebalanced.levels.Level;
import com.petercassetta.pdrebalanced.levels.Room;
import com.petercassetta.pdrebalanced.levels.Terrain;
import com.petercassetta.utils.Point;

public class StatuePainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

		Point c = room.center();
		int cx = c.x;
		int cy = c.y;
		
		Room.Door door = room.entrance();
		
		door.set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
		
		if (door.x == room.left) {
			
			fill( level, room.right - 1, room.top + 1, 1, room.height() - 1 , Terrain.STATUE );
			cx = room.right - 2;
			
		} else if (door.x == room.right) {
			
			fill( level, room.left + 1, room.top + 1, 1, room.height() - 1 , Terrain.STATUE );
			cx = room.left + 2;
			
		} else if (door.y == room.top) {
			
			fill( level, room.left + 1, room.bottom - 1, room.width() - 1, 1 , Terrain.STATUE );
			cy = room.bottom - 2;
			
		} else if (door.y == room.bottom) {
			
			fill( level, room.left + 1, room.top + 1, room.width() - 1, 1 , Terrain.STATUE );
			cy = room.top + 2;
			
		}
		
		Statue statue = new Statue();
		statue.pos = cx + cy * Level.WIDTH;
		level.mobs.add( statue );
		Actor.occupyCell( statue );
	}
}
