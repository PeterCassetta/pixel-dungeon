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
package com.petercassetta.pdrebalanced.actors.mobs.npcs;

import java.util.HashSet;

import com.petercassetta.pdrebalanced.actors.buffs.Bleeding;
import com.petercassetta.pdrebalanced.actors.buffs.Levitation;
import com.petercassetta.pdrebalanced.items.Gold;
import com.petercassetta.pdrebalanced.items.quest.BrokenRapier;
import com.petercassetta.pdrebalanced.utils.GLog;
import com.petercassetta.noosa.audio.Sample;
import com.petercassetta.pdrebalanced.Assets;
import com.petercassetta.pdrebalanced.Challenges;
import com.petercassetta.pdrebalanced.Dungeon;
import com.petercassetta.pdrebalanced.Journal;
import com.petercassetta.pdrebalanced.actors.Actor;
import com.petercassetta.pdrebalanced.actors.Char;
import com.petercassetta.pdrebalanced.actors.blobs.Blob;
import com.petercassetta.pdrebalanced.actors.blobs.ParalyticGas;
import com.petercassetta.pdrebalanced.actors.buffs.Buff;
import com.petercassetta.pdrebalanced.actors.buffs.Paralysis;
import com.petercassetta.pdrebalanced.actors.buffs.Roots;
import com.petercassetta.pdrebalanced.actors.mobs.Mob;
import com.petercassetta.pdrebalanced.effects.CellEmitter;
import com.petercassetta.pdrebalanced.effects.Speck;
import com.petercassetta.pdrebalanced.items.Generator;
import com.petercassetta.pdrebalanced.items.Item;
import com.petercassetta.pdrebalanced.items.armor.Armor;
import com.petercassetta.pdrebalanced.items.armor.ClothArmor;
import com.petercassetta.pdrebalanced.items.quest.DriedRose;
import com.petercassetta.pdrebalanced.items.quest.RatSkull;
import com.petercassetta.pdrebalanced.items.weapon.Weapon;
import com.petercassetta.pdrebalanced.items.weapon.missiles.MissileWeapon;
import com.petercassetta.pdrebalanced.levels.SewerLevel;
import com.petercassetta.pdrebalanced.scenes.GameScene;
import com.petercassetta.pdrebalanced.sprites.CasdaSprite;
import com.petercassetta.pdrebalanced.sprites.FetidRatSprite;
import com.petercassetta.pdrebalanced.sprites.GhostSprite;
import com.petercassetta.pdrebalanced.windows.WndQuest;
import com.petercassetta.pdrebalanced.windows.WndSadGhost;
import com.petercassetta.utils.Bundle;
import com.petercassetta.utils.Random;

public class Ghost extends NPC {

	{
		name = "sad ghost";
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = WANDERING;
	}
	
	private static final String TXT_ROSE1	=
		"Hello adventurer... Once I was like you - strong and confident... " +
		"But now I'm dead... and I cannot leave this place, not until I have my _dried rose_... " +
		"It's very important to me... Some monster stole it from my body...";
	
	private static final String TXT_ROSE2	=
		"Please... Help me... Find the rose...";

    private static final String TXT_RAT1	=
            "Hello adventurer... Once I was like you - strong and confident... " +
                    "But now I'm dead... and I cannot leave this place, not until I am have been avenged... " +
                    "Slay the _fetid rat_ that has taken my life...";

    private static final String TXT_RAT2	=
            "Please... Help me... Slay the abomination...";

    private static final String TXT_CASDA1	=
            "Hello adventurer... Once I was like you - strong and confident... " +
                    "But now I'm dead... and I cannot leave this place, not until I have reclaimed what is mine... " +
                    "Slay _Cas'da the Defiler_, who has destroyed my beloved sword and I, taking its hilt for himself...";

