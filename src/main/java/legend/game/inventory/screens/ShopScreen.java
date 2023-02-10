package legend.game.inventory.screens;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.memory.Memory;
import legend.game.inventory.WhichMenu;
import legend.game.types.ActiveStatsa0;
import legend.game.types.LodString;
import legend.game.types.MenuItemStruct04;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;

import java.util.Arrays;
import java.util.Objects;

import static legend.core.GameEngine.MEMORY;
import static legend.game.SItem.Buy_8011c6a4;
import static legend.game.SItem.Cannot_be_armed_with_8011c6d4;
import static legend.game.SItem.Cannot_carry_anymore_8011c43c;
import static legend.game.SItem.Carried_8011c6b8;
import static legend.game.SItem.FUN_801038d4;
import static legend.game.SItem.FUN_80103b10;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.FUN_8010a808;
import static legend.game.SItem.Leave_8011c6c8;
import static legend.game.SItem.Not_enough_money_8011c468;
import static legend.game.SItem.Number_kept_8011c7f4;
import static legend.game.SItem.Sell_8011c6ac;
import static legend.game.SItem.Which_item_do_you_want_to_sell_8011c4e4;
import static legend.game.SItem.Which_weapon_do_you_want_to_sell_8011c524;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.canEquip;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.equipItem;
import static legend.game.SItem.equipment_8011972c;
import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.glyphs_80114510;
import static legend.game.SItem.initGlyph;
import static legend.game.SItem.itemPrices_80114310;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderCentredText;
import static legend.game.SItem.renderEightDigitNumber;
import static legend.game.SItem.renderFiveDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderItemIcon;
import static legend.game.SItem.renderNumber;
import static legend.game.SItem.renderString;
import static legend.game.SItem.renderText;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.SItem.renderThreeDigitNumberComparison;
import static legend.game.SItem.renderTwoDigitNumber;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.SMap.shops_800f4930;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.addGold;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getItemIcon;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.intToStr;
import static legend.game.Scus94491BpeSegment_8002.itemCantBeDiscarded;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.recalcInventory;
import static legend.game.Scus94491BpeSegment_8002.takeEquipment;
import static legend.game.Scus94491BpeSegment_8002.takeItem;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8004.loadingGameStateOverlay_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8007.shopId_8007a3b4;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameOverMcq_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class ShopScreen extends MenuScreen {
  private MenuState menuState = MenuState.INIT_0;
  private MenuState confirmDest;

  private int equipCharIndex;
  private int menuIndex_8011e0dc;
  private int menuIndex_8011e0e0;
  private int menuScroll_8011e0e4;
  private Renderable58 renderable_8011e0f0;
  private Renderable58 renderable_8011e0f4;
  private Renderable58 selectedMenuOptionRenderablePtr_800bdbe0;
  private Renderable58 selectedMenuOptionRenderablePtr_800bdbe4;
  private Renderable58 charHighlight;

  private final MenuItemStruct04[] menuItems = new MenuItemStruct04[17];

  private int itemCount;
  /**
   * <ul>
   *   <li>0 - Item Shop</li>
   *   <li>1 - Weapon Shop</li>
   * </ul>
   */
  private int shopType;
  /**
   * <ul>
   *   <li>0 - Item Shop</li>
   *   <li>1 - Weapon Shop</li>
   * </ul>
   */
  private int shopType2;

  private final Renderable58[] charRenderables = new Renderable58[9];

  private double scrollAccumulator;
  private int mouseX;
  private int mouseY;

  @Override
  protected void render() {
    switch(this.menuState) {
      case INIT_0 -> {
        Arrays.setAll(this.menuItems, i -> new MenuItemStruct04());
        loadCharacterStats(0);
        this.menuIndex_8011e0dc = 0;
        this.menuIndex_8011e0e0 = 0;
        this.menuScroll_8011e0e4 = 0;
        this.menuState = MenuState.AWAIT_INIT_1;
      }

      case AWAIT_INIT_1 -> {
        if(!drgn0_6666FilePtr_800bdc3c.isNull()) {
          scriptStartEffect(2, 10);
          this.menuState = MenuState.INIT_2;
        }
      }

      case INIT_2 -> {
        deallocateRenderables(0xff);
        renderGlyphs(glyphs_80114510, 0, 0);
        this.selectedMenuOptionRenderablePtr_800bdbe0 = allocateUiElement(0x7a, 0x7a, 49, this.getShopMenuYOffset(this.menuIndex_8011e0dc));
        FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe0);
        this.itemCount = 0;

        for(int i = 0; i < 16; i++) {
          final int menuItemIndex = this.itemCount;
          final int itemId = shops_800f4930.get(shopId_8007a3b4.get()).item_00.get(menuItemIndex).id_01.get();

          if(itemId != 0xff) {
            final MenuItemStruct04 menuItem = this.menuItems[menuItemIndex];
            menuItem.itemId_00 = itemId;
            menuItem.flags_02 = itemPrices_80114310.get(itemId).get() * 2;
            this.itemCount++;
          } else {
            final MenuItemStruct04 menuItem = this.menuItems[i];
            menuItem.itemId_00 = 0xff;
            menuItem.flags_02 = 0;
          }
        }

        final MenuItemStruct04 menuItem = this.menuItems[16];
        menuItem.itemId_00 = 0xff;
        menuItem.flags_02 = 0;
        recalcInventory();
        FUN_80103b10();

        for(int charSlot = 0; charSlot < characterCount_8011d7c4.get(); charSlot++) {
          this.charRenderables[charSlot] = this.allocateCharRenderable(this.FUN_8010a818(charSlot), 174, characterIndices_800bdbb8.get(charSlot).get());
        }

        this.shopType = shops_800f4930.get(shopId_8007a3b4.get()).shopType_00.get() & 1;
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
        this.menuState = MenuState.RENDER_3;
      }

      case RENDER_3 -> this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);

      case BUY_4 -> {
        if(this.shopType == 0) {
          this.renderEquipmentStatChange(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00, characterIndices_800bdbb8.get(this.equipCharIndex).get());
        } else {
          this.renderNumberOfItems(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00);
        }

        renderString(0, 16, 122, this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00, false);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.menuScroll_8011e0e4 > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.menuScroll_8011e0e4 - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.menuScroll_8011e0e4 < this.itemCount - 6 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.menuScroll_8011e0e4 + 1);
          }
        }

        this.FUN_8010c458(this.menuScroll_8011e0e4, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
      }

      case BUY_SELECT_CHAR_5 -> {
        this.renderEquipmentStatChange(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00, characterIndices_800bdbb8.get(this.equipCharIndex).get());
        renderString(0, 16, 122, this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00, false);
        this.FUN_8010c458(this.menuScroll_8011e0e4, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
      }

      case SELL_10 -> {
        final int count;
        if(this.shopType2 != 0) {
          renderText(Which_item_do_you_want_to_sell_8011c4e4, 16, 128, 4);
          renderString(0, 193, 122, gameState_800babc8.items_2e9.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).get(), false);
          count = gameState_800babc8.itemCount_1e6.get();
        } else {
          renderText(Which_weapon_do_you_want_to_sell_8011c524, 16, 128, 4);
          renderString(0, 193, 122, gameState_800babc8.equipment_1e8.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).get(), false);
          count = gameState_800babc8.equipmentCount_1e4.get();
        }

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.menuScroll_8011e0e4 > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            playSound(1);
            this.menuScroll_8011e0e4--;

            if(this.shopType2 == 0) {
              this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).get());
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(this.menuIndex_8011e0e0);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.menuScroll_8011e0e4 < count - 6 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            playSound(1);
            this.menuScroll_8011e0e4++;

            if(this.shopType2 == 0) {
              this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).get());
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(this.menuIndex_8011e0e0);
          }
        }

        this.renderItemList(this.menuScroll_8011e0e4, this.shopType2, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType2);
      }

      case _16, _17 -> {
        if(this.menuState == MenuState._16) {
          scriptStartEffect(1, 10);
          this.menuState = MenuState._17;
        }

        if(_800bb168.get() >= 0xff) {
          this.menuState = this.confirmDest;
        }

        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
      }

      case UNLOAD_19 -> {
        scriptStartEffect(2, 10);
        deallocateRenderables(0xff);
        free(gameOverMcq_800bdc3c.getPointer());
        if(mainCallbackIndex_8004dd20.get() == 5 && loadingGameStateOverlay_8004dd08.get() == 0) {
          FUN_800e3fac();
        }

        whichMenu_800bdc38 = WhichMenu.UNLOAD_SHOP_MENU_10;
        textZ_800bdf00.set(13);
      }
    }
  }

  private void scroll(final int scroll) {
    this.menuScroll_8011e0e4 = scroll;
  }

  private int FUN_8010a864(final int equipmentId) {
    int s3 = -1;

    for(int i = 0; i < 7; i++) {
      if(characterIndices_800bdbb8.get(i).get() != -1) {
        this.charRenderables[i].y_44 = 174;

        if(equipmentId != 0xff) {
          if(!canEquip(equipmentId, characterIndices_800bdbb8.get(i).get())) {
            this.charRenderables[i].y_44 = 250;
          } else if(s3 == -1) {
            s3 = i;
          }
        }
      }
    }

    if(s3 == -1) {
      s3 = 0;
    }

    return s3;
  }

  private void renderShopMenu(final int selectedMenuItem, final int isItemMenu) {
    renderCentredText(Buy_8011c6a4, 72, this.getShopMenuYOffset(0) + 2, selectedMenuItem != 0 ? 4 : 5);
    renderCentredText(Sell_8011c6ac, 72, this.getShopMenuYOffset(1) + 2, selectedMenuItem != 1 ? 4 : 5);
    renderCentredText(Carried_8011c6b8, 72, this.getShopMenuYOffset(2) + 2, selectedMenuItem != 2 ? 4 : 5);
    renderCentredText(Leave_8011c6c8, 72, this.getShopMenuYOffset(3) + 2, selectedMenuItem != 3 ? 4 : 5);

    if(isItemMenu != 0) {
      renderTwoDigitNumber(105, 36, gameState_800babc8.itemCount_1e6.get(), 0x2L);
      FUN_801038d4(94, 16, 16);
      renderTwoDigitNumber(123, 36, Config.inventorySize(), 0x2L);
    } else {
      renderThreeDigitNumber(93, 36, gameState_800babc8.equipmentCount_1e4.get(), 0x2L);
      FUN_801038d4(95, 16, 16);
      renderThreeDigitNumber(117, 36, 255, 0x2L);
    }

    renderEightDigitNumber(87, 24, gameState_800babc8.gold_94.get(), 0x2L);
    uploadRenderables();
  }

  private void renderEquipmentStatChange(final int equipmentId, final int charIndex) {
    if(charIndex != -1) {
      final Memory.TemporaryReservation tmp = MEMORY.temp(0xa0);
      final ActiveStatsa0 oldStats = new ActiveStatsa0(tmp.get());

      memcpy(oldStats.getAddress(), stats_800be5f8.get(charIndex).getAddress(), 0xa0);

      final int[] oldEquipment = new int[5];
      for(int equipmentSlot = 0; equipmentSlot < 5; equipmentSlot++) {
        oldEquipment[equipmentSlot] = gameState_800babc8.charData_32c.get(charIndex).equipment_14.get(equipmentSlot).get();
      }

      if(equipItem(equipmentId, charIndex) != 0xff) {
        FUN_801038d4(0x67, 210, 127);
        FUN_801038d4(0x68, 210, 137);
        FUN_801038d4(0x69, 210, 147);
        FUN_801038d4(0x6a, 210, 157);
        final ActiveStatsa0 newStats = stats_800be5f8.get(charIndex);
        renderThreeDigitNumber(246, 127, newStats.gearAttack_88.get(), 0x2L);
        renderThreeDigitNumber(246, 137, newStats.gearDefence_8c.get(), 0x2L);
        renderThreeDigitNumber(246, 147, newStats.gearMagicAttack_8a.get(), 0x2L);
        renderThreeDigitNumber(246, 157, newStats.gearMagicDefence_8e.get(), 0x2L);
        FUN_801038d4(0x6b, 274, 127);
        FUN_801038d4(0x6b, 274, 137);
        FUN_801038d4(0x6b, 274, 147);
        FUN_801038d4(0x6b, 274, 157);
        loadCharacterStats(0);
        renderThreeDigitNumberComparison(284, 127, oldStats.gearAttack_88.get(), newStats.gearAttack_88.get());
        renderThreeDigitNumberComparison(284, 137, oldStats.gearDefence_8c.get(), newStats.gearDefence_8c.get());
        renderThreeDigitNumberComparison(284, 147, oldStats.gearMagicAttack_8a.get(), newStats.gearMagicAttack_8a.get());
        renderThreeDigitNumberComparison(284, 157, oldStats.gearMagicDefence_8e.get(), newStats.gearMagicDefence_8e.get());
      } else {
        renderText(Cannot_be_armed_with_8011c6d4, 228, 137, 4);
      }

      for(int equipmentSlot = 0; equipmentSlot < 5; equipmentSlot++) {
        gameState_800babc8.charData_32c.get(charIndex).equipment_14.get(equipmentSlot).set(oldEquipment[equipmentSlot]);
      }

      loadCharacterStats(0);

      tmp.release();
    }
  }

  private void renderNumberOfItems(final int itemId) {
    if(itemId != 0xff) {
      int count = 0;
      for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
        if(gameState_800babc8.items_2e9.get(i).get() == itemId) {
          count++;
        }
      }

      final LodString num = new LodString(11);
      intToStr(count, num);
      renderText(Number_kept_8011c7f4, 228, 137, 4);
      renderText(num, 274, 137, 4);
    }
  }

  private void renderItemList(final int firstItem, final int isItemMenu, final Renderable58 upArrow, final Renderable58 downArrow) {
    if(isItemMenu != 0) {
      int i;
      for(i = 0; gameState_800babc8.items_2e9.get(firstItem + i).get() != 0xff && i < 6; i++) {
        final int itemId = gameState_800babc8.items_2e9.get(firstItem + i).get();
        renderItemIcon(getItemIcon(itemId), 151, FUN_8010a808(i), 0x8L);
        renderText(equipment_8011972c.get(itemId).deref(), 168, FUN_8010a808(i) + 2, !itemCantBeDiscarded(itemId) ? 4 : 6);
        this.FUN_801069d0(324, FUN_8010a808(i) + 4, itemPrices_80114310.get(itemId).get());
      }

      if(gameState_800babc8.items_2e9.get(firstItem + i).get() == 0xff) {
        downArrow.flags_00 |= 0x40;
      } else {
        downArrow.flags_00 &= 0xffff_ffbf;
      }
    } else {
      int i;
      for(i = 0; gameState_800babc8.equipment_1e8.get(firstItem + i).get() != 0xff && i < 6; i++) {
        final int itemId = gameState_800babc8.equipment_1e8.get(firstItem + i).get();
        renderItemIcon(getItemIcon(itemId), 151, FUN_8010a808(i), 0x8L);
        renderText(equipment_8011972c.get(itemId).deref(), 168, FUN_8010a808(i) + 2, !itemCantBeDiscarded(itemId) ? 4 : 6);

        if(itemCantBeDiscarded(itemId)) {
          renderItemIcon(58, 330, FUN_8010a808(i), 0x8L).clut_30 = 0x7eaa;
        } else {
          renderFiveDigitNumber(322, FUN_8010a808(i) + 4, itemPrices_80114310.get(itemId).get());
        }
      }

      if(gameState_800babc8.equipment_1e8.get(firstItem + i).get() == 0xff) {
        downArrow.flags_00 |= 0x40;
      } else {
        downArrow.flags_00 &= 0xffff_ffbf;
      }
    }

    if(firstItem == 0) {
      upArrow.flags_00 |= 0x40;
    } else {
      upArrow.flags_00 &= 0xffff_ffbf;
    }
  }

  private void FUN_8010c458(final int startItemIndex, final Renderable58 a2, final Renderable58 a3) {
    int i;
    for(i = 0; this.menuItems[startItemIndex + i].itemId_00 != 0xff; i++) {
      if(i >= 6) {
        break;
      }

      final MenuItemStruct04 item = this.menuItems[startItemIndex + i];
      renderText(equipment_8011972c.get(item.itemId_00).deref(), 168, FUN_8010a808(i) + 2, 4);
      renderFiveDigitNumber(324, FUN_8010a808(i) + 4, item.flags_02);
      renderItemIcon(getItemIcon(item.itemId_00), 151, FUN_8010a808(i), 0x8L);
    }

    if(startItemIndex != 0) {
      a2.flags_00 &= 0xffff_ffbf;
    } else {
      a2.flags_00 |= 0x40;
    }

    if(this.menuItems[i + startItemIndex].itemId_00 != 0xff) {
      a3.flags_00 &= 0xffff_ffbf;
    } else {
      a3.flags_00 |= 0x40;
    }
  }

  private Renderable58 allocateCharRenderable(final int x, final int y, final int glyph) {
    if(glyph >= 9) {
      return null;
    }

    final Renderable58 s0 = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
    initGlyph(s0, glyph_801142d4);
    s0.tpage_2c++;
    s0.glyph_04 = glyph;
    s0.z_3c = 33;
    s0.x_40 = x;
    s0.y_44 = y;

    return s0;
  }

  private void FUN_801069d0(final int x, final int y, final int value) {
    // I didn't look at this method too closely, this may or may not be right
    renderNumber(x, y, value, 0x2L, 4);
  }

  private int FUN_8010a818(final int slot) {
    return slot * 50 + 17;
  }

  private int getShopMenuYOffset(final int slot) {
    return slot * 16 + 58;
  }

  private void FUN_8010a844(final MenuState nextMenuState) {
    this.menuState = MenuState._16;
    this.confirmDest = nextMenuState;
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    this.mouseX = x;
    this.mouseY = y;

    if(this.menuState == MenuState.RENDER_3) {
      for(int i = 0; i < 4; i++) {
        if(this.menuIndex_8011e0dc != i && MathHelper.inBox(x, y, 41, this.getShopMenuYOffset(i), 59, 16)) {
          playSound(1);
          this.menuIndex_8011e0dc = i;

          this.menuScroll_8011e0e4 = 0;
          this.menuIndex_8011e0e0 = 0;
          this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(i);
        }
      }
    } else if(this.menuState == MenuState.BUY_4) {
      for(int i = 0; i < 6; i++) {
        if(this.menuIndex_8011e0e0 != i && MathHelper.inBox(this.mouseX, this.mouseY, 138, FUN_8010a808(i) - 2, 220, 17)) {
          playSound(1);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(i);

          if(this.shopType == 0) {
            this.equipCharIndex = this.FUN_8010a864(this.menuItems[this.menuScroll_8011e0e4 + i].itemId_00);
          }
        }
      }
    } else if(this.menuState == MenuState.BUY_SELECT_CHAR_5) {
      for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
        if(this.equipCharIndex != i && MathHelper.inBox(x, y, this.FUN_8010a818(i) - 9, 174, 50, 48)) {
          playSound(1);
          this.equipCharIndex = i;
          this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
        }
      }
    } else if(this.menuState == MenuState.SELL_10) {
      for(int i = 0; i < 6; i++) {
        if(this.menuIndex_8011e0e0 != i && MathHelper.inBox(this.mouseX, this.mouseY, 138, FUN_8010a808(i) - 2, 220, 17)) {
          playSound(1);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(i);
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.menuState == MenuState.RENDER_3) {
      for(int i = 0; i < 4; i++) {
        if(MathHelper.inBox(x, y, 41, this.getShopMenuYOffset(i), 59, 16)) {
          playSound(2);
          this.menuIndex_8011e0dc = i;

          this.menuScroll_8011e0e4 = 0;
          this.menuIndex_8011e0e0 = 0;
          this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(i);

          this.handleSelectedMenu(i);
        }
      }
    } else if(this.menuState == MenuState.BUY_4) {
      for(int i = 0; i < 6; i++) {
        if(MathHelper.inBox(this.mouseX, this.mouseY, 138, FUN_8010a808(i) - 2, 220, 17)) {
          playSound(2);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(i);

          if(this.shopType == 0) {
            this.equipCharIndex = this.FUN_8010a864(this.menuItems[this.menuScroll_8011e0e4 + i].itemId_00);
          }

          if(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00 == 0xff) {
            playSound(40);
          } else {
            playSound(2);

            final boolean hasSpace;
            if(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00 < 0xc0) {
              hasSpace = gameState_800babc8.equipmentCount_1e4.get() < 255;
            } else {
              hasSpace = gameState_800babc8.itemCount_1e6.get() < Config.inventorySize();
            }

            if(!hasSpace) {
              menuStack.pushScreen(new MessageBoxScreen(Cannot_carry_anymore_8011c43c, 0, result -> {
              }));
            } else if(gameState_800babc8.gold_94.get() < this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].flags_02) {
              menuStack.pushScreen(new MessageBoxScreen(Not_enough_money_8011c468, 0, result -> {
              }));
            } else {
              if(this.shopType != 0) {
                menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
                  if(result == MessageBoxResult.YES) {
                    gameState_800babc8.gold_94.sub(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].flags_02);
                    giveItem(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00);
                  }
                }));
              } else {
                this.charHighlight = allocateUiElement(0x83, 0x83, this.FUN_8010a818(this.equipCharIndex), 174);
                FUN_80104b60(this.charHighlight);
                this.menuState = MenuState.BUY_SELECT_CHAR_5;
              }
            }
          }
        }
      }
    } else if(this.menuState == MenuState.BUY_SELECT_CHAR_5) {
      for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
        if(MathHelper.inBox(x, y, this.FUN_8010a818(i) - 9, 174, 50, 48)) {
          playSound(2);
          this.equipCharIndex = i;
          this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);

          menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
            if(result == MessageBoxResult.YES) {
              gameState_800babc8.gold_94.sub(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].flags_02);

              menuStack.pushScreen(new MessageBoxScreen(new LodString("Equip item?"), 2, result1 -> {
                if(result1 == MessageBoxResult.YES && canEquip(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00, characterIndices_800bdbb8.get(this.equipCharIndex).get())) {
                  giveItem(equipItem(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00, characterIndices_800bdbb8.get(this.equipCharIndex).get()));
                } else {
                  giveItem(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00);
                }

                this.menuState = MenuState.BUY_4;
                unloadRenderable(this.charHighlight);
                this.charHighlight = null;
              }));
            }
          }));
        }
      }
    } else if(this.menuState == MenuState.SELL_10) {
      for(int i = 0; i < 6; i++) {
        if(MathHelper.inBox(this.mouseX, this.mouseY, 138, FUN_8010a808(i) - 2, 220, 17)) {
          playSound(2);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(i);

          final int scroll = this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0;
          //TODO not sure if this condition is right
          if(this.shopType2 != 0 && gameState_800babc8.items_2e9.get(scroll).get() == 0xffL || this.shopType2 == 0 && (gameState_800babc8.equipment_1e8.get(scroll).get() == 0xffL || itemCantBeDiscarded(gameState_800babc8.equipment_1e8.get(scroll).get()))) {
            playSound(40);
          } else {
            playSound(2);

            menuStack.pushScreen(new MessageBoxScreen(new LodString("Sell item?"), 2, result -> {
              if(Objects.requireNonNull(result) == MessageBoxResult.YES) {
                final int itemId;
                final int v0;
                if(this.shopType2 != 0) {
                  itemId = gameState_800babc8.items_2e9.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).get();
                  v0 = takeItem(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);
                } else {
                  itemId = gameState_800babc8.equipment_1e8.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).get();
                  v0 = takeEquipment(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);
                }

                if(v0 == 0) {
                  addGold(itemPrices_80114310.get(itemId).get());
                }
              }
            }));
          }
        }
      }
    }
  }

  protected void handleSelectedMenu(final int i) {
    switch(i) {
      case 0 -> { // Buy
        this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, FUN_8010a808(this.menuIndex_8011e0e0));
        FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);

        if(this.shopType == 0) {
          this.equipCharIndex = this.FUN_8010a864(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00);
        }

        this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, FUN_8010a808(0));
        this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, FUN_8010a808(5));
        this.menuState = MenuState.BUY_4;
      }

      case 1 -> { // Sell
        this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, FUN_8010a808(0));
        this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, FUN_8010a808(5));

        menuStack.pushScreen(new MessageBoxScreen(new LodString("What do you want to sell?"), new LodString("Equipment"), new LodString("Items"), 2, result -> {
          switch(result) {
            case YES -> {
              this.menuIndex_8011e0e0 = 0;
              this.menuScroll_8011e0e4 = 0;
              this.shopType2 = 0;

              if(gameState_800babc8.equipmentCount_1e4.get() != 0) {
                this.menuState = MenuState.SELL_10;
                this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, FUN_8010a808(0));
                FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);
                this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(0).get());
              } else {
                menuStack.pushScreen(new MessageBoxScreen(new LodString("You have no equipment\nto sell"), 0, result1 -> { }));
              }
            }

            case NO -> {
              this.shopType2 = 1;
              this.menuScroll_8011e0e4 = 0;
              this.menuIndex_8011e0e0 = 0;

              if(gameState_800babc8.itemCount_1e6.get() != 0) {
                this.menuState = MenuState.SELL_10;
                this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, FUN_8010a808(0));
                FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);
              } else {
                menuStack.pushScreen(new MessageBoxScreen(new LodString("You have no items\nto sell"), 0, result1 -> {
                }));
              }
            }
          }
        }));
      }

      case 2 -> // Carried
        menuStack.pushScreen(new ItemListScreen(() -> {
          menuStack.popScreen();
          scriptStartEffect(2, 10);
          this.menuState = MenuState.INIT_2;
        }));

      case 3 -> // Leave
        this.FUN_8010a844(MenuState.UNLOAD_19);
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    if(this.menuState != MenuState.BUY_4 && this.menuState != MenuState.SELL_10) {
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

    switch(this.menuState) {
      case RENDER_3 -> {
        switch(key) {
          case GLFW_KEY_UP -> {
            playSound(1);

            if(this.menuIndex_8011e0dc > 0) {
              this.menuIndex_8011e0dc--;
            }

            this.menuScroll_8011e0e4 = 0;
            this.menuIndex_8011e0e0 = 0;
            this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(this.menuIndex_8011e0dc);
          }

          case GLFW_KEY_DOWN -> {
            playSound(1);

            if(this.menuIndex_8011e0dc < 3) {
              this.menuIndex_8011e0dc++;
            }

            this.menuScroll_8011e0e4 = 0;
            this.menuIndex_8011e0e0 = 0;
            this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(this.menuIndex_8011e0dc);
          }

          case GLFW_KEY_ENTER, GLFW_KEY_S -> {
            playSound(2);
            this.handleSelectedMenu(this.menuIndex_8011e0dc);
          }

          case GLFW_KEY_ESCAPE -> {
            playSound(3);
            this.FUN_8010a844(MenuState.UNLOAD_19);
          }
        }
      }

      case BUY_4 -> {
        switch(key) {
          case GLFW_KEY_UP -> {
            playSound(1);

            if(this.menuIndex_8011e0e0 > 0) {
              this.menuIndex_8011e0e0--;
            } else if(this.menuScroll_8011e0e4 > 0) {
              this.menuScroll_8011e0e4--;
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(this.menuIndex_8011e0e0);

            if(this.shopType == 0) {
              this.equipCharIndex = this.FUN_8010a864(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00);
            }
          }

          case GLFW_KEY_DOWN -> {
            playSound(1);

            if(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00 == 0xff) {
              playSound(40);
              break;
            }

            if(this.menuIndex_8011e0e0 < 5 && this.menuIndex_8011e0e0 < this.itemCount - 1) {
              this.menuIndex_8011e0e0++;
            } else if((this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0) < this.itemCount - 1) {
              this.menuScroll_8011e0e4++;
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(this.menuIndex_8011e0e0);

            if(this.shopType == 0) {
              this.equipCharIndex = this.FUN_8010a864(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00);
            }
          }

          case GLFW_KEY_ENTER, GLFW_KEY_S -> {
            if(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00 == 0xff) {
              playSound(40);
            } else {
              playSound(2);

              final boolean hasSpace;
              if(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00 < 0xc0) {
                hasSpace = gameState_800babc8.equipmentCount_1e4.get() < 255;
              } else {
                hasSpace = gameState_800babc8.itemCount_1e6.get() < Config.inventorySize();
              }

              if(!hasSpace) {
                menuStack.pushScreen(new MessageBoxScreen(Cannot_carry_anymore_8011c43c, 0, result -> {
                }));
              } else if(gameState_800babc8.gold_94.get() < this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].flags_02) {
                menuStack.pushScreen(new MessageBoxScreen(Not_enough_money_8011c468, 0, result -> {
                }));
              } else if(this.shopType != 0) {
                menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
                  if(result == MessageBoxResult.YES) {
                    gameState_800babc8.gold_94.sub(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].flags_02);
                    giveItem(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00);
                  }
                }));
              } else {
                this.charHighlight = allocateUiElement(0x83, 0x83, this.FUN_8010a818(this.equipCharIndex), 174);
                FUN_80104b60(this.charHighlight);
                this.menuState = MenuState.BUY_SELECT_CHAR_5;
              }
            }
          }

          case GLFW_KEY_ESCAPE -> {
            playSound(3);
            this.menuState = MenuState.INIT_2;
          }
        }
      }

      case BUY_SELECT_CHAR_5 -> {
        switch(key) {
          case GLFW_KEY_LEFT -> {
            playSound(1);

            if(this.equipCharIndex > 0) {
              this.equipCharIndex--;
            }

            this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
          }

          case GLFW_KEY_RIGHT -> {
            playSound(1);

            if(this.equipCharIndex < characterCount_8011d7c4.get() - 1) {
              this.equipCharIndex++;
            }

            this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
          }

          case GLFW_KEY_ENTER, GLFW_KEY_S -> menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
            if(result == MessageBoxResult.YES) {
              gameState_800babc8.gold_94.sub(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].flags_02);

              menuStack.pushScreen(new MessageBoxScreen(new LodString("Equip item?"), 2, result1 -> {
                if(result1 == MessageBoxResult.YES && canEquip(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00, characterIndices_800bdbb8.get(this.equipCharIndex).get())) {
                  giveItem(equipItem(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00, characterIndices_800bdbb8.get(this.equipCharIndex).get()));
                } else {
                  giveItem(this.menuItems[this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0].itemId_00);
                }

                this.menuState = MenuState.BUY_4;
                unloadRenderable(this.charHighlight);
                this.charHighlight = null;
              }));
            }
          }));

          case GLFW_KEY_ESCAPE -> {
            playSound(3);
            this.menuState = MenuState.BUY_4;
            unloadRenderable(this.charHighlight);
            this.charHighlight = null;
          }
        }
      }

      case SELL_10 -> {
        switch(key) {
          case GLFW_KEY_UP -> {
            playSound(1);

            if(this.menuIndex_8011e0e0 > 0) {
              this.menuIndex_8011e0e0--;
            } else if(this.menuScroll_8011e0e4 > 0) {
              this.menuScroll_8011e0e4--;
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(this.menuIndex_8011e0e0);
          }

          case GLFW_KEY_DOWN -> {
            playSound(1);

            final int itemCount;
            if(this.shopType2 == 0) { // equipment
              itemCount = gameState_800babc8.equipmentCount_1e4.get();
            } else { // items
              itemCount = gameState_800babc8.itemCount_1e6.get();
            }

            if(this.menuIndex_8011e0e0 < 5) {
              this.menuIndex_8011e0e0++;
            } else if((this.menuIndex_8011e0e0 + this.menuScroll_8011e0e4) < itemCount - 1) {
              this.menuScroll_8011e0e4++;
            } else {
              playSound(40);
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(this.menuIndex_8011e0e0);
          }

          case GLFW_KEY_ENTER, GLFW_KEY_S -> {
            final int scroll = this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0;
            //TODO not sure if this condition is right
            if(this.shopType2 != 0 && gameState_800babc8.items_2e9.get(scroll).get() == 0xffL || this.shopType2 == 0 && (gameState_800babc8.equipment_1e8.get(scroll).get() == 0xffL || itemCantBeDiscarded(gameState_800babc8.equipment_1e8.get(scroll).get()))) {
              playSound(40);
            } else {
              playSound(2);

              menuStack.pushScreen(new MessageBoxScreen(new LodString("Sell item?"), 2, result -> {
                if(Objects.requireNonNull(result) == MessageBoxResult.YES) {
                  final int itemId;
                  final int v0;
                  if(this.shopType2 != 0) {
                    itemId = gameState_800babc8.items_2e9.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).get();
                    v0 = takeItem(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);
                  } else {
                    itemId = gameState_800babc8.equipment_1e8.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).get();
                    v0 = takeEquipment(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);
                  }

                  if(v0 == 0) {
                    addGold(itemPrices_80114310.get(itemId).get());
                  }
                }
              }));
            }
          }

          case GLFW_KEY_ESCAPE -> {
            playSound(3);
            unloadRenderable(this.selectedMenuOptionRenderablePtr_800bdbe4);
            this.menuState = MenuState.INIT_2;
          }
        }
      }
    }
  }

  public enum MenuState {
    INIT_0,
    AWAIT_INIT_1,
    INIT_2,
    RENDER_3,
    BUY_4,
    BUY_SELECT_CHAR_5,
    SELL_10,
    _16,
    _17,
    UNLOAD_19,
  }
}
