package legend.lodmod.items;

import legend.game.SMap;
import legend.game.inventory.Item;
import legend.game.inventory.UseItemResponse;
import legend.game.wmap.WMap;

import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_800b.hasNoEncounters_800bed58;

public class CharmPotionItem extends Item {
  public CharmPotionItem(final String name, final String description, final String combatDescription, final int price) {
    super(name, description, combatDescription, 45, price);
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
  public void useItemInMenu(final UseItemResponse response, final int charIndex) {
    if(currentEngineState_8004dd04 instanceof final WMap wmap) {
      //LAB_80022e40
      response._00 = 8;
      wmap.encounterAccumulator_800c6ae8 = 0;
    } else if(currentEngineState_8004dd04 instanceof final SMap smap && hasNoEncounters_800bed58.get() == 0) {
      response._00 = 8;
      smap.encounterAccumulator_800c6ae8 = 0;
    } else {
      //LAB_80022e50
      response._00 = 9;
    }

    response.value_04 = 0;
  }
}
