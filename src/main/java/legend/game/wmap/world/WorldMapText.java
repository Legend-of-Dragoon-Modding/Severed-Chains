package legend.game.wmap.world;

import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;

import java.util.Objects;

import static legend.core.GameEngine.DEFAULT_FONT;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Text.renderText;

/** Screen-space text for custom presentation controllers. Uses logical viewport coordinates. */
public final class WorldMapText {
  private static final float INSET = 8;
  private WorldMapText() { }

  /** Left aligned, with the requested number of complete text heights below the label. */
  public static void renderBottomLeft(final String text, final float clearanceInTextHeights) {
    renderBottomLeft(text, clearanceInTextHeights, new FontOptions().colour(TextColour.WHITE).shadowColour(TextColour.BLACK).size(0.67f));
  }

  public static void renderBottomLeft(final String text, final float clearanceInTextHeights, final FontOptions style) {
    if(!Float.isFinite(clearanceInTextHeights) || clearanceInTextHeights < 0) {
      throw new IllegalArgumentException("World map text clearance must be finite and nonnegative");
    }
    final FontOptions fitted = fit(text, style).horizontalAlign(HorizontalAlign.LEFT);
    final float height = lineCount(text) * 12 * fitted.getSize();
    final float y = RENDERER.getNativeHeight() - height - clearanceInTextHeights * 12 * fitted.getSize();
    render(text, -RENDERER.getWidescreenOrthoOffsetX() + INSET, y, fitted);
  }

  /** Fit and clamp the full text block, preserving the requested horizontal alignment. */
  public static void render(final String text, final float x, final float y, final FontOptions style) {
    if(!Float.isFinite(x) || !Float.isFinite(y)) {
      throw new IllegalArgumentException("World map text coordinates must be finite");
    }
    final FontOptions fitted = fit(text, style);
    final float width = DEFAULT_FONT.textWidth(text) * fitted.getSize();
    final float height = lineCount(text) * 12 * fitted.getSize();
    final float left = -RENDERER.getWidescreenOrthoOffsetX() + INSET;
    final float right = RENDERER.getNativeWidth() + RENDERER.getWidescreenOrthoOffsetX() - INSET;
    final float alignment = switch(fitted.getHorizontalAlign()) {
      case LEFT -> 0;
      case CENTRE -> width / 2;
      case RIGHT -> width;
    };
    final float originX = Math.clamp(x - alignment, left, Math.max(left, right - width)) + alignment;
    final float originY = Math.clamp(y, INSET, Math.max(INSET, RENDERER.getNativeHeight() - INSET - height));
    renderText(text, originX, originY, fitted);
  }

  private static FontOptions fit(final String text, final FontOptions style) {
    Objects.requireNonNull(text, "text");
    Objects.requireNonNull(style, "style");
    if(!Float.isFinite(style.getSize()) || style.getSize() <= 0) {
      throw new IllegalArgumentException("World map text size must be finite and positive");
    }
    final float width = Math.max(1, RENDERER.getNativeWidth() + 2 * RENDERER.getWidescreenOrthoOffsetX() - 2 * INSET);
    final float height = Math.max(1, RENDERER.getNativeHeight() - 2 * INSET);
    final float size = Math.min(style.getSize(), Math.min(width / Math.max(1, DEFAULT_FONT.textWidth(text)), height / (lineCount(text) * 12)));
    return new FontOptions().set(style).size(size);
  }

  private static int lineCount(final String text) {
    return 1 + (int)text.chars().filter(c -> c == '\n').count();
  }
}
