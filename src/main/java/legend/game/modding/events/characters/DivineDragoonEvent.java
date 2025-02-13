package legend.game.modding.events.characters;

import org.legendofdragoon.modloader.events.Event;

/**
 * Fired before the game auto levels Dart's dragoon level and exp when acquiring the Divine Dragoon Spirit.
 */
public class DivineDragoonEvent extends Event {
  /** Setting this to true will prevent the base game from auto leveling Dart's Dragoon EXP and Level to a set amount. */
  public boolean bypassOverride;
}
