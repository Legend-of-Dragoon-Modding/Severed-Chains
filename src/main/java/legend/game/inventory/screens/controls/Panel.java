package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.types.MenuGlyph06;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.UiPart;
import legend.game.types.UiType;

import static legend.game.SItem.initGlyph;
import static legend.game.Scus94491BpeSegment_8002.allocateManualRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderable;

public class Panel extends Control {
  private final Renderable58 background;

  public Panel() {
    final UiPart part = new UiPart(new RenderableMetrics14[9], 1);
    final UiType type = new UiType(new UiPart[] {part});

    this.background = allocateManualRenderable(type, null);
    initGlyph(this.background, new MenuGlyph06(0, 0, 0));

    this.setSize(100, 50);
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.background.z_3c = z;
  }

  @Override
  protected void onResize() {
    super.onResize();

    final UiPart uiType = this.background.uiType_20.entries_08()[0];
    uiType.metrics_00()[0] = new RenderableMetrics14(128, 48, 9, 0, 0x7c29, 0x200c, 9, 8, 9, 8, (short)0x1000, (short)0x1000);
    uiType.metrics_00()[1] = new RenderableMetrics14(144, 48, 18, 0, 0x7c29, 0x200c, this.getWidth() - 18, 8, 1, 8, (short)0x1000, (short)0x1000);
    uiType.metrics_00()[2] = new RenderableMetrics14(167, 48, this.getWidth(), 0, 0x7c29, 0x200c, 9, 8, 9, 8, (short)0x1000, (short)0x1000);

    uiType.metrics_00()[3] = new RenderableMetrics14(128, 64, 9, 8, 0x7c29, 0x200c, 9, this.getHeight() - 16, 9, 1, (short)0x1000, (short)0x1000);
    uiType.metrics_00()[4] = new RenderableMetrics14(144, 64, 18, 8, 0x7c29, 0x200c, this.getWidth() - 18, this.getHeight() - 16, 1, 1, (short)0x1000, (short)0x1000);
    uiType.metrics_00()[5] = new RenderableMetrics14(167, 64, this.getWidth(), 8, 0x7c29, 0x200c, 9, this.getHeight() - 16, 9, 1, (short)0x1000, (short)0x1000);

    uiType.metrics_00()[6] = new RenderableMetrics14(128, 72, 9, this.getHeight() - 8, 0x7c29, 0x200c, 9, 8, 9, 8, (short)0x1000, (short)0x1000);
    uiType.metrics_00()[7] = new RenderableMetrics14(144, 72, 18, this.getHeight() - 8, 0x7c29, 0x200c, this.getWidth() - 18, 8, 1, 8, (short)0x1000, (short)0x1000);
    uiType.metrics_00()[8] = new RenderableMetrics14(167, 72, this.getWidth(), this.getHeight() - 8, 0x7c29, 0x200c, 9, 8, 9, 8, (short)0x1000, (short)0x1000);
  }

  @Override
  protected void render(final int x, final int y) {
    uploadRenderable(this.background, x, y);
  }
}
