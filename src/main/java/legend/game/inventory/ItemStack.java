package legend.game.inventory;

import legend.game.characters.Element;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.FlowControl;
import legend.game.scripting.ScriptState;
import legend.game.types.Renderable58;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.GameEngine.RENDERER;

public class ItemStack implements InventoryEntry {
  public static final ItemStack EMPTY = new Empty();

  private final Item item;
  private int size;
  private int durability;

  public ItemStack(final Item item, final int size, final int durability) {
    this.item = item;
    this.size = size;
    this.durability = durability >= 0 ? durability : item.getMaxDurability(this);
  }

  public ItemStack(final Item item, final int size) {
    // -1 will get replaced with item's max durability
    this(item, size, -1);
  }

  public ItemStack(final Item item) {
    this(item, 1);
  }

  public ItemStack(final ItemStack other) {
    this(other.item, other.size, other.durability);
  }

  @Override
  public RegistryId getRegistryId() {
    return this.getItem().getRegistryId();
  }

  public Item getItem() {
    if(this.isEmpty()) {
      return CoreMod.NOTHING.get();
    }

    return this.item;
  }

  @Override
  public int getSize() {
    if(this.isEmpty()) {
      return 0;
    }

    return this.size;
  }

  @Override
  public int getMaxSize() {
    if(this.isEmpty()) {
      return 0;
    }

    return this.item.getMaxStackSize(this);
  }

  public int getRemainingCapacity() {
    if(this.isEmpty()) {
      return 0;
    }

    return Math.max(0, this.getMaxSize() - this.getSize());
  }

  public boolean isFull() {
    if(this.isEmpty()) {
      return false;
    }

    return this.getRemainingCapacity() > 0;
  }

  public boolean canStack() {
    return this.getMaxSize() > 1;
  }

  public ItemStack setSize(final int size) {
    if(this.isEmpty()) {
      return EMPTY;
    }

    this.size = Math.clamp(size, 0, this.getMaxSize());
    return this;
  }

  public ItemStack grow(final int amount) {
    return this.setSize(this.getSize() + amount);
  }

  public ItemStack shrink(final int amount) {
    return this.setSize(this.getSize() - amount);
  }

  public ItemStack take(final int amount) {
    final ItemStack newStack = new ItemStack(this).setSize(Math.clamp(amount, 0, this.getSize()));
    this.shrink(newStack.getSize());
    return newStack;
  }

  public boolean hasDurability() {
    if(this.isEmpty()) {
      return false;
    }

    return this.getItem().hasDurability(this);
  }

  public int getMaxDurability() {
    if(this.isEmpty()) {
      return 0;
    }

    if(!this.hasDurability()) {
      return 1;
    }

    return this.getItem().getMaxDurability(this);
  }

  public ItemStack damage(final int amount) {
    if(this.isEmpty()) {
      return EMPTY;
    }

    if(!this.hasDurability()) {
      return this;
    }

    this.durability -= amount;

    while(this.durability < 0) {
      this.durability += this.getMaxDurability();
      this.shrink(1);
    }

    if(this.isEmpty()) {
      return EMPTY;
    }

    return this;
  }

  public int getCurrentDurability() {
    if(this.isEmpty()) {
      return 0;
    }

    if(!this.hasDurability()) {
      return 1;
    }

    return this.durability;
  }

  public float getDurabilityFraction() {
    if(this.isEmpty()) {
      return 0.0f;
    }

    return (float)this.getCurrentDurability() / this.getMaxDurability();
  }

  @Override
  public boolean isEmpty() {
    return this.size < 1 || this.durability < 1;
  }

  public boolean isSameItem(final Item item) {
    return item.isSame(this);
  }

  public boolean isSameItem(final ItemStack other) {
    return other.getItem().isSame(this);
  }

  @Override
  public boolean equals(final Object obj) {
    return obj instanceof final ItemStack other && this.item == other.item && this.size == other.size && this.durability == other.durability;
  }

