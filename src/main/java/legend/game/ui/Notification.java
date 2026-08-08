package legend.game.ui;

import legend.core.lang.TextComponent;

import static legend.core.GameEngine.DEFAULT_FONT;
import static legend.game.SItem.UI_WHITE_SHADOWED_SMALL;
import static legend.game.Text.renderText;

public class Notification {
  public final long expiration;
  public final TextComponent text;

  public Notification(final long expiration, final TextComponent text) {
    this.expiration = expiration;
    this.text = text;
  }

  public float draw(final float x, final float y) {
    final String text = this.text.get();
    renderText(text, x, y, UI_WHITE_SHADOWED_SMALL);
    return DEFAULT_FONT.textHeight(text) * UI_WHITE_SHADOWED_SMALL.getSize();
  }

  public boolean isFinished() {
    return System.nanoTime() >= this.expiration;
  }
}
