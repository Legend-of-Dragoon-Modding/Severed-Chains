package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.ItemStack;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.ItemList;
import legend.game.inventory.screens.controls.Label;
import legend.game.modding.coremod.CoreMod;
import legend.game.inventory.Inventory;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MessageBoxResult;
import legend.lodmod.LodMod;

import javax.annotation.Nullable;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Audio.playMenuSound;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.SItem.loadItemsAndEquipmentForDisplay;
import static legend.game.SItem.menuEquipmentSlotComparator;
import static legend.game.SItem.menuItemIconComparator;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.setInventoryFromDisplay;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DELETE;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_SORT;

public class ItemListScreen extends MenuScreen {
  private final Runnable unload;

  private final ItemList<ItemStack> itemList = new ItemList<>(stack -> stack.item_00.canStack() || stack.item_00.getSize() > 1 ? stack.item_00.getSize() : 0, null);
  private final ItemList<Equipment> equipmentList = new ItemList<>(null, i -> gameState_800babc8.equipment_1e8.size());
  private final Label description = new Label("");

  public ItemListScreen(final Runnable unload) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.unload = unload;

    this.itemList.setPos(8, 15);
    this.itemList.setTitle("Items");
    this.itemList.setMax(CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get()));

    this.equipmentList.setPos(188, 15);
    this.equipmentList.setTitle("Equipment");
    this.equipmentList.setMax(255);

    this.itemList.onHoverIn(() -> this.setFocus(this.itemList));
    this.itemList.onGotFocus(() -> {
      this.itemList.showHighlight();
      this.equipmentList.hideHighlight();
      this.updateDescription(this.itemList.getSelectedItem());
    });
    this.itemList.onInputActionPressed((action, repeat) -> {
      if(action == INPUT_ACTION_MENU_RIGHT.get()) {
        playMenuSound(1);
        this.setFocus(this.equipmentList);
        this.equipmentList.select(this.itemList.getSelectedIndex());
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });
    this.itemList.onHighlight(this::updateDescription);

    this.equipmentList.onHoverIn(() -> this.setFocus(this.equipmentList));
    this.equipmentList.onGotFocus(() -> {
      this.itemList.hideHighlight();
      this.equipmentList.showHighlight();
      this.updateDescription(this.equipmentList.getSelectedItem());
    });
    this.equipmentList.onInputActionPressed((action, repeat) -> {
      if(action == INPUT_ACTION_MENU_LEFT.get()) {
        playMenuSound(1);
        this.setFocus(this.itemList);
        this.itemList.select(this.equipmentList.getSelectedIndex());
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });
    this.equipmentList.onHighlight(this::updateDescription);

    this.addControl(new Background());
    this.addControl(Glyph.glyph(83)).setPos( 16, 164); // Button prompt pane
    this.addControl(Glyph.glyph(91)).setPos(194, 173); // Description pane

    this.addHotkey(I18n.translate("lod_core.ui.item_list.sort"), INPUT_ACTION_MENU_SORT, this::menuSort);
    this.addHotkey(I18n.translate("lod_core.ui.item_list.discard"), INPUT_ACTION_MENU_DELETE, this::menuDiscard);
    this.addHotkey(I18n.translate("lod_core.ui.item_list.back"), INPUT_ACTION_MENU_BACK, this::menuEscape);

    this.description.setPos(14, 168);

    this.addControl(this.itemList);
    this.addControl(this.equipmentList);
    this.addControl(this.description);

    this.setFocus(this.itemList);

    final MenuEntries<ItemStack> items = new MenuEntries<>();
    final MenuEntries<Equipment> equipment = new MenuEntries<>();
    loadItemsAndEquipmentForDisplay(equipment, items, 0);

    for(final MenuEntryStruct04<ItemStack> item : items) {
      this.itemList.add(item);
    }

    for(final MenuEntryStruct04<Equipment> item : equipment) {
      this.equipmentList.add(item);
    }

    this.updateDescription(this.itemList.getSelectedItem());
  }

  private void updateDescription(@Nullable final MenuEntryStruct04<?> item) {
    if(item == null) {
      this.description.setText("");
      return;
    }

    this.description.setText(I18n.translate(item.getDescriptionTranslationKey()));
  }

  @Override
  protected void render() {

  }

  private <T extends InventoryEntry<?>> void showDiscardMenu(final ItemList<T> list, final List<T> inv) {
    if(((list.getSelectedItem().flags_02 & 0x2000) != 0)) {
      playMenuSound(40);
    } else {
      playMenuSound(2);
      menuStack.pushScreen(new MessageBoxScreen("Discard?", 2, result -> this.discard(result, list, inv)));
    }
  }

  private void showDiscardMenu(final ItemList<ItemStack> list, final Inventory inv) {
    if(((list.getSelectedItem().flags_02 & 0x2000) != 0)) {
      playMenuSound(40);
    } else {
      playMenuSound(2);
      menuStack.pushScreen(new MessageBoxScreen("Discard?", 2, result -> this.discard(result, list, inv)));
    }
  }

  private <T extends InventoryEntry<?>> void discard(final MessageBoxResult result, final ItemList<T> list, final List<T> inv) {
    if(result == MessageBoxResult.YES) {
      list.remove(list.getSelectedItem());
      final List<MenuEntryStruct04<T>> items = list.getItems();
      setInventoryFromDisplay(items, inv, items.size());
      this.updateDescription(list.getSelectedItem());
    }
  }

  private void discard(final MessageBoxResult result, final ItemList<ItemStack> list, final Inventory inv) {
    if(result == MessageBoxResult.YES) {
      list.remove(list.getSelectedItem());
      final List<MenuEntryStruct04<ItemStack>> items = list.getItems();
      setInventoryFromDisplay(items, inv, items.size());
      this.updateDescription(list.getSelectedItem());
    }
  }

  private void menuEscape() {
    playMenuSound(3);
    this.unload.run();
  }

  private void menuDiscard() {
    if(this.itemList.isHighlightShown()) {
      if(!this.itemList.isEmpty()) {
        this.showDiscardMenu(this.itemList, gameState_800babc8.items_2e9);
      }
    } else if(!this.equipmentList.isEmpty()) {
      this.showDiscardMenu(this.equipmentList, gameState_800babc8.equipment_1e8);
    }
  }

  private void menuSort() {
    playMenuSound(2);
    this.itemList.sort(menuItemIconComparator(List.of(LodMod.ITEM_IDS), stack -> stack.getItem().getRegistryId()));
    this.equipmentList.sort(menuEquipmentSlotComparator());
    setInventoryFromDisplay(this.itemList.getItems(), gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.getSize());
    setInventoryFromDisplay(this.equipmentList.getItems(), gameState_800babc8.equipment_1e8, this.equipmentList.getItems().size());
    this.itemList.removeIf(MenuEntryStruct04::isEmpty);
    this.equipmentList.removeIf(MenuEntryStruct04::isEmpty);
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
