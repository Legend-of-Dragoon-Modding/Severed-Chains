package legend.game.inventory.screens;

import legend.core.Config;
import legend.core.GameEngine;
import legend.core.MathHelper;
import legend.core.memory.Memory;
import legend.game.inventory.EquipmentSlot;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.types.ActiveStatsa0;
import legend.game.types.LodString;
import legend.game.types.MenuItemStruct04;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
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
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.intToStr;
import static legend.game.Scus94491BpeSegment_8002.playSound;
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
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

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

  private final List<MenuItemStruct04> menuItems = new ArrayList<>();

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

        for(int i = 0; i < 16; i++) {
          final int itemId = shops_800f4930.get(shopId_8007a3b4.get()).item_00.get(i).id_01.get();

          if(itemId != 0xff) {
            final MenuItemStruct04 menuItem = new MenuItemStruct04();
            menuItem.item = GameEngine.REGISTRIES.items.getEntryById(itemId);
            menuItem.flags = itemPrices_80114310.get(itemId).get() * 2;
            this.menuItems.add(menuItem);
          }
        }

        FUN_80103b10();

        for(int charSlot = 0; charSlot < characterCount_8011d7c4.get(); charSlot++) {
          this.charRenderables[charSlot] = this.allocateCharRenderable(this.FUN_8010a818(charSlot), 174, characterIndices_800bdbb8.get(charSlot).get());
        }

        this.shopType = shops_800f4930.get(shopId_8007a3b4.get()).shopType_00.get() & 1;
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
        this.menuState = MenuState.RENDER_3;
      }

      case RENDER_3 ->
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);

      case BUY_4 -> {
        if(this.shopType == 0) {
          this.renderEquipmentStatChange(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item, characterIndices_800bdbb8.get(this.equipCharIndex).get());
        } else {
          this.renderNumberOfItems(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
        }

        renderText(new LodString(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item.description), 16, 122, 4);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.menuScroll_8011e0e4 > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.menuScroll_8011e0e4 - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.menuScroll_8011e0e4 < this.menuItems.size() - 6 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.menuScroll_8011e0e4 + 1);
          }
        }

        this.FUN_8010c458(this.menuScroll_8011e0e4, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
      }

      case BUY_SELECT_CHAR_5 -> {
        this.renderEquipmentStatChange(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item, characterIndices_800bdbb8.get(this.equipCharIndex).get());
        renderText(new LodString(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item.description), 16, 122, 4);
        this.FUN_8010c458(this.menuScroll_8011e0e4, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc, this.shopType);
      }

      case SELL_10 -> {
        final int count;
        if(this.shopType2 != 0) {
          renderText(Which_item_do_you_want_to_sell_8011c4e4, 16, 128, 4);
          renderText(new LodString(gameState_800babc8.items.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).description), 193, 122, 4);
          count = gameState_800babc8.items.size();
        } else {
          renderText(Which_weapon_do_you_want_to_sell_8011c524, 16, 128, 4);
          renderText(new LodString(gameState_800babc8.equipment.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).description), 193, 122, 4);
          count = gameState_800babc8.equipment.size();
        }

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.menuScroll_8011e0e4 > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            playSound(1);
            this.menuScroll_8011e0e4--;

            if(this.shopType2 == 0) {
              this.FUN_8010a864(gameState_800babc8.equipment.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0));
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
              this.FUN_8010a864(gameState_800babc8.equipment.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0));
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
        if(mainCallbackIndex_8004dd20.get() == 0x5L && loadingGameStateOverlay_8004dd08.get() == 0) {
          FUN_800e3fac();
        }

        whichMenu_800bdc38 = WhichMenu.UNLOAD_SHOP_MENU_10;
        textZ_800bdf00.set(13);
      }
    }
  }

  private void scroll(final int scroll) {
    this.menuScroll_8011e0e4 = scroll;

    if(this.shopType == 0) {
      this.equipCharIndex = this.FUN_8010a864(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
      this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
    }
  }

  private int FUN_8010a864(final Item equipment) {
    int s3 = -1;

    for(int i = 0; i < 7; i++) {
      if(characterIndices_800bdbb8.get(i).get() != -1) {
        this.charRenderables[i].y_44 = 174;

        if(!canEquip(equipment, characterIndices_800bdbb8.get(i).get())) {
          this.charRenderables[i].y_44 = 250;
        } else if(s3 == -1) {
          s3 = i;
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
      renderTwoDigitNumber(105, 36, gameState_800babc8.items.size(), 0x2L);
      FUN_801038d4(94, 16, 16);
      renderTwoDigitNumber(123, 36, Config.inventorySize(), 0x2L);
    } else {
      renderThreeDigitNumber(93, 36, gameState_800babc8.equipment.size(), 0x2L);
      FUN_801038d4(95, 16, 16);
      renderThreeDigitNumber(117, 36, 255, 0x2L);
    }

    renderEightDigitNumber(87, 24, gameState_800babc8.gold_94.get(), 0x2L);
    uploadRenderables();
  }

  private void renderEquipmentStatChange(final Item equipment, final int charIndex) {
    if(charIndex != -1) {
      final Memory.TemporaryReservation tmp = MEMORY.temp(0xa0);
      final ActiveStatsa0 oldStats = new ActiveStatsa0(tmp.get());

      memcpy(oldStats.getAddress(), stats_800be5f8.get(charIndex).getAddress(), 0xa0);

      final EnumMap<EquipmentSlot, Item> oldEquipment = new EnumMap<>(EquipmentSlot.class);
      for(final EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
        final Item item = gameState_800babc8.charData_32c.get(charIndex).equipment.get(equipmentSlot);

        if(item != null) {
          oldEquipment.put(equipmentSlot, item);
        }
      }

      if(equipItem(equipment, charIndex) != null || gameState_800babc8.charData_32c.get(charIndex).equipment.get(equipment.getEquipmentSlot()) == null) {
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

      gameState_800babc8.charData_32c.get(charIndex).equipment.clear();
      gameState_800babc8.charData_32c.get(charIndex).equipment.putAll(oldEquipment);
      loadCharacterStats(0);

      tmp.release();
    }
  }

  private void renderNumberOfItems(final Item item) {
    final int count = (int)gameState_800babc8.items.stream().filter(item1 -> item1 == item).count();

    final LodString num = new LodString(11);
    intToStr(count, num);
    renderText(Number_kept_8011c7f4, 228, 137, 4);
    renderText(num, 274, 137, 4);
  }

  private void renderItemList(final int firstItem, final int isItemMenu, final Renderable58 upArrow, final Renderable58 downArrow) {
    if(isItemMenu != 0) {
      int i;
      for(i = 0; i < Math.min(6, gameState_800babc8.items.size() - firstItem); i++) {
        final Item item = gameState_800babc8.items.get(firstItem + i);
        renderItemIcon(item.getIcon(), 151, FUN_8010a808(i), 0x8L);
        renderText(new LodString(item.name), 168, FUN_8010a808(i) + 2, item.canBeDiscarded() ? 4 : 6);
        this.FUN_801069d0(324, FUN_8010a808(i) + 4, item.price);
      }

      if(firstItem + i >= gameState_800babc8.items.size()) {
        downArrow.flags_00 |= 0x40;
      } else {
        downArrow.flags_00 &= 0xffff_ffbf;
      }
    } else {
      int i;
      for(i = 0; i < Math.min(6, gameState_800babc8.equipment.size() - firstItem); i++) {
        final Item item = gameState_800babc8.equipment.get(firstItem + i);
        renderItemIcon(item.getIcon(), 151, FUN_8010a808(i), 0x8L);
        renderText(new LodString(item.name), 168, FUN_8010a808(i) + 2, item.canBeDiscarded() ? 4 : 6);

        if(!item.canBeDiscarded()) {
          renderItemIcon(58, 330, FUN_8010a808(i), 0x8L).clut_30 = 0x7eaa;
        } else {
          renderFiveDigitNumber(322, FUN_8010a808(i) + 4, item.price);
        }
      }

      if(firstItem + i >= gameState_800babc8.equipment.size()) {
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
    for(i = 0; i < Math.min(6, this.menuItems.size() - startItemIndex); i++) {
      final MenuItemStruct04 item = this.menuItems.get(startItemIndex + i);
      renderText(new LodString(item.item.name), 168, FUN_8010a808(i) + 2, 4);
      renderFiveDigitNumber(324, FUN_8010a808(i) + 4, item.flags);
      renderItemIcon(item.item.getIcon(), 151, FUN_8010a808(i), 0x8L);
    }

    if(startItemIndex != 0) {
      a2.flags_00 &= 0xffff_ffbf;
    } else {
      a2.flags_00 |= 0x40;
    }

    if(i + startItemIndex < this.menuItems.size()) {
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
      for(int i = 0; i < Math.min(6, this.menuItems.size() - this.menuScroll_8011e0e4); i++) {
        if(this.menuIndex_8011e0e0 != i && MathHelper.inBox(this.mouseX, this.mouseY, 138, FUN_8010a808(i) - 2, 220, 17)) {
          playSound(1);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(i);

          if(this.shopType == 0) {
            this.equipCharIndex = this.FUN_8010a864(this.menuItems.get(this.menuScroll_8011e0e4 + i).item);
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

          switch(i) {
            case 0 -> { // Buy
              this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, FUN_8010a808(this.menuIndex_8011e0e0));
              FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);

              if(this.shopType == 0) {
                this.equipCharIndex = this.FUN_8010a864(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
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

                    if(!gameState_800babc8.equipment.isEmpty()) {
                      this.menuState = MenuState.SELL_10;
                      this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, FUN_8010a808(0));
                      FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);
                      this.FUN_8010a864(gameState_800babc8.equipment.get(0));
                    } else {
                      menuStack.pushScreen(new MessageBoxScreen(new LodString("You have no equipment\nto sell"), 0, result1 -> { }));
                    }
                  }

                  case NO -> {
                    this.shopType2 = 1;
                    this.menuScroll_8011e0e4 = 0;
                    this.menuIndex_8011e0e0 = 0;

                    if(!gameState_800babc8.items.isEmpty()) {
                      this.menuState = MenuState.SELL_10;
                      this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, FUN_8010a808(0));
                      FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);
                    } else {
                      menuStack.pushScreen(new MessageBoxScreen(new LodString("You have no items\nto sell"), 0, result1 -> { }));
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
      }
    } else if(this.menuState == MenuState.BUY_4) {
      for(int i = 0; i < Math.min(6, this.menuItems.size() - this.menuScroll_8011e0e4); i++) {
        if(MathHelper.inBox(this.mouseX, this.mouseY, 138, FUN_8010a808(i) - 2, 220, 17)) {
          playSound(2);
          this.menuIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = FUN_8010a808(i);

          if(this.shopType == 0) {
            this.equipCharIndex = this.FUN_8010a864(this.menuItems.get(this.menuScroll_8011e0e4 + i).item);
          }

          playSound(2);

          final boolean hasSpace;
          if(this.menuItems.get(this.menuScroll_8011e0e4 + i).item.isEquippable()) {
            hasSpace = gameState_800babc8.equipment.size() < 256;
          } else {
            hasSpace = gameState_800babc8.items.size() < Config.inventorySize();
          }

          if(!hasSpace) {
            menuStack.pushScreen(new MessageBoxScreen(Cannot_carry_anymore_8011c43c, 0, result -> { }));
          } else if(gameState_800babc8.gold_94.get() < this.menuItems.get(this.menuScroll_8011e0e4 + i).flags) {
            menuStack.pushScreen(new MessageBoxScreen(Not_enough_money_8011c468, 0, result -> {
            }));
          } else {
            if(this.shopType != 0) {
              menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
                if(result == MessageBoxResult.YES) {
                  gameState_800babc8.gold_94.sub(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).flags);
                  giveItem(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
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
    } else if(this.menuState == MenuState.BUY_SELECT_CHAR_5) {
      for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
        if(MathHelper.inBox(x, y, this.FUN_8010a818(i) - 9, 174, 50, 48)) {
          playSound(2);
          this.equipCharIndex = i;
          this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);

          menuStack.pushScreen(new MessageBoxScreen(new LodString("Buy item?"), 2, result -> {
            if(result == MessageBoxResult.YES) {
              gameState_800babc8.gold_94.sub(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).flags);

              menuStack.pushScreen(new MessageBoxScreen(new LodString("Equip item?"), 2, result1 -> {
                if(result1 == MessageBoxResult.YES && canEquip(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item, characterIndices_800bdbb8.get(this.equipCharIndex).get())) {
                  giveItem(equipItem(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item, characterIndices_800bdbb8.get(this.equipCharIndex).get()));
                } else {
                  giveItem(this.menuItems.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0).item);
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

          final int scroll = this.menuScroll_8011e0e4 + i;
          //TODO not sure if this condition is right
          if(this.shopType2 != 0 && scroll >= gameState_800babc8.items.size() || this.shopType2 == 0 && (scroll >= gameState_800babc8.equipment.size() || !gameState_800babc8.equipment.get(scroll).canBeDiscarded())) {
            playSound(40);
          } else {
            playSound(2);

            menuStack.pushScreen(new MessageBoxScreen(new LodString("Sell item?"), 2, result -> {
              if(Objects.requireNonNull(result) == MessageBoxResult.YES) {
                final Item item;
                final Item previous;
                if(this.shopType2 != 0) {
                  item = gameState_800babc8.items.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);
                  previous = takeItem(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);
                } else {
                  item = gameState_800babc8.equipment.get(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);
                  previous = takeEquipment(this.menuScroll_8011e0e4 + this.menuIndex_8011e0e0);
                }

                if(previous != null) {
                  addGold(item.price);
                }
              }
            }));
          }
        }
      }
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

    if(key == GLFW_KEY_ESCAPE) {
      if(this.menuState == MenuState.RENDER_3) {
        playSound(3);
        this.FUN_8010a844(MenuState.UNLOAD_19);
      } else if(this.menuState == MenuState.BUY_4) {
        playSound(3);
        this.menuState = MenuState.INIT_2;
      } else if(this.menuState == MenuState.BUY_SELECT_CHAR_5) {
        playSound(3);
        this.menuState = MenuState.BUY_4;
        unloadRenderable(this.charHighlight);
        this.charHighlight = null;
      } else if(this.menuState == MenuState.SELL_10) {
        playSound(3);
        unloadRenderable(this.selectedMenuOptionRenderablePtr_800bdbe4);
        this.menuState = MenuState.INIT_2;
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
