package legend.game.combat.ui;

import legend.core.Config;
import legend.core.memory.Method;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.i18n.I18n;
import legend.game.inventory.Item;
import legend.game.inventory.ItemStack;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.events.inventory.DescriptionEvent;
import legend.game.modding.events.inventory.RepeatItemReturnEvent;
import legend.game.scripting.RunningScript;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.EVENTS;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class ItemListMenu extends ListMenu {
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE);

  private UiBox description;

  private final List<CombatItem02> combatItems_800c6988 = new ArrayList<>();

  public ItemListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final ListPosition lastPosition, final Runnable onClose) {
    super(hud, activePlayer, 186, lastPosition, onClose);
    this.cacheItems();

    this.flags_02 |= 0x20;
  }

  @Override
  protected int getListCount() {
    return this.combatItems_800c6988.size();
  }

  @Override
  protected void drawListEntry(final int index, final int x, final int y, final int trim) {
    this.fontOptions.trim(trim);
    this.fontOptions.horizontalAlign(HorizontalAlign.LEFT);
    renderText(I18n.translate(this.combatItems_800c6988.get(index).getStack().getItem()), x, y, this.fontOptions);
    renderText("×", x + 143, y, this.fontOptions);

    this.fontOptions.horizontalAlign(HorizontalAlign.RIGHT);
    renderText(String.valueOf(this.combatItems_800c6988.get(index).getSize()), x + 168, y, this.fontOptions);
  }

  @Override
  protected void onSelection(final int index) {
    //LAB_800f50b8
    this.player_08.setActiveItem(this.combatItems_800c6988.get(index).getStack());
  }

  @Override
  protected void onUse(final int index) {
    final ItemStack original = this.player_08.item_d4;
    this.player_08.item_d4 = this.player_08.item_d4.take(1);
    gameState_800babc8.items_2e9.removeIfEmpty(original);

    final ItemStack stack = this.player_08.item_d4;
    final RepeatItemReturnEvent repeatItemReturnEvent = EVENTS.postEvent(new RepeatItemReturnEvent(stack, stack.isRepeat()));

    if(repeatItemReturnEvent.returnItem) {
      this.hud.battle.usedRepeatItems_800c6c3c.add(stack);
    }
  }

  @Override
  protected void onClose() {

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

    script.params_20[1].set(this.hud.battleMenu_800c6c34.target_48);
    script.params_20[2].set(-1); // Used to be item ID
    script.params_20[3].set(this.player_08.item_d4.getItem().getRegistryId());

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
    for(int itemSlot = 0; itemSlot < gameState_800babc8.items_2e9.getSize(); itemSlot++) {
      final ItemStack stack = gameState_800babc8.items_2e9.get(itemSlot);

      if(!stack.canBeUsedNow(Item.UsageLocation.BATTLE)) {
        continue;
      }

      boolean found = false;

      //LAB_800f843c
      for(final CombatItem02 combatItem : this.combatItems_800c6988) {
        if(combatItem.is(stack)) {
          combatItem.addStack(stack);
          found = true;
          break;
        }
      }

      if(!found) {
        this.combatItems_800c6988.add(new CombatItem02(stack));
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

        this.fontOptions.trim(0);
        this.fontOptions.horizontalAlign(HorizontalAlign.CENTRE);
        renderText(EVENTS.postEvent(new DescriptionEvent(this.combatItems_800c6988.get(this.listScroll_1e + this.listIndex_24).getStack().getBattleDescriptionTranslationKey(), I18n.translate(this.combatItems_800c6988.get(this.listScroll_1e + this.listIndex_24).getStack().getBattleDescriptionTranslationKey()))).description, 160, 157, this.fontOptions);
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
