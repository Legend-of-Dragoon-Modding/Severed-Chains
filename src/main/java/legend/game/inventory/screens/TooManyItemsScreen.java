package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.Scus94491BpeSegment;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.types.EngineState;
import legend.game.types.LodString;
import legend.game.types.MenuItemStruct04;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;

import java.util.ArrayList;
import java.util.List;

import static legend.game.SItem.Acquired_item_8011c2f8;
import static legend.game.SItem.Armed_item_8011c314;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.Press_to_sort_8011d024;
import static legend.game.SItem.This_item_cannot_be_thrown_away_8011c2a8;
import static legend.game.SItem.Used_item_8011c32c;
import static legend.game.SItem.allocateOneFrameGlyph;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.glyphs_80114548;
import static legend.game.SItem.loadItemsAndEquipmentForDisplay;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox_8011dc90;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.renderText;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.itemCantBeDiscarded;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.setInventoryFromDisplay;
import static legend.game.Scus94491BpeSegment_8002.sortItems;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemiesCount_800bc978;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
public class TooManyItemsScreen extends MenuScreen {
  private MenuState menuState = MenuState._1;
  private double scrollAccumulator;
  private int mouseX;
  private int mouseY;

  private int dropIndex;
  private int invIndex;
  private int invScroll;

  private Renderable58 renderable_8011e200;
  private Renderable58 renderable_8011e204;

  private final List<MenuItemStruct04> equipment = new ArrayList<>();
  private final List<MenuItemStruct04> items = new ArrayList<>();
  private final List<MenuItemStruct04> droppedItems = new ArrayList<>();

  @Override
  protected void render() {
    switch(this.menuState) {
      case _1 -> {
        if(uiFile_800bdc3c != null) {
          loadItemsAndEquipmentForDisplay(this.equipment, this.items, 0x1L);
          messageBox_8011dc90.state_0c = 0;

          for(int itemIndex = 0; itemIndex < itemsDroppedByEnemiesCount_800bc978.get(); itemIndex++) {
            final MenuItemStruct04 item = new MenuItemStruct04();
            item.itemId_00 = itemsDroppedByEnemies_800bc928.get(itemIndex).get();
            this.droppedItems.add(item);
          }

          this.menuState = MenuState._2;
        }
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
        this.FUN_8010fd80(true, this.droppedItems.get(this.dropIndex).itemId_00, this.invIndex, this.invScroll, 0);
        Scus94491BpeSegment.startFadeEffect(2, 10);
        this.menuState = MenuState._4;
      }

      case _4 -> {
        menuStack.pushScreen(new MessageBoxScreen(new LodString("Too many items. Replace?"), 2, result -> this.menuState = result == MessageBoxResult.YES ? MenuState._6 : MenuState._10));
        this.menuState = MenuState._5;
      }

      case _5 -> this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).itemId_00, this.invIndex, this.invScroll, 0);

      case _6 -> {
        this.dropIndex = 0;
        final Renderable58 renderable2 = allocateUiElement(124, 124, 42, this.FUN_8010f178(0));
        this.renderable_8011e200 = renderable2;
        FUN_80104b60(renderable2);
        deallocateRenderables(0);
        this.FUN_8010fd80(true, this.droppedItems.get(this.dropIndex).itemId_00, this.invIndex, this.invScroll, 0x1L);
        this.menuState = MenuState._8;
      }

