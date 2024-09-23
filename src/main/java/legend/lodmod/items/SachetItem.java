package legend.lodmod.items;

import legend.game.characters.Element;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;
import legend.lodmod.LodMod;

public class SachetItem extends BattleItem {
  public SachetItem() {
    super(46, 200);
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return type == TargetType.ENEMIES;
  }

  @Override
  public Element getAttackElement() {
    return LodMod.NO_ELEMENT.get();
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
