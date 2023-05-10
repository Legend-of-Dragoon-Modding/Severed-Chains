package legend.game.modding.events.characters;

import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.modding.events.Event;

public class BattleMapActiveAdditionHitPropertiesEvent extends Event {
  public final BattlePreloadedEntities_18cb0.AdditionHits_100 additionHits;
  public final int additionIndex;
  public final int charIndex;
  public final int charSlot;
  public final boolean dragoon;

  public BattleMapActiveAdditionHitPropertiesEvent(final BattlePreloadedEntities_18cb0.AdditionHits_100 additionHits, final int additionIndex, final int charIndex, final int charSlot, final boolean dragoon) {
    this.additionHits = additionHits;
    this.additionIndex = additionIndex;
    this.charIndex = charIndex;
    this.charSlot = charSlot;
    this.dragoon = dragoon;
  }
}
