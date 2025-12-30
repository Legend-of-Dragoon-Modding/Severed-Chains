package legend.game.inventory;

import legend.core.memory.Method;
import legend.game.characters.Element;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.FlowControl;
import legend.game.scripting.ScriptState;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class Item extends RegistryEntry {
  private final ItemIcon icon;
  private final int price;

  public Item(final ItemIcon icon, final int price) {
    this.icon = icon;
    this.price = price;
  }

  public ItemIcon getIcon(final ItemStack stack) {
    return this.icon;
  }

  public String getNameTranslationKey(final ItemStack stack) {
    return this.getTranslationKey();
  }

  public String getDescriptionTranslationKey(final ItemStack stack) {
    return this.getTranslationKey("description");
  }

  public String getBattleDescriptionTranslationKey(final ItemStack stack) {
    return this.getTranslationKey("battle_description");
  }

  public int getPrice(final ItemStack stack) {
    return this.price;
  }

  /** Item can't be stolen by enemies */
  public boolean isProtected(final ItemStack stack) {
    return false;
  }

  /** Item is returned after battle */
  public boolean isRepeat(final ItemStack stack) {
    return false;
  }

  public int getMaxStackSize(final ItemStack stack) {
    return 1;
  }

  public boolean hasDurability(final ItemStack stack) {
    return false;
  }

  public int getMaxDurability(final ItemStack stack) {
    return 1;
  }

  public boolean isSame(final ItemStack stack) {
    return this == stack.getItem();
  }

  /** Check if an item can ever be used in this location */
  public abstract boolean canBeUsed(final ItemStack stack, final UsageLocation location);

  /** Check if an item can be used in this location right now */
  public boolean canBeUsedNow(final ItemStack stack, final UsageLocation location) {
    return this.canBeUsed(stack, location);
  }

  public abstract boolean canTarget(final ItemStack stack, final TargetType type);

  @Method(0x80022d88L)
  public void useInMenu(final ItemStack stack, final UseItemResponse response, final int charId) {
    if(!this.canBeUsed(stack, UsageLocation.MENU)) {
      throw new RuntimeException(this + " cannot be used in menu");
    }

    throw new RuntimeException(this + " usage in menu has yet been implemented");
  }

  public FlowControl useInBattle(final ItemStack stack, final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    if(!this.canBeUsed(stack, UsageLocation.BATTLE)) {
      throw new RuntimeException(this + " cannot be used in battle");
    }

    throw new RuntimeException(this + " usage in battle has yet been implemented");
  }

  /** If you implement this, you have to implement {@link #calculateStatMod} */
  public boolean isStatMod(final ItemStack stack) {
    return false;
  }

  public int calculateStatMod(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {
    throw new IllegalStateException(this + " is not a stat mod item");
  }

  public void applyBuffs(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {

  }

  public boolean alwaysHits(final ItemStack stack) {
    return false;
  }

  public int getSpecialEffect(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {
    //TODO should we update the DEFFs to not call these?
    //throw new IllegalStateException(this + " is not an attack item");
    return 0;
  }

  public Element getAttackElement(final ItemStack stack) {
    throw new IllegalStateException(this + " is not an attack item");
  }

  public int getAttackDamageMultiplier(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {
    //TODO should we update the DEFFs to not call these?
    //throw new IllegalStateException(this + " is not an attack item");
    return 0;
  }

  public enum UsageLocation {
    MENU,
    BATTLE,
  }

  public enum TargetType {
    ALLIES,
    ENEMIES,
    ALL,
  }
}