  /**
   * Merges the other item stack into this one
   *
   * @return The remaining items not merged into this stack, or EMPTY if all items were merged
   */
  public ItemStack merge(final ItemStack other) {
    if(this.isEmpty() || !this.isSameItem(other)) {
      return other;
    }

    final int amountToTransfer = Math.min(this.getRemainingCapacity(), other.getSize());

    if(amountToTransfer == 0) {
      return other;
    }

    this.grow(amountToTransfer);
    this.damage(other.getMaxDurability() - other.getCurrentDurability());

    other.shrink(amountToTransfer);

    if(other.isEmpty()) {
      return EMPTY;
    }

    return other;
  }

  @Override
  public ItemIcon getIcon() {
    return this.getItem().getIcon(this);
  }

  @Override
  public Renderable58 renderIcon(final int x, final int y, final int flags) {
    final Renderable58 icon = InventoryEntry.super.renderIcon(x, y, flags);

    if(this.hasDurability()) {
      final Vector2f a = new Vector2f(x + 9.0f, y + 15.0f);
      final Vector2f b = new Vector2f(a);
      b.y -= this.getDurabilityFraction() * 14.0f;

      RENDERER
        .queueLine(new Matrix4f(), 140.0f, a, b)
        .colour(0.0f, 1.0f, 0.0f)
      ;
    }

    return icon;
  }

  @Override
  public String getNameTranslationKey() {
    return this.getItem().getNameTranslationKey(this);
  }

  @Override
  public String getDescriptionTranslationKey() {
    return this.getItem().getDescriptionTranslationKey(this);
  }

  public String getBattleDescriptionTranslationKey() {
    return this.getItem().getBattleDescriptionTranslationKey(this);
  }

  @Override
  public int getPrice() {
    return this.getItem().getPrice(this);
  }

  /** Item can't be stolen by enemies */
  public boolean isProtected() {
    return this.getItem().isProtected(this);
  }

  /** Item is returned after battle */
  public boolean isRepeat() {
    return this.getItem().isRepeat(this);
  }

  /** Check if an item can ever be used in this location */
  public boolean canBeUsed(final Item.UsageLocation location) {
    return this.getItem().canBeUsed(this, location);
  }

  /** Check if an item can be used in this location right now */
  public boolean canBeUsedNow(final Item.UsageLocation location) {
    return this.getItem().canBeUsedNow(this, location);
  }

  public boolean canTarget(final Item.TargetType type) {
    return this.getItem().canTarget(this, type);
  }

  public void useInMenu(final UseItemResponse response, final int charId) {
    this.getItem().useInMenu(this, response, charId);
  }

  public FlowControl useInBattle(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    return this.getItem().useInBattle(this, user, targetBentIndex);
  }

  /** If you implement this, you have to implement {@link #calculateStatMod} */
  public boolean isStatMod() {
    return this.getItem().isStatMod(this);
  }

  public int calculateStatMod(final BattleEntity27c user, final BattleEntity27c target) {
    return this.getItem().calculateStatMod(this, user, target);
  }

  public void applyBuffs(final BattleEntity27c user, final BattleEntity27c target) {
    this.getItem().applyBuffs(this, user, target);
  }

  public boolean alwaysHits() {
    return this.getItem().alwaysHits(this);
  }

  public int getSpecialEffect(final BattleEntity27c user, final BattleEntity27c target) {
    return this.getItem().getSpecialEffect(this, user, target);
  }

  public Element getAttackElement() {
    return this.getItem().getAttackElement(this);
  }

  public int getAttackDamageMultiplier(final BattleEntity27c user, final BattleEntity27c target) {
    return this.getItem().getAttackDamageMultiplier(this, user, target);
  }

  @Override
  public String toString() {
    if(this.isEmpty()) {
      return "EMPTY";
    }

    String out = this.getSize() + "x " + this.getItem();

    if(this.hasDurability()) {
      out += " (" + this.getCurrentDurability() + '/' + this.getMaxDurability() + ')';
    }

    return out;
  }

  private static final class Empty extends ItemStack {
    public Empty() {
      super(CoreMod.NOTHING.get());
    }

    @Override
    public boolean isEmpty() {
      return true;
    }
  }
}
