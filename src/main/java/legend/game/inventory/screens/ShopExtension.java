package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.InventoryEntry;
import legend.game.types.GameState52c;
import legend.game.types.Renderable58;
import legend.game.types.Shop;

import java.util.Set;

import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.allocateOneFrameGlyph;
import static legend.game.SItem.renderFiveDigitNumber;
import static legend.game.SItem.renderRightAlignedNumber;
import static legend.game.SItem.renderString;
import static legend.game.Text.renderText;

public abstract class ShopExtension<T extends InventoryEntry<T>> {
  private static final FontOptions FONT_OPTIONS = new FontOptions().set(UI_TEXT).size(0.8f);

  public abstract String getName(final T entry);

  /** Called when this extension is loaded into a shop */
  public void attach(final ShopScreen screen, final Shop shop, final GameState52c gameState) {

  }

  /** Called when this extension is displayed in a shop */
  public void activate(final ShopScreen screen, final Shop shop, final GameState52c gameState, final T entry) {

  }

  /** Called when this extension is no longer displayed in a shop */
  public void deactivate(final ShopScreen screen, final Shop shop, final GameState52c gameState) {

  }

  /** Draw the header at the top of the shop */
  public void drawShopHeader(final ShopScreen screen, final Shop shop, final GameState52c gameState, final T entry, final int x, final int y) {
    final Renderable58 renderable = allocateOneFrameGlyph(94, x, y);
    renderable.metricsCount = 16; // truncate slash from renderable

    renderText(this.getName(entry), x + 7, y + 7, FONT_OPTIONS);
    renderText(I18n.translate("lod_core.ui.shop.gold"), x + 7, y + 19.5f, FONT_OPTIONS);
    renderRightAlignedNumber(x + 120, y + 20, gameState.gold_94, 0);
  }

  /** Draw the entry row in the inventory list */
  public void drawShopRow(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<T> entry, final int index, final int x, final int y) {
    renderText(I18n.translate(entry.item.getNameTranslationKey()), x + 20, y + 2, UI_TEXT);
    renderFiveDigitNumber(x + 176, y + 4, entry.price);
    entry.item.renderIcon(x + 3, y, 0x8);
  }

  /** Draw the description for the selected entry */
  public void drawShopDescription(final ShopScreen screen, final Shop shop, final GameState52c gameState, final T entry, final int x, final int y) {
    renderString(x, y, I18n.translate(entry.getDescriptionTranslationKey()), false);
  }

  /** Draw the details for the selected entry */
  public void drawShopDetails(final ShopScreen screen, final Shop shop, final GameState52c gameState, final T entry) {

  }

  /**
   * Called when an entry is selected in the list
   *
   * @return True to give shop control to the extension (e.g. for shops selecting characters)
   */
  public abstract boolean selectEntry(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<T> entry, final int index);

  /** If the extension is given control by {@link #selectEntry}, return true to give control back to the shop */
  public boolean shouldReturnControl() {
    return true;
  }

  /** Once an entry is selected, this extension will start receiving input */
  public InputPropagation inputActionPressed(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<T> entry, final int index, final InputAction action, final boolean repeat) {
    return InputPropagation.PROPAGATE;
  }

  /** Once an entry is selected, this extension will start receiving input */
  public InputPropagation mouseMove(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<T> entry, final int index, final int x, final int y) {
    return InputPropagation.PROPAGATE;
  }

  /** Once an entry is selected, this extension will start receiving input */
  public InputPropagation mouseClick(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<T> entry, final int index, final int x, final int y, final int button, final Set<InputMod> mods) {
    return InputPropagation.PROPAGATE;
  }
}
