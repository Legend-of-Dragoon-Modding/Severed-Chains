package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.Inventory;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.ItemStack;
import legend.game.inventory.WhichMenu;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.lodmod.LodMod;

import java.util.List;
import java.util.Set;

import static legend.game.Audio.playMenuSound;
import static legend.game.FullScreenEffects.fullScreenEffect_800bb140;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Menus.unloadRenderable;
import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.glyphs_80114548;
import static legend.game.SItem.loadItemsAndEquipmentForDisplay;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.setInventoryFromDisplay;
import static legend.game.SItem.sortItems;
import static legend.game.Scus94491BpeSegment_800b.equipmentOverflow;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.itemOverflow;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BOTTOM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_END;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HOME;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_UP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_SORT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_TOP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class TooManyItemsScreen extends MenuScreen {
  private static final String TOO_MANY_ITEMS_CONFIRM = "lod_core.ui.too_many_items.too_many_items_confirm";
  private static final String DISCARD_ITEMS_CONFIRM = "lod_core.ui.too_many_items.discard_items_confirm";
  private static final String CANNOT_BE_THROWN_AWAY = "lod_core.ui.too_many_items.cannot_be_discarded";
  private static final String OVERFLOW = "lod_core.ui.too_many_items.overflow";
  private static final String ITEMS = "lod_core.ui.too_many_items.items";
  private static final String EQUIPMENT = "lod_core.ui.too_many_items.equipment";
  private static final String SORT = "lod_core.ui.too_many_items.sort";

  private static final int DROPPED_ITEM_LIST_SIZE = 5;
  private static final int INVENTORY_ITEM_LIST_SIZE = 7;

  private MenuState menuState = MenuState.LOAD_ITEMS_1;
  private double scrollAccumulator;
  private int mouseX;
  private int mouseY;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  private int dropIndex;
  private int dropScroll;
  private int invIndex;
  private int invScroll;

  private Renderable58 dropUpArrow;
  private Renderable58 dropDownArrow;
  private Renderable58 invUpArrow;
  private Renderable58 invDownArrow;
  private Renderable58 renderable_8011e200;
  private Renderable58 renderable_8011e204;

  private final MenuEntries<Equipment> equipment = new MenuEntries<>();
  private final MenuEntries<ItemStack> items = new MenuEntries<>();
  private final MenuEntries<InventoryEntry> droppedItems = new MenuEntries<>();

  public TooManyItemsScreen() {
    this.addHotkey(I18n.translate("lod_core.ui.too_many_items.sort"), INPUT_ACTION_MENU_SORT, this::sortMenuState9);
  }

  @Override
  protected void render() {
    switch(this.menuState) {
      case LOAD_ITEMS_1 -> {
        loadItemsAndEquipmentForDisplay(this.equipment, this.items, 0x1L);

        // Use a temp inventory to merge stacks where appropriate
        final Inventory temp = new Inventory();
        temp.disableEvents();

        for(final ItemStack item : itemOverflow) {
          temp.give(item);
        }

        for(final ItemStack item : temp) {
          this.droppedItems.add(new MenuEntryStruct04<>(item));
        }

        for(final Equipment equipment : equipmentOverflow) {
          this.droppedItems.add(new MenuEntryStruct04<>(equipment));
        }

        this.menuState = MenuState.INIT_2;
      }

      case INIT_2 -> {
        deallocateRenderables(0xff);
        this.invScroll = 0;
        this.invIndex = 0;
        this.dropScroll = 0;
        this.dropIndex = 0;
        this.menuState = MenuState.INIT_3;
      }

      case INIT_3 -> {
        deallocateRenderables(0);
        this.renderItemLists(true, this.droppedItems.get(this.dropScroll + this.dropIndex).item_00, 0);
        startFadeEffect(2, 10);
        this.menuState = MenuState.RENDER_4;
      }

      case RENDER_4 -> {
        menuStack.pushScreen(new MessageBoxScreen("Too many items. Replace?", 2, result -> this.menuState = result == MessageBoxResult.YES ? MenuState.RENDER_6 : MenuState.DISCARD_10));
        this.menuState = MenuState.REPLACE_5;
      }

      case REPLACE_5 -> this.renderItemLists(false, this.droppedItems.get(this.dropScroll + this.dropIndex).item_00, 0);

      case RENDER_6 -> {
        this.dropScroll = 0;
        this.dropIndex = 0;
        final Renderable58 renderable2 = allocateUiElement(124, 124, 42, this.getSlotY(0));
        this.renderable_8011e200 = renderable2;
        FUN_80104b60(renderable2);
        deallocateRenderables(0);
        this.renderItemLists(true, this.droppedItems.get(this.dropScroll + this.dropIndex).item_00, 0x1);
        this.menuState = MenuState.DROPPED_8;
      }

      case DROPPED_8 -> {
        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.dropScroll > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 10, 42, 171, 81)) {
            playMenuSound(1);
            this.dropScroll--;
            this.renderable_8011e200.y_44 = this.getSlotY(this.dropIndex);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.dropScroll < this.droppedItems.size() - DROPPED_ITEM_LIST_SIZE && MathHelper.inBox(this.mouseX, this.mouseY, 10, 42, 171, 81)) {
            playMenuSound(1);
            this.dropScroll++;
            this.renderable_8011e200.y_44 = this.getSlotY(this.dropIndex);
          }
        }

        this.renderItemLists(false, this.droppedItems.get(this.dropScroll + this.dropIndex).item_00, 0x1);
      }

      case INVENTORY_9 -> {
        final int slotCount = this.getInventoryCount();

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.invScroll > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            playMenuSound(1);
            this.invScroll--;
            this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.invScroll < slotCount - INVENTORY_ITEM_LIST_SIZE && MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            playMenuSound(1);
            this.invScroll++;
            this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
          }
        }

        this.renderItemLists(false, this.droppedItems.get(this.dropScroll + this.dropIndex).item_00, 0x3);
      }

      case DISCARD_10 -> {
        this.renderItemLists(false, this.droppedItems.get(this.dropScroll + this.dropIndex).item_00, 0);

        menuStack.pushScreen(new MessageBoxScreen("Discard extra items?", 2, result -> {
          if(result == MessageBoxResult.YES) {
            for(final MenuEntryStruct04<InventoryEntry> item : this.droppedItems) {
              if(item.item_00 instanceof final Equipment equipment && !equipment.canBeDiscarded()) {
                menuStack.pushScreen(new MessageBoxScreen(I18n.translate(CANNOT_BE_THROWN_AWAY), 0, result1 -> this.menuState = MenuState.RENDER_6));
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
        this.renderItemLists(false, this.droppedItems.get(this.dropScroll + this.dropIndex).item_00, 0);

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          menuStack.popScreen();
          whichMenu_800bdc38 = WhichMenu.UNLOAD;
        }
      }
    }
  }

  private void renderItemLists(final boolean allocate, final InventoryEntry inv, final int mode) {
    if(allocate) {
      renderGlyphs(glyphs_80114548, 0, 0);
      this.dropUpArrow = allocateUiElement(61, 68, 180, this.getSlotY(0));
      this.dropDownArrow = allocateUiElement(53, 60, 180, this.getSlotY(DROPPED_ITEM_LIST_SIZE - 1));
      this.invUpArrow = allocateUiElement(61, 68, 358, this.getSlotY(0));
      this.invDownArrow = allocateUiElement(53, 60, 358, this.getSlotY(INVENTORY_ITEM_LIST_SIZE - 1));
    }

    renderMenuItems(16, 33, this.droppedItems, this.dropScroll, Math.min(DROPPED_ITEM_LIST_SIZE, this.droppedItems.size()), this.dropUpArrow, this.dropDownArrow);

    if((mode & 0x1) != 0 && !allocate) {
      renderString(16, 164, I18n.translate(inv.getDescriptionTranslationKey()), false);
    }

    renderText(I18n.translate(OVERFLOW), 32, 22, UI_TEXT);

    if(inv instanceof ItemStack) {
      this.renderCurrentItemList(allocate, I18n.translate(EQUIPMENT), this.items, mode);
    } else {
      this.renderCurrentItemList(allocate, I18n.translate(ITEMS), this.equipment, mode);
    }
  }

  private void renderCurrentItemList(final boolean allocate, final String text, final MenuEntries<? extends InventoryEntry> entries, final int mode) {
    renderText(text, 210, 22, UI_TEXT);

    if((mode & 0x1) != 0) {
      renderMenuItems(194, 33, entries, this.invScroll, INVENTORY_ITEM_LIST_SIZE, this.invUpArrow, this.invDownArrow);
    }

    if((mode & 0x2) != 0 && this.invScroll + this.invIndex < Math.min(INVENTORY_ITEM_LIST_SIZE, entries.size())) {
      renderString(194, 164, I18n.translate(entries.get(this.invScroll + this.invIndex).getDescriptionTranslationKey()), allocate);
    }
  }

  private int getSlotY(final int slot) {
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
      for(int i = 0; i < Math.min(DROPPED_ITEM_LIST_SIZE, this.droppedItems.size()); i++) {
        if(this.dropIndex != i && MathHelper.inBox(x, y, 9, this.getSlotY(i), 171, 17)) {
          playMenuSound(1);
          this.dropIndex = i;
          this.renderable_8011e200.y_44 = this.getSlotY(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.INVENTORY_9) {
      for(int i = 0; i < Math.min(INVENTORY_ITEM_LIST_SIZE, this.items.size()); i++) {
        if(this.invIndex != i && MathHelper.inBox(x, y, 188, this.getSlotY(i), 171, 17)) {
          playMenuSound(1);
          this.invIndex = i;
          this.renderable_8011e204.y_44 = this.getSlotY(i);
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
        if(MathHelper.inBox(x, y, 9, this.getSlotY(i), 171, 17)) {
          playMenuSound(2);
          this.dropIndex = i;
          this.renderable_8011e200.y_44 = this.getSlotY(i);

          this.selectMenuState8();
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.INVENTORY_9) {
      for(int i = 0; i < Math.min(INVENTORY_ITEM_LIST_SIZE, this.getInventoryCount()); i++) {
        if(MathHelper.inBox(x, y, 188, this.getSlotY(i), 171, 17)) {
          playMenuSound(2);
          this.invIndex = i;
          this.renderable_8011e204.y_44 = this.getSlotY(i);

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

    if(this.menuState != MenuState.DROPPED_8 && this.menuState != MenuState.INVENTORY_9) {
      return InputPropagation.PROPAGATE;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
    return InputPropagation.HANDLED;
  }

  private int getInventoryCount() {
    final int slotCount;
    if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
      slotCount = gameState_800babc8.equipment_1e8.size();
    } else {
      slotCount = gameState_800babc8.items_2e9.getSize();
    }

    return slotCount;
  }

  private void handleDroppedScrollUp() {
    if(this.dropIndex == 0) {
      if(this.dropScroll > 0) {
        this.dropScroll--;
      } else {
        this.dropScroll = Math.max(0, this.droppedItems.size() - DROPPED_ITEM_LIST_SIZE);
        this.dropIndex = Math.min(this.droppedItems.size(), DROPPED_ITEM_LIST_SIZE);
      }
    }
  }

  private void handleDroppedScrollDown() {
    final int slotCount = this.droppedItems.size();

    if(this.dropIndex == DROPPED_ITEM_LIST_SIZE - 1) {
      if(this.dropScroll + this.dropIndex < slotCount - 1) {
        this.dropScroll++;
      } else if(this.dropScroll + this.dropIndex == slotCount - 1 || (this.dropScroll < 1 && this.dropIndex >= slotCount)) {
        this.dropScroll = 0;
        this.dropIndex = -1;
      }
    }
  }

  private void droppedNavigateUp() {
    final int dropCount = this.droppedItems.size();

    if(this.dropScroll + this.dropIndex > 0 || (dropCount > 1 && this.allowWrapY)) {
      playMenuSound(1);
      this.handleDroppedScrollUp();
    }

    this.dropIndex = Math.max(this.dropIndex - 1, 0);
    this.renderable_8011e200.y_44 = this.getSlotY(this.dropIndex);
  }

  private void droppedNavigateDown() {
    final int dropCount = this.droppedItems.size();

    if(this.dropScroll + this.dropIndex < dropCount - 1 || (dropCount > 1 && this.allowWrapY)) {
      playMenuSound(1);
      this.handleDroppedScrollDown();
    }

    this.dropIndex = Math.min(this.dropIndex + 1, DROPPED_ITEM_LIST_SIZE - 1);
    this.renderable_8011e200.y_44 = this.getSlotY(this.dropIndex);
  }

  private void droppedNavigateHome() {
    if(this.dropIndex != 0) {
      playMenuSound(1);
      this.dropIndex = 0;
      this.renderable_8011e200.y_44 = this.getSlotY(this.dropIndex);
    }
  }

  private void droppedNavigateEnd() {
    if(this.dropIndex != this.droppedItems.size() - 1) {
      playMenuSound(1);
      this.dropIndex = this.droppedItems.size() - 1;
      this.renderable_8011e200.y_44 = this.getSlotY(this.dropIndex);
    }
  }

  private void handleInventoryScrollUp() {
    final int slotCount = this.getInventoryCount();

    if(this.invIndex == 0) {
      if(this.invScroll > 0) {
        this.invScroll--;
      } else {
        this.invScroll = Math.max(0, slotCount - INVENTORY_ITEM_LIST_SIZE);
        this.invIndex = Math.min(slotCount, INVENTORY_ITEM_LIST_SIZE);
      }
    }
  }

  private void handleInventoryScrollDown() {
    final int slotCount = this.getInventoryCount();

    if(this.invIndex == INVENTORY_ITEM_LIST_SIZE - 1) {
      if(this.invScroll + this.invIndex < slotCount - 1) {
        this.invScroll++;
      } else if(this.invScroll + this.invIndex == slotCount - 1 || (this.invScroll < 1 && this.invIndex >= slotCount)) {
        this.invScroll = 0;
        this.invIndex = -1;
      }
    }
  }

  private void inventoryNavigateUp() {
    final int slotCount = this.getInventoryCount();

    if(this.invScroll + this.invIndex > 0 || (slotCount > 1 && this.allowWrapY)) {
      playMenuSound(1);
      this.handleInventoryScrollUp();
    }

    this.invIndex = Math.max(this.invIndex - 1, 0);
    this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
  }

  private void inventoryNavigateDown() {
    final int slotCount = this.getInventoryCount();

    if(this.invScroll + this.invIndex < slotCount - 1 || (slotCount > 1 && this.allowWrapY)) {
      playMenuSound(1);
      this.handleInventoryScrollDown();
    }

    this.invIndex = Math.min(this.invIndex + 1, INVENTORY_ITEM_LIST_SIZE - 1);
    this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
  }

  private void inventoryNavigateTop() {
    if(this.invIndex != 0) {
      playMenuSound(1);
      this.invIndex = 0;
      this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
    }
  }

  private void inventoryNavigateBottom() {
    final int slotCount = this.getInventoryCount();

    if(this.invIndex != Math.min(INVENTORY_ITEM_LIST_SIZE, slotCount) - 1) {
      playMenuSound(1);
      this.invIndex = Math.min(INVENTORY_ITEM_LIST_SIZE, slotCount) - 1;
      this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
    }
  }

  private void inventoryNavigatePageUp() {
    if(this.invScroll - (INVENTORY_ITEM_LIST_SIZE - 1) >= 0) {
      playMenuSound(1);
      this.invScroll -= INVENTORY_ITEM_LIST_SIZE - 1;
      this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
    } else if(this.invScroll != 0) {
      playMenuSound(1);
      this.invScroll = 0;
      this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
    }
  }

  private void inventoryNavigatePageDown() {
    final int slotCount;
    if(this.droppedItems.get(this.dropIndex).item_00 instanceof Equipment) {
      slotCount = gameState_800babc8.equipment_1e8.size();
    } else {
      slotCount = gameState_800babc8.items_2e9.getSize();
    }

    if(this.invScroll + (INVENTORY_ITEM_LIST_SIZE - 1) < slotCount - (INVENTORY_ITEM_LIST_SIZE - 1)) {
      playMenuSound(1);
      this.invScroll += INVENTORY_ITEM_LIST_SIZE - 1;
      this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
    } else if(slotCount > INVENTORY_ITEM_LIST_SIZE && this.invScroll != slotCount - INVENTORY_ITEM_LIST_SIZE) {
      playMenuSound(1);
      this.invScroll = slotCount - INVENTORY_ITEM_LIST_SIZE;
      this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
    }
  }

  private void inventoryNavigateHome() {
    if(this.invIndex > 0 || this.invScroll > 0) {
      playMenuSound(1);
      this.invIndex = 0;
      this.invScroll = 0;
      this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
    }
  }

  private void inventoryNavigateEnd() {
    final int slotCount = gameState_800babc8.items_2e9.getSize();

    if(this.invScroll + this.invIndex != slotCount - 1) {
      playMenuSound(1);
      this.invIndex = Math.min(INVENTORY_ITEM_LIST_SIZE - 1, slotCount - 1);
      this.invScroll = slotCount - 1 - this.invIndex;
      this.renderable_8011e204.y_44 = this.getSlotY(this.invIndex);
    }
  }

  private void escapeMenuState8() {
    playMenuSound(3);
    unloadRenderable(this.renderable_8011e200);
    this.menuState = MenuState.DISCARD_10;
  }

  private void selectMenuState8() {
    final Renderable58 renderable3 = allocateUiElement(118, 118, 220, this.getSlotY(0));
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
    if(this.invIndex + this.invScroll > this.items.size() - 1) {
      return;
    }

    final MenuEntryStruct04<InventoryEntry> newItem = this.droppedItems.get(this.dropScroll + this.dropIndex);
    final boolean isItem = newItem.item_00 instanceof ItemStack;

    if(((isItem ? this.items : this.equipment).get(this.invIndex + this.invScroll).flags_02 & 0x6000) != 0) {
      playMenuSound(40);
    } else {
      if(isItem) {
        this.droppedItems.set(this.dropScroll + this.dropIndex, (MenuEntryStruct04<InventoryEntry>)(MenuEntryStruct04)this.items.get(this.invIndex + this.invScroll));
        this.items.set(this.invIndex + this.invScroll, (MenuEntryStruct04<ItemStack>)(MenuEntryStruct04)newItem);
        setInventoryFromDisplay(this.items, gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.getSize());
      } else {
        this.droppedItems.set(this.dropScroll + this.dropIndex, (MenuEntryStruct04<InventoryEntry>)(MenuEntryStruct04)this.equipment.get(this.invIndex + this.invScroll));
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
    if(this.menuState != MenuState.INVENTORY_9) {
      playMenuSound(40);
      return;
    }

    playMenuSound(2);

    if(this.droppedItems.get(this.dropScroll + this.dropIndex).item_00 instanceof Equipment) {
      sortItems(this.equipment, gameState_800babc8.equipment_1e8, gameState_800babc8.equipment_1e8.size(), List.of(LodMod.ITEM_IDS));
    } else {
      sortItems(this.items, gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.getSize(), List.of(LodMod.EQUIPMENT_IDS));
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

      if(action == INPUT_ACTION_MENU_TOP.get()) {
        this.inventoryNavigateTop();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_BOTTOM.get()) {
        this.inventoryNavigateBottom();
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
