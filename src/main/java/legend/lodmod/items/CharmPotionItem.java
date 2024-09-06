package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.UseItemResponse;
import legend.game.scripting.ScriptState;
import legend.game.submap.SMap;
import legend.game.wmap.WMap;

import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;

public class CharmPotionItem extends BattleItem {
  public CharmPotionItem() {
    super(45, 2);
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return true;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return type == TargetType.ALLIES;
  }

  @Override
  public void useInMenu(final UseItemResponse response, final int charId) {
    if(currentEngineState_8004dd04 instanceof final WMap wmap) {
      //LAB_80022e40
      response._00 = 8;
      wmap.encounterAccumulator_800c6ae8 = 0;
    } else if(currentEngineState_8004dd04 instanceof final SMap smap && smap.submap.hasEncounters()) {
      response._00 = 8;
      smap.encounterAccumulator_800c6ae8 = 0;
    } else {
      //LAB_80022e50
      response._00 = 9;
    }

    response.value_04 = 0;
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 2;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[8] = 0xffffff; // Colour
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