      case _8 -> this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).itemId_00, this.invIndex, this.invScroll, 0x1L);

      case _9 -> {
        final int slotCount;
        if(this.droppedItems.get(this.dropIndex).itemId_00 < 0xc0) {
          slotCount = gameState_800babc8.equipment_1e8.size();
        } else {
          slotCount = gameState_800babc8.items_2e9.size();
        }

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            if(this.invScroll > 0) {
              playSound(1);
              this.invScroll--;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
            } else if(this.invScroll == 0) {
              this.invScroll = slotCount - 7;
              this.invIndex = 6;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
            }
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            if(this.invScroll < slotCount - 7) {
              playSound(1);
              this.invScroll++;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
            } else if(this.invScroll == slotCount - 7) {
              this.invScroll = 0;
              this.invIndex = 0;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
            }
          }
        }

        this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).itemId_00, this.invIndex, this.invScroll, 0x3L);
      }

      case _10 -> {
        this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).itemId_00, this.invIndex, this.invScroll, 0);

        menuStack.pushScreen(new MessageBoxScreen(new LodString("Discard extra items?"), 2, result -> {
          if(result == MessageBoxResult.YES) {
            for(final MenuItemStruct04 item : this.droppedItems) {
              if(itemCantBeDiscarded(item.itemId_00)) {
                menuStack.pushScreen(new MessageBoxScreen(This_item_cannot_be_thrown_away_8011c2a8, 0, result1 -> this.menuState = MenuState._6));
                return;
              }
            }

            Scus94491BpeSegment.startFadeEffect(1, 10);
            this.menuState = MenuState._12;
          } else {
            this.menuState = MenuState._6;
          }
        }));

        this.menuState = MenuState._5;
      }

      case _12 -> {
        this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).itemId_00, this.invIndex, this.invScroll, 0);

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          Scus94491BpeSegment.startFadeEffect(2, 10);
          deallocateRenderables(0xff);
          uiFile_800bdc3c = null;
          whichMenu_800bdc38 = WhichMenu.UNLOAD_TOO_MANY_ITEMS_MENU_35;

          if(engineState_8004dd20 == EngineState.SUBMAP_05) {
            FUN_800e3fac();
          }

          textZ_800bdf00.set(13);
        }
      }
    }
  }

  private void FUN_8010fd80(final boolean allocate, final int itemId, final int slotIndex, final int slotScroll, final long a4) {
    if(allocate) {
      renderGlyphs(glyphs_80114548, 0, 0);
      saveListUpArrow_800bdb94 = allocateUiElement(61, 68, 358, this.FUN_8010f178(0));
      saveListDownArrow_800bdb98 = allocateUiElement(53, 60, 358, this.FUN_8010f178(6));
    }

    renderMenuItems(16, 33, this.droppedItems, 0, Math.min(5, itemsDroppedByEnemiesCount_800bc978.get()), saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);

    if((a4 & 0x1L) != 0 && !allocate) {
      renderString(0, 16, 164, itemId, false);
    }

    renderText(Acquired_item_8011c2f8, 32, 22, TextColour.BROWN);

    if(itemId >= 0xc0) {
      if(itemId >= 0xff && (a4 & 0x2L) != 0) {
        final Renderable58 renderable = allocateOneFrameGlyph(137, 84, 140);
        renderable.clut_30 = 0x7ceb;
        renderText(Press_to_sort_8011d024, 37, 140, TextColour.BROWN);
      }

      renderText(Used_item_8011c32c, 210, 22, TextColour.BROWN);

      if((a4 & 0x1L) != 0) {
        renderMenuItems(194, 33, this.items, slotScroll, 7, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);
      }

      if((a4 & 0x2L) != 0) {
        renderString(0, 194, 164, this.items.get(slotScroll + slotIndex).itemId_00, allocate);

        if((a4 & 0x2L) != 0) {
          final Renderable58 renderable = allocateOneFrameGlyph(137, 84, 140);
          renderable.clut_30 = 0x7ceb;
          renderText(Press_to_sort_8011d024, 37, 140, TextColour.BROWN);
        }
      }
    } else {
      renderText(Armed_item_8011c314, 210, 22, TextColour.BROWN);

      if((a4 & 0x1L) != 0) {
        renderMenuItems(194, 33, this.equipment, slotScroll, 7, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);
      }

      if((a4 & 0x2L) != 0) {
        renderString(0, 194, 164, this.equipment.get(slotScroll + slotIndex).itemId_00, allocate);

        if((a4 & 0x2L) != 0) {
          final Renderable58 renderable = allocateOneFrameGlyph(137, 84, 140);
          renderable.clut_30 = 0x7ceb;
          renderText(Press_to_sort_8011d024, 37, 140, TextColour.BROWN);
        }
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
      for(int i = 0; i < itemsDroppedByEnemiesCount_800bc978.get(); i++) {
        if(this.dropIndex != i && MathHelper.inBox(x, y, 9, this.FUN_8010f178(i), 171, 17)) {
          playSound(1);
          this.dropIndex = i;
          this.renderable_8011e200.y_44 = this.FUN_8010f178(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState._9) {
      for(int i = 0; i < 7; i++) {
        if(this.invIndex != i && MathHelper.inBox(x, y, 188, this.FUN_8010f178(i), 171, 17)) {
          playSound(1);
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
      for(int i = 0; i < itemsDroppedByEnemiesCount_800bc978.get(); i++) {
        if(MathHelper.inBox(x, y, 9, this.FUN_8010f178(i), 171, 17)) {
          playSound(2);
          this.dropIndex = i;
          this.renderable_8011e200.y_44 = this.FUN_8010f178(i);

          this.selectMenuState8();
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState._9) {
      for(int i = 0; i < 7; i++) {
        if(MathHelper.inBox(x, y, 188, this.FUN_8010f178(i), 171, 17)) {
          playSound(2);
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
    playSound(1);
    this.dropIndex = this.dropIndex < itemsDroppedByEnemiesCount_800bc978.get() - 1 ? ++this.dropIndex : 0;
    this.renderable_8011e200.y_44 = this.FUN_8010f178(this.dropIndex);
  }

  private void droppedNavigateUp() {
    playSound(1);
    this.dropIndex = this.dropIndex > 0 ? --this.dropIndex : itemsDroppedByEnemiesCount_800bc978.get() - 1;
    this.renderable_8011e200.y_44 = this.FUN_8010f178(this.dropIndex);
  }

  private void handleInventoryScrollUp() {
    final int slotCount = gameState_800babc8.items_2e9.size();

    if(this.invIndex == 0 && this.invScroll > 0) {
      this.invScroll--;
    }

    if(this.invIndex == 0 && this.invScroll == 0) {
      this.invScroll = slotCount - 7;
      this.invIndex = 7;
    }
  }

  private void handleInventoryScrollDown() {
    final int slotCount = gameState_800babc8.items_2e9.size();

    if(this.invIndex == 6 && this.invScroll < slotCount - 7) {
      this.invScroll++;
    }

    if(this.invIndex == 6 && this.invScroll == slotCount - 7) {
      this.invScroll = 0;
      this.invIndex = -1;
    }
  }

  private void inventoryNavigateDown() {
    playSound(1);
    this.handleInventoryScrollDown();
    this.invIndex = this.invIndex < 6 ? ++this.invIndex : 6;
    this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
  }

  private void inventoryNavigateUp() {
    playSound(1);
    this.handleInventoryScrollUp();
    this.invIndex = this.invIndex > 0 ? --this.invIndex : 0;
    this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
  }

  private void escapeMenuState8() {
    playSound(3);
    unloadRenderable(this.renderable_8011e200);
    this.menuState = MenuState._10;
  }

  private void selectMenuState8() {
    final Renderable58 renderable3 = allocateUiElement(118, 118, 220, this.FUN_8010f178(0));
    this.renderable_8011e204 = renderable3;
    FUN_80104b60(renderable3);
    playSound(2);
    this.menuState = MenuState._9;
  }

  private void escapeMenuState9() {
    this.invScroll = 0;
    this.invIndex = 0;

    playSound(3);
    unloadRenderable(this.renderable_8011e204);
    this.menuState = MenuState._8;
  }

  private void selectMenuState9() {
    final MenuItemStruct04 newItem = this.droppedItems.get(this.dropIndex);
    final int isItem = this.droppedItems.get(this.dropIndex).itemId_00 >= 0xc0 ? 1 : 0;
    final MenuItemStruct04 existingItem;
    if(isItem == 0) {
      existingItem = this.equipment.get(this.invIndex + this.invScroll);
    } else {
      existingItem = this.items.get(this.invIndex + this.invScroll);
    }

    if((existingItem.flags_02 & 0x6000) != 0) {
      playSound(40);
    } else {
      final int itemId = existingItem.itemId_00;
      final int flags = existingItem.flags_02;

      existingItem.itemId_00 = newItem.itemId_00;
      existingItem.flags_02 = newItem.flags_02;

      newItem.itemId_00 = itemId;
      newItem.flags_02 = flags;

      if(isItem != 0) {
        setInventoryFromDisplay(this.items, gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.size());
      } else {
        setInventoryFromDisplay(this.equipment, gameState_800babc8.equipment_1e8, gameState_800babc8.equipment_1e8.size());
      }

      this.invScroll = 0;
      this.invIndex = 0;

      playSound(2);
      unloadRenderable(this.renderable_8011e204);
      this.menuState = MenuState._8;
    }
  }

  private void sortMenuState9() {
    playSound(2);

    if(this.droppedItems.get(this.dropIndex).itemId_00 < 0xc0) {
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
