package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;

import java.util.Set;

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
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_END;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HOME;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_UP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_SORT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class TooManyItemsScreen extends MenuScreen {
  private static final String This_item_cannot_be_thrown_away_8011c2a8 = "This item cannot\nbe thrown away";
  private static final String Acquired_item_8011c2f8 = "Acquired item";
  private static final String Armed_item_8011c314 = "Armed item";
  private static final String Used_item_8011c32c = "Used item";
  private static final String Press_to_sort_8011d024 = "Press  to sort";

  private MenuState menuState = MenuState.LOAD_ITEMS_1;
  private double scrollAccumulator;
  private int mouseX;
  private int mouseY;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

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
      case LOAD_ITEMS_1 -> {
        loadItemsAndEquipmentForDisplay(this.equipment, this.items, 0x1L);

        for(final Item item : itemOverflow) {
          this.droppedItems.add(MenuEntryStruct04.make(item));
        }

        for(final Equipment equipment : equipmentOverflow) {
          this.droppedItems.add(MenuEntryStruct04.make(equipment));
        }

        this.menuState = MenuState.INIT_2;
      }

      case INIT_2 -> {
        deallocateRenderables(0xff);
        this.invScroll = 0;
        this.invIndex = 0;
        this.dropIndex = 0;
        this.menuState = MenuState.INIT_3;
      }

      case INIT_3 -> {
        deallocateRenderables(0);
        this.FUN_8010fd80(true, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0);
        startFadeEffect(2, 10);
        this.menuState = MenuState.RENDER_4;
      }

      case RENDER_4 -> {
        menuStack.pushScreen(new MessageBoxScreen("Too many items. Replace?", 2, result -> this.menuState = result == MessageBoxResult.YES ? MenuState.RENDER_6 : MenuState.DISCARD_10));
        this.menuState = MenuState.REPLACE_5;
      }

      case REPLACE_5 -> this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0);

      case RENDER_6 -> {
        this.dropIndex = 0;
        final Renderable58 renderable2 = allocateUiElement(124, 124, 42, this.FUN_8010f178(0));
        this.renderable_8011e200 = renderable2;
        FUN_80104b60(renderable2);
        deallocateRenderables(0);
        this.FUN_8010fd80(true, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0x1L);
        this.menuState = MenuState.DROPPED_8;
      }

      case DROPPED_8 -> this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0x1L);

      case INVENTORY_9 -> {
        final int slotCount;
        if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
          slotCount = gameState_800babc8.equipment_1e8.size();
        } else {
          slotCount = gameState_800babc8.items_2e9.size();
        }

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.invScroll > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            playMenuSound(1);
            this.invScroll--;
            this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.invScroll < slotCount - 7 && MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            playMenuSound(1);
            this.invScroll++;
            this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
          }
        }

        this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0x3L);
      }

      case DISCARD_10 -> {
        this.FUN_8010fd80(false, this.droppedItems.get(this.dropIndex).item_00, this.invIndex, this.invScroll, 0);

        menuStack.pushScreen(new MessageBoxScreen("Discard extra items?", 2, result -> {
          if(result == MessageBoxResult.YES) {
            for(final MenuEntryStruct04<InventoryEntry> item : this.droppedItems) {
              if(item.item_00 instanceof final Equipment equipment && !equipment.canBeDiscarded()) {
                menuStack.pushScreen(new MessageBoxScreen(This_item_cannot_be_thrown_away_8011c2a8, 0, result1 -> this.menuState = MenuState.RENDER_6));
                return;
              }
            }

            startFadeEffect(1, 10);
            this.menuState = MenuState.UNLOAD_12;
          } else {
            this.menuState = MenuState.RENDER_6;
          }
        }));

        this.menuState = MenuState.REPLACE_5;
      }

      case UNLOAD_12 -> {
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

    if(this.menuState == MenuState.DROPPED_8) {
      for(int i = 0; i < Math.min(5, this.droppedItems.size()); i++) {
        if(this.dropIndex != i && MathHelper.inBox(x, y, 9, this.FUN_8010f178(i), 171, 17)) {
          playMenuSound(1);
          this.dropIndex = i;
          this.renderable_8011e200.y_44 = this.FUN_8010f178(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.INVENTORY_9) {
      for(int i = 0; i < Math.min(7, this.items.size()); i++) {
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
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState == MenuState.DROPPED_8) {
      for(int i = 0; i < this.droppedItems.size(); i++) {
        if(MathHelper.inBox(x, y, 9, this.FUN_8010f178(i), 171, 17)) {
          playMenuSound(2);
          this.dropIndex = i;
          this.renderable_8011e200.y_44 = this.FUN_8010f178(i);

          this.selectMenuState8();
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.INVENTORY_9) {
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

    if(this.menuState != MenuState.INVENTORY_9) {
      return InputPropagation.PROPAGATE;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
    return InputPropagation.HANDLED;
  }

  private void droppedNavigateUp() {
    final int dropCount = this.droppedItems.size();
    if(this.dropIndex > 0) {
      playMenuSound(1);
      this.dropIndex--;
    } else if(dropCount > 1 && this.allowWrapY) {
      playMenuSound(1);
      this.dropIndex = dropCount - 1;
    }

    this.renderable_8011e200.y_44 = this.FUN_8010f178(this.dropIndex);
  }

  private void droppedNavigateDown() {
    final int dropCount = this.droppedItems.size();
    if(this.dropIndex < dropCount - 1) {
      playMenuSound(1);
      this.dropIndex++;
    } else if(dropCount > 1 && this.allowWrapY) {
      playMenuSound(1);
      this.dropIndex = 0;
    }

    this.renderable_8011e200.y_44 = this.FUN_8010f178(this.dropIndex);
  }

  private void droppedNavigateHome() {
    if(this.dropIndex != 0) {
      playMenuSound(1);
      this.dropIndex = 0;
      this.renderable_8011e200.y_44 = this.FUN_8010f178(this.dropIndex);
    }
  }

  private void droppedNavigateEnd() {
    if(this.dropIndex != this.droppedItems.size() - 1) {
      playMenuSound(1);
      this.dropIndex = this.droppedItems.size() - 1;
      this.renderable_8011e200.y_44 = this.FUN_8010f178(this.dropIndex);
    }
  }

  private void handleInventoryScrollUp() {
    final int slotCount;
    if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
      slotCount = gameState_800babc8.equipment_1e8.size();
    } else {
      slotCount = gameState_800babc8.items_2e9.size();
    }

    if(this.invIndex == 0) {
      if(this.invScroll > 0) {
        this.invScroll--;
      } else {
        this.invScroll = Math.max(0, slotCount - 7);
        this.invIndex = Math.min(slotCount, 7);
      }
    }
  }

  private void handleInventoryScrollDown() {
    final int slotCount;
    if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
      slotCount = gameState_800babc8.equipment_1e8.size();
    } else {
      slotCount = gameState_800babc8.items_2e9.size();
    }

    if(this.invIndex == 6) {
      if(this.invScroll + this.invIndex < slotCount - 1) {
        this.invScroll++;
      } else if(this.invScroll + this.invIndex == slotCount - 1 || (this.invScroll < 1 && this.invIndex >= slotCount)) {
        this.invScroll = 0;
        this.invIndex = -1;
      }
    }
  }

  private void inventoryNavigateUp() {
    final int slotCount;
    if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
      slotCount = gameState_800babc8.equipment_1e8.size();
    } else {
      slotCount = gameState_800babc8.items_2e9.size();
    }

    if(this.invScroll + this.invIndex > 0 || (slotCount > 1 && this.allowWrapY)) {
      playMenuSound(1);
      this.handleInventoryScrollUp();
    }

    this.invIndex = Math.max(this.invIndex - 1, 0);
    this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
  }

  private void inventoryNavigateDown() {
    final int slotCount;
    if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
      slotCount = gameState_800babc8.equipment_1e8.size();
    } else {
      slotCount = gameState_800babc8.items_2e9.size();
    }

    if(this.invScroll + this.invIndex < slotCount - 1 || (slotCount > 1 && this.allowWrapY)) {
      playMenuSound(1);
      this.handleInventoryScrollDown();
    }

    this.invIndex = Math.min(this.invIndex + 1, 6);
    this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
  }

  private void inventoryNavigatePageUp() {
    if(this.invScroll - 6 >= 0) {
      playMenuSound(1);
      this.invScroll -= 6;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    } else if(this.invScroll != 0) {
      playMenuSound(1);
      this.invScroll = 0;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    }
  }

  private void inventoryNavigatePageDown() {
    final int slotCount;
    if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
      slotCount = gameState_800babc8.equipment_1e8.size();
    } else {
      slotCount = gameState_800babc8.items_2e9.size();
    }

    if(this.invScroll + 6 < slotCount - 6) {
      playMenuSound(1);
      this.invScroll += 6;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    } else if(slotCount > 7 && this.invScroll != slotCount - 7) {
      playMenuSound(1);
      this.invScroll = slotCount - 7;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    }
  }

  private void inventoryNavigateHome() {
    if(this.invIndex > 0 || this.invScroll > 0) {
      playMenuSound(1);
      this.invIndex = 0;
      this.invScroll = 0;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    }
  }

  private void inventoryNavigateEnd() {
    final int slotCount = gameState_800babc8.items_2e9.size();

    if(this.invScroll + this.invIndex != slotCount - 1) {
      playMenuSound(1);
      this.invIndex = Math.min(6, slotCount - 1);
      this.invScroll = slotCount - 1 - this.invIndex;
      this.renderable_8011e204.y_44 = this.FUN_8010f178(this.invIndex);
    }
  }

  private void escapeMenuState8() {
    playMenuSound(3);
    unloadRenderable(this.renderable_8011e200);
    this.menuState = MenuState.DISCARD_10;
  }

  private void selectMenuState8() {
    final Renderable58 renderable3 = allocateUiElement(118, 118, 220, this.FUN_8010f178(0));
    this.renderable_8011e204 = renderable3;
    FUN_80104b60(renderable3);
    playMenuSound(2);
    this.menuState = MenuState.INVENTORY_9;
  }

  private void escapeMenuState9() {
    this.invScroll = 0;
    this.invIndex = 0;

    playMenuSound(3);
    unloadRenderable(this.renderable_8011e204);
    this.menuState = MenuState.DROPPED_8;
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
      this.menuState = MenuState.DROPPED_8;
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
  public InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState == MenuState.DROPPED_8) {
      if(action == INPUT_ACTION_MENU_HOME.get()) {
        this.droppedNavigateHome();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_END.get()) {
        this.droppedNavigateEnd();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_UP.get()) {
        this.droppedNavigateUp();
        this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_DOWN.get()) {
        this.droppedNavigateDown();
        this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
        this.escapeMenuState8();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
        this.selectMenuState8();
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    }

    if(this.menuState == MenuState.INVENTORY_9) {
      if(action == INPUT_ACTION_MENU_HOME.get()) {
        this.inventoryNavigateHome();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_END.get()) {
        this.inventoryNavigateEnd();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_PAGE_UP.get()) {
        this.inventoryNavigatePageUp();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_PAGE_DOWN.get()) {
        this.inventoryNavigatePageDown();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_UP.get()) {
        this.inventoryNavigateUp();
        this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_DOWN.get()) {
        this.inventoryNavigateDown();
        this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
        this.escapeMenuState9();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
        this.selectMenuState9();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_SORT.get() && !repeat) {
        this.sortMenuState9();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation inputActionReleased(final InputAction action) {
    if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_UP.get() || action == INPUT_ACTION_MENU_DOWN.get()) {
      this.allowWrapY = true;
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  public enum MenuState {
    LOAD_ITEMS_1,
    INIT_2,
    INIT_3,
    RENDER_4,
    REPLACE_5,
    RENDER_6,
    DROPPED_8,
    INVENTORY_9,
    DISCARD_10,
    UNLOAD_12,
  }
}
