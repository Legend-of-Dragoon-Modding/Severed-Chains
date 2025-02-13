package legend.game.modding.events.characters;

import org.legendofdragoon.modloader.events.Event;

/**
 * Fired any time a goods is checked for Divine Dragon Spirit or initially getting Divine Dragoon
 */
public class DivineDragoonEvent extends Event {
  /** Setting this to true will prevent the base game from auto leveling Dart's Dragoon EXP and Level to a set amount. */
  public boolean bypassOverride;
}
