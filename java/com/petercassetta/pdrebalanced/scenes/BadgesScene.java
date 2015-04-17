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
package com.petercassetta.pdrebalanced.scenes;

import com.petercassetta.pdrebalanced.PDRebalanced;
import com.petercassetta.noosa.BitmapText;
import com.petercassetta.noosa.Camera;
import com.petercassetta.noosa.Game;
import com.petercassetta.noosa.NinePatch;
import com.petercassetta.noosa.audio.Music;
import com.petercassetta.pdrebalanced.Assets;
import com.petercassetta.pdrebalanced.Badges;
import com.petercassetta.pdrebalanced.Chrome;
import com.petercassetta.pdrebalanced.ui.Archs;
import com.petercassetta.pdrebalanced.ui.BadgesList;
import com.petercassetta.pdrebalanced.ui.ExitButton;
import com.petercassetta.pdrebalanced.ui.ScrollPane;
import com.petercassetta.pdrebalanced.ui.Window;
import com.petercassetta.utils.Callback;

public class BadgesScene extends PixelScene {
	
	private static final String TXT_TITLE = "Your Badges";
	
	private static final int MAX_PANE_WIDTH	= 160;
	
	@Override
	public void create() {
		
		super.create();
		
		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		int pw = Math.min( MAX_PANE_WIDTH, w - 6 );
		int ph = h - 30;
		
		NinePatch panel = Chrome.get( Chrome.Type.WINDOW );
		panel.size( pw, ph );
		panel.x = (w - pw) / 2;
		panel.y = (h - ph) / 2;
		add( panel );
		
		BitmapText title = PixelScene.createText( TXT_TITLE, 9 );
		title.hardlight( Window.TITLE_COLOR );
		title.measure();
		title.x = align( (w - title.width()) / 2 );
		title.y = align( (panel.y - title.baseLine()) / 2 );
		add( title );
		
		Badges.loadGlobal();
		
		ScrollPane list = new BadgesList( true );
		add( list );
		
		list.setRect( 
			panel.x + panel.marginLeft(), 
			panel.y + panel.marginTop(), 
			panel.innerWidth(), 
			panel.innerHeight() );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
		
		Badges.loadingListener = new Callback() {
			@Override
			public void call() {
				if (Game.scene() == BadgesScene.this) {
					PDRebalanced.switchNoFade(BadgesScene.class);
				}
			}
		};
	}
	
	@Override
	public void destroy() {
		
		Badges.saveGlobal();
		Badges.loadingListener = null;
		
		super.destroy();
	}
	
	@Override
	protected void onBackPressed() {
		PDRebalanced.switchNoFade(TitleScene.class);
	}
}
