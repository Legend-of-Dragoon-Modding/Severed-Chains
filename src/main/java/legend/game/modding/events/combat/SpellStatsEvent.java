package legend.game.modding.events.combat;

import legend.game.modding.events.Event;
import legend.game.types.SpellStats0c;

public class SpellStatsEvent extends Event {
  public final int spellId;
  public SpellStats0c spell;

  public SpellStatsEvent(final int spellId, final SpellStats0c spell) {
    this.spellId = spellId;
    this.spell = spell;
  }
}
