package legend.game.inventory.screens;

import legend.game.types.GameState52c;
import legend.game.types.Renderable58;
import legend.game.types.Shop;

import static legend.game.SItem.allocateOneFrameGlyph;

public class UnknownShopExtension extends ShopExtension {
  @Override
  public boolean accepts(final ShopScreen.ShopEntry entry) {
    return false;
  }

  @Override
  public String getName(final ShopScreen.ShopEntry entry) {
    return "Unknown";
  }

  @Override
  public void drawShopHeader(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry entry, final int x, final int y) {
    final Renderable58 renderable = allocateOneFrameGlyph(94, x, y);
    renderable.metricsCount = 16; // truncate slash from renderable
  }

  @Override
  public boolean selectEntry(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry entry, final int index) {
    return false;
  }
}
