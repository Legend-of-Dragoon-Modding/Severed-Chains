package legend.game.modding.events.inventory;

import legend.game.inventory.Item;
import legend.game.inventory.ItemStack;
import legend.game.inventory.OverflowMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static legend.core.GameEngine.EVENTS;

public class Inventory implements Iterable<ItemStack> {
  private final List<ItemStack> stacks = new ArrayList<>();
  private int maxSize;
  private boolean enableEvents = true;

  public Inventory() {

  }

  public Inventory(final Inventory other) {
    this.maxSize = other.maxSize;

    for(int i = 0; i < other.stacks.size(); i++) {
      this.stacks.add(new ItemStack(other.stacks.get(i)));
    }
  }

  public void enableEvents() {
    this.enableEvents = true;
  }

  public void disableEvents() {
    this.enableEvents = false;
  }

  public void setMaxSize(final int maxSize) {
    this.maxSize = maxSize;
  }

  public int getMaxSize() {
    return this.maxSize;
  }

  public int getSize() {
    return this.stacks.size();
  }

  public boolean isEmpty() {
    return this.getSize() == 0;
  }

  public boolean isEmpty(final Item.UsageLocation usageLocation) {
    return this.stacks.stream().noneMatch(stack -> stack.canBeUsed(usageLocation));
  }

  public void clear() {
    this.stacks.clear();
  }

  public ItemStack get(final int slot) {
    return this.stacks.get(slot);
  }

  public ItemStack set(final int slot, final Item stack) {
    return this.set(slot, new ItemStack(stack));
  }

  public ItemStack set(final int slot, final ItemStack stack) {
    return this.stacks.set(slot, stack);
  }

  /**
   * Adds the item to this inventory, merging it into existing item stacks if they have room
   *
   * @return A stack containing the item if it couldn't fit into the inventory, or EMPTY if the item was added
   */
  public ItemStack give(final Item item) {
    return this.give(new ItemStack(item));
  }

  /**
   * Adds the item stack to this inventory, merging it into existing item stacks if they have room
   *
   * @return Any portion of the stack that couldn't fit into the inventory, or EMPTY if the whole stack was added
   */
  public ItemStack give(final ItemStack stack) {
    if(!this.enableEvents) {
      return this.giveImpl(stack, false);
    }

    final GiveItemEvent event = EVENTS.postEvent(new GiveItemEvent(this, stack));

    if(event.isCanceled() || event.givenItems.isEmpty()) {
      return stack;
    }

    final Inventory temp = new Inventory(this);
    temp.disableEvents();

    boolean overflowed = false;
    for(int i = 0; i < event.givenItems.size(); i++) {
      if(!temp.give(new ItemStack(event.givenItems.get(i))).isEmpty()) {
        overflowed = true;
        break;
      }
    }

    if(event.overflowMode == OverflowMode.FAIL && overflowed) {
      return stack;
    }

    for(int i = 0; i < event.givenItems.size(); i++) {
      this.giveImpl(event.givenItems.get(i), event.overflowMode == OverflowMode.OVERFLOW);
    }

    return ItemStack.EMPTY;
  }

  /**
   * Adds the item stack to this inventory, merging it into existing item stacks if they have room
   *
   * @param force If true, items will be added even if they don't fit in the max size of the inventory
   *
   * @return Any portion of the stack that couldn't fit into the inventory, or EMPTY if the whole stack was added
   */
  private ItemStack giveImpl(final ItemStack stack, final boolean force) {
    ItemStack remaining = stack;

    // Try to merge the stack into our current stacks
    for(int i = 0; i < this.stacks.size(); i++) {
      final ItemStack current = this.stacks.get(i);

      if(current.isEmpty()) {
        continue;
      }

      remaining = current.merge(remaining);

      if(remaining.isEmpty()) {
        return ItemStack.EMPTY;
      }
    }

    // Try to insert the item into an empty slot
    for(int i = 0; i < this.stacks.size(); i++) {
      if(this.stacks.get(i).isEmpty()) {
        this.stacks.set(i, stack);
        return ItemStack.EMPTY;
      }
    }

    // Try to expand the item list to fit the new item
    if(force || this.maxSize == 0 || this.stacks.size() < this.maxSize) {
      this.stacks.add(stack);
      return ItemStack.EMPTY;
    }

    // We couldn't fit the whole stack - return what we didn't add
    return remaining;
  }

  /**
   * Removes the item from the inventory
   *
   * @return A stack containing the item if it couldn't be removed, or EMPTY if it was removed
   */
  public ItemStack take(final Item item) {
    return this.take(new ItemStack(item));
  }

