package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;

public class ShieldItem extends BuffItem {
  public ShieldItem(final int useItemEntrypoint, final boolean physicalImmunity, final boolean magicalImmunity) {
    super(useItemEntrypoint, 42, 200, TargetType.ALLIES, 0, 0, 0, 0, 0, 0, 0, 0, physicalImmunity, magicalImmunity, 0, 0, 0, 0, 0, 0);
  }

  @Override
  protected void loadDeff(final ScriptState<? extends BattleEntity27c> user, final int entrypoint, final int param) {
    // no-op
  }
}
