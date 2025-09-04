package legend.game.modding.events.inventory;

import org.legendofdragoon.modloader.events.Event;

public class AddGoldEvent extends Event {
  public int gold;

  public AddGoldEvent(final int gold) {
    this.gold = gold;
  }
}
