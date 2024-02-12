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

public class Brackets extends Control {
  private final Renderable58 background;
  private int clut = 0x7c29;

  public Brackets() {
    final UiPart part = new UiPart(new RenderableMetrics14[6], 1);
    final UiType type = new UiType(new UiPart[] {part});

    this.background = allocateManualRenderable(type, null);
    initGlyph(this.background, new MenuGlyph06(0, 0, 0));

    this.setSize(16, 16);
    this.ignoreInput();
  }

  public void setClut(final int clut) {
    this.clut = clut;
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.background.z_3c = z;
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.rebuild();
  }

  private void rebuild() {
    final UiPart uiType = this.background.uiType_20.entries_08[0];
    uiType.metrics_00()[0] = new RenderableMetrics14(240, 64, 8, 0, this.clut, 0x200c, 5, 5, 5, 5);
    uiType.metrics_00()[1] = new RenderableMetrics14(251, 64, 8 + this.getWidth() - 5, 0, this.clut, 0x200c, 5, 5, 5, 5);

    uiType.metrics_00()[2] = new RenderableMetrics14(240, 69, 8, 5, this.clut, 0x200c, 5, this.getHeight() - 10, 5, 1);
    uiType.metrics_00()[3] = new RenderableMetrics14(251, 69, 8 + this.getWidth() - 5, 5, this.clut, 0x200c, 5, this.getHeight() - 10, 5, 1);

    uiType.metrics_00()[4] = new RenderableMetrics14(240, 71, 8, this.getHeight() - 5, this.clut, 0x200c, 5, 5, 5, 5);
    uiType.metrics_00()[5] = new RenderableMetrics14(251, 71, 8 + this.getWidth() - 5, this.getHeight() - 5, this.clut, 0x200c, 5, 5, 5, 5);

    if(this.background.uiType_20.obj != null) {
      this.background.uiType_20.obj.delete();
      this.background.uiType_20.obj = null;
    }
  }

  @Override
  protected void delete() {
    super.delete();

    if(this.background.uiType_20.obj != null) {
      this.background.uiType_20.obj.delete();
      this.background.uiType_20.obj = null;
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.background.uiType_20.obj == null) {
      this.background.uiType_20.obj = buildUiRenderable(this.background.uiType_20, "Brackets");
    }

    uploadRenderable(this.background, x, y);
  }
}
