package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.ItemList;
import legend.game.inventory.screens.controls.Label;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MessageBoxResult;

import javax.annotation.Nullable;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.game.SItem.loadItemsAndEquipmentForDisplay;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.menuEquipmentSlotComparator;
import static legend.game.Scus94491BpeSegment_8002.menuItemIconComparator;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.setInventoryFromDisplay;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DELETE;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_SORT;

public class ItemListScreen extends MenuScreen {
  private final Runnable unload;

  private final ItemList<Item> itemList = new ItemList<>();
  private final ItemList<Equipment> equipmentList = new ItemList<>(i -> gameState_800babc8.equipment_1e8.size());
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

    this.addControl(new Label("Press   to sort")).setPos(30, 179);
    this.addControl(new Label("Press   to discard")).setPos(30, 194);

    final Glyph sortButton = this.addControl(Glyph.glyph(0x89));
    final Glyph discardButton = this.addControl(Glyph.glyph(0x88));
    sortButton.setPos(81, 179);
    sortButton.getRenderable().clut_30 = 0x7ceb;
    discardButton.setPos(81, 194);
    discardButton.getRenderable().clut_30 = 0x7ceb;

    this.description.setPos(194, 178);

    this.addControl(this.itemList);
    this.addControl(this.equipmentList);
    this.addControl(this.description);

    this.setFocus(this.itemList);

    final MenuEntries<Item> items = new MenuEntries<>();
    final MenuEntries<Equipment> equipment = new MenuEntries<>();
    loadItemsAndEquipmentForDisplay(equipment, items, 0);

    for(final MenuEntryStruct04<Item> item : items) {
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

  private <T> void showDiscardMenu(final ItemList<T> list, final List<T> inv) {
    if(((list.getSelectedItem().flags_02 & 0x2000) != 0)) {
      playMenuSound(40);
    } else {
      playMenuSound(2);
      menuStack.pushScreen(new MessageBoxScreen("Discard?", 2, result -> this.discard(result, list, inv)));
    }
  }

  private <T> void discard(final MessageBoxResult result, final ItemList<T> list, final List<T> inv) {
    if(result == MessageBoxResult.YES) {
      list.remove(list.getSelectedItem());
      final List<MenuEntryStruct04<T>> items = list.getItems();
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
    this.itemList.sort(menuItemIconComparator());
    this.equipmentList.sort(menuEquipmentSlotComparator());
    setInventoryFromDisplay(this.itemList.getItems(), gameState_800babc8.items_2e9, this.itemList.getItems().size());
    setInventoryFromDisplay(this.equipmentList.getItems(), gameState_800babc8.equipment_1e8, this.equipmentList.getItems().size());
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

    if(action == INPUT_ACTION_MENU_DELETE.get()) {
      this.menuDiscard();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_SORT.get() && !repeat) {
      this.menuSort();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
