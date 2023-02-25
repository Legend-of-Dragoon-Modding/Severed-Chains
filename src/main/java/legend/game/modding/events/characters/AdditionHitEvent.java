package legend.game.modding.events.characters;

import legend.game.combat.types.BattlePreloadedEntities_18cb0;
import legend.game.modding.events.Event;

public class AdditionHitEvent extends Event {
  public final BattlePreloadedEntities_18cb0.AdditionHits_100 addition;

  public AdditionHitEvent(final BattlePreloadedEntities_18cb0.AdditionHits_100 addition) {
    this.addition = addition;
  }
}
