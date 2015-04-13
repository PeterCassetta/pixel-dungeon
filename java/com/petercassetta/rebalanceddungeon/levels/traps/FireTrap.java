/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.petercassetta.rebalanceddungeon.levels.traps;

import com.petercassetta.rebalanceddungeon.actors.Char;
import com.petercassetta.rebalanceddungeon.actors.blobs.Blob;
import com.petercassetta.rebalanceddungeon.actors.blobs.Fire;
import com.petercassetta.rebalanceddungeon.effects.CellEmitter;
import com.petercassetta.rebalanceddungeon.effects.particles.FlameParticle;
import com.petercassetta.rebalanceddungeon.scenes.GameScene;

public class FireTrap {

	// 0xFF7708
	
	public static void trigger( int pos, Char ch ) {
		
		GameScene.add( Blob.seed( pos, 2, Fire.class ) );
		CellEmitter.get( pos ).burst( FlameParticle.FACTORY, 5 );
		
	}
}
