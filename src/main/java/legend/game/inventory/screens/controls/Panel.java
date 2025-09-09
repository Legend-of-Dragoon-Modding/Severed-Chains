package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.types.MenuGlyph06;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.UiPart;
import legend.game.types.UiType;

import static legend.game.SItem.buildUiRenderable;
import static legend.game.SItem.initGlyph;
import static legend.game.Scus94491BpeSegment_8002.allocateManualRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderable;

public class Panel extends Control {
  public static Panel panel() {
    return new Panel(true);
  }

  public static Panel subtle() {
    final Panel panel = new Panel(true);

    panel.setMetrics(
      240, 245, 250, 16, 21, 27, 196, 70,
      5, 5,
      1, 1,
      3
    );

    panel.setClut(0x7ca9);
    return panel;
  }

  public static Panel dark() {
    final Panel panel = new Panel(true);

    panel.setMetrics(
      240, 245, 250, 16, 21, 27, 196, 70,
      5, 5,
      1, 1,
      3
    );

    panel.setClut(0x7c29);
    return panel;
  }

  public static Panel invisible() {
    return new Panel(false);
  }

  private final Renderable58 background;

  private int u0 = 128;
  private int u1 = 144;
  private int u2 = 167;
  private int v0 = 48;
  private int v1 = 64;
  private int v2 = 72;
  /** Middle u */
  private int mu = 144;
  /** Middle v */
  private int mv = 64;
  /** Corner width */
  private int cw = 9;
  /** Corner height */
  private int ch = 8;
  /** Middle width */
  private int mw = 1;
  /** Middle height */
  private int mh = 1;
  /** Middle grow */
  private int mg;
  private int clut = 0x7c29;
  private int tpage = 0x200c;

  protected Panel(final boolean hasBackground) {
    if(hasBackground) {
      final UiPart part = new UiPart(new RenderableMetrics14[9], 1);
      final UiType type = new UiType(new UiPart[]{part});

      this.background = allocateManualRenderable(type, null);
      initGlyph(this.background, new MenuGlyph06(0, 0, 0));
    } else {
      this.background = null;
    }

    this.setSize(100, 50);
  }

  public void setMetrics(final int u0, final int u1, final int u2, final int v0, final int v1, final int v2, final int middleU, final int middleV, final int cornerW, final int cornerH, final int middleW, final int middleH, final int middleGrow) {
    this.u0 = u0;
    this.u1 = u1;
    this.u2 = u2;
    this.v0 = v0;
    this.v1 = v1;
    this.v2 = v2;
    this.mu = middleU;
    this.mv = middleV;
    this.cw = cornerW;
    this.ch = cornerH;
    this.mw = middleW;
    this.mh = middleH;
    this.mg = middleGrow;
  }

  public void setClut(final int clut) {
    this.clut = clut;
  }

  public void setTpage(final int tpage) {
    this.tpage = tpage;
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);

    if(this.background != null) {
      this.background.z_3c = z;
    }
  }

  @Override
  protected void onResize() {
    super.onResize();

    if(this.background != null) {
      final UiPart uiType = this.background.uiType_20.entries_08[0];
      uiType.metrics_00()[0] = new RenderableMetrics14(this.mu, this.mv, 8 + this.cw - this.mg, this.ch - this.mg, this.clut, this.tpage, this.getWidth() - (this.cw - this.mg) * 2 + this.mw, this.getHeight() - (this.ch - this.mg) * 2 + this.mh, this.mw, this.mh);

      uiType.metrics_00()[1] = new RenderableMetrics14(this.u0, this.v0, 8, 0, this.clut, this.tpage, this.cw, this.ch, this.cw, this.ch);
      uiType.metrics_00()[2] = new RenderableMetrics14(this.u1, this.v0, 8 + this.cw, 0, this.clut, this.tpage, this.getWidth() - this.cw * 2, this.ch, this.mw, this.ch);
      uiType.metrics_00()[3] = new RenderableMetrics14(this.u2, this.v0, 8 + this.getWidth() - this.cw, 0, this.clut, this.tpage, this.cw, this.ch, this.cw, this.ch);

      uiType.metrics_00()[4] = new RenderableMetrics14(this.u0, this.v1, 8, this.ch, this.clut, this.tpage, this.cw, this.getHeight() - this.ch * 2, this.cw, this.mh);
      uiType.metrics_00()[5] = new RenderableMetrics14(this.u2, this.v1, 8 + this.getWidth() - this.cw, this.ch, this.clut, this.tpage, this.cw, this.getHeight() - this.ch * 2, this.cw, this.mh);

      uiType.metrics_00()[6] = new RenderableMetrics14(this.u0, this.v2, 8, this.getHeight() - this.ch, this.clut, this.tpage, this.cw, this.ch, this.cw, this.ch);
      uiType.metrics_00()[7] = new RenderableMetrics14(this.u1, this.v2, 8 + this.cw, this.getHeight() - this.ch, this.clut, this.tpage, this.getWidth() - this.cw * 2, this.ch, this.mw, this.ch);
      uiType.metrics_00()[8] = new RenderableMetrics14(this.u2, this.v2, 8 + this.getWidth() - this.cw, this.getHeight() - this.ch, this.clut, this.tpage, this.cw, this.ch, this.cw, this.ch);

      if(this.background.uiType_20.obj != null) {
        this.background.uiType_20.obj.delete();
        this.background.uiType_20.obj = null;
      }
    }
  }

  @Override
  protected void delete() {
    super.delete();

    if(this.background.uiType_20.obj != null) {
      this.background.uiType_20.obj.delete();
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.background != null) {
      if(this.background.uiType_20.obj == null) {
        this.background.uiType_20.obj = buildUiRenderable(this.background.uiType_20, "Panel Background");
      }

      uploadRenderable(this.background, x, y);
    }
  }
}
