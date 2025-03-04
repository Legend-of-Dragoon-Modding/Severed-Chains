package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.EquipItemResult;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.Item;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.WhichMenu;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.inventory.ShopContentsEvent;
import legend.game.modding.events.inventory.ShopSellPriceEvent;
import legend.game.types.ActiveStatsa0;
import legend.game.types.EquipmentSlot;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.game.types.Shop;
import legend.lodmod.LodMod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.UI_TEXT_DISABLED;
import static legend.game.SItem.UI_TEXT_SELECTED_CENTERED;
import static legend.game.SItem.allocateOneFrameGlyph;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.canEquip;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.equipItem;
import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.glyphs_80114510;
import static legend.game.SItem.initGlyph;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderEightDigitNumber;
import static legend.game.SItem.renderFiveDigitNumber;
import static legend.game.SItem.renderFraction;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderItemIcon;
import static legend.game.SItem.renderString;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.SItem.renderThreeDigitNumberComparison;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.addGold;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.takeEquipment;
import static legend.game.Scus94491BpeSegment_8002.takeItem;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8007.shopId_8007a3b4;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_END;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HOME;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_UP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class ShopScreen extends MenuScreen {
  private static final String Not_enough_money_8011c468 = "Not enough\nmoney";
  private static final String Which_item_do_you_want_to_sell_8011c4e4 = "Which item do you\nwant to sell?";
  private static final String Which_weapon_do_you_want_to_sell_8011c524 = "Which weapon do\nyou want to sell?";
  private static final String Buy_8011c6a4 = "Buy";
  private static final String Sell_8011c6ac = "Sell";
  private static final String Carried_8011c6b8 = "Carried";
  private static final String Leave_8011c6c8 = "Leave";
  private static final String Cannot_be_armed_with_8011c6d4 = "Cannot be armed\nwith";
  private static final String Number_kept_8011c7f4 = "Carrying: ";

  private MenuState menuState = MenuState.INIT_0;
  private MenuState confirmDest;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  private int equipCharIndex;
  private int menuIndex_8011e0dc;
  private int invIndex_8011e0e0;
  private int invScroll_8011e0e4;
  private Renderable58 renderable_8011e0f0;
  private Renderable58 renderable_8011e0f4;
  private Renderable58 selectedMenuOptionRenderablePtr_800bdbe0;
  private Renderable58 selectedMenuOptionRenderablePtr_800bdbe4;
  private Renderable58 charHighlight;

  private final List<ShopEntry<? extends InventoryEntry>> inv = new ArrayList<>();

  /**
   * <ul>
   *   <li>0 - Weapon Shop</li>
   *   <li>1 - Item Shop</li>
   * </ul>
   */
  private int sellType;

  private final Renderable58[] charRenderables = new Renderable58[9];

  private double scrollAccumulator;
  private int mouseX;
  private int mouseY;

  @Override
  protected void render() {
    switch(this.menuState) {
      case INIT_0 -> {
        this.inv.clear();
        loadCharacterStats();
        this.menuIndex_8011e0dc = 0;
        this.invIndex_8011e0e0 = 0;
        this.invScroll_8011e0e4 = 0;
        this.menuState = MenuState.LOAD_ITEMS_1;
      }

      case LOAD_ITEMS_1 -> {
        startFadeEffect(2, 10);

        final Shop shop = REGISTRIES.shop.getEntry(LodMod.id(LodMod.SHOP_IDS[shopId_8007a3b4])).get();

        final List<ShopEntry<InventoryEntry>> shopEntries = new ArrayList<>();

        for(int i = 0; i < shop.getInventoryCount(); i++) {
          final InventoryEntry entry = shop.getItem(i);
          shopEntries.add(new ShopEntry<>(entry, entry.getPrice() * 2));
        }

        final ShopContentsEvent event = EVENTS.postEvent(new ShopContentsEvent(shop, shopEntries));
        this.inv.addAll(event.contents);

        cacheCharacterSlots();

        this.menuState = MenuState.INIT_2;
      }

      case INIT_2 -> {
        deallocateRenderables(0xff);
        renderGlyphs(glyphs_80114510, 0, 0);
        this.selectedMenuOptionRenderablePtr_800bdbe0 = allocateUiElement(0x7a, 0x7a, 49, this.getShopMenuYOffset(this.menuIndex_8011e0dc));
        FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe0);

        for(int charSlot = 0; charSlot < characterCount_8011d7c4; charSlot++) {
          this.charRenderables[charSlot] = this.allocateCharRenderable(this.FUN_8010a818(charSlot), 174, characterIndices_800bdbb8[charSlot]);
        }

        this.renderShopMenu(this.menuIndex_8011e0dc);

        if(!this.inv.isEmpty()) {
          this.renderShopTypeInfo(this.inv.getFirst().item instanceof Item);
        }

        this.menuState = MenuState.RENDER_3;
      }

      case RENDER_3 -> {
        this.renderShopMenu(this.menuIndex_8011e0dc);

        if(!this.inv.isEmpty()) {
          this.renderShopTypeInfo(this.inv.getFirst().item instanceof Item);
        }
      }

      case BUY_4 -> {
        final ShopEntry<? extends InventoryEntry> entry = this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0);

        if(entry.item instanceof Equipment) {
          this.renderEquipmentStatChange((Equipment)entry.item, characterIndices_800bdbb8[this.equipCharIndex]);
        } else {
          this.renderNumberOfItems((Item)entry.item);
        }

        renderString(16, 122, I18n.translate(entry.item.getDescriptionTranslationKey()), false);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.invScroll_8011e0e4 > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.invScroll_8011e0e4 - 1);
          }

          if(entry.item instanceof Equipment) {
            this.FUN_8010a864((Equipment)this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.invScroll_8011e0e4 < this.inv.size() - 6 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.invScroll_8011e0e4 + 1);
          }

          if(entry.item instanceof Equipment) {
            this.FUN_8010a864((Equipment)this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item);
          }
        }

        this.renderMenuEntries(this.inv, this.invScroll_8011e0e4, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc);
      }

      case BUY_SELECT_CHAR_5 -> {
        final ShopEntry<? extends InventoryEntry> equipment = this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0);
        this.renderEquipmentStatChange((Equipment)equipment.item, characterIndices_800bdbb8[this.equipCharIndex]);
        renderString(16, 122, I18n.translate(equipment.item.getDescriptionTranslationKey()), false);
        this.renderMenuEntries(this.inv, this.invScroll_8011e0e4, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc);
      }

      case SELL_10 -> {
        final int count;
        if(this.sellType != 0) {
          renderText(Which_item_do_you_want_to_sell_8011c4e4, 16, 128, UI_TEXT);
          count = gameState_800babc8.items_2e9.size();

          if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 < count) {
            renderString(193, 122, I18n.translate(gameState_800babc8.items_2e9.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).getDescriptionTranslationKey()), false);
          }
        } else {
          renderText(Which_weapon_do_you_want_to_sell_8011c524, 16, 128, UI_TEXT);
          count = gameState_800babc8.equipment_1e8.size();

          if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 < count) {
            renderString(193, 122, I18n.translate(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).getDescriptionTranslationKey()), false);
          }
        }

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.invScroll_8011e0e4 > 0 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            playMenuSound(1);
            this.invScroll_8011e0e4--;

            if(this.sellType == 0) {
              this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.invScroll_8011e0e4 < count - 6 && MathHelper.inBox(this.mouseX, this.mouseY, 138, 16, 220, 104)) {
            playMenuSound(1);
            this.invScroll_8011e0e4++;

            if(this.sellType == 0) {
              this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
            }

            this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
          }
        }

        this.renderSellList(this.invScroll_8011e0e4, this.sellType == 1, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc);
      }

      case _16, _17 -> {
        if(this.menuState == MenuState._16) {
          startFadeEffect(1, 10);
          this.menuState = MenuState._17;
        }

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          this.menuState = this.confirmDest;
        }

        this.renderShopMenu(this.menuIndex_8011e0dc);
        this.renderShopTypeInfo(this.inv.isEmpty() || this.inv.getFirst().item instanceof Item);
      }

      case UNLOAD_19 -> whichMenu_800bdc38 = WhichMenu.UNLOAD;
    }
  }

  private void scroll(final int scroll) {
    playMenuSound(1);
    this.invScroll_8011e0e4 = scroll;
  }

  private int FUN_8010a864(@Nullable final Equipment equipment) {
    int s3 = -1;

    for(int i = 0; i < 7; i++) {
      if(characterIndices_800bdbb8[i] != -1) {
        this.charRenderables[i].y_44 = 174;

        if(equipment != null) {
          if(!canEquip(equipment, characterIndices_800bdbb8[i])) {
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

  private void renderShopMenu(final int selectedMenuItem) {
    renderText(Buy_8011c6a4, 72, this.getShopMenuYOffset(0) + 2, selectedMenuItem != 0 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);
    renderText(Sell_8011c6ac, 72, this.getShopMenuYOffset(1) + 2, selectedMenuItem != 1 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);
    renderText(Carried_8011c6b8, 72, this.getShopMenuYOffset(2) + 2, selectedMenuItem != 2 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);
    renderText(Leave_8011c6c8, 72, this.getShopMenuYOffset(3) + 2, selectedMenuItem != 3 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);
  }

  private void renderShopTypeInfo(final boolean useItemShop) {
    final int size;
    final int current;
    if(useItemShop) {
      size = CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get());
      current = gameState_800babc8.items_2e9.size();
      final Renderable58 renderable = allocateOneFrameGlyph(94, 16, 16);
      renderable.metricsCount = 18; // truncate slash from renderable
    } else {
      size = 255;
      current = gameState_800babc8.equipment_1e8.size();
      final Renderable58 renderable = allocateOneFrameGlyph(95, 16, 16);
      renderable.metricsCount = 18; // truncate slash from renderable
    }

    renderEightDigitNumber(87, 24, gameState_800babc8.gold_94, 0x2);
    renderFraction(135, 36, current, size);
  }

  private void renderEquipmentStatChange(final Equipment equipment, final int charIndex) {
    if(charIndex != -1) {
      final ActiveStatsa0 oldStats = new ActiveStatsa0(stats_800be5f8[charIndex]);

      final Map<EquipmentSlot, Equipment> oldEquipment = new EnumMap<>(gameState_800babc8.charData_32c[charIndex].equipment_14);

      if(equipItem(equipment, charIndex).success) {
        allocateOneFrameGlyph(0x67, 210, 127);
        allocateOneFrameGlyph(0x68, 210, 137);
        allocateOneFrameGlyph(0x69, 210, 147);
        allocateOneFrameGlyph(0x6a, 210, 157);
        final ActiveStatsa0 newStats = stats_800be5f8[charIndex];
        renderThreeDigitNumber(246, 127, newStats.equipmentAttack_88, 0x2);
        renderThreeDigitNumber(246, 137, newStats.equipmentDefence_8c, 0x2);
        renderThreeDigitNumber(246, 147, newStats.equipmentMagicAttack_8a, 0x2);
        renderThreeDigitNumber(246, 157, newStats.equipmentMagicDefence_8e, 0x2);
        allocateOneFrameGlyph(0x6b, 274, 127);
        allocateOneFrameGlyph(0x6b, 274, 137);
        allocateOneFrameGlyph(0x6b, 274, 147);
        allocateOneFrameGlyph(0x6b, 274, 157);
        loadCharacterStats();
        renderThreeDigitNumberComparison(284, 127, oldStats.equipmentAttack_88, newStats.equipmentAttack_88);
        renderThreeDigitNumberComparison(284, 137, oldStats.equipmentDefence_8c, newStats.equipmentDefence_8c);
        renderThreeDigitNumberComparison(284, 147, oldStats.equipmentMagicAttack_8a, newStats.equipmentMagicAttack_8a);
        renderThreeDigitNumberComparison(284, 157, oldStats.equipmentMagicDefence_8e, newStats.equipmentMagicDefence_8e);
      } else {
        renderText(Cannot_be_armed_with_8011c6d4, 228, 137, UI_TEXT);
      }

      gameState_800babc8.charData_32c[charIndex].equipment_14.clear();
      gameState_800babc8.charData_32c[charIndex].equipment_14.putAll(oldEquipment);

      loadCharacterStats();
    }
  }

  private void renderNumberOfItems(final Item item) {
    int count = 0;
    for(int i = 0; i < gameState_800babc8.items_2e9.size(); i++) {
      if(gameState_800babc8.items_2e9.get(i) == item) {
        count++;
      }
    }
    renderText(Number_kept_8011c7f4 + count, 195, 125, UI_TEXT);
  }

  private void renderSellList(final int firstItem, final boolean isItemShop, final Renderable58 upArrow, final Renderable58 downArrow) {
    if(isItemShop) {
      int i;
      for(i = 0; firstItem + i < gameState_800babc8.items_2e9.size() && i < 6; i++) {
        final Item item = gameState_800babc8.items_2e9.get(firstItem + i);
        renderItemIcon(item.getIcon(), 151, this.menuEntryY(i), 0x8);
        renderText(I18n.translate(item), 168, this.menuEntryY(i) + 2, UI_TEXT);

        final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(shopId_8007a3b4, item, item.getPrice()));
        this.FUN_801069d0(324, this.menuEntryY(i) + 4, event.price);
      }

      downArrow.setVisible(firstItem + 6 <= gameState_800babc8.items_2e9.size() - 1);
    } else {
      int i;
      for(i = 0; firstItem + i < gameState_800babc8.equipment_1e8.size() && i < 6; i++) {
        final Equipment equipment = gameState_800babc8.equipment_1e8.get(firstItem + i);
        renderItemIcon(equipment.icon_0e, 151, this.menuEntryY(i), 0x8);
        renderText(I18n.translate(equipment), 168, this.menuEntryY(i) + 2, equipment.canBeDiscarded() ? UI_TEXT : UI_TEXT_DISABLED);

        if(equipment.canBeDiscarded()) {
          final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(shopId_8007a3b4, equipment, equipment.getPrice()));
          renderFiveDigitNumber(322, this.menuEntryY(i) + 4, event.price);
        } else {
          renderItemIcon(ItemIcon.WARNING, 330, this.menuEntryY(i), 0x8).clut_30 = 0x7eaa;
        }
      }

      downArrow.setVisible(firstItem + 6 <= gameState_800babc8.equipment_1e8.size() - 1);
    }

    upArrow.setVisible(firstItem != 0);

    this.renderShopTypeInfo(isItemShop);
  }

  private void renderMenuEntries(final List<ShopEntry<? extends InventoryEntry>> list, final int startItemIndex, final Renderable58 upArrow, final Renderable58 downArrow) {
    int i;
    for(i = 0; i < Math.min(6, list.size() - startItemIndex); i++) {
      final ShopEntry<? extends InventoryEntry> item = list.get(startItemIndex + i);
      renderText(I18n.translate(item.item.getNameTranslationKey()), 168, this.menuEntryY(i) + 2, UI_TEXT);
      renderFiveDigitNumber(324, this.menuEntryY(i) + 4, item.price);
      renderItemIcon(item.item.getIcon(), 151, this.menuEntryY(i), 0x8);
    }

    upArrow.setVisible(startItemIndex != 0);
    downArrow.setVisible(i + startItemIndex < list.size());

    this.renderShopTypeInfo(list.get(startItemIndex + this.invIndex_8011e0e0).item instanceof Item);
  }

  private Renderable58 allocateCharRenderable(final int x, final int y, final int glyph) {
    if(glyph >= 9) {
      return null;
    }

    final Renderable58 s0 = allocateRenderable(uiFile_800bdc3c.portraits_cfac(), null);
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
    this.renderNumber(x, y, value, 4);
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

  @Method(0x8010a808L)
  public int menuEntryY(final int index) {
    return index * 17 + 18;
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    this.mouseX = x;
    this.mouseY = y;

    if(this.menuState == MenuState.RENDER_3) {
      for(int i = 0; i < 4; i++) {
        if(this.menuIndex_8011e0dc != i && MathHelper.inBox(x, y, 41, this.getShopMenuYOffset(i), 59, 16)) {
          playMenuSound(1);
          this.menuIndex_8011e0dc = i;

          this.invScroll_8011e0e4 = 0;
          this.invIndex_8011e0e0 = 0;
          this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_4) {
      for(int i = 0; i < Math.min(6, this.inv.size() - this.invScroll_8011e0e4); i++) {
        if(this.invIndex_8011e0e0 != i && MathHelper.inBox(this.mouseX, this.mouseY, 138, this.menuEntryY(i) - 2, 220, 17)) {
          playMenuSound(1);
          this.invIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(i);

          if(this.inv.get(this.invScroll_8011e0e4 + i).item instanceof final Equipment equipment) {
            this.equipCharIndex = this.FUN_8010a864(equipment);
          }

          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_SELECT_CHAR_5) {
      for(int i = 0; i < characterCount_8011d7c4; i++) {
        if(this.equipCharIndex != i && MathHelper.inBox(x, y, this.FUN_8010a818(i) - 9, 174, 50, 48)) {
          playMenuSound(1);
          this.equipCharIndex = i;
          this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.SELL_10) {
      final int count = this.sellType != 0 ? gameState_800babc8.items_2e9.size() : gameState_800babc8.equipment_1e8.size();

      for(int i = 0; i < Math.min(count, 6); i++) {
        if(this.invIndex_8011e0e0 != i && MathHelper.inBox(this.mouseX, this.mouseY, 138, this.menuEntryY(i), 220, 17)) {
          playMenuSound(1);
          this.invIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(i);

          if(this.sellType == 0) {
            this.equipCharIndex = this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + i));
          }

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

    if(this.menuState == MenuState.RENDER_3) {
      for(int i = 0; i < 4; i++) {
        if(MathHelper.inBox(x, y, 41, this.getShopMenuYOffset(i), 59, 16)) {
          playMenuSound(2);
          this.menuIndex_8011e0dc = i;

          this.invScroll_8011e0e4 = 0;
          this.invIndex_8011e0e0 = 0;
          this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(i);

          this.handleSelectedMenu(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_4) {
      for(int i = 0; i < Math.min(6, this.inv.size() - this.invScroll_8011e0e4); i++) {
        if(MathHelper.inBox(this.mouseX, this.mouseY, 138, this.menuEntryY(i) - 2, 220, 17)) {
          playMenuSound(2);
          this.invIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(i);

          final ShopEntry<? extends InventoryEntry> inv = this.inv.get(this.invScroll_8011e0e4 + i);
          final boolean hasSpace;
          if(inv.item instanceof final Equipment equipment) {
            this.equipCharIndex = this.FUN_8010a864(equipment);
            hasSpace = gameState_800babc8.equipment_1e8.size() < 255;
          } else {
            hasSpace = gameState_800babc8.items_2e9.size() < CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get());
          }

          if(!hasSpace) {
            menuStack.pushScreen(new MessageBoxScreen("Cannot carry any more", 0, result -> { }));
          } else if(gameState_800babc8.gold_94 < inv.price) {
            menuStack.pushScreen(new MessageBoxScreen(Not_enough_money_8011c468, 0, result -> { }));
          } else {
            if(inv.item instanceof final Item item) {
              menuStack.pushScreen(new MessageBoxScreen("Buy item?", 2, result -> {
                if(result == MessageBoxResult.YES) {
                  if(giveItem(item)) {
                    gameState_800babc8.gold_94 -= inv.price;
                  } else {
                    menuStack.pushScreen(new MessageBoxScreen("Cannot carry any more", 0, onResult -> { }));
                  }
                }
              }));
            } else {
              this.charHighlight = allocateUiElement(0x83, 0x83, this.FUN_8010a818(this.equipCharIndex), 174);
              FUN_80104b60(this.charHighlight);
              this.menuState = MenuState.BUY_SELECT_CHAR_5;
            }
          }

          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_SELECT_CHAR_5) {
      for(int i = 0; i < characterCount_8011d7c4; i++) {
        if(MathHelper.inBox(x, y, this.FUN_8010a818(i) - 9, 174, 50, 48)) {
          playMenuSound(2);
          this.equipCharIndex = i;
          this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);

          menuStack.pushScreen(new MessageBoxScreen("Buy item?", 2, result -> {
            if(result == MessageBoxResult.YES) {
              if(canEquip((Equipment)this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item, characterIndices_800bdbb8[this.equipCharIndex])) {
                menuStack.pushScreen(new MessageBoxScreen("Equip item?", 2, result1 -> {
                  if(result1 == MessageBoxResult.YES) {
                    final EquipItemResult equipResult = equipItem((Equipment)this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item, characterIndices_800bdbb8[this.equipCharIndex]);

                    if(equipResult.previousEquipment != null) {
                      if(equipResult.success) {
                        if(giveEquipment(equipResult.previousEquipment)) {
                          gameState_800babc8.gold_94 -= this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).price;
                        } else {
                          equipItem(equipResult.previousEquipment, characterIndices_800bdbb8[this.equipCharIndex]);
                          menuStack.pushScreen(new MessageBoxScreen("Cannot carry any more", 0, onResult -> {}));
                        }
                      } else {
                        equipItem(equipResult.previousEquipment, characterIndices_800bdbb8[this.equipCharIndex]);
                        menuStack.pushScreen(new MessageBoxScreen("Failed to equip new item", 0, onResult -> {}));
                      }
                    }
                  } else {
                    this.giveUnequipped(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
                  }

                  this.menuState = MenuState.BUY_4;
                  unloadRenderable(this.charHighlight);
                  this.charHighlight = null;
                }));
              } else {
                this.giveUnequipped(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
                this.menuState = MenuState.BUY_4;
                unloadRenderable(this.charHighlight);
                this.charHighlight = null;
              }
            }
          }));

          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.SELL_10) {
      final int count = this.sellType != 0 ? gameState_800babc8.items_2e9.size() : gameState_800babc8.equipment_1e8.size();

      for(int i = 0; i < Math.min(count, 6); i++) {
        if(MathHelper.inBox(this.mouseX, this.mouseY, 138, this.menuEntryY(i), 220, 17)) {
          this.invIndex_8011e0e0 = i;
          this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(i);

          final int slot = this.invScroll_8011e0e4 + this.invIndex_8011e0e0;
          if(this.sellType != 0 && slot >= gameState_800babc8.items_2e9.size() || this.sellType == 0 && (slot >= gameState_800babc8.equipment_1e8.size() || !gameState_800babc8.equipment_1e8.get(slot).canBeDiscarded())) {
            playMenuSound(40);
          } else {
            playMenuSound(2);

            menuStack.pushScreen(new MessageBoxScreen("Sell item?", 2, result -> {
              if(Objects.requireNonNull(result) == MessageBoxResult.YES) {
                final InventoryEntry inv;
                final boolean success;
                if(this.sellType != 0) {
                  inv = gameState_800babc8.items_2e9.get(slot);
                  success = takeItem(slot);
                } else {
                  inv = gameState_800babc8.equipment_1e8.get(slot);
                  success = takeEquipment(slot);
                }

                if(success) {
                  final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(shopId_8007a3b4, inv, inv.getPrice()));
                  addGold(event.price);

                  if(this.invScroll_8011e0e4 > 0 && this.invScroll_8011e0e4 + 6 > count - 1) {
                    this.invScroll_8011e0e4--;
                  }

                  if(this.invIndex_8011e0e0 != 0 && this.invIndex_8011e0e0 > count - 2) {
                    this.invIndex_8011e0e0--;
                    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
                  }
                }
              }
            }));
          }

          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  protected void handleSelectedMenu(final int i) {
    switch(i) {
      case 0 -> { // Buy
        if(this.inv.isEmpty()) {
          menuStack.pushScreen(new MessageBoxScreen("This shop has nothing\nto buy", 0, result -> {}));
          return;
        }

        this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, this.menuEntryY(this.invIndex_8011e0e0));
        FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);

        if(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item instanceof final Equipment equipment) {
          this.equipCharIndex = this.FUN_8010a864(equipment);
        }

        this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, this.menuEntryY(0));
        this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, this.menuEntryY(5));
        this.menuState = MenuState.BUY_4;
      }

      case 1 -> // Sell
        menuStack.pushScreen(new MessageBoxScreen("What do you want to sell?", "Armed", "Items", 2, result -> {
          switch(result) {
            case YES -> {
              this.invIndex_8011e0e0 = 0;
              this.invScroll_8011e0e4 = 0;
              this.sellType = 0;

              if(!gameState_800babc8.equipment_1e8.isEmpty()) {
                this.menuState = MenuState.SELL_10;
                this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, this.menuEntryY(0));
                this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, this.menuEntryY(0));
                this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, this.menuEntryY(5));
                FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);
                this.FUN_8010a864(gameState_800babc8.equipment_1e8.getFirst());
              } else {
                menuStack.pushScreen(new MessageBoxScreen("You have no equipment\nto sell", 0, result1 -> {}));
              }
            }

            case NO -> {
              this.sellType = 1;
              this.invScroll_8011e0e4 = 0;
              this.invIndex_8011e0e0 = 0;

              if(!gameState_800babc8.items_2e9.isEmpty()) {
                this.menuState = MenuState.SELL_10;
                this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, this.menuEntryY(0));
                this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, this.menuEntryY(5));
                this.selectedMenuOptionRenderablePtr_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, this.menuEntryY(0));
                FUN_80104b60(this.selectedMenuOptionRenderablePtr_800bdbe4);
              } else {
                menuStack.pushScreen(new MessageBoxScreen("You have no items\nto sell", 0, result1 -> {
                }));
              }
            }
          }
        }));

      case 2 -> // Carried
        menuStack.pushScreen(new ItemListScreen(() -> {
          menuStack.popScreen();
          startFadeEffect(2, 10);
          this.menuState = MenuState.INIT_2;
        }));

      case 3 -> // Leave
        this.FUN_8010a844(MenuState.UNLOAD_19);
    }
  }

  @Override
  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    if(super.mouseScrollHighRes(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState != MenuState.BUY_4 && this.menuState != MenuState.SELL_10) {
      return InputPropagation.PROPAGATE;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
    return InputPropagation.HANDLED;
  }

  private void menuMainShopRender3Escape() {
    playMenuSound(3);
    this.FUN_8010a844(MenuState.UNLOAD_19);
  }

  private void menuMainShopRender3Select() {
    playMenuSound(2);
    this.handleSelectedMenu(this.menuIndex_8011e0dc);
  }

  private void menuMainShopRender3NavigateUp() {
    if(this.menuIndex_8011e0dc > 0) {
      playMenuSound(1);
      this.menuIndex_8011e0dc--;
    } else {
      playMenuSound(1);
      this.menuIndex_8011e0dc = 3;
    }

    this.invScroll_8011e0e4 = 0;
    this.invIndex_8011e0e0 = 0;
    this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(this.menuIndex_8011e0dc);
  }

  private void menuMainShopRender3NavigateDown() {
    if(this.menuIndex_8011e0dc < 3) {
      playMenuSound(1);
      this.menuIndex_8011e0dc++;
    } else {
      playMenuSound(1);
      this.menuIndex_8011e0dc = 0;
    }

    this.invScroll_8011e0e4 = 0;
    this.invIndex_8011e0e0 = 0;
    this.selectedMenuOptionRenderablePtr_800bdbe0.y_44 = this.getShopMenuYOffset(this.menuIndex_8011e0dc);
  }

  private void menuBuy4Escape() {
    playMenuSound(3);
    this.menuState = MenuState.INIT_2;
  }

  private void menuBuy4Select() {
    playMenuSound(2);

    final ShopEntry<? extends InventoryEntry> inv = this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0);

    final boolean hasSpace;
    if(inv.item instanceof Equipment) {
      hasSpace = gameState_800babc8.equipment_1e8.size() < 255;
    } else {
      hasSpace = gameState_800babc8.items_2e9.size() < CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get());
    }

    if(!hasSpace) {
      menuStack.pushScreen(new MessageBoxScreen("Cannot carry anymore", 0, result -> { }));
    } else if(gameState_800babc8.gold_94 < inv.price) {
      menuStack.pushScreen(new MessageBoxScreen(Not_enough_money_8011c468, 0, result -> { }));
    } else if(inv.item instanceof final Item item) {
      menuStack.pushScreen(new MessageBoxScreen("Buy item?", 2, result -> {
        if(result == MessageBoxResult.YES) {
          if(giveItem(item)) {
            gameState_800babc8.gold_94 -= inv.price;
          } else {
            menuStack.pushScreen(new MessageBoxScreen("Cannot carry any more", 0, onResult -> { }));
          }
        }
      }));
    } else {
      this.charHighlight = allocateUiElement(0x83, 0x83, this.FUN_8010a818(this.equipCharIndex), 174);
      FUN_80104b60(this.charHighlight);
      this.menuState = MenuState.BUY_SELECT_CHAR_5;
    }
  }

  private void menuBuy4NavigateUp() {
    final int invCount = this.inv.size();
    if(this.invIndex_8011e0e0 > 0) {
      playMenuSound(1);
      this.invIndex_8011e0e0--;
    } else if(this.invScroll_8011e0e4 > 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4--;
    } else if(invCount > 1 && this.allowWrapY) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = invCount > 5 ? 5 : invCount - 1;
      this.invScroll_8011e0e4 = invCount > 6 ? invCount - 6 : 0;
    }

    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);

    if(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item instanceof final Equipment equipment) {
      this.equipCharIndex = this.FUN_8010a864(equipment);
    }
  }

  private void menuBuy4NavigateDown() {
    final int invCount = this.inv.size();
    if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 < invCount - 1) {
      playMenuSound(1);
      if(this.invIndex_8011e0e0 < 5) {
        this.invIndex_8011e0e0++;
      } else {
        this.invScroll_8011e0e4++;
      }
    } else if(invCount > 1 && this.allowWrapY) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = 0;
      this.invScroll_8011e0e4 = 0;
    }

    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);

    if(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item instanceof final Equipment equipment) {
      this.equipCharIndex = this.FUN_8010a864(equipment);
    }
  }

  private void menuBuy4NavigatePageUp() {
    if(this.invScroll_8011e0e4 - 6 >= 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4 -= 6;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    } else if(this.invScroll_8011e0e4 != 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4 = 0;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }

    if(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item instanceof final Equipment equipment) {
      this.equipCharIndex = this.FUN_8010a864(equipment);
    }
  }

  private void menuBuy4NavigatePageDown() {
    final int invCount = this.inv.size();
    if((this.invScroll_8011e0e4 + this.invIndex_8011e0e0) + 6 < invCount - 7) {
      playMenuSound(1);
      this.invScroll_8011e0e4 += 6;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    } else if(invCount > 6 && this.invScroll_8011e0e4 != invCount - 6) {
      playMenuSound(1);
      this.invScroll_8011e0e4 = invCount - 6;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }

    if(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item instanceof final Equipment equipment) {
      this.equipCharIndex = this.FUN_8010a864(equipment);
    }
  }

  private void menuBuy4NavigateHome() {
    if(this.invIndex_8011e0e0 > 0 || this.invScroll_8011e0e4 > 0) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = 0;
      this.invScroll_8011e0e4 = 0;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);

      if(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item instanceof final Equipment equipment) {
        this.equipCharIndex = this.FUN_8010a864(equipment);
      }
    }
  }

  private void menuBuy4NavigateEnd() {
    final int invCount = this.inv.size();
    if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 != invCount - 1) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = Math.min(5, invCount - 1);
      this.invScroll_8011e0e4 = invCount - 1 - this.invIndex_8011e0e0;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);

      if(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item instanceof final Equipment equipment) {
        this.equipCharIndex = this.FUN_8010a864(equipment);
      }
    }
  }

  private void menuSelectChar5Escape() {
    playMenuSound(3);
    this.menuState = MenuState.BUY_4;
    unloadRenderable(this.charHighlight);
    this.charHighlight = null;
  }

  private void menuSelectChar5Select() {
    playMenuSound(2);

    menuStack.pushScreen(new MessageBoxScreen("Buy item?", 2, result -> {
      if(result == MessageBoxResult.YES) {
        if(canEquip((Equipment)this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item, characterIndices_800bdbb8[this.equipCharIndex])) {
          menuStack.pushScreen(new MessageBoxScreen("Equip item?", 2, result1 -> {
            if(result1 == MessageBoxResult.YES) {
              final EquipItemResult equipResult = equipItem((Equipment)this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).item, characterIndices_800bdbb8[this.equipCharIndex]);

              if(equipResult.previousEquipment != null) {
                if(equipResult.success) {
                  if(giveEquipment(equipResult.previousEquipment)) {
                    gameState_800babc8.gold_94 -= this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).price;
                  } else {
                    equipItem(equipResult.previousEquipment, characterIndices_800bdbb8[this.equipCharIndex]);
                    menuStack.pushScreen(new MessageBoxScreen("Cannot carry any more", 0, onResult -> {}));
                  }
                } else {
                  equipItem(equipResult.previousEquipment, characterIndices_800bdbb8[this.equipCharIndex]);
                  menuStack.pushScreen(new MessageBoxScreen("Failed to equip new item", 0, onResult -> {}));
                }
              }
            } else {
              this.giveUnequipped(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
            }

            this.menuState = MenuState.BUY_4;
            unloadRenderable(this.charHighlight);
            this.charHighlight = null;
          }));
        } else {
          this.giveUnequipped(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
          this.menuState = MenuState.BUY_4;
          unloadRenderable(this.charHighlight);
          this.charHighlight = null;
        }
      }
    }));
  }

  private void giveUnequipped(final ShopEntry<? extends InventoryEntry> shopEntry) {
    if(giveEquipment((Equipment)shopEntry.item)) {
      gameState_800babc8.gold_94 -= shopEntry.price;
    } else {
      menuStack.pushScreen(new MessageBoxScreen("Cannot carry any more", 0, onResult -> { }));
    }
  }

  private void menuSelectChar5NavigateLeft() {
    playMenuSound(1);

    if(this.equipCharIndex > 0) {
      this.equipCharIndex--;
    } else {
      this.equipCharIndex = characterCount_8011d7c4 - 1;
    }

    this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
  }

  private void menuSelectChar5NavigateRight() {
    playMenuSound(1);

    if(this.equipCharIndex < characterCount_8011d7c4 - 1) {
      this.equipCharIndex++;
    } else {
      this.equipCharIndex = 0;
    }

    this.charHighlight.x_40 = this.FUN_8010a818(this.equipCharIndex);
  }

  private void menuSell10Escape() {
    playMenuSound(3);
    unloadRenderable(this.selectedMenuOptionRenderablePtr_800bdbe4);
    this.menuState = MenuState.INIT_2;
  }

  private void menuSell10Select() {
    final int slot = this.invScroll_8011e0e4 + this.invIndex_8011e0e0;
    if(this.sellType != 0 && slot >= gameState_800babc8.items_2e9.size() || this.sellType == 0 && (slot >= gameState_800babc8.equipment_1e8.size() || !gameState_800babc8.equipment_1e8.get(slot).canBeDiscarded())) {
      playMenuSound(40);
    } else {
      playMenuSound(2);

      menuStack.pushScreen(new MessageBoxScreen("Sell item?", 2, result -> {
        if(Objects.requireNonNull(result) == MessageBoxResult.YES) {
          final InventoryEntry inv;
          final boolean taken;
          final int count;
          if(this.sellType != 0) {
            inv = gameState_800babc8.items_2e9.get(slot);
            taken = takeItem(slot);
            count = gameState_800babc8.items_2e9.size();
          } else {
            inv = gameState_800babc8.equipment_1e8.get(slot);
            taken = takeEquipment(slot);
            count = gameState_800babc8.equipment_1e8.size();
          }

          if(taken) {
            final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(shopId_8007a3b4, inv, inv.getPrice()));
            addGold(event.price);

            if(this.invScroll_8011e0e4 > 0 && this.invScroll_8011e0e4 + 6 > count) {
              this.invScroll_8011e0e4--;
            }

            if(this.invIndex_8011e0e0 != 0 && this.invIndex_8011e0e0 > count - 1) {
              this.invIndex_8011e0e0--;
              this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
            }
          }
        }
      }));
    }
  }

  private void menuSell10NavigateUp() {
    final int itemCount;
    if(this.sellType == 0) { // equipment
      itemCount = gameState_800babc8.equipment_1e8.size();
    } else { // items
      itemCount = gameState_800babc8.items_2e9.size();
    }

    if(this.invIndex_8011e0e0 > 0) {
      playMenuSound(1);
      this.invIndex_8011e0e0--;
    } else if(this.invScroll_8011e0e4 > 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4--;
    } else if(itemCount > 1 && this.allowWrapY) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = itemCount > 5 ? 5 : itemCount - 1;
      this.invScroll_8011e0e4 = itemCount > 6 ? itemCount - 6 : 0;
    }

    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);

    if(this.sellType == 0) {
      this.equipCharIndex = this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
    }
  }

  private void menuSell10NavigateDown() {
    final int itemCount;
    if(this.sellType == 0) { // equipment
      itemCount = gameState_800babc8.equipment_1e8.size();
    } else { // items
      itemCount = gameState_800babc8.items_2e9.size();
    }

    if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 < itemCount - 1) {
      playMenuSound(1);
      if(this.invIndex_8011e0e0 < 5) {
        this.invIndex_8011e0e0++;
      } else {
        this.invScroll_8011e0e4++;
      }
    } else if(itemCount > 1 && this.allowWrapY) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = 0;
      this.invScroll_8011e0e4 = 0;
    }

    this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);

    if(this.sellType == 0) {
      this.equipCharIndex = this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
    }
  }

  private void menuSell10NavigatePageUp() {
    if(this.invScroll_8011e0e4 - 6 >= 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4 -= 6;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    } else if(this.invScroll_8011e0e4 != 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4 = 0;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }

    if(this.sellType == 0) {
      this.equipCharIndex = this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
    }
  }

  private void menuSell10NavigatePageDown() {
    final int itemCount;
    if(this.sellType == 0) { // equipment
      itemCount = gameState_800babc8.equipment_1e8.size();
    } else { // items
      itemCount = gameState_800babc8.items_2e9.size();
    }

    if((this.invScroll_8011e0e4 + this.invIndex_8011e0e0) + 6 < itemCount - 7) {
      playMenuSound(1);
      this.invScroll_8011e0e4 += 6;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    } else if(itemCount > 6 && this.invScroll_8011e0e4 != itemCount - 6) {
      playMenuSound(1);
      this.invScroll_8011e0e4 = itemCount - 6;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }

    if(this.sellType == 0) {
      this.equipCharIndex = this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
    }
  }

  private void menuSell10NavigateHome() {
    if(this.invIndex_8011e0e0 > 0 || this.invScroll_8011e0e4 > 0) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = 0;
      this.invScroll_8011e0e4 = 0;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);

      if(this.sellType == 0) {
        this.equipCharIndex = this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
      }
    }
  }

  private void menuSell10NavigateEnd() {
    final int itemCount;
    if(this.sellType == 0) { // equipment
      itemCount = gameState_800babc8.equipment_1e8.size();
    } else { // items
      itemCount = gameState_800babc8.items_2e9.size();
    }

    if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 != itemCount - 1) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = Math.min(5, itemCount - 1);
      this.invScroll_8011e0e4 = itemCount - 1 - this.invIndex_8011e0e0;
      this.selectedMenuOptionRenderablePtr_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);

      if(this.sellType == 0) {
        this.equipCharIndex = this.FUN_8010a864(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
      }
    }
  }

  @Override
  public InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    switch(this.menuState) {
      case RENDER_3 -> {
        if(action == INPUT_ACTION_MENU_UP.get()) {
          this.menuMainShopRender3NavigateUp();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_DOWN.get()) {
          this.menuMainShopRender3NavigateDown();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
          this.menuMainShopRender3Escape();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
          this.menuMainShopRender3Select();
          return InputPropagation.HANDLED;
        }
      }

      case BUY_4 -> {
        if(action == INPUT_ACTION_MENU_HOME.get()) {
          this.menuBuy4NavigateHome();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_END.get()) {
          this.menuBuy4NavigateEnd();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_PAGE_UP.get()) {
          this.menuBuy4NavigatePageUp();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_PAGE_DOWN.get()) {
          this.menuBuy4NavigatePageDown();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_UP.get()) {
          this.menuBuy4NavigateUp();
          this.allowWrapY = false;
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_DOWN.get()) {
          this.menuBuy4NavigateDown();
          this.allowWrapY = false;
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
          this.menuBuy4Escape();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
          this.menuBuy4Select();
          return InputPropagation.HANDLED;
        }
      }

      case BUY_SELECT_CHAR_5 -> {
        if(action == INPUT_ACTION_MENU_LEFT.get()) {
          this.menuSelectChar5NavigateLeft();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_RIGHT.get()) {
          this.menuSelectChar5NavigateRight();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
          this.menuSelectChar5Escape();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
          this.menuSelectChar5Select();
          return InputPropagation.HANDLED;
        }
      }

      case SELL_10 -> {
        if(action == INPUT_ACTION_MENU_HOME.get()) {
          this.menuSell10NavigateHome();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_END.get()) {
          this.menuSell10NavigateEnd();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_PAGE_UP.get()) {
          this.menuSell10NavigatePageUp();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_PAGE_DOWN.get()) {
          this.menuSell10NavigatePageDown();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_UP.get()) {
          this.menuSell10NavigateUp();
          this.allowWrapY = false;
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_DOWN.get()) {
          this.menuSell10NavigateDown();
          this.allowWrapY = false;
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
          this.menuSell10Escape();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
          this.menuSell10Select();
          return InputPropagation.HANDLED;
        }
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

  public static class ShopEntry<T extends InventoryEntry> {
    public final T item;
    public int price;

    public ShopEntry(final T item, final int price) {
      this.item = item;
      this.price = price;
    }
  }

  public enum MenuState {
    INIT_0,
    LOAD_ITEMS_1,
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
