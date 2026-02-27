package legend.game.inventory.screens;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.inventory.WhichMenu;
import legend.game.modding.coremod.shops.EquipmentShopExtension;
import legend.game.modding.coremod.shops.ItemShopExtension;
import legend.game.modding.events.inventory.ShopContentsEvent;
import legend.game.modding.events.inventory.ShopSellEvent;
import legend.game.modding.events.inventory.ShopSellPriceEvent;
import legend.game.types.MenuGlyph06;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.game.types.Shop;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static legend.core.GameEngine.EVENTS;
import static legend.game.sound.Audio.playMenuSound;
import static legend.game.FullScreenEffects.fullScreenEffect_800bb140;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Menus.unloadRenderable;
import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.SItem.initHighlight;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.UI_TEXT_DISABLED;
import static legend.game.SItem.UI_TEXT_SELECTED_CENTERED;
import static legend.game.SItem.addGold;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderFiveDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderString;
import static legend.game.SItem.takeEquipment;
import static legend.game.SItem.takeItemFromSlot;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BOTTOM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_END;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HOME;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_UP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_TOP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class ShopScreen extends MenuScreen {
  private static final MenuGlyph06[] glyphs_80114510 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(96, 40, 56),
    new MenuGlyph06(97, 146, 16),
    new MenuGlyph06(91, 16, 123),
    new MenuGlyph06(91, 194, 123),
    new MenuGlyph06(98, 16, 172),
    new MenuGlyph06(99, 192, 172),
  };

  private final Shop shop;
  private final Object2IntMap<ShopExtension> extensions = new Object2IntOpenHashMap<>();
  private final UnknownShopExtension unknownExtension = new UnknownShopExtension();
  private final ItemShopExtension itemExtension = new ItemShopExtension();
  private final EquipmentShopExtension equipmentExtension = new EquipmentShopExtension();
  private ShopExtension activeExtension;
  private ShopEntry<? extends InventoryEntry> selectedEntry;

  private MenuState menuState = MenuState.INIT_0;
  private MenuState confirmDest;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  private int menuIndex_8011e0dc;
  private int invIndex_8011e0e0;
  private int invScroll_8011e0e4;
  private Renderable58 renderable_8011e0f0;
  private Renderable58 renderable_8011e0f4;
  private Renderable58 selectedMenuOptionRenderable_800bdbe0;
  private Renderable58 selectedInventoryRowRenderable_800bdbe4;

  private final List<ShopEntry<? extends InventoryEntry<?>>> inv = new ArrayList<>();

  /**
   * <ul>
   *   <li>0 - Weapon Shop</li>
   *   <li>1 - Item Shop</li>
   * </ul>
   */
  private int sellType;

  private double scrollAccumulator;

  public ShopScreen(final Shop shop) {
    this.shop = shop;
  }

  @Override
  protected void render() {
    switch(this.menuState) {
      case INIT_0 -> {
        loadCharacterStats();
        cacheCharacterSlots();

        this.inv.clear();
        this.menuIndex_8011e0dc = 0;
        this.invIndex_8011e0e0 = 0;
        this.invScroll_8011e0e4 = 0;

        final GatherShopExtensionsEvent shopExtensionsEvent = EVENTS.postEvent(new GatherShopExtensionsEvent(this, this.shop));
        this.extensions.putAll(shopExtensionsEvent.extensions);
        this.extensions.keySet().forEach(extension -> extension.attach(this, this.shop, gameState_800babc8));

        this.menuState = MenuState.LOAD_ITEMS_1;
      }

      case LOAD_ITEMS_1 -> {
        startFadeEffect(2, 10);

        final List<ShopEntry<InventoryEntry<?>>> shopEntries = new ArrayList<>();

        for(int i = 0; i < this.shop.getInventoryCount(); i++) {
          final InventoryEntry<?> entry = this.shop.getItem(i);
          shopEntries.add(new ShopEntry<>(entry, entry.getBuyPrice()));
        }

        final ShopContentsEvent event = EVENTS.postEvent(new ShopContentsEvent(this.shop, shopEntries));
        this.inv.addAll(event.contents);

        this.menuState = MenuState.INIT_2;
      }

      case INIT_2 -> {
        deallocateRenderables(0xff);
        renderGlyphs(glyphs_80114510, 0, 0);
        this.selectedMenuOptionRenderable_800bdbe0 = allocateUiElement(0x7a, 0x7a, 49, this.getShopMenuYOffset(this.menuIndex_8011e0dc));
        initHighlight(this.selectedMenuOptionRenderable_800bdbe0);

        this.renderShopMenu(this.menuIndex_8011e0dc);
        this.setSelectedEntry(null);
        this.drawShopHeader();

        this.menuState = MenuState.RENDER_3;
      }

      case RENDER_3 -> {
        this.renderShopMenu(this.menuIndex_8011e0dc);
        this.drawShopHeader();
      }

      case BUY_4 -> {
        this.drawBuyMenu();

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.invScroll_8011e0e4 > 0 && MathHelper.inBox((int)this.mouseX, (int)this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.invScroll_8011e0e4 - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.invScroll_8011e0e4 < this.inv.size() - 6 && MathHelper.inBox((int)this.mouseX, (int)this.mouseY, 138, 16, 220, 104)) {
            this.scroll(this.invScroll_8011e0e4 + 1);
          }
        }
      }

      case EXTENSION_5 -> {
        this.drawBuyMenu();

        if(this.activeExtension.shouldReturnControl()) {
          this.menuState = MenuState.BUY_4;
        }
      }

      case SELL_10 -> {
        renderText(I18n.translate("lod_core.ui.shop.what_do_you_want_to_sell"), 16, 128, UI_TEXT);

        final int count;
        if(this.sellType != 0) {
          count = gameState_800babc8.items_2e9.getSize();

          if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 < count) {
            renderString(193, 122, I18n.translate(gameState_800babc8.items_2e9.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).getDescriptionTranslationKey()), false);
          }
        } else {
          count = gameState_800babc8.equipment_1e8.size();

          if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 < count) {
            renderString(193, 122, I18n.translate(gameState_800babc8.equipment_1e8.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0).getDescriptionTranslationKey()), false);
          }
        }

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.invScroll_8011e0e4 > 0 && MathHelper.inBox((int)this.mouseX, (int)this.mouseY, 138, 16, 220, 104)) {
            playMenuSound(1);
            this.invScroll_8011e0e4--;

            this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.invScroll_8011e0e4 < count - 6 && MathHelper.inBox((int)this.mouseX, (int)this.mouseY, 138, 16, 220, 104)) {
            playMenuSound(1);
            this.invScroll_8011e0e4++;

            this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
          }
        }

        this.renderSellList(this.invScroll_8011e0e4, this.sellType == 1, this.renderable_8011e0f0, this.renderable_8011e0f4);
        this.renderShopMenu(this.menuIndex_8011e0dc);
      }

      case START_FADE_16, FINISH_FADE_17 -> {
        if(this.menuState == MenuState.START_FADE_16) {
          startFadeEffect(1, 10);
          this.menuState = MenuState.FINISH_FADE_17;
        }

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          this.menuState = this.confirmDest;
        }

        this.renderShopMenu(this.menuIndex_8011e0dc);
        this.drawShopHeader();
      }

      case UNLOAD_19 -> whichMenu_800bdc38 = WhichMenu.UNLOAD;
    }
  }

  private void scroll(final int scroll) {
    playMenuSound(1);
    this.invScroll_8011e0e4 = scroll;
  }

  private void renderShopMenu(final int selectedMenuItem) {
    renderText(I18n.translate("lod_core.ui.shop.category_buy"), 72, this.getShopMenuYOffset(0) + 2, selectedMenuItem != 0 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);
    renderText(I18n.translate("lod_core.ui.shop.category_sell"), 72, this.getShopMenuYOffset(1) + 2, selectedMenuItem != 1 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);
    renderText(I18n.translate("lod_core.ui.shop.category_carried"), 72, this.getShopMenuYOffset(2) + 2, selectedMenuItem != 2 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);
    renderText(I18n.translate("lod_core.ui.shop.category_leave"), 72, this.getShopMenuYOffset(3) + 2, selectedMenuItem != 3 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);
  }

  private void setSelectedEntry(@Nullable final ShopEntry<? extends InventoryEntry<?>> shopEntry) {
    if(this.activeExtension != null) {
      this.activeExtension.deactivate(this, this.shop, gameState_800babc8);
    }

    if(shopEntry == null) {
      this.activeExtension = this.unknownExtension;
    } else {
      this.activeExtension = this.extensions.object2IntEntrySet().stream()
        .sorted(Comparator.comparingInt(Object2IntMap.Entry::getIntValue))
        .map(Object2IntMap.Entry::getKey)
        .filter(extension -> extension.accepts(shopEntry))
        .findFirst()
        .orElse(this.unknownExtension)
      ;
    }

    this.selectedEntry = shopEntry;
    this.activeExtension.activate(this, this.shop, gameState_800babc8, shopEntry);
  }

  private void drawBuyMenu() {
    this.renderShopMenu(this.menuIndex_8011e0dc);
    this.renderMenuEntries(this.inv, this.invScroll_8011e0e4, this.renderable_8011e0f0, this.renderable_8011e0f4);
    this.drawShopHeader();
    this.drawShopDetails();
    this.drawShopDescription();
  }

  private void drawShopHeader() {
    this.activeExtension.drawShopHeader(this, this.shop, gameState_800babc8, this.selectedEntry, 16, 16);
  }

  private void drawShopDescription() {
    this.activeExtension.drawShopDescription(this, this.shop, gameState_800babc8, this.selectedEntry, 16, 122);
  }

  private void drawShopDetails() {
    this.activeExtension.drawShopDetails(this, this.shop, gameState_800babc8, this.selectedEntry);
  }

  private void renderSellList(final int firstItem, final boolean isItemShop, final Renderable58 upArrow, final Renderable58 downArrow) {
    if(isItemShop) {
      this.itemExtension.drawShopHeader(this, this.shop, gameState_800babc8, null, 16, 16);

      for(int i = 0; firstItem + i < gameState_800babc8.items_2e9.getSize() && i < 6; i++) {
        final ItemStack stack = gameState_800babc8.items_2e9.get(firstItem + i);
        stack.renderIcon(151, this.menuEntryY(i), 0x8);
        renderText(I18n.translate(stack.getItem()), 168, this.menuEntryY(i) + 2, UI_TEXT);

        if(stack.canStack()) {
          this.renderNumber(247, this.menuEntryY(i) + 4, stack.getSize(), 10);
        }

        final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(this.shop, stack, stack.getSellPrice()));
        this.renderFourDigitNumber(324, this.menuEntryY(i) + 4, event.price);
      }

      downArrow.setVisible(firstItem + 6 <= gameState_800babc8.items_2e9.getSize() - 1);
    } else {
      this.equipmentExtension.drawShopHeader(this, this.shop, gameState_800babc8, null, 16, 16);

      for(int i = 0; firstItem + i < gameState_800babc8.equipment_1e8.size() && i < 6; i++) {
        final Equipment equipment = gameState_800babc8.equipment_1e8.get(firstItem + i);
        equipment.renderIcon(151, this.menuEntryY(i), 0x8);
        renderText(I18n.translate(equipment), 168, this.menuEntryY(i) + 2, equipment.canBeDiscarded() ? UI_TEXT : UI_TEXT_DISABLED);

        if(equipment.canBeDiscarded()) {
          final ShopSellPriceEvent event = EVENTS.postEvent(new ShopSellPriceEvent(this.shop, equipment, equipment.getSellPrice()));
          renderFiveDigitNumber(322, this.menuEntryY(i) + 4, event.price);
        } else {
          ItemIcon.WARNING.render(330, this.menuEntryY(i), 0x8).clut_30 = 0x7eaa;
        }
      }

      downArrow.setVisible(firstItem + 6 <= gameState_800babc8.equipment_1e8.size() - 1);
    }

    upArrow.setVisible(firstItem != 0);
  }

  private void renderMenuEntries(final List<ShopEntry<? extends InventoryEntry<?>>> list, final int startItemIndex, final Renderable58 upArrow, final Renderable58 downArrow) {
    int i;
    for(i = 0; i < Math.min(6, list.size() - startItemIndex); i++) {
      final ShopEntry<? extends InventoryEntry<?>> item = list.get(startItemIndex + i);
      this.activeExtension.drawShopRow(this, this.shop, gameState_800babc8, item, i, 148, this.menuEntryY(i));
    }

    upArrow.setVisible(startItemIndex != 0);
    downArrow.setVisible(i + startItemIndex < list.size());
  }

  private void renderFourDigitNumber(final int x, final int y, final int value) {
    // I didn't look at this method too closely, this may or may not be right
    this.renderNumber(x, y, value, 4);
  }

  private int getShopMenuYOffset(final int slot) {
    return slot * 16 + 58;
  }

  private void fadeToMenuState(final MenuState nextMenuState) {
    this.menuState = MenuState.START_FADE_16;
    this.confirmDest = nextMenuState;
  }

  @Method(0x8010a808L)
  public int menuEntryY(final int index) {
    return index * 17 + 18;
  }

  @Override
  protected InputPropagation mouseMove(final double x, final double y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState == MenuState.RENDER_3) {
      for(int i = 0; i < 4; i++) {
        if(this.menuIndex_8011e0dc != i && MathHelper.inBox((int)x, (int)y, 41, this.getShopMenuYOffset(i), 59, 16)) {
          playMenuSound(1);
          this.menuIndex_8011e0dc = i;

          this.invScroll_8011e0e4 = 0;
          this.invIndex_8011e0e0 = 0;
          this.selectedMenuOptionRenderable_800bdbe0.y_44 = this.getShopMenuYOffset(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_4) {
      for(int i = 0; i < Math.min(6, this.inv.size() - this.invScroll_8011e0e4); i++) {
        if(this.invIndex_8011e0e0 != i && MathHelper.inBox((int)this.mouseX, (int)this.mouseY, 138, this.menuEntryY(i) - 2, 220, 17)) {
          playMenuSound(1);
          this.invIndex_8011e0e0 = i;
          this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(i);
          this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));

          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.EXTENSION_5) {
      final ShopEntry<? extends InventoryEntry<?>> inv = this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0);
      this.activeExtension.mouseMove(this, this.shop, gameState_800babc8, inv, this.invScroll_8011e0e4 + this.invIndex_8011e0e0, (int)x, (int)y);
    } else if(this.menuState == MenuState.SELL_10) {
      final int count = this.sellType != 0 ? gameState_800babc8.items_2e9.getSize() : gameState_800babc8.equipment_1e8.size();

      for(int i = 0; i < Math.min(count, 6); i++) {
        if(this.invIndex_8011e0e0 != i && MathHelper.inBox((int)this.mouseX, (int)this.mouseY, 138, this.menuEntryY(i), 220, 17)) {
          playMenuSound(1);
          this.invIndex_8011e0e0 = i;
          this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(i);

          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.menuState == MenuState.RENDER_3) {
      for(int i = 0; i < 4; i++) {
        if(MathHelper.inBox((int)x, (int)y, 41, this.getShopMenuYOffset(i), 59, 16)) {
          playMenuSound(2);
          this.menuIndex_8011e0dc = i;

          this.invScroll_8011e0e4 = 0;
          this.invIndex_8011e0e0 = 0;
          this.selectedMenuOptionRenderable_800bdbe0.y_44 = this.getShopMenuYOffset(i);

          this.handleSelectedMenu(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.BUY_4) {
      for(int i = 0; i < Math.min(6, this.inv.size() - this.invScroll_8011e0e4); i++) {
        if(MathHelper.inBox((int)this.mouseX, (int)this.mouseY, 138, this.menuEntryY(i) - 2, 220, 17)) {
          this.invIndex_8011e0e0 = i;
          this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(i);
          this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
          this.menuBuy4Select();
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.menuState == MenuState.EXTENSION_5) {
      final ShopEntry<? extends InventoryEntry<?>> inv = this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0);
      this.activeExtension.mouseClick(this, this.shop, gameState_800babc8, inv, this.invScroll_8011e0e4 + this.invIndex_8011e0e0, (int)x, (int)y, button, mods);
    } else if(this.menuState == MenuState.SELL_10) {
      final int count = this.sellType != 0 ? gameState_800babc8.items_2e9.getSize() : gameState_800babc8.equipment_1e8.size();

      for(int i = 0; i < Math.min(count, 6); i++) {
        if(MathHelper.inBox((int)this.mouseX, (int)this.mouseY, 138, this.menuEntryY(i), 220, 17)) {
          this.invIndex_8011e0e0 = i;
          this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(i);
          this.menuSell10Select();
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
          menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.shop_empty"), 0, result -> {}));
          return;
        }

        this.selectedInventoryRowRenderable_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, this.menuEntryY(this.invIndex_8011e0e0));
        initHighlight(this.selectedInventoryRowRenderable_800bdbe4);

        this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, this.menuEntryY(0));
        this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, this.menuEntryY(5));

        this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));

        this.menuState = MenuState.BUY_4;
      }

      case 1 -> // Sell
        menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.what_do_you_want_to_sell"), I18n.translate("lod_core.ui.shop.equipment"), I18n.translate("lod_core.ui.shop.items"), 2, result -> {
          switch(result) {
            case YES -> {
              this.invIndex_8011e0e0 = 0;
              this.invScroll_8011e0e4 = 0;
              this.sellType = 0;

              if(!gameState_800babc8.equipment_1e8.isEmpty()) {
                this.menuState = MenuState.SELL_10;
                this.selectedInventoryRowRenderable_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, this.menuEntryY(0));
                this.renderable_8011e0f0 = allocateUiElement(0x3d, 0x44, 358, this.menuEntryY(0));
                this.renderable_8011e0f4 = allocateUiElement(0x35, 0x3c, 358, this.menuEntryY(5));
                initHighlight(this.selectedInventoryRowRenderable_800bdbe4);
              } else {
                menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.you_have_nothing_to_sell"), 0, result1 -> {}));
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
                this.selectedInventoryRowRenderable_800bdbe4 = allocateUiElement(0x7b, 0x7b, 170, this.menuEntryY(0));
                initHighlight(this.selectedInventoryRowRenderable_800bdbe4);
              } else {
                menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.you_have_nothing_to_sell"), 0, result1 -> {}));
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
        this.fadeToMenuState(MenuState.UNLOAD_19);
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
    this.fadeToMenuState(MenuState.UNLOAD_19);
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
    this.selectedMenuOptionRenderable_800bdbe0.y_44 = this.getShopMenuYOffset(this.menuIndex_8011e0dc);
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
    this.selectedMenuOptionRenderable_800bdbe0.y_44 = this.getShopMenuYOffset(this.menuIndex_8011e0dc);
  }

  private void menuBuy4Escape() {
    playMenuSound(3);
    this.menuState = MenuState.INIT_2;
  }

  private void menuBuy4Select() {
    playMenuSound(2);

    final ShopEntry<? extends InventoryEntry<?>> inv = this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0);

    if(this.activeExtension.selectEntry(this, this.shop, gameState_800babc8, inv, this.invScroll_8011e0e4 + this.invIndex_8011e0e0)) {
      this.menuState = MenuState.EXTENSION_5;
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

    this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
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

    this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
  }

  private void menuBuy4NavigateTop() {
    if(this.invIndex_8011e0e0 != 0) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = 0;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
      this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
    }
  }

  private void menuBuy4NavigateBottom() {
    if(this.invIndex_8011e0e0 != Math.min(5, this.inv.size() - 1)) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = Math.min(5, this.inv.size() - 1);
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
      this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
    }
  }

  private void menuBuy4NavigatePageUp() {
    if(this.invScroll_8011e0e4 - 6 >= 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4 -= 6;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    } else if(this.invScroll_8011e0e4 != 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4 = 0;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }

    this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
  }

  private void menuBuy4NavigatePageDown() {
    final int invCount = this.inv.size();
    if((this.invScroll_8011e0e4 + this.invIndex_8011e0e0) + 6 < invCount - 7) {
      playMenuSound(1);
      this.invScroll_8011e0e4 += 6;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    } else if(invCount > 6 && this.invScroll_8011e0e4 != invCount - 6) {
      playMenuSound(1);
      this.invScroll_8011e0e4 = invCount - 6;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }

    this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
  }

  private void menuBuy4NavigateHome() {
    if(this.invIndex_8011e0e0 > 0 || this.invScroll_8011e0e4 > 0) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = 0;
      this.invScroll_8011e0e4 = 0;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
      this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
    }
  }

  private void menuBuy4NavigateEnd() {
    final int invCount = this.inv.size();
    if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 != invCount - 1) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = Math.min(5, invCount - 1);
      this.invScroll_8011e0e4 = invCount - 1 - this.invIndex_8011e0e0;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
      this.setSelectedEntry(this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0));
    }
  }

  private void menuSell10Escape() {
    playMenuSound(3);
    unloadRenderable(this.selectedInventoryRowRenderable_800bdbe4);
    this.menuState = MenuState.INIT_2;
  }

  private void menuSell10Select() {
    final int slot = this.invScroll_8011e0e4 + this.invIndex_8011e0e0;
    if(this.sellType != 0 && slot >= gameState_800babc8.items_2e9.getSize() || this.sellType == 0 && (slot >= gameState_800babc8.equipment_1e8.size() || !gameState_800babc8.equipment_1e8.get(slot).canBeDiscarded())) {
      playMenuSound(40);
    } else {
      playMenuSound(2);

      final InventoryEntry<?> inv;
      if(this.sellType != 0) {
        inv = new ItemStack(gameState_800babc8.items_2e9.get(slot));
      } else {
        inv = gameState_800babc8.equipment_1e8.get(slot);
      }

      menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.sell", I18n.translate(inv.getNameTranslationKey())), 2, result -> {
        if(result == MessageBoxResult.YES) {
          final boolean taken;
          final int count;
          if(this.sellType != 0) {
            taken = takeItemFromSlot(slot, 1);
            count = gameState_800babc8.items_2e9.getSize();
          } else {
            taken = takeEquipment(slot);
            count = gameState_800babc8.equipment_1e8.size();
          }

          if(taken) {
            EVENTS.postEvent(new ShopSellEvent(this.shop, inv));
            final ShopSellPriceEvent priceEvent = EVENTS.postEvent(new ShopSellPriceEvent(this.shop, inv, inv.getSellPrice()));
            addGold(priceEvent.price);

            if(count == 0) {
              menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.you_have_nothing_to_sell"), 0, result1 -> {}));
              unloadRenderable(this.selectedInventoryRowRenderable_800bdbe4);
              this.menuState = MenuState.INIT_2;
              return;
            }

            if(this.invScroll_8011e0e4 > 0 && this.invScroll_8011e0e4 + 6 > count) {
              this.invScroll_8011e0e4--;
            }

            if(this.invIndex_8011e0e0 != 0 && this.invIndex_8011e0e0 > count - 1) {
              this.invIndex_8011e0e0--;
              this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
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
      itemCount = gameState_800babc8.items_2e9.getSize();
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

    this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
  }

  private void menuSell10NavigateDown() {
    final int itemCount;
    if(this.sellType == 0) { // equipment
      itemCount = gameState_800babc8.equipment_1e8.size();
    } else { // items
      itemCount = gameState_800babc8.items_2e9.getSize();
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

    this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
  }

  private void menuSell10NavigateTop() {
    if(this.invIndex_8011e0e0 != 0) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = 0;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }
  }

  private void menuSell10NavigateBottom() {
    final int itemCount;
    if(this.sellType == 0) { // equipment
      itemCount = gameState_800babc8.equipment_1e8.size();
    } else { // items
      itemCount = gameState_800babc8.items_2e9.getSize();
    }

    if(this.invIndex_8011e0e0 != Math.min(5, itemCount - 1)) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = Math.min(5, itemCount - 1);
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }
  }

  private void menuSell10NavigatePageUp() {
    if(this.invScroll_8011e0e4 - 6 >= 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4 -= 6;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    } else if(this.invScroll_8011e0e4 != 0) {
      playMenuSound(1);
      this.invScroll_8011e0e4 = 0;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }
  }

  private void menuSell10NavigatePageDown() {
    final int itemCount;
    if(this.sellType == 0) { // equipment
      itemCount = gameState_800babc8.equipment_1e8.size();
    } else { // items
      itemCount = gameState_800babc8.items_2e9.getSize();
    }

    if((this.invScroll_8011e0e4 + this.invIndex_8011e0e0) + 6 < itemCount - 7) {
      playMenuSound(1);
      this.invScroll_8011e0e4 += 6;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    } else if(itemCount > 6 && this.invScroll_8011e0e4 != itemCount - 6) {
      playMenuSound(1);
      this.invScroll_8011e0e4 = itemCount - 6;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }
  }

  private void menuSell10NavigateHome() {
    if(this.invIndex_8011e0e0 > 0 || this.invScroll_8011e0e4 > 0) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = 0;
      this.invScroll_8011e0e4 = 0;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
    }
  }

  private void menuSell10NavigateEnd() {
    final int itemCount;
    if(this.sellType == 0) { // equipment
      itemCount = gameState_800babc8.equipment_1e8.size();
    } else { // items
      itemCount = gameState_800babc8.items_2e9.getSize();
    }

    if(this.invScroll_8011e0e4 + this.invIndex_8011e0e0 != itemCount - 1) {
      playMenuSound(1);
      this.invIndex_8011e0e0 = Math.min(5, itemCount - 1);
      this.invScroll_8011e0e4 = itemCount - 1 - this.invIndex_8011e0e0;
      this.selectedInventoryRowRenderable_800bdbe4.y_44 = this.menuEntryY(this.invIndex_8011e0e0);
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

        if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
          this.menuMainShopRender3Escape();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
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

        if(action == INPUT_ACTION_MENU_TOP.get()) {
          this.menuBuy4NavigateTop();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_BOTTOM.get()) {
          this.menuBuy4NavigateBottom();
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

      case EXTENSION_5 -> {
        final ShopEntry<? extends InventoryEntry<?>> inv = this.inv.get(this.invScroll_8011e0e4 + this.invIndex_8011e0e0);
        return this.activeExtension.inputActionPressed(this, this.shop, gameState_800babc8, inv, this.invScroll_8011e0e4 + this.invIndex_8011e0e0, action, repeat);
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

        if(action == INPUT_ACTION_MENU_TOP.get()) {
          this.menuSell10NavigateTop();
          return InputPropagation.HANDLED;
        }

        if(action == INPUT_ACTION_MENU_BOTTOM.get()) {
          this.menuSell10NavigateBottom();
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

  public static class ShopEntry<T extends InventoryEntry<?>> {
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
    EXTENSION_5,
    SELL_10,
    START_FADE_16,
    FINISH_FADE_17,
    UNLOAD_19,
  }
}
