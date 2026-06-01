package legend.game.modding.events.inventory;

import legend.game.inventory.ItemStack;
import org.legendofdragoon.modloader.events.Event;

public class RepeatItemReturnEvent extends Event {
  public final ItemStack stack;
  public boolean returnItem;

  public RepeatItemReturnEvent(final ItemStack stack, final boolean returnItem) {
    this.stack = stack;
    this.returnItem = returnItem;
  }
}
