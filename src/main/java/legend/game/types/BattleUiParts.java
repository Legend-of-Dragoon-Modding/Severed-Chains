package legend.game.types;

import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;

import javax.annotation.Nullable;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.levelUpUs_8001032c;

public class BattleUiParts {
  private Obj obj;
  private final MV mv = new MV();

  private int levelUpVert;

  public void init() {
    final QuadBuilder builder = new QuadBuilder("Battle UI Parts");

    this.levelUpVert = builder.currentQuadIndex() * 4;

    for(final int u : levelUpUs_8001032c) {
      builder
        .add()
        .bpp(Bpp.BITS_4)
        .size(7, 16)
        .uv(u, 64);
    }

    this.obj = builder.build();
    this.obj.persistent = true;
  }

  public void queueLevelUp(final BattleReportOverlay0e overlay) {
    this.queue(
      this.levelUpVert + overlay.letterIndex * 4,
      overlay.x_00, overlay.y_02,
      7, 16,
      overlay.clutAndTranslucency_0c,
      Translucency.of((overlay.clutAndTranslucency_0c >>> 12 & 0x7) - 1),
      0xff,
      overlay.widthScale_04 / (float)0x1000 + 1.0f,
      overlay.heightScale_06 / (float)0x1000 + 1.0f);
  }

  public void queue(final int vertexIndex, final int x, final int y, final int w, final int h, final int packedClut, @Nullable final Translucency transMode, final int brightness, final float widthScale, final float heightScale) {
    final float left = x + w / 2.0f;
    final float top = y + h / 2.0f;
    final float offsetX = w * widthScale / 2.0f;
    final float offsetY = h * heightScale / 2.0f;

    final int clutOffsetX = packedClut / 16;
    final int clutOffsetY = packedClut % 16;
    final int clutX;
    final int clutY;
    if(clutOffsetX >= 4) {
      clutX = clutOffsetX * 16 + 832;
      clutY = clutOffsetY + 304;
    } else {
      clutX = clutOffsetX * 16 + 704;
      clutY = clutOffsetY + 496;
    }

    this.mv.scaling(widthScale, heightScale, 1.0f);
    this.mv.transfer.set(left - offsetX + displayWidth_1f8003e0 / 2.0f, top - offsetY + displayHeight_1f8003e4 / 2.0f, 2.0f);
    final RenderEngine.QueuedModel<?> model = RENDERER.queueOrthoModel(this.obj, this.mv)
      .vertices(vertexIndex, 4)
      .clutOverride(clutX & 0x3f0, clutY)
      .tpageOverride(704, 256)
      .monochrome(brightness / 255.0f);

    if(transMode != null) {
      model.translucency(transMode);
    }
  }
}
