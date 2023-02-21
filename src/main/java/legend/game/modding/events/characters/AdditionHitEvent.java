package legend.game.modding.events.characters;

import legend.game.combat.types.BattlePreloadedEntities;
import legend.game.modding.events.Event;

public class AdditionHitEvent extends Event {
  public final BattlePreloadedEntities.AdditionHits addition;

  public AdditionHitEvent(final BattlePreloadedEntities.AdditionHits addition) {
    this.addition = addition;
  }
}