    private static final String TXT_CASDA2	=
            "Please... Help me... Slay the Defiler...";

	
	public Ghost() {
		super();

		Sample.INSTANCE.load( Assets.SND_GHOST );
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public float speed() {
		return 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		sprite.turnTo( pos, Dungeon.hero.pos );
		
		Sample.INSTANCE.play( Assets.SND_GHOST );
		
		if (Quest.given) {
			Item item;
			if (Quest.alternative) {
                if (Quest.casda)
                    item = Dungeon.hero.belongings.getItem(BrokenRapier.class);
                else
                    item = Dungeon.hero.belongings.getItem(RatSkull.class);
            } else {
                item = Dungeon.hero.belongings.getItem(DriedRose.class);
            }

			if (item != null) {
				GameScene.show( new WndSadGhost( this, item ) );
			} else {
                if (Quest.alternative) {
                    GameScene.show(new WndQuest(this, Quest.casda ? TXT_CASDA2 : TXT_RAT2));
                } else {
                    GameScene.show(new WndQuest(this, TXT_ROSE2));
                }

				int newPos = -1;
				for (int i=0; i < 10; i++) {
					newPos = Dungeon.level.randomRespawnCell();
					if (newPos != -1) {
						break;
					}
				}
				if (newPos != -1) {
					
					Actor.freeCell( pos );
					
					CellEmitter.get( pos ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
					pos = newPos;
					sprite.place( pos );
					sprite.visible = Dungeon.visible[pos];
				}
			}
			
		} else {

            if (Quest.alternative) {
                GameScene.show(new WndQuest(this, Quest.casda ? TXT_CASDA1 : TXT_RAT1));
            } else {
                GameScene.show(new WndQuest(this, TXT_ROSE1));
            }
			Quest.given = true;
			
			Journal.add( Journal.Feature.GHOST );
		}
	}
	
	@Override
	public String description() {
		return 
			"The ghost is barely visible. It looks like a shapeless " +
			"spot of faint light with a sorrowful face.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Paralysis.class );
		IMMUNITIES.add( Roots.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	public static class Quest {

		private static boolean spawned;

        private static boolean alternative;

        private static boolean casda;

		private static boolean given;

		private static boolean processed;

		private static int depth;
		
		private static int left2kill;
		
		public static Weapon weapon;
		public static Armor armor;
		
		public static void reset() {
			spawned = false; 
			
			weapon = null;
			armor = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
        private static final String ALTERNATIVE	= "alternative";
        private static final String CASDA	    = "casda";
		private static final String LEFT2KILL	= "left2kill";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String ARMOR		= "armor";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {

                node.put( ALTERNATIVE, alternative );
				if (!alternative) {
					node.put( LEFT2KILL, left2kill );
				} else {
                    node.put( CASDA, casda );
                }
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
				node.put( PROCESSED, processed );
				
				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				
				alternative	= node.getBoolean( ALTERNATIVE );
				if (!alternative) {
					left2kill = node.getInt( LEFT2KILL );
				} else {
                    casda = node.getBoolean( CASDA );
                }
				
				given	    = node.getBoolean( GIVEN );
				depth	    = node.getInt( DEPTH );
				processed	= node.getBoolean( PROCESSED );
				
				weapon	    = (Weapon)node.get( WEAPON );
				armor	    = (Armor)node.get( ARMOR );
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level ) {
			if (!spawned && Dungeon.depth > 1 && Random.Int( 5 - Dungeon.depth ) == 0) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.randomRespawnCell();
				} while (ghost.pos == -1);
				level.mobs.add( ghost );
				Actor.occupyCell( ghost );
				
				spawned = true;
				alternative = Random.Int( 2 ) == 0;
				if (!alternative) {
					left2kill = 8;
				} else {
                    casda = Random.Int(2) == 0;
                }
				
				given = false;
				processed = false;
				depth = Dungeon.depth;
				
				for (int i=0; i < 4; i++) {
					Item another;
					do {
						another = (Weapon)Generator.random( Generator.Category.WEAPON );
					} while (another instanceof MissileWeapon);
					
					if (weapon == null || another.level > weapon.level) {
						weapon = (Weapon)another;
					}
				}
				
				if (Dungeon.isChallenged( Challenges.NO_ARMOR )) {
					armor = (Armor)new ClothArmor().degrade();
				} else {
					armor = (Armor)Generator.random( Generator.Category.ARMOR );
					for (int i=0; i < 3; i++) {
						Item another = Generator.random( Generator.Category.ARMOR );
						if (another.level > armor.level) {
							armor = (Armor)another;
						}
					}
				}
				
				weapon.identify();
				armor.identify();
			}
		}

		public static void process( int pos ) {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				if (alternative) {

					if (casda) {
                        Casda casda = new Casda();
                        casda.pos = Dungeon.level.randomRespawnCell();
                        if (casda.pos != -1) {
                            GameScene.add(casda);
                            processed = true;
                        }
                    } else {
                        FetidRat rat = new FetidRat();
                        rat.pos = Dungeon.level.randomRespawnCell();
                        if (rat.pos != -1) {
                            GameScene.add(rat);
                            processed = true;
                        }
                    }
					
				} else {
					
					if (Random.Int( left2kill ) == 0) {
						Dungeon.level.drop( new DriedRose(), pos ).sprite.drop();
						processed = true;
					} else {
						left2kill--;
					}
					
				}
			}
		}
		
		public static void complete() {
			weapon = null;
			armor = null;
			
			Journal.remove( Journal.Feature.GHOST );
		}
	}

    public static class FetidRat extends Mob {

        {
            name = "fetid rat";
            spriteClass = FetidRatSprite.class;

            HP = HT = 15;
            defenseSkill = 5;

            EXP = 0;
            maxLvl = 5;

            state = WANDERING;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 2, 6 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 12;
        }

        @Override
        public int dr() {
            return 2;
        }

        @Override
        public int defenseProc( Char enemy, int damage ) {

            GameScene.add( Blob.seed( pos, 20, ParalyticGas.class ) );

            return super.defenseProc(enemy, damage);
        }

        @Override
        public void die( Object cause ) {
            super.die( cause );

            Dungeon.level.drop( new RatSkull(), pos ).sprite.drop();
        }

        @Override
        public String description() {
            return
                    "This marsupial rat is much larger, than a regular one. It is surrounded by a foul cloud.";
        }

        private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
        static {
            IMMUNITIES.add( Paralysis.class );
        }

        @Override
        public HashSet<Class<?>> immunities() {
            return IMMUNITIES;
        }
    }

    public static class Casda extends Mob {

        {
            name = "Cas'da the Defiler";
            spriteClass = CasdaSprite.class;

            HP = HT = 20;
            defenseSkill = 5;

            EXP = 0;
            maxLvl = 5;

            state = WANDERING;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 2, 4 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 12;
        }

        @Override
        public int dr() {
            return 2;
        }

        @Override
        public int defenseProc( Char enemy, int damage ) {

            if ( Random.Int( 4 ) == 0 && enemy == Dungeon.hero ) {
                GLog.n("Cas'da waves his hand, and you are suddenly lifted into the air by an unseen force, suspended and powerless.");
                Buff.affect( enemy, Levitation.class, 1f );
                Buff.affect( enemy, Paralysis.class, 1f );
                return 0;
            }
            if ( Random.Int( 5 ) == 0)
                yell("Judge me by my size, do you?");
            return super.defenseProc(enemy, damage);
        }

        @Override
        public int attackProc( Char enemy, int damage ) {
            if (Random.Int( 2 ) == 0) {
                Buff.affect( enemy, Bleeding.class ).set( damage );
            }

            return damage;
        }

        @Override
        public void die( Object cause ) {
            super.die( cause );

            GLog.n( "A disembodied voice, that of Cas'da, thunders out: \"Made me more powerful than you can possibly imagine, you have!\"" );
            Dungeon.level.drop( new Gold( Random.NormalIntRange( 40 , 70 ) ), pos ).sprite.drop();
            Dungeon.level.drop( new BrokenRapier(), pos ).sprite.drop();
        }

        @Override
        public String description() {
            return "This mysterious warrior is known only as \"Cas'da\".";
        }
    }
}
