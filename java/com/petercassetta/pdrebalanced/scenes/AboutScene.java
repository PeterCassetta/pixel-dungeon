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

import android.content.Intent;
import android.net.Uri;

import com.petercassetta.pdrebalanced.PDRebalanced;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.petercassetta.pdrebalanced.effects.Flare;
import com.petercassetta.pdrebalanced.ui.Archs;
import com.petercassetta.pdrebalanced.ui.ExitButton;
import com.petercassetta.pdrebalanced.ui.Icons;
import com.petercassetta.pdrebalanced.ui.Window;

public class AboutScene extends PixelScene {

    private static final String MODTXT =
        "\"Pixel Dungeon: Rebalanced\" mod by Peter Cassetta.\n\n" +
        "News and project info:\n";

    private static final String MODLNK = "peter.cassetta.info/pd-rebalanced";

    private static final String TXT =
        "Based on Pixel Dungeon 1.7.5a by Watabou, with music by Cube_Code;" +
        " used under the GPLv3. \n\n" +
        "Official website:";

    private static final String LNK = "pixeldungeon.watabou.ru";

	@Override
	public void create() {
        super.create();

        // About mod
        BitmapTextMultiline mtext = createMultiline(MODTXT, 8);
        mtext.maxWidth = Math.min( Camera.main.width / ((PDRebalanced.landscape()) ? 2 : 1) , 110 );
        mtext.measure();
        add(mtext);

        if (PDRebalanced.landscape()) {
            mtext.x = align( Camera.main.width/4 - mtext.width()/2 );
            mtext.y = align( (Camera.main.height - mtext.height()) / 2 );
        } else {
            mtext.x = align( (Camera.main.width - mtext.width()) / 2 );
            mtext.y = align( (Camera.main.height - mtext.height()) / 3 );
        }

        BitmapTextMultiline mlink = createMultiline( MODLNK, 8 );
        mlink.maxWidth = Math.min( Camera.main.width / ((PDRebalanced.landscape()) ? 2 : 1) , 110 );
        mlink.measure();
        mlink.hardlight( Window.REBALANCED_TITLE_COLOR );
        add( mlink );

        if (PDRebalanced.landscape())
            mlink.x = align( Camera.main.width/4 - mlink.width()/2 );
        else
            mlink.x = mtext.x;
        mlink.y = mtext.y + mtext.height();

        TouchArea hotArea1 = new TouchArea( mlink ) {
            @Override
            protected void onClick( Touch touch ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + MODLNK ) );
                Game.instance.startActivity( intent );
            }
        };
        add( hotArea1 );

        Image scales = Icons.SCALES.get();
        if (PDRebalanced.landscape())
            scales.x = align( Camera.main.width/4 - scales.width/2 );
        else
            scales.x = align( (Camera.main.width - scales.width) / 2 );
        scales.y = mtext.y - scales.height - 8;
        add( scales );

        new Flare( 7, 64 ).color( 0x332211, true ).show( scales, 0 ).angularSpeed = +20;

        // About Vanilla PD
        BitmapTextMultiline text = createMultiline( TXT, 8 );
        text.maxWidth = Math.min( Camera.main.width / ((PDRebalanced.landscape()) ? 2 : 1) , 100 );
        text.measure();
        add( text );

        if (PDRebalanced.landscape()) {
            text.x = align( ((3 * Camera.main.width)/4) - text.width()/2 );
            text.y = align( ( Camera.main.height - text.height() ) / 2 );
        } else {
            text.x = align( (Camera.main.width - text.width()) / 2 );
            text.y = align( ( 3 * (Camera.main.height - text.height())) / 4 );
        }

        BitmapTextMultiline link = createMultiline( LNK, 8 );
        link.maxWidth = Math.min( Camera.main.width / ((PDRebalanced.landscape()) ? 2 : 1) , 100 );
        link.measure();
        link.hardlight( Window.TITLE_COLOR );
        add( link );

        link.x = text.x;
        link.y = text.y + text.height();

        TouchArea hotArea2 = new TouchArea( link ) {
            @Override
            protected void onClick( Touch touch ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + LNK ) );
                Game.instance.startActivity( intent );
            }
        };
        add( hotArea2 );

		Image wata = Icons.WATA.get();
        if (PDRebalanced.landscape())
            wata.x = align( ((3 * Camera.main.width)/4) - wata.width/2 );
        else
            wata.x = align( (Camera.main.width - wata.width) / 2 );
		wata.y = text.y - wata.height - 8;
		add( wata );

		new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +20;

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		PDRebalanced.switchNoFade(TitleScene.class);
	}
}
