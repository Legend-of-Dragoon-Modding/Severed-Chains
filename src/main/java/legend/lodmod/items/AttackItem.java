package legend.lodmod.items;

import legend.game.characters.Element;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;

public class AttackItem extends BattleItem {
  private final boolean targetAll;
  private final Element element;
  private final int damageMultiplier;

  public AttackItem(final ItemIcon icon, final int price, final boolean targetAll, final Element element, final int damageMultiplier) {
    super(icon, price);
    this.targetAll = targetAll;
    this.element = element;
    this.damageMultiplier = damageMultiplier;
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return type == TargetType.ENEMIES || type == TargetType.ALL && this.targetAll;
  }

  @Override
  public Element getAttackElement() {
    return this.element;
  }

  @Override
  public int getAttackDamageMultiplier(final BattleEntity27c user, final BattleEntity27c target) {
    return this.damageMultiplier;
  }

  @Override
  public int getSpecialEffect(final BattleEntity27c user, final BattleEntity27c target) {
    return -1;
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 1;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
