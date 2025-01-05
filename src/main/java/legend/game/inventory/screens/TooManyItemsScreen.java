package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.i18n.I18n;
import legend.game.input.InputAction;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.allocateOneFrameGlyph;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.glyphs_80114548;
import static legend.game.SItem.loadItemsAndEquipmentForDisplay;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.setInventoryFromDisplay;
import static legend.game.Scus94491BpeSegment_8002.sortItems;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_800b.equipmentOverflow;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.itemOverflow;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class TooManyItemsScreen extends MenuScreen {
  private static final String This_item_cannot_be_thrown_away_8011c2a8 = "This item cannot\nbe thrown away";
  private static final String Acquired_item_8011c2f8 = "Acquired item";
  private static final String Armed_item_8011c314 = "Armed item";
  private static final String Used_item_8011c32c = "Used item";
  private static final String Press_to_sort_8011d024 = "Press  to sort";

  private MenuState menuState = MenuState._1;
  private double scrollAccumulator;
  private int mouseX;
  private int mouseY;

  private int dropIndex;
  private int invIndex;
  private int invScroll;

  private Renderable58 renderable_8011e200;
  private Renderable58 renderable_8011e204;

  private final MenuEntries<Equipment> equipment = new MenuEntries<>();
  private final MenuEntries<Item> items = new MenuEntries<>();
  private final MenuEntries<InventoryEntry> droppedItems = new MenuEntries<>();

  @Override
  protected void render() {
    switch(this.menuState) {
      case _1 -> {
        loadItemsAndEquipmentForDisplay(this.equipment, this.items, 0x1L);

        for(final Item item : itemOverflow) {
          this.droppedItems.add(MenuEntryStruct04.make(item));
        }

        for(final Equipment equipment : equipmentOverflow) {
          this.droppedItems.add(MenuEntryStruct04.make(equipment));
        }

        this.menuState = MenuState._2;
      }

      case _2 -> {
        deallocateRenderables(0xff);
        this.invScroll = 0;
        this.invIndex = 0;
        this.dropIndex = 0;
        this.menuState = MenuState._3;
      }

      case _3 -> {
        deallocateRenderables(0);
        this.FUN_8010fd80(true, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0);
        startFadeEffect(2, 10);
        this.menuState = MenuState._4;
      }

      case _4 -> {
        menuStack.pushScreen(new MessageBoxScreen("Too many items. Replace?", 2, result -> this.menuState = result == MessageBoxResult.YES ? MenuState._6 : MenuState._10));
        this.menuState = MenuState._5;
      }

      case _5 -> this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0);

      case _6 -> {
        this.dropIndex = 0;
        final Renderable58 renderable2 = allocateUiElement(124, 124, 42, this.FUN_8010f178(0));
        this.renderable_8011e200 = renderable2;
        FUN_80104b60(renderable2);
        deallocateRenderables(0);
        this.FUN_8010fd80(true, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0x1L);
        this.menuState = MenuState._8;
      }

      case _8 -> this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0x1L);

      case _9 -> {
        final int slotCount;
        if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
          slotCount = gameState_800babc8.equipment_1e8.size();
        } else {
          slotCount = gameState_800babc8.items_2e9.size();
        }

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            if(this.invScroll > 0) {
              playMenuSound(1);
              this.invScroll--;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
            } else if(this.invScroll == 0) {
              this.invScroll = Math.max(0, slotCount - 7);
              this.invIndex = this.invScroll > 0 ? 6 : 0;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
            }
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            if(this.invScroll < slotCount - 7) {
              playMenuSound(1);
              this.invScroll++;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
            } else if(this.invScroll == slotCount - 7) {
              this.invScroll = 0;
              this.invIndex = 0;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
            }
          }
        }

        this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0x3L);
      }

      case _10 -> {
        this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0);

        menuStack.pushScreen(new MessageBoxScreen("Discard extra items?", 2, result -> {
          if(result == MessageBoxResult.YES) {
            for(final MenuEntryStruct04<InventoryEntry> item : this.droppedItems) {
              if(item.item_00 instanceof final Equipment equipment && !equipment.canBeDiscarded()) {
                menuStack.pushScreen(new MessageBoxScreen(This_item_cannot_be_thrown_away_8011c2a8, 0, result1 -> this.menuState = MenuState._6));
                return;
              }
            }

            startFadeEffect(1, 10);
            this.menuState = MenuState._12;
          } else {
            this.menuState = MenuState._6;
          }
        }));

        this.menuState = MenuState._5;
      }

      case _12 -> {
        this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0);

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          menuStack.popScreen();
          whichMenu_800bdc38 = WhichMenu.UNLOAD;
        }
      }
    }
  }

  private void FUN_8010fd80(final boolean allocate, final InventoryEntry inv, final int slotIndex, final int slotScroll, final long a4) {
    if(allocate) {
      renderGlyphs(glyphs_80114548, 0, 0);
      saveListUpArrow_800bdb94 = allocateUiElement(61, 68, 358, this.FUN_8010f178(0));
      saveListDownArrow_800bdb98 = allocateUiElement(53, 60, 358, this.FUN_8010f178(6));
    }

    renderMenuItems(16, 33, this.droppedItems, 0, Math.min(5, this.droppedItems.size()), saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);

    if((a4 & 0x1) != 0 && !allocate) {
      renderString(16, 164, I18n.translate(inv.getDescriptionTranslationKey()), false);
    }

    renderText(Acquired_item_8011c2f8, 32, 22, UI_TEXT);

    if(inv instanceof Item) {
      renderText(Used_item_8011c32c, 210, 22, UI_TEXT);

      if((a4 & 0x1) != 0) {
        renderMenuItems(194, 33, this.items, slotScroll, 7, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);
      }

      if((a4 & 0x2) != 0) {
        if(slotScroll + slotIndex < this.items.size()) {
          renderString(194, 164, I18n.translate(this.items.get(slotScroll + slotIndex).getDescriptionTranslationKey()), allocate);
        }

        final Renderable58 renderable = allocateOneFrameGlyph(137, 84, 140);
        renderable.clut_30 = 0x7ceb;
        renderText(Press_to_sort_8011d024, 37, 140, UI_TEXT);
      }
    } else {
      renderText(Armed_item_8011c314, 210, 22, UI_TEXT);

      if((a4 & 0x1) != 0) {
        renderMenuItems(194, 33, this.equipment, slotScroll, 7, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);
      }

      if((a4 & 0x2) != 0) {
        if(slotScroll + slotIndex < this.equipment.size()) {
          renderString(194, 164, I18n.translate(this.equipment.get(slotScroll + slotIndex).getDescriptionTranslationKey()), allocate);
        }

        final Renderable58 renderable = allocateOneFrameGlyph(137, 84, 140);
        renderable.clut_30 = 0x7ceb;
        renderText(Press_to_sort_8011d024, 37, 140, UI_TEXT);
      }
    }
  }

  private int FUN_8010f178(final int slot) {
    return 42 + slot * 17;
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    this.mouseX = x;
    this.mouseY = y;

    if(this.menuState == MenuState._8) {
      for(int i = 0; i < this.droppedItems.size(); i++) {
        if(this.dropIndex != i && MathHelper.inBox(x, y, 9, this.FUN_8010f178(i), 171, 17)) {
          playMenuSound(1);
          this.dropIndex = i;
          this.renderable_8011e200.y_44 = this.FUN_8010f178(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState._9) {
      for(int i = 0; i < this.items.size(); i++) {
        if(this.invIndex != i && MathHelper.inBox(x, y, 188, this.FUN_8010f178(i), 171, 17)) {
          playMenuSound(1);
          this.invIndex = i;
          this.renderable_8011e204.y_44 = this.FUN_8010f178(i);
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState == MenuState._8) {
      for(int i = 0; i < this.droppedItems.size(); i++) {
        if(MathHelper.inBox(x, y, 9, this.FUN_8010f178(i), 171, 17)) {
          playMenuSound(2);
          this.dropIndex = i;
          this.renderable_8011e200.y_44 = this.FUN_8010f178(i);

          this.selectMenuState8();
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState._9) {
      for(int i = 0; i < 7; i++) {
        if(MathHelper.inBox(x, y, 188, this.FUN_8010f178(i), 171, 17)) {
          playMenuSound(2);
          this.invIndex = i;
          this.renderable_8011e204.y_44 = this.FUN_8010f178(i);

          this.selectMenuState9();
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    if(super.mouseScrollHighRes(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState != MenuState._9) {
      return InputPropagation.PROPAGATE;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
    return InputPropagation.HANDLED;
  }

  private void droppedNavigateDown() {
    playMenuSound(1);
    this.dropIndex = this.dropIndex < this.droppedItems.size() - 1 ? ++this.dropIndex : 0;
    this.renderable_8011e200.y_44 = this.FUN_8010f178(this.dropIndex);
  }

  private void droppedNavigateUp() {
    playMenuSound(1);
    this.dropIndex = this.dropIndex > 0 ? --this.dropIndex : this.droppedItems.size() - 1;
    this.renderable_8011e200.y_44 = this.FUN_8010f178(this.dropIndex);
  }

  private void handleInventoryScrollUp() {
    final int slotCount = gameState_800babc8.items_2e9.size();

    if(this.invIndex == 0 && this.invScroll > 0) {
      this.invScroll--;
    }

    if(this.invIndex == 0 && this.invScroll == 0) {
      this.invScroll = Math.max(0, slotCount - 7);
      this.invIndex = Math.min(slotCount, 7);
    }
  }

  private void handleInventoryScrollDown() {
    final int slotCount = gameState_800babc8.items_2e9.size();

    if(this.invIndex == 6 && this.invScroll < slotCount - 7) {
      this.invScroll++;
    }

    if((this.invIndex == 6 && this.invScroll == slotCount - 7) || (this.invScroll < 1 && this.invIndex >= slotCount - 1)) {
      this.invScroll = 0;
      this.invIndex = -1;
    }
  }

  private void inventoryNavigateDown() {
    playMenuSound(1);
    this.handleInventoryScrollDown();
    this.invIndex = Math.min(this.invIndex + 1, 6);
    this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
  }

  private void inventoryNavigateUp() {
    playMenuSound(1);
    this.handleInventoryScrollUp();
    this.invIndex = Math.max(this.invIndex - 1, 0);
    this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
  }

  private void inventoryNavigateTop() {
    if(this.invIndex != 0) {
      playMenuSound(1);
      this.invIndex = 0;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    }
  }

  private void inventoryNavigateBottom() {
    if(this.invIndex != 6) {
      playMenuSound(1);
      this.invIndex = 6;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    }
  }

  private void inventoryNavigatePageUp() {
    if(this.invScroll - 6 >= 0) {
      playMenuSound(1);
      this.invScroll -= 6;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    } else {
      if(this.invScroll != 0) {
        this.invScroll = 0;
        this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
      }
    }
  }

  private void inventoryNavigatePageDown() {
    final int slotCount = gameState_800babc8.items_2e9.size();

    if(this.invScroll + 6 < slotCount - 6) {
      playMenuSound(1);
      this.invScroll += 6;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    } else {
      if(slotCount > 7 && this.invScroll != slotCount - 7) {
        this.invScroll = slotCount - 7;
        this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
      }
    }
  }

  private void escapeMenuState8() {
    playMenuSound(3);
    unloadRenderable(this.renderable_8011e200);
    this.menuState = MenuState._10;
  }

  private void selectMenuState8() {
    final Renderable58 renderable3 = allocateUiElement(118, 118, 220, this.FUN_8010f178(0));
    this.renderable_8011e204 = renderable3;
    FUN_80104b60(renderable3);
    playMenuSound(2);
    this.menuState = MenuState._9;
  }

  private void escapeMenuState9() {
    this.invScroll = 0;
    this.invIndex = 0;

    playMenuSound(3);
    unloadRenderable(this.renderable_8011e204);
    this.menuState = MenuState._8;
  }

  private void selectMenuState9() {
    final MenuEntryStruct04<InventoryEntry> newItem = this.droppedItems.get(this.dropIndex);
    final boolean isItem = this.droppedItems.get(this.dropIndex).item_00 instanceof Item;

    if(this.invIndex + this.invScroll > this.items.size() - 1) {
      return;
    }

    if(((isItem ? this.items : this.equipment).get(this.invIndex + this.invScroll).flags_02 & 0x6000) != 0) {
      playMenuSound(40);
    } else {
      if(isItem) {
        this.droppedItems.set(this.dropIndex, (MenuEntryStruct04<InventoryEntry>)(MenuEntryStruct04)this.items.get(this.invIndex + this.invScroll));
        this.items.set(this.invIndex + this.invScroll, (MenuEntryStruct04<Item>)(MenuEntryStruct04)newItem);
        setInventoryFromDisplay(this.items, gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.size());
      } else {
        this.droppedItems.set(this.dropIndex, (MenuEntryStruct04<InventoryEntry>)(MenuEntryStruct04)this.equipment.get(this.invIndex + this.invScroll));
        this.equipment.set(this.invIndex + this.invScroll, (MenuEntryStruct04<Equipment>)(MenuEntryStruct04)newItem);
        setInventoryFromDisplay(this.equipment, gameState_800babc8.equipment_1e8, gameState_800babc8.equipment_1e8.size());
      }

      this.invScroll = 0;
      this.invIndex = 0;

      playMenuSound(2);
      unloadRenderable(this.renderable_8011e204);
      this.menuState = MenuState._8;
    }
  }

  private void sortMenuState9() {
    playMenuSound(2);

    if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
      sortItems(this.equipment, gameState_800babc8.equipment_1e8, gameState_800babc8.equipment_1e8.size());
    } else {
      sortItems(this.items, gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.size());
    }
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState == MenuState._8) {
      if(inputAction == InputAction.BUTTON_EAST) {
        this.escapeMenuState8();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_SOUTH) {
        this.selectMenuState8();
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    }

    if(this.menuState == MenuState._9) {
      if(inputAction == InputAction.BUTTON_EAST) {
        this.escapeMenuState9();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_SOUTH) {
        this.selectMenuState9();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_NORTH) {
        this.sortMenuState9();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState == MenuState._8) {
      if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
        this.droppedNavigateUp();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
        this.droppedNavigateDown();
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    }

    if(this.menuState == MenuState._9) {
      if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
        this.inventoryNavigateUp();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
        this.inventoryNavigateDown();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_SHOULDER_LEFT_1) {
        this.inventoryNavigateTop();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_SHOULDER_LEFT_2) {
        this.inventoryNavigateBottom();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_SHOULDER_RIGHT_1) {
        this.inventoryNavigatePageUp();
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.BUTTON_SHOULDER_RIGHT_2) {
        this.inventoryNavigatePageDown();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  public enum MenuState {
    _1,
    _2,
    _3,
    _4,
    _5,
    _6,
    _8,
    _9,
    _10,
    _12,
  }
}
