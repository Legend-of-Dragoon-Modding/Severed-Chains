package legend.game.modding.coremod.shops;

import legend.game.i18n.I18n;
import legend.game.inventory.Good;
import legend.game.inventory.screens.MessageBoxScreen;
import legend.game.inventory.screens.ShopExtension;
import legend.game.inventory.screens.ShopScreen;
import legend.game.modding.events.inventory.ShopBuyEvent;
import legend.game.types.GameState52c;
import legend.game.types.MessageBoxResult;
import legend.game.types.Shop;

import static legend.core.GameEngine.EVENTS;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Text.renderText;

public class GoodShopExtension extends ShopExtension<Good> {
  @Override
  public String getName(final Good entry) {
    return I18n.translate("lod_core.ui.shop.goods");
  }

  @Override
  public void drawShopDetails(final ShopScreen screen, final Shop shop, final GameState52c gameState, final Good entry) {
    if(gameState.goods_19c.has(entry)) {
      renderText(I18n.translate("lod_core.ui.shop.already_have_good", I18n.translate(entry.getNameTranslationKey())), 195, 125, UI_TEXT);
    }
  }

  @Override
  public boolean selectEntry(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<Good> entry, final int index) {
    if(gameState.goods_19c.has(entry.item)) {
      screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.already_have_good", I18n.translate(entry.item.getNameTranslationKey())), 0, result -> {})));
    } else if(gameState_800babc8.gold_94 < entry.price) {
      screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.not_enough_gold"), 0, result -> { })));
    } else {
      menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.buy", I18n.translate(entry.item.getNameTranslationKey())), 2, result -> {
        if(result == MessageBoxResult.YES) {
          EVENTS.postEvent(new ShopBuyEvent(shop, entry.item));
          gameState.goods_19c.give(entry.item);
          gameState_800babc8.gold_94 -= entry.price;
        }
      }));
    }

    return false;
  }
}
