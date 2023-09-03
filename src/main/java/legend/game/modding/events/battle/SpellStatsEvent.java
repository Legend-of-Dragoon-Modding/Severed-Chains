package legend.game.modding.events.battle;

import legend.game.types.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;

public class SpellStatsEvent extends Event {
  public final int spellId;
  public SpellStats0c spell;

  public SpellStatsEvent(final int spellId, final SpellStats0c spell) {
    this.spellId = spellId;
    this.spell = spell;
  }
}
