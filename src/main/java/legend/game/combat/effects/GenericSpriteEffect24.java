package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.types.Translucency;
import org.joml.Vector3f;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public class GenericSpriteEffect24 {
  /** uint */
  public final int flags_00;
  /** short */
  public final int x_04;
  /** short */
  public final int y_06;
  /** ushort */
  public final int w_08;
  /** ushort */
  public final int h_0a;
  /** ushort */
  public final int tpage_0c;
  /** ubyte */
  public final int u_0e;
  /** ubyte */
  public final int v_0f;
  /** ushort */
  public final int clutX_10;
  /** ushort */
  public final int clutY_12;
  /** ubyte */
  public int r_14;
  /** ubyte */
  public int g_15;
  /** ubyte */
  public int b_16;

  /** short */
  public float scaleX_1c;
  /** short */
  public float scaleY_1e;
  public float angle_20;

  private Obj obj;
  private final MV transforms = new MV();

  public GenericSpriteEffect24(final int flags, final int x, final int y, final int w, final int h, final int tpage, final int clutX, final int clutY, final int u, final int v) {
    this.flags_00 = flags;
    this.x_04 = x;
    this.y_06 = y;
    this.w_08 = w;
    this.h_0a = h;
    this.tpage_0c = tpage;
    this.clutX_10 = clutX;
    this.clutY_12 = clutY;
    this.u_0e = u;
    this.v_0f = v;
  }

  public GenericSpriteEffect24(final int flags, final SpriteMetrics08 sprite) {
    this(flags, -sprite.w_04 / 2, -sprite.h_05 / 2, sprite.w_04, sprite.h_05, (sprite.v_02 & 0x100) >>> 4 | (sprite.u_00 & 0x3ff) >>> 6, sprite.clut_06 << 4 & 0x3ff, sprite.clut_06 >>> 6 & 0x1ff, (sprite.u_00 & 0x3f) * 4, sprite.v_02);
  }

  /**
   * Renderer for some kind of effect sprites like those in HUD DEFF.
   * Used for example for sprite effect overlays on red glow in Death Dimension.
   */
  @Method(0x800e7944L)
  public void render(final Vector3f translation, final int zMod) {
    if(this.flags_00 >= 0) { // No errors
      if(this.obj == null) {
        this.obj = new QuadBuilder("Generic sprite")
          .bpp(Bpp.BITS_4)
          .vramPos((this.tpage_0c & 0b1111) * 64, (this.tpage_0c & 0b10000) != 0 ? 256 : 0)
          .clut(this.clutX_10, this.clutY_12)
          .pos(this.x_04, this.y_06, 0.0f)
          .size(this.w_08, this.h_0a)
          .uv(this.u_0e, this.v_0f)
          .build();
      }

      final Vector3f finalTranslation = new Vector3f();
      translation.mul(worldToScreenMatrix_800c3548, finalTranslation);
      finalTranslation.add(worldToScreenMatrix_800c3548.transfer);

      // zMod needs to be ignored in z check or poly positions will overflow at low z values
      float z = zMod + finalTranslation.z / 4.0f;
      if(finalTranslation.z / 4.0f >= 40 && z >= 40) {
        if(z > 0x3ff8) {
          z = 0x3ff8;
        }

        final float x0 = MathHelper.safeDiv(finalTranslation.x * projectionPlaneDistance_1f8003f8, finalTranslation.z);
        final float y0 = MathHelper.safeDiv(finalTranslation.y * projectionPlaneDistance_1f8003f8, finalTranslation.z);

        //LAB_800e7a38
        final float zDepth = MathHelper.safeDiv(projectionPlaneDistance_1f8003f8 * 0x1000 / 4.0f, finalTranslation.z / 4.0f);

        this.transforms.transfer.set(GPU.getOffsetX() + x0, GPU.getOffsetY() + y0, z);
        this.transforms
          .rotationZ(this.angle_20)
          .scale(this.scaleX_1c / 8.0f * zDepth / 8.0f, this.scaleY_1e / 8.0f * zDepth / 8.0f, 1.0f);
        final QueuedModelStandard model = RENDERER.queueOrthoModel(this.obj, this.transforms, QueuedModelStandard.class)
          .colour(this.r_14 / 255.0f, this.g_15 / 255.0f, this.b_16 / 255.0f);

        if((this.flags_00 & 0x4000_0000) != 0) {
          model.translucency(Translucency.of(this.flags_00 >>> 28 & 0x3));
        }
      }
    }

    //LAB_800e7d8c
  }

  @Method(0x800e7ea4L)
  public void render(final Vector3f translation) {
    this.render(translation, 0);
  }

  public void delete() {
    if(this.obj != null) {
      this.obj.delete();
      this.obj = null;
    }
  }
}
