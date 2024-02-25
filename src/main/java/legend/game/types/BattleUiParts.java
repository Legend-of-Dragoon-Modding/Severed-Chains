package legend.game.types;

import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.combat.effects.ButtonPressHudMetrics06;

import javax.annotation.Nullable;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.levelUpUs_8001032c;
import static legend.game.combat.Battle.buttonPressHudMetrics_800faaa0;

public class BattleUiParts {
  private Obj obj;
  private final MV mv = new MV();

  private int levelUpVert;
  private int bigNumberVert;
  private int pointsVert;
  private int textVert;
  private int buttonVert;

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

    this.bigNumberVert = builder.currentQuadIndex() * 4;

    for(int i = 0; i < 13; i++) {
      builder
        .add()
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_PLUS_F)
        .size(8, 16)
        .uv(16 + i * 8, 40);
    }

    this.pointsVert = builder.currentQuadIndex() * 4;

    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(32, 16)
      .uv(120, 40);

    this.textVert = builder.currentQuadIndex() * 4;

    for(int i = 0; i < 65; i++) {
      builder
        .add()
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_PLUS_F)
        .size(12, 12)
        .uv(i % 21 * 12, 144 + i / 21 * 12);
    }

    this.buttonVert = builder.currentQuadIndex() * 4;

    for(final ButtonPressHudMetrics06 metrics : buttonPressHudMetrics_800faaa0) {
      builder
        .add()
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_PLUS_F)
        .uv(metrics.u_01, metrics.v_02);

      if(metrics.hudElementType_00 == 0) {
        builder.size(metrics.wOrRightU_03, metrics.hOrBottomV_04);
      } else {
        final int w = metrics.wOrRightU_03 - metrics.u_01;
        final int h = metrics.hOrBottomV_04 - metrics.v_02;
        builder.posSize(Math.abs(w), Math.abs(h));
        builder.uvSize(w, h);
      }
    }

    this.obj = builder.build();
    this.obj.persistent = true;
  }

  /** Also includes +, S, P as indices 10, 11, 12 */
  public void queueBigNumber(final int digit, final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness, final float widthScale, final float heightScale) {
    this.queue(this.bigNumberVert + digit * 4, x, y, 8, 16, packedClut, translucency, brightness, widthScale, heightScale);
  }

  public void queuePoints(final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness, final float widthScale, final float heightScale) {
    this.queue(this.pointsVert, x, y, 8, 16, packedClut, translucency, brightness, widthScale, heightScale);
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

  public void queueLetter(final int index, final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness, final float widthScale, final float heightScale) {
    this.queue(this.textVert + index * 4, x, y, 12, 12, packedClut, translucency, brightness, widthScale, heightScale);
  }

  public void queueButton(final int index, final int x, final int y, @Nullable final Translucency translucency, final int brightness, final float widthScale, final float heightScale) {
    final ButtonPressHudMetrics06 metrics = buttonPressHudMetrics_800faaa0[index];

    int w = metrics.wOrRightU_03;
    int h = metrics.hOrBottomV_04;

    if(metrics.hudElementType_00 == 1) {
      w -= metrics.u_01;
      h -= metrics.v_02;
    }

    this.queue(this.buttonVert + index * 4, x, y, w, h, metrics.packedClut_05, translucency, brightness, widthScale, heightScale);
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
      .monochrome(brightness / 255.0f)
      .translucency(transMode);
  }
}
