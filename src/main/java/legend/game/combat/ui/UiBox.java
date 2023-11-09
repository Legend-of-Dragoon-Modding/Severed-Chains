package legend.game.combat.ui;

import legend.core.gpu.Bpp;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.combat.environment.BattleHudBorderMetrics14;
import legend.game.types.Translucency;
import org.joml.Vector3f;

import static legend.core.GameEngine.RENDERER;
import static legend.game.combat.Bttl_800c.battleHudBorderMetrics_800c6f4c;

public class UiBox {
  private final Obj hudBackgroundObj;
  private final Obj hudBackgroundButDarkerObj;
  private final Obj[] hudBackgroundBorders = new Obj[8];

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
      .pos(x, y, 124.0f)
      .size(width, height)
      .build();

    // Darkening overlay
    this.hudBackgroundButDarkerObj = new QuadBuilder(name + " Background Darkening Overlay")
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0, 0.0f)
      .pos(x, y, 124.0f)
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

    //LAB_800f1060
    for(int i = 0; i < 8; i++) {
      final BattleHudBorderMetrics14 borderMetrics = battleHudBorderMetrics_800c6f4c.get(i);

      final int leftX;
      final int rightX;
      final int leftU;
      final int rightU;
      final int topY = ys[borderMetrics.indexXy0_00.get()] - borderMetrics.offsetY_0a.get();
      final int bottomY = ys[borderMetrics.indexXy1_02.get()] + borderMetrics.offsetY_0a.get();
      final int topV = borderMetrics.v_06.get();
      final int bottomV = topV + borderMetrics.h_0e.get();

      if(i == 5 || i == 7) {
        //LAB_800f10ac
        leftX = xs[borderMetrics.indexXy1_02.get()] + borderMetrics.offsetX_08.get();
        rightX = xs[borderMetrics.indexXy0_00.get()] - borderMetrics.offsetX_08.get();
        rightU = borderMetrics.u_04.get();
        leftU = rightU + borderMetrics.w_0c.get() - 1;
      } else {
        //LAB_800f1128
        leftX = xs[borderMetrics.indexXy0_00.get()] - borderMetrics.offsetX_08.get();
        rightX = xs[borderMetrics.indexXy1_02.get()] + borderMetrics.offsetX_08.get();
        leftU = borderMetrics.u_04.get();
        rightU = leftU + borderMetrics.w_0c.get();
      }

      if(this.hudBackgroundBorders[i] == null) {
        this.hudBackgroundBorders[i] = new QuadBuilder("Battle UI Border " + i)
          .bpp(Bpp.BITS_4)
          .clut(720, 497)
          .vramPos(704, 256)
          .monochrome(0.5f)
          .uv(leftU, topV)
          .pos(leftX, topY, 124.0f)
          .posSize(rightX - leftX, bottomY - topY)
          .uvSize(rightU - leftU, bottomV - topV)
          .build();
      }
    }
  }

  public void render() {
    this.render(1.0f, 1.0f, 1.0f);
  }

  public void render(final Vector3f colour) {
    this.render(colour.x, colour.y, colour.z);
  }

  public void render(final float r, final float g, final float b) {
    RENDERER.queueOrthoOverlayModel(this.hudBackgroundButDarkerObj);
    RENDERER.queueOrthoOverlayModel(this.hudBackgroundObj)
      .colour(r, g, b);

    for(int i = 0; i < this.hudBackgroundBorders.length; i++) {
      RENDERER.queueOrthoOverlayModel(this.hudBackgroundBorders[i]);
    }
  }

  public void delete() {
    this.hudBackgroundObj.delete();
    this.hudBackgroundButDarkerObj.delete();

    for(final Obj border : this.hudBackgroundBorders) {
      border.delete();
    }
  }
}
