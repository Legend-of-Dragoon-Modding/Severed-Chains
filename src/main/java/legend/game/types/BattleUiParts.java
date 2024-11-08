package legend.game.types;

import legend.core.QueuedModelStandard;
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
import static legend.game.combat.Battle.asciiTable_800fa788;
import static legend.game.combat.Battle.buttonPressHudMetrics_800faaa0;
import static legend.game.combat.SEffe.daddyHudMeterDimensions_800fb82c;
import static legend.game.combat.SEffe.daddyHudMeterOffsets_800fb804;
import static legend.game.combat.SEffe.daddyHudMeterUvs_800fb818;
import static legend.game.combat.SEffe.perfectDaddyGlyphUs_80119fbc;
import static legend.game.combat.SEffe.perfectDaddyGlyphVs_80119fc4;

public class BattleUiParts {
  private Obj obj;
  private final MV mv = new MV();

  private int levelUpVert;
  private int bigNumberVert;
  private int pointsVert;
  private int textVert;
  private int buttonVert;
  private int daddyMeterVert;
  private int daddyFrameVert;
  private int daddyArrowVert;
  private int daddyDarkEyeVert;
  private int daddyFlatCenterVert;
  private int daddyEyeFlashVert;
  private int daddyDivineIrisVert;
  private int daddyStarVert;
  private int daddyButtonGlowVert;
  private int daddyPerfectVert;

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

    for(int i = 0; i < asciiTable_800fa788.length; i++) {
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

    this.daddyFrameVert = builder.currentQuadIndex() * 4;

    // Frame top portion
    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(64, 48)
      .pos(0.0f, 0.0f, 10.0f)
      .uv(160, 192);

    // Frame bottom portion
    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(42, 8)
      .pos(8.0f, 48.0f, 10.0f)
      .uv(200, 80);

    this.daddyArrowVert = builder.currentQuadIndex() * 4;

    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(8, 24)
      .pos(32.0f, -4.0f, 0.0f)
      .uv(152, 208);

    this.daddyDarkEyeVert = builder.currentQuadIndex() * 4;

    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(31, 31)
      .pos(18.0f, 16.0f, 8.0f)
      .uv(224, 208);

    this.daddyFlatCenterVert = builder.currentQuadIndex() * 4;

    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(40, 40)
      .pos(17.0f, 14.0f, 9.0f)
      .uv(112, 200);

    this.daddyMeterVert = builder.currentQuadIndex() * 4;

    for(int i = 0; i < 5; i++) {
      builder
        .add()
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_PLUS_F)
        .size(daddyHudMeterDimensions_800fb82c[i][0], daddyHudMeterDimensions_800fb82c[i][1])
        .pos(daddyHudMeterOffsets_800fb804[i][0], daddyHudMeterOffsets_800fb804[i][1], 0.0f)
        .uv(daddyHudMeterUvs_800fb818[i][0], daddyHudMeterUvs_800fb818[i][1]);
    }

    this.daddyEyeFlashVert = builder.currentQuadIndex() * 4;

    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(31, 31)
      .pos(18.0f, 16.0f, 2.0f)
      .uv(224, 208);

    this.daddyDivineIrisVert = builder.currentQuadIndex() * 4;

    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(31, 31)
      .pos(23.0f, 21.0f, 1.0f)
      .uv(232, 120);

    this.daddyStarVert = builder.currentQuadIndex() * 4;

    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(16, 16)
      .pos(0.0f, 0.0f, 0.0f)
      .uv(128, 64);

    this.daddyButtonGlowVert = builder.currentQuadIndex() * 4;

    builder
      .add()
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F)
      .size(23, 23)
      .pos(-2.0f, -5.0f, 0.0f)
      .uv(232, 120);

    this.daddyPerfectVert = builder.currentQuadIndex() * 4;

    for(int i = 0; i < 8; i++) {
      builder
        .add()
        .bpp(Bpp.BITS_4)
        .translucency(Translucency.B_PLUS_F)
        .size(8, 16)
        .uv(perfectDaddyGlyphUs_80119fbc[i], perfectDaddyGlyphVs_80119fc4[i]);
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

  public void queueDaddyFrame(final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness) {
    this.queue(this.daddyFrameVert, x, y, 0, 0, packedClut, translucency, brightness, 1.0f, 1.0f);
    this.queue(this.daddyFrameVert + 4, x, y, 0, 0, packedClut, translucency, brightness, 1.0f, 1.0f);
  }

  public void queueDaddyArrow(final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness) {
    this.queue(this.daddyArrowVert, x, y, 0, 0, packedClut, translucency, brightness, 1.0f, 1.0f);
  }

  public void queueDaddyDarkEye(final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness) {
    this.queue(this.daddyDarkEyeVert, x, y, 0, 0, packedClut, translucency, brightness, 1.0f, 1.0f);
  }

  public void queueDaddyFlatCenter(final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness) {
    this.queue(this.daddyFlatCenterVert, x, y, 0, 0, packedClut, translucency, brightness, 1.0f, 1.0f);
  }

  public void queueDaddyMeter(final int index, final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness) {
    this.queue(this.daddyMeterVert + index * 4, x, y, 0, 0, packedClut, translucency, brightness, 1.0f, 1.0f);
  }

  public void queueDaddyEyeFlash(final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness) {
    this.queue(this.daddyEyeFlashVert, x, y, 0, 0, packedClut, translucency, brightness, 1.0f, 1.0f);
  }

  public void queueDaddyDivineIris(final int x, final int y) {
    this.queue(this.daddyDivineIrisVert, x, y, 0, 0, 0xc, Translucency.B_PLUS_F, 0x80, 0.5f, 1.5f);
  }

  public void queueDaddyStar(final int x, final int y, final int packedClut, @Nullable final Translucency translucency, final int brightness) {
    this.queue(this.daddyStarVert, x, y, 0, 0, packedClut, translucency, brightness, 1.0f, 1.0f);
  }

  public void queueDaddyButtonGlow(final int x, final int y, final int brightness, final float widthScale, final float heightScale) {
    this.queue(this.daddyButtonGlowVert, x, y, 23, 23, 0xc, Translucency.B_PLUS_F, brightness, widthScale, heightScale);
  }

  public void queueDaddyPerfect(final int index, final int x, final int y, final int brightness) {
    this.queue(this.daddyPerfectVert + index * 4, x, y, 8, 16, 0x29, Translucency.B_PLUS_F, brightness, 1.0f, 1.0f);
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
    RENDERER.queueOrthoModel(this.obj, this.mv, QueuedModelStandard.class)
      .vertices(vertexIndex, 4)
      .clutOverride(clutX & 0x3f0, clutY)
      .tpageOverride(704, 256)
      .monochrome(brightness / 128.0f)
      .translucency(transMode);
  }
}
