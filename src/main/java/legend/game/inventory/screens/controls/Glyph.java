package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.types.MenuGlyph06;
import legend.game.types.Renderable58;
import legend.game.types.UiType;

import static legend.core.GameEngine.CONFIG;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.initGlyph;
import static legend.game.Scus94491BpeSegment_8002.allocateManualRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderable;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.modding.coremod.CoreMod.REDUCE_MOTION_FLASHING_CONFIG;

public class Glyph extends Control {
  private final Renderable58 renderable;

  public static Glyph glyph(final UiType uiType, final int glyph) {
    final Renderable58 renderable = allocateManualRenderable(uiType, null);
    initGlyph(renderable, new MenuGlyph06(glyph, 0, 0));

    return new Glyph(renderable);
  }

  public static Glyph glyph(final int glyph) {
    return glyph(uiFile_800bdc3c.uiElements_0000(), glyph);
  }

  public static Glyph uiElement(final UiType uiType, final int startGlyph, final int endGlyph) {
    final Renderable58 renderable = allocateUiElement(allocateManualRenderable(uiType, null), startGlyph, endGlyph, 0, 0);
    return new Glyph(renderable);
  }

  public static Glyph uiElement(final int startGlyph, final int endGlyph) {
    return uiElement(uiFile_800bdc3c.uiElements_0000(), startGlyph, endGlyph);
  }

  public static Glyph blueSpinnerUp() {
    if(CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get())) {
      return uiElement(67, 67);
    }

    return uiElement(61, 68);
  }

  public static Glyph blueSpinnerDown() {
    if(CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get())) {
      return uiElement(59, 59);
    }

    return uiElement(53, 60);
  }

  protected Glyph(final Renderable58 renderable) {
    this.renderable = renderable;
    this.ignoreInput();
  }

  public Renderable58 getRenderable() {
    return this.renderable;
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.renderable.z_3c = z;
  }

  @Override
  public void setScale(final float scale) {
    super.setScale(scale);
    this.renderable.widthScale = scale;
    this.renderable.heightScale_38 = scale;
  }

  @Override
  protected void render(final int x, final int y) {
    uploadRenderable(this.renderable, x, y);
  }
}
