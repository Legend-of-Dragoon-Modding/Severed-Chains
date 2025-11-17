package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.types.Model124;

public class CombatantModelLoadedEvent extends BattleEvent {
  public final CombatantStruct1a8 combatant;
  public final Model124 model;

  public CombatantModelLoadedEvent(final CombatantStruct1a8 combatant, final Model124 model) {
    this.combatant = combatant;
    this.model = model;
  }
}
