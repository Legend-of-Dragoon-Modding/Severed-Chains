package legend.game.modding.events.characters;

import legend.game.modding.events.Event;

public class AdditionUnlockEvent extends Event {
  public int additionId;
  public int additionLevel;

  public AdditionUnlockEvent(final int additionId, final int additionLevel) {
    this.additionId = additionId;
    this.additionLevel = additionLevel;
  }
}
