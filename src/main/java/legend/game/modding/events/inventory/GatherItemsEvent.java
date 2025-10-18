package legend.game.modding.events.inventory;

import legend.game.inventory.ItemStack;
import org.legendofdragoon.modloader.events.Event;

import java.util.ArrayList;
import java.util.List;

public class GatherItemsEvent extends Event {
  private final List<ItemStack> stacks = new ArrayList<>();

  public void add(final ItemStack stack) {
    this.stacks.add(stack);
  }

  public ItemStack[] getStacks() {
    return this.stacks.toArray(ItemStack[]::new);
  }
}
