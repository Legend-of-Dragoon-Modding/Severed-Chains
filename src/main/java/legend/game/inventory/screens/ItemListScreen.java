package legend.game.inventory.screens;

import it.unimi.dsi.fastutil.ints.IntList;
import legend.game.BaseMod;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.ItemList;
import legend.game.inventory.screens.controls.Label;
import legend.game.types.LodString;
import legend.game.types.MenuItemStruct04;
import legend.game.types.MessageBoxResult;

import java.util.ArrayList;
import java.util.List;

import static legend.game.SItem.itemDescriptions_80117a10;
import static legend.game.SItem.loadItemsAndEquipmentForDisplay;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.menuItemComparator;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.setInventoryFromDisplay;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class ItemListScreen extends MenuScreen {
  private final Runnable unload;

  private final ItemList itemList = new ItemList();
  private final ItemList equipmentList = new ItemList();
  private final Label description = new Label("");

  public ItemListScreen(final Runnable unload) {
    deallocateRenderables(0xff);
    scriptStartEffect(2, 10);

    this.unload = unload;

    this.itemList.setPos(8, 15);
    this.itemList.setTitle("Items");
    this.itemList.setMax(gameState_800babc8.getConfig(BaseMod.INVENTORY_SIZE_CONFIG));

    this.equipmentList.setPos(188, 15);
    this.equipmentList.setTitle("Equipment");
    this.equipmentList.setMax(255);

    this.itemList.onHoverIn(() -> this.setFocus(this.itemList));
    this.itemList.onGotFocus(() -> {
      this.itemList.showHighlight();
      this.equipmentList.hideHighlight();
      this.description.show();
    });
    this.itemList.onPressedThisFrame(inputAction -> {
      if(inputAction == InputAction.DPAD_RIGHT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_RIGHT) {
        this.setFocus(this.equipmentList);
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });
    this.itemList.onHighlight(item -> this.description.setText(itemDescriptions_80117a10.get(item.itemId_00).deref()));

    this.equipmentList.onHoverIn(() -> this.setFocus(this.equipmentList));
    this.equipmentList.onGotFocus(() -> {
      this.itemList.hideHighlight();
      this.equipmentList.showHighlight();
    });
    this.equipmentList.onPressedThisFrame(inputAction -> {
      if(inputAction == InputAction.DPAD_LEFT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_LEFT) {
        this.setFocus(this.itemList);
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });

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
    this.description.hide();

    this.addControl(this.itemList);
    this.addControl(this.equipmentList);
    this.addControl(this.description);

    this.setFocus(this.itemList);

    final List<MenuItemStruct04> items = new ArrayList<>();
    final List<MenuItemStruct04> equipment = new ArrayList<>();
    loadItemsAndEquipmentForDisplay(equipment, items, 0x1L);

    for(final MenuItemStruct04 item : items) {
      this.itemList.add(item);
    }

    for(final MenuItemStruct04 item : equipment) {
      this.equipmentList.add(item);
    }
  }

  @Override
  protected void render() {

  }

  private void showDiscardMenu(final ItemList list, final IntList inv) {
    if(((list.getSelectedItem().flags_02 & 0x2000) != 0)) {
      playSound(40);
    } else {
      playSound(2);
      menuStack.pushScreen(new MessageBoxScreen(new LodString("Discard?"), 2, result -> this.discard(result, list, inv)));
    }
  }

  private void discard(final MessageBoxResult result, final ItemList list, final IntList inv) {
    if(result == MessageBoxResult.YES) {
      list.remove(list.getSelectedItem());
      final List<MenuItemStruct04> items = list.getItems();
      setInventoryFromDisplay(items, inv, items.size());
    }
  }

  private void menuEscape() {
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
    playSound(2);
    this.itemList.sort(menuItemComparator());
    this.equipmentList.sort(menuItemComparator());
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_WEST) {
      this.menuDiscard();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_NORTH) {
      this.menuSort();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
