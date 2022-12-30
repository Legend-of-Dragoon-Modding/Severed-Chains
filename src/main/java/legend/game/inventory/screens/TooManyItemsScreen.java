package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.inventory.WhichMenu;
import legend.game.types.LodString;
import legend.game.types.MenuItemStruct04;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;

import static legend.game.SItem.Acquired_item_8011c2f8;
import static legend.game.SItem.FUN_801038d4;
import static legend.game.SItem.FUN_80104738;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.Press_to_sort_8011d024;
import static legend.game.SItem.This_item_cannot_be_thrown_away_8011c2a8;
import static legend.game.SItem._8011c314;
import static legend.game.SItem._8011c32c;
import static legend.game.SItem._8011dcb8;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.glyphs_80114548;
import static legend.game.SItem.menuItems_8011d7c8;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox_8011dc90;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.renderText;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.FUN_800239e0;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.itemCantBeDiscarded;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.recalcInventory;
import static legend.game.Scus94491BpeSegment_8002.sortItems;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8004.loadingGameStateOverlay_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemiesCount_800bc978;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class TooManyItemsScreen extends MenuScreen {
  private MenuState menuState = MenuState._1;
  private double scrollAccumulator;
  private int mouseX;
  private int mouseY;

  private int newItemIndex;
  private int slotIndex;
  private int slotScroll;

  private Renderable58 renderable_8011e200;
  private Renderable58 renderable_8011e204;

  @Override
  protected void render() {
    switch(this.menuState) {
      case _1 -> {
        if(!drgn0_6666FilePtr_800bdc3c.isNull()) {
          _8011dcb8.get(0).setPointer(mallocTail(0x4c0));
          _8011dcb8.get(1).setPointer(mallocTail(0x4c0));
          recalcInventory();
          FUN_80104738(0x1L);
          messageBox_8011dc90.state_0c = 0;

          for(int itemIndex = 0; itemIndex < 5; itemIndex++) {
            final MenuItemStruct04 item = menuItems_8011d7c8.get(itemIndex);

            final int itemId;
            if(itemIndex >= itemsDroppedByEnemiesCount_800bc978.get()) {
              itemId = 0xff;
            } else {
              itemId = itemsDroppedByEnemies_800bc928.get(itemIndex).get();
            }

            item.itemId_00.set(itemId);
            item.itemSlot_01.set(0);
            item.price_02.set(0);
          }

          this.menuState = MenuState._2;
        }
      }

      case _2 -> {
        deallocateRenderables(0xff);
        this.slotScroll = 0;
        this.slotIndex = 0;
        this.newItemIndex = 0;
        this.menuState = MenuState._3;
      }

      case _3 -> {
        deallocateRenderables(0);
        this.FUN_8010fd80(true, menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get(), this.slotIndex, this.slotScroll, 0);
        scriptStartEffect(2, 10);
        this.menuState = MenuState._4;
      }

      case _4 -> {
        menuStack.pushScreen(new MessageBoxScreen(new LodString("Too many items. Replace?"), 2, result -> this.menuState = result == MessageBoxResult.YES ? MenuState._6 : MenuState._10));
        this.menuState = MenuState._5;
      }

      case _5 ->
        this.FUN_8010fd80(false, menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get(), this.slotIndex, this.slotScroll, 0);

      case _6 -> {
        this.newItemIndex = 0;
        final Renderable58 renderable2 = allocateUiElement(124, 124, 42, this.FUN_8010f178(0));
        this.renderable_8011e200 = renderable2;
        FUN_80104b60(renderable2);
        deallocateRenderables(0);
        this.FUN_8010fd80(true, menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get(), this.slotIndex, this.slotScroll, 0x1L);
        this.menuState = MenuState._8;
      }

      case _8 ->
        this.FUN_8010fd80(false, menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get(), this.slotIndex, this.slotScroll, 0x1L);

      case _9 -> {
        final int slotCount;
        if(menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get() < 0xc0) {
          slotCount = gameState_800babc8.equipmentCount_1e4.get();
        } else {
          slotCount = gameState_800babc8.itemCount_1e6.get();
        }

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            if(this.slotScroll > 0) {
              playSound(1);
              this.slotScroll--;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.slotIndex);
            }
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(MathHelper.inBox(this.mouseX, this.mouseY, 188, 42, 171, 119)) {
            if(this.slotScroll < slotCount - 7) {
              playSound(1);
              this.slotScroll++;
              this.renderable_8011e204.y_44 = this.FUN_8010f178(this.slotIndex);
            }
          }
        }

        this.FUN_8010fd80(false, menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get(), this.slotIndex, this.slotScroll, 0x3L);
      }

      case _10 -> {
        this.FUN_8010fd80(false, menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get(), this.slotIndex, this.slotScroll, 0);

        menuStack.pushScreen(new MessageBoxScreen(new LodString("Discard extra items?"), 2, result -> {
          if(result == MessageBoxResult.YES) {
            for(int i = 0; i < itemsDroppedByEnemiesCount_800bc978.get(); i++) {
              if(itemCantBeDiscarded(menuItems_8011d7c8.get(i).itemId_00.get())) {
                menuStack.pushScreen(new MessageBoxScreen(This_item_cannot_be_thrown_away_8011c2a8, 0, result1 -> this.menuState = MenuState._6));
                return;
              }
            }

            scriptStartEffect(1, 10);
            this.menuState = MenuState._12;
          } else {
            this.menuState = MenuState._6;
          }
        }));

        this.menuState = MenuState._5;
      }

      case _12 -> {
        this.FUN_8010fd80(false, menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get(), this.slotIndex, this.slotScroll, 0);

        if(_800bb168.get() >= 0xff) {
          scriptStartEffect(2, 10);
          free(_8011dcb8.get(0).getPointer());
          free(_8011dcb8.get(1).getPointer());
          deallocateRenderables(0xff);
          free(drgn0_6666FilePtr_800bdc3c.getPointer());
          whichMenu_800bdc38 = WhichMenu.UNLOAD_TOO_MANY_ITEMS_MENU_35;

          if(mainCallbackIndex_8004dd20.get() == 0x5L && loadingGameStateOverlay_8004dd08.get() == 0) {
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

    renderMenuItems(16, 33, menuItems_8011d7c8, 0, Math.min(5, itemsDroppedByEnemiesCount_800bc978.get()), saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);

    if((a4 & 0x1L) != 0 && !allocate) {
      renderString(0, 16, 164, itemId, false);
    }

    renderText(Acquired_item_8011c2f8, 32, 22, 4);

    if(itemId >= 0xc0) {
      if(itemId >= 0xff && (a4 & 0x2L) != 0) {
        final Renderable58 renderable = FUN_801038d4(137, 84, 140);
        renderable.clut_30 = 0x7ceb;
        renderText(Press_to_sort_8011d024, 37, 140, 4);
      }

      renderText(_8011c32c, 210, 22, 4);

      if((a4 & 0x1L) != 0) {
        renderMenuItems(194, 33, _8011dcb8.get(1).deref(), slotScroll, 7, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);
      }

      if((a4 & 0x2L) != 0) {
        renderString(0, 194, 164, _8011dcb8.get(1).deref().get(slotScroll + slotIndex).itemId_00.get(), allocate);

        if((a4 & 0x2L) != 0) {
          final Renderable58 renderable = FUN_801038d4(137, 84, 140);
          renderable.clut_30 = 0x7ceb;
          renderText(Press_to_sort_8011d024, 37, 140, 4);
        }
      }
    } else {
      renderText(_8011c314, 210, 22, 4);

      if((a4 & 0x1L) != 0) {
        renderMenuItems(194, 33, _8011dcb8.get(0).deref(), slotScroll, 7, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);
      }

      if((a4 & 0x2L) != 0) {
        renderString(0, 194, 164, _8011dcb8.get(0).deref().get(slotScroll + slotIndex).itemId_00.get(), allocate);

        if((a4 & 0x2L) != 0) {
          final Renderable58 renderable = FUN_801038d4(137, 84, 140);
          renderable.clut_30 = 0x7ceb;
          renderText(Press_to_sort_8011d024, 37, 140, 4);
        }
      }
    }

    uploadRenderables();
  }

  private int FUN_8010f178(final int slot) {
    return 42 + slot * 17;
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    this.mouseX = x;
    this.mouseY = y;

    if(this.menuState == MenuState._8) {
      for(int i = 0; i < 5; i++) {
        if(this.newItemIndex != i && MathHelper.inBox(x, y, 9, this.FUN_8010f178(i), 171, 17)) {
          playSound(1);
          this.newItemIndex = i;
          this.renderable_8011e200.y_44 = this.FUN_8010f178(i);
        }
      }
    } else if(this.menuState == MenuState._9) {
      for(int i = 0; i < 7; i++) {
        if(this.slotIndex != i && MathHelper.inBox(x, y, 188, this.FUN_8010f178(i), 171, 17)) {
          playSound(1);
          this.slotIndex = i;
          this.renderable_8011e204.y_44 = this.FUN_8010f178(i);
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.menuState == MenuState._8) {
      for(int i = 0; i < 5; i++) {
        if(MathHelper.inBox(x, y, 9, this.FUN_8010f178(i), 171, 17)) {
          playSound(2);
          this.newItemIndex = i;
          this.renderable_8011e200.y_44 = this.FUN_8010f178(i);

          if(menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get() != 0xff) {
            this.slotScroll = 0;
            this.slotIndex = 0;
            final Renderable58 renderable3 = allocateUiElement(118, 118, 220, this.FUN_8010f178(0));
            this.renderable_8011e204 = renderable3;
            FUN_80104b60(renderable3);
            playSound(2);
            this.menuState = MenuState._9;
          } else {
            playSound(40);
          }
        }
      }
    } else if(this.menuState == MenuState._9) {
      for(int i = 0; i < 7; i++) {
        if(MathHelper.inBox(x, y, 188, this.FUN_8010f178(i), 171, 17)) {
          playSound(2);
          this.slotIndex = i;
          this.renderable_8011e204.y_44 = this.FUN_8010f178(i);

          final MenuItemStruct04 newItem = menuItems_8011d7c8.get(this.newItemIndex);
          final int isItem = menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get() >= 0xc0 ? 1 : 0;
          final MenuItemStruct04 existingItem = _8011dcb8.get(isItem).deref().get(this.slotIndex + this.slotScroll);

          if((existingItem.price_02.get() & 0x6000) != 0) {
            playSound(40);
          } else {
            final int itemId = existingItem.itemId_00.get();
            final int itemSlot = existingItem.itemSlot_01.get();
            final int flags = existingItem.price_02.get();

            existingItem.itemId_00.set(newItem.itemId_00.get());
            existingItem.itemSlot_01.set(newItem.itemSlot_01.get());
            existingItem.price_02.set(newItem.price_02.get());

            newItem.itemId_00.set(itemId);
            newItem.itemSlot_01.set(itemSlot);
            newItem.price_02.set(flags);

            playSound(2);
            unloadRenderable(this.renderable_8011e204);
            this.menuState = MenuState._8;

            if(isItem != 0) {
              FUN_800239e0(_8011dcb8.get(1).deref(), gameState_800babc8.items_2e9, gameState_800babc8.itemCount_1e6.get());
            } else {
              FUN_800239e0(_8011dcb8.get(0).deref(), gameState_800babc8.equipment_1e8, gameState_800babc8.equipmentCount_1e4.get());
            }
          }
        }
      }
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    if(this.menuState != MenuState._9) {
      return;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    if(mods != 0) {
      return;
    }

    if(key == GLFW_KEY_ESCAPE) {
      if(this.menuState == MenuState._8) {
        playSound(3);
        unloadRenderable(this.renderable_8011e200);
        this.menuState = MenuState._10;
      } else if(this.menuState == MenuState._9) {
        playSound(3);
        unloadRenderable(this.renderable_8011e204);
        this.menuState = MenuState._8;
      }
    } else if(key == GLFW_KEY_S && this.menuState == MenuState._9) {
      playSound(2);

      if(menuItems_8011d7c8.get(this.newItemIndex).itemId_00.get() < 0xc0) {
        sortItems(_8011dcb8.get(0).deref(), gameState_800babc8.equipment_1e8, gameState_800babc8.equipmentCount_1e4.get());
      } else {
        sortItems(_8011dcb8.get(1).deref(), gameState_800babc8.items_2e9, gameState_800babc8.itemCount_1e6.get());
      }
    }
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
