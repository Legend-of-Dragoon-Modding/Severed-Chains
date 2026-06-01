package legend.game.modding.events.battle;

import legend.game.characters.CharacterData2c;
import legend.game.inventory.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;

public class SpellStatsEvent extends Event {
  public final CharacterData2c character;
  public SpellStats0c spell;

  public SpellStatsEvent(final CharacterData2c character, final SpellStats0c spell) {
    this.character = character;
    this.spell = spell;
  }
}
