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
package com.petercassetta.pdrebalanced.effects;

import com.petercassetta.noosa.particles.Emitter;
import com.petercassetta.noosa.particles.PixelParticle;
import com.petercassetta.pdrebalanced.DungeonTilemap;
import com.petercassetta.pdrebalanced.scenes.GameScene;
import com.petercassetta.utils.PointF;
import com.petercassetta.utils.Random;

public class Splash {
	
	public static void at( int cell, final int color, int n ) {
		at( DungeonTilemap.tileCenterToWorld( cell ), color, n );
	}
	
	public static void at( PointF p, final int color, int n ) {
		
		if (n <= 0) {
			return;
		}
		
		Emitter emitter = GameScene.emitter();
		emitter.pos( p );
		
		FACTORY.color = color;
		FACTORY.dir = -3.1415926f / 2;
		FACTORY.cone = 3.1415926f;
		emitter.burst( FACTORY, n );
	}
	
	public static void at( PointF p, final float dir, final float cone, final int color, int n ) {
		
		if (n <= 0) {
			return;
		}
		
		Emitter emitter = GameScene.emitter();
		emitter.pos( p );
		
		FACTORY.color = color;
		FACTORY.dir = dir;
		FACTORY.cone = cone;
		emitter.burst( FACTORY, n );
	}
	
	private static final SplashFactory FACTORY = new SplashFactory(); 
			
	private static class SplashFactory extends Emitter.Factory {

		public int color;
		public float dir;
		public float cone;
		
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			PixelParticle p = (PixelParticle)emitter.recycle( PixelParticle.Shrinking.class );
			
			p.reset( x, y, color, 4, Random.Float( 0.5f, 1.0f ) );
			p.speed.polar( Random.Float( dir - cone / 2, dir + cone / 2 ), Random.Float( 40, 80 ) );
			p.acc.set( 0, +100 );
		}
	}
}
