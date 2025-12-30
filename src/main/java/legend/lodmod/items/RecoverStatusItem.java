package legend.lodmod.items;

import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.i18n.I18n;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.inventory.UseItemResponse;
import legend.game.scripting.ScriptState;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.lodmod.LodConfig.ITEM_STACK_SIZE;

public class RecoverStatusItem extends BattleItem {
  private final int status;

  public RecoverStatusItem(final int price, final int status) {
    super(ItemIcon.GREEN_POTION, price);
    this.status = status;
  }

  @Override
  public int getMaxStackSize(final ItemStack stack) {
    return CONFIG.getConfig(ITEM_STACK_SIZE.get());
  }

  @Override
  public boolean canBeUsed(final ItemStack stack, final UsageLocation location) {
    return true;
  }

  @Override
  public boolean canTarget(final ItemStack stack, final TargetType type) {
    return type == TargetType.ALLIES;
  }

  @Override
  public boolean canBeUsedNow(final ItemStack stack, final UsageLocation location) {
    if(location == UsageLocation.MENU) {
      int allStatus = 0;
      for(int i = 0; i < characterCount_8011d7c4; i++) {
        allStatus |= gameState_800babc8.charData_32c[characterIndices_800bdbb8[i]].status_10;
      }

      return (this.status & allStatus) != 0;
    }

    return true;
  }

  @Override
  @Method(0x80022d88L)
  public void useInMenu(final ItemStack stack, final UseItemResponse response, final int charId) {
    final int status = gameState_800babc8.charData_32c[charId].status_10;

    if((this.status & status) == 0) {
      response.failure();
      return;
    }

    gameState_800babc8.charData_32c[charId].status_10 &= ~status;

    final List<String> cured = new ArrayList<>();

    if((status & 0x80) != 0) {
      cured.add(I18n.translate("lod.status.poison.name"));
    }

    if((status & 0x40) != 0) {
      cured.add(I18n.translate("lod.status.dispirit.name"));
    }

    if((status & 0x20) != 0) {
      cured.add(I18n.translate("lod.status.weapon_block.name"));
    }

    if((status & 0x10) != 0) {
      cured.add(I18n.translate("lod.status.stun.name"));
    }

    if((status & 0x8) != 0) {
      cured.add(I18n.translate("lod.status.fear.name"));
    }

    if((status & 0x4) != 0) {
      cured.add(I18n.translate("lod.status.confuse.name"));
    }

    if((status & 0x2) != 0) {
      cured.add(I18n.translate("lod.status.bewitch.name"));
    }

    if((status & 0x1) != 0) {
      cured.add(I18n.translate("lod.status.petrify.name"));
    }

    response.success(I18n.translate(this.getTranslationKey("use"), String.join(", ", cured)));
  }

  @Override
  public boolean isStatMod(final ItemStack stack) {
    return true;
  }

  @Override
  public int calculateStatMod(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {
    return 0;
  }

  @Override
  public boolean alwaysHits(final ItemStack stack) {
    return true;
  }

  /** TODO I dunno if this does anything for status recovery */
  @Override
  public int getSpecialEffect(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {
    int effect = -1;
    if(simpleRand() * 101 >> 16 < 101) {
      if((this.status & 0xff) != 0) {
        int statusIndex;
        for(statusIndex = 0; statusIndex < 8; statusIndex++) {
          if((this.status & (0x80 >> statusIndex)) != 0) {
            break;
          }
        }

        effect = 0x80 >> statusIndex;
      }
    }

    return effect;
  }

  @Override
  protected void loadDeff(final ScriptState<? extends BattleEntity27c> user, final int entrypoint, final int param) {
    // no-op
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.setStor(8, this.status);
    user.setStor(28, targetBentIndex);
    user.setStor(30, user.index);
  }
}
