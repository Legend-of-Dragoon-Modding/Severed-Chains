package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

/**
 * DEPRECATED: subject to removal, use not recommended. Better ways to do this will be introduced in the future.
 */
@Deprecated
public class BattleDescriptionEvent extends BattleEvent {
  public final int textType;
  public final int textIndex;
  public String string;

  public BattleDescriptionEvent(final int textType, final int textIndex, final String string) {
    this.textType = textType;
    this.textIndex = textIndex;
    this.string = string;
  }
}
