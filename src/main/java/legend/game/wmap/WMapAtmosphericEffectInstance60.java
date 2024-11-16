package legend.game.wmap;

import legend.core.gpu.Bpp;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.types.Translucency;
import org.joml.Vector3f;

public class WMapAtmosphericEffectInstance60 {
  public static final int[] snowUs = {32, 40, 32, 40, 32, 40};
  public static final int[] snowVs = {16, 16, 24, 24, 24, 16};

  public final MV transforms = new MV();
  public float queueZ;

  public final GsCOORDINATE2 coord2_00 = new GsCOORDINATE2();
  /** Originally vector rotation_50 */
  public float snowTick_50;
  /** Was short x_58, short y_5a, byte z_5e */
  public final Vector3f translation_58 = new Vector3f();
  /** short */
  public float brightness_5c;

  public void set(final WMapAtmosphericEffectInstance60 other) {
    this.coord2_00.set(other.coord2_00);
    this.snowTick_50 = other.snowTick_50;
    this.translation_58.set(other.translation_58);
    this.brightness_5c = other.brightness_5c;
  }

  public static Obj[] buildCloudSprites() {
    final Obj[] cloudArray = new Obj[3];
    for(int i = 0; i < 3; i++) {
      cloudArray[i] = new QuadBuilder("Cloud (index " + i + ')')
        .bpp(Bpp.BITS_4)
        .clut(576, 496 + i % 3)
        .vramPos(576, 256)
        .size(1.0f, 1.0f)
        .uv(0, i % 3 * 64)
        .uvSize(255, 64)
        .translucency(Translucency.B_PLUS_F)
        .build();
    }

    return cloudArray;
  }

  public static Obj[] buildSnowSprites() {
    final Obj[] snowArray = new Obj[6];
    for(int i = 0; i < 6; i++) {
      snowArray[i] = new QuadBuilder("Snowflake (index " + i + ')')
        .bpp(Bpp.BITS_4)
        .clut(640, 496)
        .vramPos(640, 256)
        .size(1.0f, 1.0f)
        .uv(WMapAtmosphericEffectInstance60.snowUs[i], WMapAtmosphericEffectInstance60.snowVs[i])
        .uvSize(8, 8)
        .translucency(Translucency.B_PLUS_F)
        .monochrome(1.0f)
        .build();
    }

    return snowArray;
  }
}
