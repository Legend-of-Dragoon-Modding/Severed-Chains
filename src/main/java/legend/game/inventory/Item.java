package legend.game.inventory;

import legend.core.memory.Method;
import legend.game.characters.Element;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.FlowControl;
import legend.game.scripting.ScriptState;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class Item extends RegistryEntry implements InventoryEntry {
  private final ItemIcon icon;
  private final int price;

  public Item(final ItemIcon icon, final int price) {
    this.icon = icon;
    this.price = price;
  }

  @Override
  public ItemIcon getIcon() {
    return this.icon;
  }

  @Override
  public String getNameTranslationKey() {
    return this.getTranslationKey();
  }

  @Override
  public String getDescriptionTranslationKey() {
    return this.getTranslationKey("description");
  }

  public String getBattleDescriptionTranslationKey() {
    return this.getTranslationKey("battle_description");
  }

  @Override
  public int getPrice() {
    return this.price;
  }

  /** Item can't be stolen by enemies */
  public boolean isProtected() {
    return false;
  }

  /** Item is returned after battle */
  public boolean isRepeat() {
    return false;
  }

  /** Check if an item can ever be used in this location */
  public abstract boolean canBeUsed(final UsageLocation location);

  /** Check if an item can be used in this location right now */
  public boolean canBeUsedNow(final UsageLocation location) {
    return this.canBeUsed(location);
  }

  public abstract boolean canTarget(final TargetType type);

  @Method(0x80022d88L)
  public void useInMenu(final UseItemResponse response, final int charId) {
    if(!this.canBeUsed(UsageLocation.MENU)) {
      throw new RuntimeException(this + " cannot be used in menu");
    }

    throw new RuntimeException(this + " usage in menu has yet been implemented");
  }

  public FlowControl useInBattle(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    if(!this.canBeUsed(UsageLocation.BATTLE)) {
      throw new RuntimeException(this + " cannot be used in battle");
    }

    throw new RuntimeException(this + " usage in battle has yet been implemented");
  }

  /** If you implement this, you have to implement {@link #calculateStatMod} */
  public boolean isStatMod() {
    return false;
  }

  public int calculateStatMod(final BattleEntity27c user, final BattleEntity27c target) {
    throw new IllegalStateException(this + " is not a stat mod item");
  }

  public void applyBuffs(final BattleEntity27c user, final BattleEntity27c target) {

  }

  public boolean alwaysHits() {
    return false;
  }

  public int getSpecialEffect(final BattleEntity27c user, final BattleEntity27c target) {
    //TODO should we update the DEFFs to not call these?
    //throw new IllegalStateException(this + " is not an attack item");
    return 0;
  }

  public Element getAttackElement() {
    throw new IllegalStateException(this + " is not an attack item");
  }

  public int getAttackDamageMultiplier(final BattleEntity27c user, final BattleEntity27c target) {
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
