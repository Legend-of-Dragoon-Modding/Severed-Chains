package legend.game.inventory.screens;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.types.ActiveStatsa0;
import legend.game.types.ItemStats0c;
import legend.game.types.LodString;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Renderable58;
import legend.game.types.UseItemResponse;

import static legend.core.MemoryHelper.getBiFunctionAddress;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.FUN_80107e70;
import static legend.game.SItem._8011cfcc;
import static legend.game.SItem._8011cff8;
import static legend.game.SItem._8011d534;
import static legend.game.SItem._8011d560;
import static legend.game.SItem._8011d57c;
import static legend.game.SItem._8011d584;
import static legend.game.SItem._8011d58c;
import static legend.game.SItem._8011d594;
import static legend.game.SItem._8011d5c8;
import static legend.game.SItem._8011d5e0;
import static legend.game.SItem._8011d604;
import static legend.game.SItem._8011d618;
import static legend.game.SItem._8011d754;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.getCharacterPortraitX;
import static legend.game.SItem.getItemSlotY;
import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.initGlyph;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuItems_8011d7c8;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderFourDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.textLength;
import static legend.game.SItem.useItemGlyphs_801141fc;
import static legend.game.Scus94491BpeSegment.qsort;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.intToStr;
import static legend.game.Scus94491BpeSegment_8002.itemCanBeUsedInMenu;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.takeItem;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8002.useItemInMenu;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class UseItemScreen extends MenuScreen {
  private int loadingStage;
  private double scrollAccumulator;
  private final Runnable unload;

  private int charSlot;
  private int selectedSlot;
  private int slotScroll;
  private int itemCount;
  private int itemUseFlags;
  private final UseItemResponse useItemResponse = new UseItemResponse();
  private Renderable58 itemHighlight;
  private Renderable58 charHighlight;
  private final Renderable58[] _8011d718 = new Renderable58[7];

  public UseItemScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        scriptStartEffect(2, 10);
        this.charSlot = 0;
        this.slotScroll = 0;
        this.selectedSlot = 0;
        this.useItemResponse._00 = 0;
        this.useItemResponse.value_04 = 0;
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);
        renderGlyphs(useItemGlyphs_801141fc, 0, 0);
        this.itemHighlight = allocateUiElement(0x77, 0x77, 42, getItemSlotY(this.selectedSlot));
        FUN_80104b60(this.itemHighlight);
        this.itemCount = this.getUsableItemsInMenu();
        this.renderUseItemMenu(this.selectedSlot, this.slotScroll, 0xff);
        this.loadingStage++;
      }

      // Render menu
      case 2 -> {
        this.renderUseItemMenu(this.selectedSlot, this.slotScroll, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.slotScroll > 0) {
            this.scroll(this.slotScroll - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.slotScroll < this.itemCount - 5) {
            this.scroll(this.slotScroll + 1);
          }
        }
      }

      // Select character on whom to use item
      case 3 -> this.renderUseItemMenu(this.selectedSlot, this.slotScroll, 0);

      // Fade out
      case 100 -> {
        this.renderUseItemMenu(this.selectedSlot, this.slotScroll, 0);

        saveListDownArrow_800bdb98 = null;
        saveListUpArrow_800bdb94 = null;
        scriptStartEffect(1, 10);
        this.loadingStage++;
      }

      // Unload
      case 101 -> {
        this.renderUseItemMenu(this.selectedSlot, this.slotScroll, 0);

        if(_800bb168.get() >= 0xff) {
          this.unload.run();
        }
      }
    }
  }

  private void scroll(final int scroll) {
    this.slotScroll = scroll;
    this.itemHighlight.y_44 = getItemSlotY(this.selectedSlot);
  }

  private void renderUseItemMenu(final int selectedSlot, final int slotScroll, final long a3) {
    final boolean allocate = a3 == 0xff;

    //LAB_80102e48
    for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
      this.renderUseItemCharacterPortrait(getCharacterPortraitX(i) - 5, 120, characterIndices_800bdbb8.get(i).get(), allocate);
    }

    //LAB_80102e88
    if(allocate) {
      allocateUiElement(84, 84, 16, 16);
      saveListUpArrow_800bdb94 = allocateUiElement(61, 68, 180, getItemSlotY(0) + 2);
      saveListDownArrow_800bdb98 = allocateUiElement(53, 60, 180, getItemSlotY(4) + 2);
    }

    //LAB_80102ee8
    renderMenuItems(16, 10, menuItems_8011d7c8, slotScroll, 5, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);
    renderString(0, 194, 16, menuItems_8011d7c8.get(selectedSlot + slotScroll).itemId_00.get(), allocate);
    uploadRenderables();
  }

  @Method(0x80108464L)
  private void renderUseItemCharacterPortrait(final int x, final int y, final int charIndex, final boolean allocate) {
    if(charIndex != -1) {
      FUN_80107e70(x - 4, y - 6, charIndex);

      if(allocate) {
        allocateUiElement(112, 112, x, y).z_3c = 33;

        if(charIndex < 9) {
          final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          initGlyph(renderable, glyph_801142d4);
          renderable.glyph_04 = charIndex;
          renderable.tpage_2c++;
          renderable.z_3c = 33;
          renderable.x_40 = x + 2;
          renderable.y_44 = y + 8;
        }

        //LAB_80108544
        final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);
        renderFourDigitNumber(x + 25, y + 57, stats.hp_04.get(), stats.maxHp_66.get());
        renderFourDigitNumber(x + 25, y + 68, stats.maxHp_66.get());
        renderFourDigitNumber(x + 25, y + 79, stats.mp_06.get());
        renderFourDigitNumber(x + 25, y + 90, stats.maxMp_6e.get());
      }
    }
  }

  private int getUsableItemsInMenu() {
    int allStatus = 0;
    for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
      allStatus |= gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(i).get()).status_10.get();
    }

    for(int i = 0; i < Config.inventorySize(); i++) {
      menuItems_8011d7c8.get(i).itemId_00.set(0xff);
    }

    int count = 0;
    for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
      final int itemId = gameState_800babc8.items_2e9.get(i).get();

      if(itemCanBeUsedInMenu(itemId) != 0) {
        final ItemStats0c itemStats = itemStats_8004f2ac.get(itemId - 0xc0);
        final MenuItemStruct04 s3 = menuItems_8011d7c8.get(count);
        s3.itemId_00.set(itemId);
        s3.itemSlot_01.set(i);
        s3.price_02.set(0);

        if(itemStats.type_0b.get() == 0x8 && (itemStats.status_08.get() & allStatus) == 0) {
          s3.price_02.set(0x4000);
        }

        count++;
      }
    }

    qsort(menuItems_8011d7c8, count, 0x4, getBiFunctionAddress(Scus94491BpeSegment_8002.class, "compareItems", MenuItemStruct04.class, MenuItemStruct04.class, long.class));
    return count;
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    if(this.loadingStage == 2) {
      for(int slot = 0; slot < Math.min(5, this.itemCount - this.slotScroll); slot++) {
        if(this.selectedSlot != slot && MathHelper.inBox(x, y, 33, getItemSlotY(slot), 136, 17)) {
          playSound(1);
          this.selectedSlot = slot;
          this.itemHighlight.y_44 = getItemSlotY(this.selectedSlot);
        }
      }
    } else if(this.loadingStage == 3 && (this.itemUseFlags & 0x2) == 0) {
      for(int slot = 0; slot < characterCount_8011d7c4.get(); slot++) {
        if(this.charSlot != slot && MathHelper.inBox(x, y, getCharacterPortraitX(slot) - 11, 110, 48, 112)) {
          playSound(1);
          this.charSlot = slot;
          this.charHighlight.x_40 = getCharacterPortraitX(this.charSlot) - 3;
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(mods != 0) {
      return;
    }

    if(this.loadingStage == 2) {
      for(int slot = 0; slot < Math.min(5, this.itemCount - this.slotScroll); slot++) {
        if(MathHelper.inBox(x, y, 33, getItemSlotY(slot), 136, 17)) {
          this.selectedSlot = slot;
          this.itemHighlight.y_44 = getItemSlotY(this.selectedSlot);

          this.itemUseFlags = itemCanBeUsedInMenu(menuItems_8011d7c8.get(this.selectedSlot + this.slotScroll).itemId_00.get());

          if(this.itemUseFlags != 0 && (menuItems_8011d7c8.get(this.selectedSlot + this.slotScroll).price_02.get() & 0x4000) == 0) {
            if((this.itemUseFlags & 0x2) != 0) {
              for(int i = 0; i < 7; i++) {
                this._8011d718[i] = allocateUiElement(0x7e, 0x7e, getCharacterPortraitX(i), 110);
                FUN_80104b60(this._8011d718[i]);
              }
            } else {
              this.charHighlight = allocateUiElement(0x7e, 0x7e, getCharacterPortraitX(this.charSlot), 110);
              FUN_80104b60(this.charHighlight);
            }

            playSound(2);
            this.loadingStage = 3;
          } else {
            playSound(40);
          }
        }
      }
    } else if(this.loadingStage == 3) {
      for(int slot = 0; slot < characterCount_8011d7c4.get(); slot++) {
        if(MathHelper.inBox(x, y, getCharacterPortraitX(slot) - 11, 110, 48, 112)) {
          if((this.itemUseFlags & 0x2) == 0) {
            useItemInMenu(this.useItemResponse, menuItems_8011d7c8.get(this.selectedSlot + this.slotScroll).itemId_00.get(), characterIndices_800bdbb8.get(this.charSlot).get());
          } else {
            _8011d754.setu(-2);

            for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
              useItemInMenu(this.useItemResponse, menuItems_8011d7c8.get(this.selectedSlot + this.slotScroll).itemId_00.get(), characterIndices_800bdbb8.get(i).get());

              if(this.useItemResponse.value_04 != -2) {
                _8011d754.setu(0);
              }
            }

            this.useItemResponse.value_04 = (int)_8011d754.get();
          }

          playSound(2);
          takeItem(menuItems_8011d7c8.get(this.selectedSlot + this.slotScroll).itemId_00.get());
          this.itemCount = this.getUsableItemsInMenu();
          loadCharacterStats(0);
          this.FUN_80104324(this.useItemResponse);
          menuStack.pushScreen(new MessageBoxScreen(this.useItemResponse.string_08, 0, result -> { }));
          this.loadingStage = 1;
        }
      }
    }
  }

  private void FUN_80104324(final UseItemResponse response) {
    switch(response._00) {
      case 2:
        this.FUN_80104254(_8011d57c, response);
        break;

      case 3:
        this.FUN_80104254(_8011cfcc, response);
        break;

      case 4:
        this.FUN_80104254(_8011d584, response);
        break;

      case 5:
        this.FUN_80104254(_8011cff8, response);
        break;

      case 6:
        this.FUN_80104254(_8011d58c, response);
        break;

      case 8:
        this.FUN_80103e04(response.string_08, _8011d594);
        break;

      case 7:
        final int value = response.value_04;

        if((value & 0x80) != 0) {
          this.FUN_80103e04(response.string_08, _8011d5c8);
          break;
        }

        if((value & 0x40) != 0) {
          this.FUN_80103e04(response.string_08, _8011d5e0);
          break;
        }

        if((value & 0x8) != 0) {
          this.FUN_80103e04(response.string_08, _8011d604);
          break;
        }

      case 9:
        this.FUN_80103e04(response.string_08, _8011d618);
        break;
    }
  }

  private void FUN_80104254(final LodString a0, final UseItemResponse response) {
    final int value = response.value_04;

    final LodString a0_0;
    final LodString a1_0;
    if(value == -2) {
      a0_0 = response.string_08;
      a1_0 = _8011d618;
    } else if(value == -1) {
      a0_0 = this.FUN_80103e04(response.string_08, a0);
      a1_0 = _8011d534;
    } else if(value != 0) {
      intToStr(value, this.FUN_80103e04(response.string_08, a0));
      a0_0 = response.string_08.slice(textLength(response.string_08));
      a1_0 = _8011d560;
    } else {
      a0_0 = response.string_08;
      a1_0 = a0;
    }

    this.FUN_80103e04(a0_0, a1_0);
  }

  /** TODO I'm pretty sure this is right, by why return the end of the string? */
  private LodString FUN_80103e04(final LodString a0, final LodString a1) {
    int i;
    for(i = 0; i < textLength(a1); i++) {
      a0.charAt(i, a1.charAt(i));
    }

    a0.charAt(i, 0xa0ff);
    return a0.slice(i);
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
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

    if(this.loadingStage == 2) {
      if(key == GLFW_KEY_ESCAPE) {
        playSound(3);
        this.loadingStage = 100;
      }
    } else if(this.loadingStage == 3) {
      if((this.itemUseFlags & 0x2) == 0) {
        unloadRenderable(this.charHighlight);
      } else {
        for(int i = 0; i < 7; i++) {
          unloadRenderable(this._8011d718[i]);
        }
      }

      playSound(3);
      this.loadingStage = 2;
    }
  }
}
