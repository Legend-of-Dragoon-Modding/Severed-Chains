package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

/**
 * DEPRECATED: subject to removal, use not recommended. Better ways to do this will be introduced in the future.
 */
@Deprecated
public class SpellCombatDescriptionEvent extends BattleEvent {
  public final int spellId;
  public String description;

  public SpellCombatDescriptionEvent(final int spellId, final String description) {
    this.spellId = spellId;
    this.description = description;
  }
}
