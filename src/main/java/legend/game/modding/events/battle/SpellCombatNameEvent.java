package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

/**
 * DEPRECATED: subject to removal, use not recommended. Better ways to do this will be introduced in the future.
 */
@Deprecated
public class SpellCombatNameEvent extends BattleEvent {
  public final int spellId;
  public String name;

  public SpellCombatNameEvent(final int spellId, final String name) {
    this.spellId = spellId;
    this.name = name;
  }
}
