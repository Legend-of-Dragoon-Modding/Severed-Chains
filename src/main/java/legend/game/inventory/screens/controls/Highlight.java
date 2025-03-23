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

public class Highlight extends Control {
  private final Renderable58 background;
  private final Renderable58 brackets;

  public Highlight() {
    final UiPart backgroundPart = new UiPart(new RenderableMetrics14[9], 1);
    final UiType backgroundType = new UiType(new UiPart[] {backgroundPart});

    this.background = allocateManualRenderable(backgroundType, null);
    initGlyph(this.background, new MenuGlyph06(0, 0, 0));

    final UiPart bracketsPart = new UiPart(new RenderableMetrics14[6], 1);
    final UiType bracketsType = new UiType(new UiPart[] {bracketsPart});

    this.brackets = allocateManualRenderable(bracketsType, null);
    initGlyph(this.brackets, new MenuGlyph06(0, 0, 0));

    this.setSize(16, 16);
    this.ignoreInput();
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.brackets.z_3c = z;
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.rebuild();
  }

  private int u0 = 176;
  private int u1 = 185;
  private int u2 = 215;
  private int v0 = 64;
  private int v1 = 70;
  private int v2 = 74;
  /** Middle u */
  private int mu = 190;
  /** Middle v */
  private int mv = 70;
  /** Corner width */
  private int cw = 9;
  /** Corner height */
  private int ch = 5;
  /** Middle width */
  private int mw = 1;
  /** Middle height */
  private int mh = 0;
  /** Middle grow */
  private int mg;
  private int clut = 0xff29;
  private int tpage = 0x240c;

  private void rebuild() {
    final UiPart backgroundPart = this.background.uiType_20.entries_08[0];
    backgroundPart.metrics_00()[0] = new RenderableMetrics14(this.mu, this.mv, 8 + this.cw - this.mg, this.ch - this.mg, this.clut, this.tpage, this.getWidth() - (this.cw - this.mg) * 2 + this.mw, this.getHeight() - (this.ch - this.mg) * 2 + this.mh, this.mw, this.mh);

    backgroundPart.metrics_00()[1] = new RenderableMetrics14(this.u0, this.v0, 8, 0, this.clut, this.tpage, this.cw, this.ch, this.cw, this.ch);
    backgroundPart.metrics_00()[2] = new RenderableMetrics14(this.u1, this.v0, 8 + this.cw, 0, this.clut, this.tpage, this.getWidth() - this.cw * 2, this.ch, this.mw, this.ch);
    backgroundPart.metrics_00()[3] = new RenderableMetrics14(this.u2, this.v0, 8 + this.getWidth() - this.cw, 0, this.clut, this.tpage, this.cw, this.ch, this.cw, this.ch);

    backgroundPart.metrics_00()[4] = new RenderableMetrics14(this.u0, this.v1, 8, this.ch, this.clut, this.tpage, this.cw, this.getHeight() - this.ch * 2, this.cw, this.mh);
    backgroundPart.metrics_00()[5] = new RenderableMetrics14(this.u2, this.v1, 8 + this.getWidth() - this.cw, this.ch, this.clut, this.tpage, this.cw, this.getHeight() - this.ch * 2, this.cw, this.mh);

    backgroundPart.metrics_00()[6] = new RenderableMetrics14(this.u0, this.v2, 8, this.getHeight() - this.ch, this.clut, this.tpage, this.cw, this.ch, this.cw, this.ch);
    backgroundPart.metrics_00()[7] = new RenderableMetrics14(this.u1, this.v2, 8 + this.cw, this.getHeight() - this.ch, this.clut, this.tpage, this.getWidth() - this.cw * 2, this.ch, this.mw, this.ch);
    backgroundPart.metrics_00()[8] = new RenderableMetrics14(this.u2, this.v2, 8 + this.getWidth() - this.cw, this.getHeight() - this.ch, this.clut, this.tpage, this.cw, this.ch, this.cw, this.ch);

    final UiPart bracketsPart = this.brackets.uiType_20.entries_08[0];
    bracketsPart.metrics_00()[0] = new RenderableMetrics14(240, 64, 8, 0, 0xfc29, 0x2c, 5, 5, 5, 5);
    bracketsPart.metrics_00()[1] = new RenderableMetrics14(251, 64, 8 + this.getWidth() - 4, 0, 0xfc29, 0x2c, 5, 5, 5, 5);

    bracketsPart.metrics_00()[2] = new RenderableMetrics14(240, 69, 8, 5, 0xfc29, 0x2c, 5, this.getHeight() - 10, 5, 1);
    bracketsPart.metrics_00()[3] = new RenderableMetrics14(251, 69, 8 + this.getWidth() - 4, 5, 0xfc29, 0x2c, 5, this.getHeight() - 10, 5, 1);

    bracketsPart.metrics_00()[4] = new RenderableMetrics14(240, 71, 8, this.getHeight() - 5, 0xfc29, 0x2c, 5, 5, 5, 5);
    bracketsPart.metrics_00()[5] = new RenderableMetrics14(251, 71, 8 + this.getWidth() - 4, this.getHeight() - 5, 0xfc29, 0x2c, 5, 5, 5, 5);

    if(this.background.uiType_20.obj != null) {
      this.background.uiType_20.obj.delete();
      this.background.uiType_20.obj = null;
    }

    if(this.brackets.uiType_20.obj != null) {
      this.brackets.uiType_20.obj.delete();
      this.brackets.uiType_20.obj = null;
    }
  }

  @Override
  protected void delete() {
    super.delete();

    if(this.background.uiType_20.obj != null) {
      this.background.uiType_20.obj.delete();
    }

    if(this.brackets.uiType_20.obj != null) {
      this.brackets.uiType_20.obj.delete();
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.background.uiType_20.obj == null) {
      this.background.uiType_20.obj = buildUiRenderable(this.background.uiType_20, "Highlight Background");
    }

    if(this.brackets.uiType_20.obj == null) {
      this.brackets.uiType_20.obj = buildUiRenderable(this.brackets.uiType_20, "Highlight Brackets");
    }

    uploadRenderable(this.background, x, y);
    uploadRenderable(this.brackets, x, y);
  }
}
