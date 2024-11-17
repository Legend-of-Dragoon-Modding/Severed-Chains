package legend.game.combat.ui;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.combat.environment.BattleHudBorderMetrics14;
import legend.game.types.Translucency;
import org.joml.Vector3f;

import static legend.core.GameEngine.RENDERER;

public class UiBox {
  private static final BattleHudBorderMetrics14[] battleHudBorderMetrics_800c6f4c = {
    new BattleHudBorderMetrics14(0, 1, 200, 48, 0, 4, 48, 8),
    new BattleHudBorderMetrics14(0, 2, 0, 128, 4, 0, 8, 15),
    new BattleHudBorderMetrics14(1, 3, 8, 128, 4, 1, 8, 15),
    new BattleHudBorderMetrics14(2, 3, 200, 88, 0, 4, 48, 8),
    new BattleHudBorderMetrics14(0, 0, 192, 48, 4, 4, 8, 8),
    new BattleHudBorderMetrics14(1, 1, 248, 48, 4, 4, 8, 8),
    new BattleHudBorderMetrics14(2, 2, 192, 88, 4, 4, 8, 8),
    new BattleHudBorderMetrics14(3, 3, 248, 88, 4, 4, 8, 8),
  };

  private final Obj hudBackgroundObj;
  private final Obj hudBackgroundButDarkerObj;
  private Obj hudBackgroundBorders;
  private final MV transforms = new MV();

  public UiBox(final String name, final int x, final int y, final int width, final int height) {
    this(name, x, y, width, height, 1.0f, 1.0f, 1.0f);
  }

  public UiBox(final String name, final int x, final int y, final int width, final int height, final Vector3f colour) {
    this(name, x, y, width, height, colour.x, colour.y, colour.z);
  }

  @Method(0x800f1268L) // buildBattleHudBackground
  public UiBox(final String name, final int x, final int y, final int width, final int height, final float r, final float g, final float b) {
    // Gradient
    this.hudBackgroundObj = new QuadBuilder(name + " Background")
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0, 0.0f)
      .rgb(1, r, g, b)
      .rgb(2, r, g, b)
      .monochrome(3, 0.0f)
      .pos(x, y, 0.0f)
      .size(width, height)
      .build();

    // Darkening overlay
    this.hudBackgroundButDarkerObj = new QuadBuilder(name + " Background Darkening Overlay")
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0, 0.0f)
      .pos(x, y, 0.0f)
      .size(width, height)
      .build();

    this.buildBattleHudBorder(x, y, x + width, y + height);
  }

  @Method(0x800f0f5cL)
  private void buildBattleHudBorder(final int x0, final int y0, final int x1, final int y1) {
    //LAB_800f0fe4
    //LAB_800f0fe8
    //LAB_800f1014
    final int[] xs = new int[4];
    final int[] ys = new int[4];
    xs[0] = x0 + 1;
    ys[0] = y0;
    xs[1] = x1 - 1;
    ys[1] = y0;
    xs[2] = x0 + 1;
    ys[2] = y1;
    xs[3] = x1 - 1;
    ys[3] = y1;

    final QuadBuilder builder = new QuadBuilder("Battle UI Border")
      .bpp(Bpp.BITS_4);

    //LAB_800f1060
    for(int i = 0; i < 8; i++) {
      final BattleHudBorderMetrics14 borderMetrics = battleHudBorderMetrics_800c6f4c[i];

      final int leftX;
      final int rightX;
      final int leftU;
      final int rightU;
      final int topY = ys[borderMetrics.indexXy0_00] - borderMetrics.offsetY_0a;
      final int bottomY = ys[borderMetrics.indexXy1_02] + borderMetrics.offsetY_0a;
      final int topV = borderMetrics.v_06;
      final int bottomV = topV + borderMetrics.h_0e;

      if(i == 5 || i == 7) {
        //LAB_800f10ac
        leftX = xs[borderMetrics.indexXy1_02] + borderMetrics.offsetX_08;
        rightX = xs[borderMetrics.indexXy0_00] - borderMetrics.offsetX_08;
        rightU = borderMetrics.u_04;
        leftU = rightU + borderMetrics.w_0c - 1;
      } else {
        //LAB_800f1128
        leftX = xs[borderMetrics.indexXy0_00] - borderMetrics.offsetX_08;
        rightX = xs[borderMetrics.indexXy1_02] + borderMetrics.offsetX_08;
        leftU = borderMetrics.u_04;
        rightU = leftU + borderMetrics.w_0c;
      }

      builder
        .add()
        .clut(720, 497)
        .vramPos(704, 256)
        .monochrome(0.5f)
        .uv(leftU, topV)
        .pos(leftX, topY, 0.0f)
        .posSize(rightX - leftX, bottomY - topY)
        .uvSize(rightU - leftU, bottomV - topV);
    }

    this.hudBackgroundBorders = builder.build();
  }

  public void render() {
    this.render(1.0f, 1.0f, 1.0f);
  }

  public void render(final Vector3f colour) {
    this.render(colour.x, colour.y, colour.z);
  }

  public void render(final float r, final float g, final float b) {
    this.transforms.transfer.set(0.0f, 0.0f, 125.0f);

    RENDERER.queueOrthoModel(this.hudBackgroundButDarkerObj, this.transforms, QueuedModelStandard.class);
    RENDERER.queueOrthoModel(this.hudBackgroundObj, this.transforms, QueuedModelStandard.class)
      .colour(r, g, b);

    for(int i = 0; i < 8; i++) {
      RENDERER.queueOrthoModel(this.hudBackgroundBorders, this.transforms, QueuedModelStandard.class)
        .vertices(i * 4, 4);
    }
  }

  public void delete() {
    this.hudBackgroundObj.delete();
    this.hudBackgroundButDarkerObj.delete();
    this.hudBackgroundBorders.delete();
  }
}
