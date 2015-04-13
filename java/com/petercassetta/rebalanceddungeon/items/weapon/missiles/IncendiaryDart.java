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
package com.petercassetta.rebalanceddungeon.items.weapon.missiles;

import com.petercassetta.rebalanceddungeon.Dungeon;
import com.petercassetta.rebalanceddungeon.actors.Actor;
import com.petercassetta.rebalanceddungeon.actors.Char;
import com.petercassetta.rebalanceddungeon.actors.blobs.Blob;
import com.petercassetta.rebalanceddungeon.actors.blobs.Fire;
import com.petercassetta.rebalanceddungeon.actors.buffs.Buff;
import com.petercassetta.rebalanceddungeon.actors.buffs.Burning;
import com.petercassetta.rebalanceddungeon.items.Item;
import com.petercassetta.rebalanceddungeon.levels.Level;
import com.petercassetta.rebalanceddungeon.scenes.GameScene;
import com.petercassetta.rebalanceddungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class IncendiaryDart extends MissileWeapon {

	{
		name = "incendiary dart";
		image = ItemSpriteSheet.INCENDIARY_DART;
		
		STR = 12;
		
		MIN = 1;
		MAX = 2;
	}
	
	public IncendiaryDart() {
		this( 1 );
	}
	
	public IncendiaryDart( int number ) {
		super();
		quantity = number;
	}
	
	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == curUser) {
			if (Level.flamable[cell]) {
				GameScene.add( Blob.seed( cell, 4, Fire.class ) );
			} else {
				super.onThrow( cell );
			}
		} else {
			if (!curUser.shoot( enemy, this )) {
				Dungeon.level.drop( this, cell ).sprite.drop();
			}
		}
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		Buff.affect( defender, Burning.class ).reignite( defender );
		super.proc( attacker, defender, damage );
	}
	
	@Override
	public String desc() {
		return 
			"The spike on each of these darts is designed to pin it to its target " +
			"while the unstable compounds strapped to its length burst into brilliant flames.";
	}
	
	@Override
	public Item random() {
		quantity = Random.Int( 3, 6 );
		return this;
	}
	
	@Override
	public int price() {
		return 10 * quantity;
	}
}
