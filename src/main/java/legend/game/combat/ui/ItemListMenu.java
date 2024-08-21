package legend.game.combat.ui;

import legend.core.Config;
import legend.core.memory.Method;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.Item;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.events.inventory.RepeatItemReturnEvent;
import legend.game.scripting.RunningScript;
import legend.lodmod.LodMod;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.EVENTS;
import static legend.game.Scus94491BpeSegment_8002.renderCentredText;
import static legend.game.Scus94491BpeSegment_8002.renderRightText;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class ItemListMenu extends ListMenu {
  private UiBox description;

  private final List<CombatItem02> combatItems_800c6988 = new ArrayList<>();

  public ItemListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final Runnable onClose) {
    super(hud, activePlayer, 186, onClose);
    this.cacheItems();

    this.flags_02 |= 0x20;
    this.listIndex_24 = this.lastListIndex_26;
    this.listScroll_1e = this.lastListScroll_28;
    this.offsetFromStartOfListY_94 = this.lastOffsetFromStartOfListY_2c;

    if(this.combatItems_800c6988.size() - 1 < this.listIndex_24 + this.listScroll_1e) {
      this.listIndex_24--;

      if(this.listIndex_24 < 0) {
        this.listIndex_24 = 0;
        this.listScroll_1e = 0;
        this.offsetFromStartOfListY_94 = 0;
      }
    }

    this.listOffsetY_20 = (short)(this.y_06 - this.height_12 - 16 - this.offsetFromStartOfListY_94);
  }

  @Override
  protected int getListCount() {
    return this.combatItems_800c6988.size();
  }

  @Override
  protected void drawListEntry(final int index, final int x, final int y, final int trim) {
    renderText(this.combatItems_800c6988.get(index).item.getName(), x, y, TextColour.WHITE, trim);
    renderText("\u011d", x + 143, y, TextColour.WHITE, trim);
    renderRightText(String.valueOf(this.combatItems_800c6988.get(index).count), x + 168, y, TextColour.WHITE, trim);
  }

  @Override
  protected void onSelection(final int index) {
    //LAB_800f50b8
    this.player_08.setActiveItem(this.combatItems_800c6988.get(index).item);
    this.offsetFromStartOfListY_94 = this.listStartY_1a - this.listOffsetY_20;
  }

  @Override
  protected void onUse(final int index) {
    final Item item = this.player_08.item_d4;
    takeItemId(item);

    final RepeatItemReturnEvent repeatItemReturnEvent = EVENTS.postEvent(new RepeatItemReturnEvent(item, item.isRepeat()));

    if(repeatItemReturnEvent.returnItem) {
      this.hud.battle.usedRepeatItems_800c6c3c.add(item);
    }
  }

  @Override
  protected void onClose() {
    final int offset = this.listStartY_1a - this.listOffsetY_20;
    this.lastListIndex_26 = this.listIndex_24;
    this.lastListScroll_28 = this.listScroll_1e;
    this.lastListOffsetY_2a = this.listOffsetY_20;
    this.offsetFromStartOfListY_94 = offset;
    this.lastOffsetFromStartOfListY_2c = offset;
  }

  @Override
  protected int handleTargeting() {
    //TODO
    return this.hud.handleTargeting(this.player_08.item_d4.canTarget(Item.TargetType.ALLIES) ? 0 : 1, this.player_08.item_d4.canTarget(Item.TargetType.ALL));
  }

  @Override
  public void getTargetingInfo(final RunningScript<?> script) {
    script.params_20[0].set(this.selectionState_a0);

    if(this.selectionState_a0 < 1) {
      return;
    }

    // > 64 will make the script do nothing
    final int retailId = LodMod.idItemMap.getOrDefault(this.player_08.item_d4.getRegistryId(), 65); //TODO

    script.params_20[1].set(this.hud.battleMenu_800c6c34.target_48);
    script.params_20[2].set(retailId);
    script.params_20[3].set(this.player_08.item_d4.getRegistryId());

    // If it's a target all item, -1 the target
    if(this.selectionState_a0 == 1 && this.player_08.item_d4.canTarget(Item.TargetType.ALL)) {
      script.params_20[1].set(-1);
    }
  }

  @Method(0x800f83c8L)
  private void cacheItems() {
    //LAB_800f83dc
    this.combatItems_800c6988.clear();

    //LAB_800f8420
    for(int itemSlot = 0; itemSlot < gameState_800babc8.items_2e9.size(); itemSlot++) {
      final Item item = gameState_800babc8.items_2e9.get(itemSlot);

      boolean found = false;

      //LAB_800f843c
      for(final CombatItem02 combatItem : this.combatItems_800c6988) {
        if(combatItem.item == item) {
          found = true;
          combatItem.count++;
          break;
        }
      }

      if(!found) {
        this.combatItems_800c6988.add(new CombatItem02(item));
      }
    }
  }

  @Override
  public void draw() {
    super.draw();

    if(this.menuState_00 != 0 && (this.flags_02 & 0x1) != 0) {
      //LAB_800f5f50
      if((this.flags_02 & 0x40) != 0) {
        //Selected item description
        if(this.description == null) {
          this.description = new UiBox("Battle UI Item Description", 44, 156, 232, 14);
        }

        this.description.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
        renderCentredText(this.combatItems_800c6988.get(this.listScroll_1e + this.listIndex_24).item.getCombatDescription(), 160, 157, TextColour.WHITE, 0);
      }
    }
  }

  @Override
  public void delete() {
    super.delete();

    if(this.description != null) {
      this.description.delete();
      this.description = null;
    }
  }
}
