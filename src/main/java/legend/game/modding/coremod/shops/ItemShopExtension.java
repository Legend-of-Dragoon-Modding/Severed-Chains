package legend.game.modding.coremod.shops;

import legend.game.i18n.I18n;
import legend.game.inventory.ItemStack;
import legend.game.inventory.screens.MessageBoxScreen;
import legend.game.inventory.screens.ShopExtension;
import legend.game.inventory.screens.ShopScreen;
import legend.game.modding.events.inventory.ShopBuyEvent;
import legend.game.types.GameState52c;
import legend.game.types.MessageBoxResult;
import legend.game.types.MessageBoxType;
import legend.game.types.Shop;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderFraction;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INVENTORY_SIZE_CONFIG;

public class ItemShopExtension extends ShopExtension<ItemStack> {
  @Override
  public String getName(final ShopScreen.ShopEntry<ItemStack> entry) {
    return I18n.translate("lod_core.ui.shop.items");
  }

  @Override
  public boolean accepts(final ShopScreen.ShopEntry<?> entry) {
    return entry.item instanceof ItemStack;
  }

  @Override
  public void drawShopHeader(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<ItemStack> entry, final int x, final int y) {
    super.drawShopHeader(screen, shop, gameState, entry, x, y);
    renderFraction(x + 120, y + 8, gameState.items_2e9.getSize(), CONFIG.getConfig(INVENTORY_SIZE_CONFIG.get()));
  }

  @Override
  public void drawShopDetails(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<ItemStack> entry) {
    int count = 0;
    for(int i = 0; i < gameState.items_2e9.getSize(); i++) {
      if(gameState.items_2e9.get(i).isSameItem(entry.item.getItem())) {
        count += gameState.items_2e9.get(i).getSize();
      }
    }

    renderText(I18n.translate("lod_core.ui.shop.carrying", count), 195, 125, UI_TEXT);
  }

  @Override
  public boolean selectEntry(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<ItemStack> entry, final int index) {
    if(gameState_800babc8.gold_94 < entry.price) {
      screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.not_enough_gold"), MessageBoxType.ALERT, result -> { })));
    } else {
      menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.buy", I18n.translate(entry.item.getNameTranslationKey())), MessageBoxType.CONFIRMATION, result -> {
        if(result == MessageBoxResult.YES) {
          EVENTS.postEvent(new ShopBuyEvent(shop, entry.item));

          if(gameState.items_2e9.give(entry.item).isEmpty()) {
            gameState_800babc8.gold_94 -= entry.price;
          } else {
            screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.inventory_full"), MessageBoxType.ALERT, onResult -> { })));
          }
        }
      }));
    }

    return false;
  }
}
