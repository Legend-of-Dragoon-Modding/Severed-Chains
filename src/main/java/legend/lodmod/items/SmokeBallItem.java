package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;

public class SmokeBallItem extends BattleItem {
  public SmokeBallItem() {
    super(ItemIcon.SACK, 200);
  }

  @Override
  public boolean isRepeat() {
    return true;
  }

  @Override
  public boolean isProtected() {
    return true;
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return false;
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 9;
  }

  @Override
  protected void loadDeff(final ScriptState<? extends BattleEntity27c> user, final int entrypoint, final int param) {
    // no-op
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
