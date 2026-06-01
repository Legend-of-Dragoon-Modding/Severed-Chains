package legend.game.combat.ui;

import legend.game.inventory.Item;
import legend.game.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CombatItem02 {
  private final Item item;
  private final List<ItemStack> stacks = new ArrayList<>();
  private int size;

  public CombatItem02(final ItemStack firstStack) {
    this.item = firstStack.getItem();
    this.addStack(firstStack);
  }

  public boolean is(final ItemStack stack) {
    return this.item.isSame(stack);
  }

  public void addStack(final ItemStack stack) {
    this.stacks.add(stack);
    this.size += stack.getSize();
  }

  public ItemStack getStack() {
    if(this.stacks.isEmpty()) {
      return ItemStack.EMPTY;
    }

    return this.stacks.getLast();
  }

  public int getSize() {
    return this.size;
  }
}
