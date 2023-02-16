package legend.game.modding.events.characters;

import legend.game.combat.types.BattleStruct18cb0;
import legend.game.modding.events.Event;

public class AdditionHitEvent extends Event {
  public final BattleStruct18cb0.AdditionStruct100 addition;

  public AdditionHitEvent(final BattleStruct18cb0.AdditionStruct100 addition) {
    this.addition = addition;
  }
}