  /**
   * Removes one or more items from the inventory
   *
   * @return Any portion of the stack that couldn't be removed, or EMPTY if all items were removed
   */
  public ItemStack take(ItemStack stack) {
    if(this.enableEvents) {
      final TakeItemEvent event = EVENTS.postEvent(new TakeItemEvent(this, stack));

      if(event.isCanceled() || event.stack.isEmpty()) {
        return stack;
      }

      stack = event.stack;
    }

    for(int i = 0; i < this.stacks.size(); i++) {
      final ItemStack invStack = this.stacks.get(i);

      if(invStack.isSameItem(stack)) {
        final int numberToRemove = Math.min(invStack.getSize(), stack.getSize());
        invStack.shrink(numberToRemove);
        stack.shrink(numberToRemove);

        if(invStack.isEmpty()) {
          // Stack is empty, remove it and backtrack one element since everything will get shifted up by one
          this.stacks.remove(i);
          i--;
        }

        if(stack.isEmpty()) {
          // We removed everything, return EMPTY
          return ItemStack.EMPTY;
        }
      }
    }

    // Return the amount that didn't get removed from the inventory
    return stack;
  }

  /**
   * Attempts to take some number of items from the given slot. The
   * {@link TakeItemEvent} may modify what is taken from the inventory.
   * If the {@link TakeItemEvent} changes the {@link Item} to be taken,
   * it will be taken from anywhere in the inventory as if calling
   * {@link #take} instead.
   * <p>
   * If amount is greater than the number of items in the stack, the
   * rest will be taken from other stacks in the inventory.
   *
   * @param slot The slot number of the stack to take items from
   * @param amount The number of items to take from the stack
   *
   * @return EMPTY if all items were taken, or a stack containing the
   *         number of items that couldn't be taken if take failed
   */
  public ItemStack takeFromSlot(final int slot, final int amount) {
    final ItemStack stack = this.stacks.get(slot);
    ItemStack toTake = new ItemStack(stack).setSize(Math.clamp(amount, 0, stack.getSize()));

    if(this.enableEvents) {
      final TakeItemEvent event = EVENTS.postEvent(new TakeItemEvent(this, toTake));

      if(event.isCanceled() || event.stack.isEmpty()) {
        return new ItemStack(stack).setSize(amount);
      }

      if(!event.stack.isSameItem(toTake)) {
        // Event changed the item, use the regular take method to take from
        // wherever we can since it no longer matches the item in this slot
        return this.take(event.stack);
      }

      toTake = event.stack;
    }

    // Reduce the size of the stack by the given amount
    final int originalSize = stack.getSize();
    stack.shrink(toTake.getSize());

    // Check to see if they tried to reduce the stack size by more than what was in there
    final int remaining = toTake.getSize() - originalSize;
    if(remaining > 0) {
      // The selected slot didn't have enough items in it, try to pull the rest from elsewhere in the inventory
      return this.take(new ItemStack(toTake.getItem(), remaining));
    }

    // Remove the stack from the inventory if it's empty
    if(stack.isEmpty()) {
      this.stacks.remove(slot);
    }

    // We took all items, nothing to see here
    return ItemStack.EMPTY;
  }

  /**
   * Removes the stack from the slot if it's empty
   */
  public void removeIfEmpty(final int slot) {
    if(this.stacks.get(slot).isEmpty()) {
      this.stacks.remove(slot);
    }
  }

  /**
   * Removes the stack from the slot if it's empty
   */
  public void removeIfEmpty(final ItemStack stack) {
    if(stack.isEmpty()) {
      this.stacks.remove(stack);
    }
  }

  /**
   * Check if there's room to fit the entire stack in this inventory
   */
  public boolean hasRoom(final ItemStack stack) {
    if(stack.canStack()) {
      // Check if we can merge into other stacks
      int remaining = stack.getSize();

      for(int i = 0; i < this.stacks.size(); i++) {
        final ItemStack s = this.stacks.get(i);

        if(s.isSameItem(stack)) {
          remaining -= s.getRemainingCapacity();

          if(remaining <= 0) {
            // Other stacks of this item had enough room
            return true;
          }
        }
      }
    }

    // Return true if there's room for a new stack
    return this.maxSize == 0 || this.stacks.size() < this.maxSize;
  }

  @Override
  public @NotNull Iterator<ItemStack> iterator() {
    return this.stacks.iterator();
  }
}
