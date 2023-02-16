package legend.game.modding.events.characters;

import legend.game.modding.events.Event;

public class AdditionHitMultiplierEvent extends Event {
  public int additionId;
  public int additionLevel;
  public int additionSpMulti;
  public int additionDmgMulti;

  public AdditionHitMultiplierEvent(final int additionId, final int additionLevel, final int spMulti, final int dmgMulti) {
    this.additionId = additionId;
    this.additionLevel = additionLevel;
    this.additionSpMulti = spMulti;
    this.additionDmgMulti = dmgMulti;
  }
}
