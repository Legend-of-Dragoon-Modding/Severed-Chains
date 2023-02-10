package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.inventory.UseItemResponse;
import legend.game.types.ActiveStatsa0;
import legend.game.types.ItemStats0c;
import legend.game.types.LodString;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Renderable58;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static legend.game.SItem.Completely_recovered_8011d534;
import static legend.game.SItem.Detoxified_8011d5c8;
import static legend.game.SItem.Encounter_risk_reduced_8011d594;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.FUN_80107e70;
import static legend.game.SItem.Fear_gone_8011d604;
import static legend.game.SItem.HP_8011d57c;
import static legend.game.SItem.HP_recovered_for_all_8011cfcc;
import static legend.game.SItem.MP_8011d584;
import static legend.game.SItem.MP_recovered_for_all_8011cff8;
import static legend.game.SItem.Nothing_happened_8011d618;
import static legend.game.SItem.Recovered_8011d560;
import static legend.game.SItem.SP_8011d58c;
import static legend.game.SItem.Spirit_recovered_8011d5e0;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.getCharacterPortraitX;
import static legend.game.SItem.getItemSlotY;
import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.initGlyph;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderFourDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.useItemGlyphs_801141fc;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getItemIcon;
import static legend.game.Scus94491BpeSegment_8002.itemCanBeUsedInMenu;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.takeItem;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8002.useItemInMenu;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

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

  private final List<MenuItemStruct04> menuItems = new ArrayList<>();

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
        this.unload.run();
      }
    }
  }

  private void scroll(final int scroll) {
    playSound(1);
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
    renderMenuItems(16, 10, this.menuItems, slotScroll, 5, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);

    if(selectedSlot + slotScroll < this.menuItems.size()) {
      renderString(0, 194, 16, this.menuItems.get(selectedSlot + slotScroll).itemId_00, allocate);
    }

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

    this.menuItems.clear();

    for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
      final int itemId = gameState_800babc8.items_2e9.get(i).get();

      if(itemCanBeUsedInMenu(itemId) != 0) {
        final ItemStats0c itemStats = itemStats_8004f2ac.get(itemId - 0xc0);
        final MenuItemStruct04 item = new MenuItemStruct04();
        item.itemId_00 = itemId;
        item.flags_02 = 0;

        if(itemStats.type_0b.get() == 0x8 && (itemStats.status_08.get() & allStatus) == 0) {
          item.flags_02 = 0x4000;
        }

        this.menuItems.add(item);
      }
    }

    this.menuItems.sort(Comparator.comparingInt(o -> getItemIcon(o.itemId_00)));
    return this.menuItems.size();
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

          this.itemUseFlags = itemCanBeUsedInMenu(this.menuItems.get(this.selectedSlot + this.slotScroll).itemId_00);

          if(this.itemUseFlags != 0 && (this.menuItems.get(this.selectedSlot + this.slotScroll).flags_02 & 0x4000) == 0) {
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
            useItemInMenu(this.useItemResponse, this.menuItems.get(this.selectedSlot + this.slotScroll).itemId_00, characterIndices_800bdbb8.get(this.charSlot).get());
          } else {
            int responseValue = -2;

            for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
              useItemInMenu(this.useItemResponse, this.menuItems.get(this.selectedSlot + this.slotScroll).itemId_00, characterIndices_800bdbb8.get(i).get());

              if(this.useItemResponse.value_04 != -2) {
                responseValue = 0;
              }
            }

            this.useItemResponse.value_04 = responseValue;
          }

          playSound(2);
          takeItem(this.menuItems.get(this.selectedSlot + this.slotScroll).itemId_00);
          this.itemCount = this.getUsableItemsInMenu();
          loadCharacterStats(0);
          this.getItemResponseText(this.useItemResponse);
          menuStack.pushScreen(new MessageBoxScreen(this.useItemResponse.string_08, 0, result -> { }));
          this.loadingStage = 1;
        }
      }
    }
  }

  private void getItemResponseText(final UseItemResponse response) {
    switch(response._00) {
      case 2:
        this.FUN_80104254(HP_8011d57c, response);
        break;

      case 3:
        this.FUN_80104254(HP_recovered_for_all_8011cfcc, response);
        break;

      case 4:
        this.FUN_80104254(MP_8011d584, response);
        break;

      case 5:
        this.FUN_80104254(MP_recovered_for_all_8011cff8, response);
        break;

      case 6:
        this.FUN_80104254(SP_8011d58c, response);
        break;

      case 8:
        response.string_08 = Encounter_risk_reduced_8011d594;
        break;

      case 7:
        final int value = response.value_04;

        if((value & 0x80) != 0) {
          response.string_08 = Detoxified_8011d5c8;
          break;
        }

        if((value & 0x40) != 0) {
          response.string_08 = Spirit_recovered_8011d5e0;
          break;
        }

        if((value & 0x8) != 0) {
          response.string_08 = Fear_gone_8011d604;
          break;
        }

      case 9:
        response.string_08 = Nothing_happened_8011d618;
        break;
    }
  }

  private void FUN_80104254(final LodString a0, final UseItemResponse response) {
    if(response.value_04 == -2) {
      response.string_08 = Nothing_happened_8011d618;
    } else if(response.value_04 == -1) {
      response.string_08 = new LodString(a0.get() + Completely_recovered_8011d534.get());
    } else if(response.value_04 != 0) {
      response.string_08 = new LodString(response.value_04 + a0.get() + Recovered_8011d560.get());
    } else {
      response.string_08 = a0;
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    if(this.loadingStage != 2) {
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

    if(this.loadingStage == 2) {
      switch(key) {
        case GLFW_KEY_ESCAPE -> {
          playSound(3);
          this.loadingStage = 100;
        }

        case GLFW_KEY_UP -> {
          if(this.selectedSlot > 0) {
            playSound(1);
            this.selectedSlot--;
            this.itemHighlight.y_44 = getItemSlotY(this.selectedSlot);
          } else if(this.slotScroll > 0) {
            this.slotScroll--;
            this.itemHighlight.y_44 = getItemSlotY(this.selectedSlot);
          }
        }

        case GLFW_KEY_DOWN -> {
          if((this.selectedSlot + this.slotScroll) < this.itemCount - 1) {
            playSound(1);

            if(this.selectedSlot == 4) {
              this.slotScroll++;
            } else {
              this.selectedSlot++;
            }

            this.itemHighlight.y_44 = getItemSlotY(this.selectedSlot);
          }
        }

        case GLFW_KEY_ENTER, GLFW_KEY_S -> {
          if(this.slotScroll + this.selectedSlot >= this.itemCount) {
            playSound(40);
            break;
          }

          this.itemUseFlags = itemCanBeUsedInMenu(this.menuItems.get(this.selectedSlot + this.slotScroll).itemId_00);

          if(this.itemUseFlags == 0 || (this.menuItems.get(this.selectedSlot + this.slotScroll).flags_02 & 0x4000) != 0) {
            playSound(40);
            break;
          }

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
        }
      }
    } else if(this.loadingStage == 3) {
      switch(key) {
        case GLFW_KEY_LEFT -> {
          if((this.itemUseFlags & 0x2) == 0) {
            playSound(1);

            if(this.charSlot > 0) {
              this.charSlot--;
            }

            this.charHighlight.x_40 = getCharacterPortraitX(this.charSlot) - 3;
          }
        }

        case GLFW_KEY_RIGHT -> {
          if((this.itemUseFlags & 0x2) == 0) {
            playSound(1);

            if(this.charSlot < characterCount_8011d7c4.get() - 1) {
              this.charSlot++;
            }

            this.charHighlight.x_40 = getCharacterPortraitX(this.charSlot) - 3;
          }
        }

        case GLFW_KEY_ESCAPE -> {
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

        case GLFW_KEY_ENTER, GLFW_KEY_S -> {
          if((this.itemUseFlags & 0x2) == 0) {
            useItemInMenu(this.useItemResponse, this.menuItems.get(this.selectedSlot + this.slotScroll).itemId_00, characterIndices_800bdbb8.get(this.charSlot).get());
          } else {
            int responseValue = -2;

            for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
              useItemInMenu(this.useItemResponse, this.menuItems.get(this.selectedSlot + this.slotScroll).itemId_00, characterIndices_800bdbb8.get(i).get());

              if(this.useItemResponse.value_04 != -2) {
                responseValue = 0;
              }
            }

            this.useItemResponse.value_04 = responseValue;
          }

          playSound(2);
          takeItem(this.menuItems.get(this.selectedSlot + this.slotScroll).itemId_00);
          this.itemCount = this.getUsableItemsInMenu();
          loadCharacterStats(0);
          this.getItemResponseText(this.useItemResponse);
          menuStack.pushScreen(new MessageBoxScreen(this.useItemResponse.string_08, 0, result -> { }));
          this.loadingStage = 1;
        }
      }
    }
  }
}
