package legend.game.inventory.screens;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Renderable58;

import static legend.game.SItem.FUN_800fc814;
import static legend.game.SItem.FUN_800fc824;
import static legend.game.SItem.FUN_800fc860;
import static legend.game.SItem.FUN_801038d4;
import static legend.game.SItem.FUN_80104738;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.No_8011c214;
import static legend.game.SItem.Press_to_sort_8011d024;
import static legend.game.SItem.Really_want_to_throw_this_away_8011c8d4;
import static legend.game.SItem.Yes_8011c20c;
import static legend.game.SItem._8011c314;
import static legend.game.SItem._8011c32c;
import static legend.game.SItem._8011d754;
import static legend.game.SItem._8011dcb8;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.goodsGlyphs_801141c4;
import static legend.game.SItem.handleYesNo;
import static legend.game.SItem.renderCentredText;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.renderText;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.SItem.renderTwoDigitNumber;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.FUN_800239e0;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.recalcInventory;
import static legend.game.Scus94491BpeSegment_8002.sortItems;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc20;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class ItemListScreen extends MenuScreen {
  private int loadingStage;
  private double scrollAccumulator;
  private final Runnable unload;

  private int selectedSlotEquipment;
  private int selectedSlotItem;
  private int slotScrollEquipment;
  private int slotScrollItem;
  private int menuIndex;
  private Renderable58 equipmentHighlight;
  private Renderable58 itemHighlight;
  private Renderable58 _800bdb9c;
  private Renderable58 _800bdba0;
  private int mouseX;
  private int mouseY;

  private ArrayRef<UnsignedByteRef> currentList;
  private ArrayRef<MenuItemStruct04> currentDisplayList;
  private int currentIndex;
  private int currentItemId;

  public ItemListScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        scriptStartEffect(2, 10);
        deallocateRenderables(0xff);
        renderGlyphs(goodsGlyphs_801141c4, 0, 0);
        _8011dcb8.get(0).setPointer(mallocTail(0x4c0));
        _8011dcb8.get(1).setPointer(mallocTail(0x4c0));
        recalcInventory();
        this.selectedSlotEquipment = 0;
        this.selectedSlotItem = 0;
        this.slotScrollEquipment = 0;
        this.slotScrollItem = 0;
        this.menuIndex = 0;
        this.currentItemId = 0xff;
        this.equipmentHighlight = allocateUiElement(0x76, 0x76, FUN_800fc824(0), FUN_800fc814(this.selectedSlotEquipment) + 32);
        FUN_80104b60(this.equipmentHighlight);
        this.itemHighlight = allocateUiElement(0x76, 0x76, FUN_800fc824(1), FUN_800fc814(this.selectedSlotItem) + 32);
        FUN_80104b60(this.itemHighlight);
        this.renderItemList(this.slotScrollEquipment, this.slotScrollItem, 0xff, 0xff);
        _8011d754.setu(FUN_80104738(0x1L));
        this.loadingStage++;
      }

      case 1 -> {
        this.renderItemList(this.slotScrollEquipment, this.slotScrollItem, this.currentItemId, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(MathHelper.inBox(this.mouseX, this.mouseY, 8, 40, 174, 122)) {
            if(this.slotScrollEquipment > 0) {
              this.slotScrollEquipment--;
              this.setCurrent(gameState_800babc8.equipment_1e8, _8011dcb8.get(0).deref(), this.slotScrollEquipment + this.selectedSlotEquipment);
            }
          }

          if(MathHelper.inBox(this.mouseX, this.mouseY, 186, 40, 174, 122)) {
            if(this.slotScrollItem > 0) {
              this.slotScrollItem--;
              this.setCurrent(gameState_800babc8.items_2e9, _8011dcb8.get(1).deref(), this.slotScrollItem + this.selectedSlotItem);
            }
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(MathHelper.inBox(this.mouseX, this.mouseY, 8, 40, 174, 122)) {
            if(this.slotScrollEquipment < gameState_800babc8.equipmentCount_1e4.get() + (int)_8011d754.get() - 7) {
              this.slotScrollEquipment++;
              this.setCurrent(gameState_800babc8.equipment_1e8, _8011dcb8.get(0).deref(), this.slotScrollEquipment + this.selectedSlotEquipment);
            }
          }

          if(MathHelper.inBox(this.mouseX, this.mouseY, 186, 40, 174, 122)) {
            if(this.slotScrollItem < gameState_800babc8.itemCount_1e6.get() - 7) {
              this.slotScrollItem++;
              this.setCurrent(gameState_800babc8.items_2e9, _8011dcb8.get(1).deref(), this.slotScrollItem + this.selectedSlotItem);
            }
          }
        }
      }

      case 2 -> {
        renderText(Really_want_to_throw_this_away_8011c8d4, 192, 180, 4);
        renderCentredText(Yes_8011c20c, 328, FUN_800fc860(0) + 2, this.menuIndex == 0 ? 5 : 6);
        renderCentredText(No_8011c214, 328, FUN_800fc860(1) + 2, this.menuIndex == 0 ? 6 : 5);

        final IntRef menuIndex = new IntRef().set(this.menuIndex);
        switch(handleYesNo(menuIndex)) {
          case SCROLLED ->
            renderablePtr_800bdc20.y_44 = FUN_800fc860(this.menuIndex);

          case YES -> {
            int i;
            for(i = this.currentIndex; ; i++) {
              final MenuItemStruct04 a = this.currentDisplayList.get(i);
              final MenuItemStruct04 b = this.currentDisplayList.get(i + 1);
              a.itemId_00.set(b.itemId_00);
              a.itemSlot_01.set(b.itemSlot_01);
              a.price_02.set(b.price_02);

              if(b.itemId_00.get() == 0xff) {
                break;
              }
            }

            FUN_800239e0(this.currentDisplayList, this.currentList, i);
            recalcInventory();

            unloadRenderable(renderablePtr_800bdc20);
            this.loadingStage = 1;
          }

          case NO, CANCELLED -> {
            unloadRenderable(renderablePtr_800bdc20);
            this.loadingStage = 1;
          }
        }

        this.menuIndex = menuIndex.get();

        this.renderItemList(this.slotScrollEquipment, this.slotScrollItem, 0xff, 0x1L);
      }

      // Fade out
      case 100 -> {
        this.renderItemList(this.slotScrollEquipment, this.slotScrollItem, this.currentItemId, 0);
        this._800bdba0 = null;

        this._800bdb9c = null;
        saveListDownArrow_800bdb98 = null;
        saveListUpArrow_800bdb94 = null;

        free(_8011dcb8.get(0).getPointer());
        free(_8011dcb8.get(1).getPointer());

        this.unload.run();
      }
    }
  }

  private void setCurrent(final ArrayRef<UnsignedByteRef> list, final ArrayRef<MenuItemStruct04> display, final int index) {
    this.currentList = list;
    this.currentDisplayList = display;
    this.currentIndex = index;
    this.currentItemId = list.get(index).get();
  }

  private void renderItemList(final int slotScroll1, final int slotScroll2, final int itemId, final long a3) {
    renderMenuItems( 16, 33, _8011dcb8.get(0).deref(), slotScroll1, 7, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);
    renderMenuItems(194, 33, _8011dcb8.get(1).deref(), slotScroll2, 7, this._800bdb9c, this._800bdba0);
    renderThreeDigitNumber(136, 24, gameState_800babc8.equipmentCount_1e4.get(), 0x2L);
    renderTwoDigitNumber(326, 24, gameState_800babc8.itemCount_1e6.get(), 0x2L);

    final boolean allocate = a3 == 0xff;
    if(allocate) {
      allocateUiElement(0xb, 0xb, 154, 24);
      renderThreeDigitNumber(160, 24, 0xff);
      allocateUiElement(0xb, 0xb, 338, 24);
      renderTwoDigitNumber(344, 24, Config.inventorySize());
      allocateUiElement(0x55, 0x55, 16, 16);
      saveListUpArrow_800bdb94 = allocateUiElement(0x3d, 0x44, 180, FUN_800fc814(2));
      saveListDownArrow_800bdb98 = allocateUiElement(0x35, 0x3c, 180, FUN_800fc814(8));
      allocateUiElement(0x55, 0x55, 194, 16);
      this._800bdb9c = allocateUiElement(0x3d, 0x44, 358, FUN_800fc814(2));
      this._800bdba0 = allocateUiElement(0x35, 0x3c, 358, FUN_800fc814(8));
    }

    renderText(_8011c314,  32, 22, 4);
    renderText(_8011c32c, 210, 22, 4);

    if(a3 != 1) {
      FUN_801038d4(0x89, 84, 178).clut_30 = 0x7ceb;
      renderText(Press_to_sort_8011d024, 37, 178, 4);
    }

    renderString(0, 194, 178, itemId, allocate);
    uploadRenderables();
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    this.mouseX = x;
    this.mouseY = y;

    if(this.loadingStage == 1) {
      for(int i = 0; i < Math.min(7, gameState_800babc8.equipmentCount_1e4.get()); i++) {
        if(this.selectedSlotEquipment != i && MathHelper.inBox(x, y, 8, 31 + FUN_800fc814(i), 174, 17)) {
          playSound(1);
          this.selectedSlotEquipment = i;
          this.equipmentHighlight.y_44 = FUN_800fc814(i) + 32;
          this.setCurrent(gameState_800babc8.equipment_1e8, _8011dcb8.get(0).deref(), this.slotScrollEquipment + this.selectedSlotEquipment);
        } else if(this.selectedSlotItem != i && MathHelper.inBox(x, y, 186, 31 + FUN_800fc814(i), 174, 17)) {
          playSound(1);
          this.selectedSlotItem = i;
          this.itemHighlight.y_44 = FUN_800fc814(i) + 32;
          this.setCurrent(gameState_800babc8.items_2e9, _8011dcb8.get(1).deref(), this.slotScrollItem + this.selectedSlotItem);
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(mods != 0) {
      return;
    }

    if(this.loadingStage == 1) {
      for(int i = 0; i < Math.min(7, gameState_800babc8.equipmentCount_1e4.get()); i++) {
        if(MathHelper.inBox(x, y, 8, 31 + FUN_800fc814(i), 174, 17)) {
          playSound(1);
          this.selectedSlotEquipment = i;
          this.equipmentHighlight.y_44 = FUN_800fc814(i) + 32;
          this.setCurrent(gameState_800babc8.equipment_1e8, _8011dcb8.get(0).deref(), this.slotScrollEquipment + this.selectedSlotEquipment);

          if((this.currentDisplayList.get(this.currentIndex).price_02.get() & 0x2000) != 0 || this.currentDisplayList.get(this.currentIndex).itemId_00.get() == 0xff) {
            playSound(40);
          } else {
            playSound(2);
            this.menuIndex = 0;
            renderablePtr_800bdc20 = allocateUiElement(0x7d, 0x7d, 314, FUN_800fc860(0));
            FUN_80104b60(renderablePtr_800bdc20);
            this.loadingStage = 2;
          }
        } else if(MathHelper.inBox(x, y, 186, 31 + FUN_800fc814(i), 174, 17)) {
          playSound(1);
          this.selectedSlotItem = i;
          this.itemHighlight.y_44 = FUN_800fc814(i) + 32;
          this.setCurrent(gameState_800babc8.items_2e9, _8011dcb8.get(1).deref(), this.slotScrollItem + this.selectedSlotItem);

          if((this.currentDisplayList.get(this.currentIndex).price_02.get() & 0x2000) != 0 || this.currentDisplayList.get(this.currentIndex).itemId_00.get() == 0xff) {
            playSound(40);
          } else {
            playSound(2);
            this.menuIndex = 0;
            renderablePtr_800bdc20 = allocateUiElement(0x7d, 0x7d, 314, FUN_800fc860(0));
            FUN_80104b60(renderablePtr_800bdc20);
            this.loadingStage = 2;
          }
        }
      }
    }
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    if(mods != 0) {
      return;
    }

    if(this.loadingStage == 1) {
      if(key == GLFW_KEY_ESCAPE) {
        playSound(3);
        this.loadingStage = 100;
      }

      if(key == GLFW_KEY_S) { // Sort items
        playSound(2);
        sortItems(_8011dcb8.get(0).deref(), gameState_800babc8.equipment_1e8, gameState_800babc8.equipmentCount_1e4.get() + (int)_8011d754.get());
        sortItems(_8011dcb8.get(1).deref(), gameState_800babc8.items_2e9, gameState_800babc8.itemCount_1e6.get());
      }
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    if(this.loadingStage != 1) {
      return;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
  }
}
