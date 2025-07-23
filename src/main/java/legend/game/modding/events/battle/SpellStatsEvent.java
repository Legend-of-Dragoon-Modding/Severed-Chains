package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.types.SpellStats0c;
import org.legendofdragoon.modloader.events.Event;

public class SpellStatsEvent extends Event {
  public final BattleEntity27c bent;
  public final int spellId;
  public SpellStats0c spell;

  public SpellStatsEvent(final BattleEntity27c bent, final int spellId, final SpellStats0c spell) {
    this.bent = bent;
    this.spellId = spellId;
    this.spell = spell;
  }
}
