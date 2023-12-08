package legend.game.combat.effects;

public class GenericSpriteEffect24 {
  /** uint */
  public int flags_00;
  /** short */
  public short x_04;
  /** short */
  public short y_06;
  /** ushort */
  public int w_08;
  /** ushort */
  public int h_0a;
  /** ushort */
  public int tpage_0c;
  /** ubyte */
  public int u_0e;
  /** ubyte */
  public int v_0f;
  /** ushort */
  public int clutX_10;
  /** ushort */
  public int clutY_12;
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

  public GenericSpriteEffect24() {

  }

  public GenericSpriteEffect24(final int flags, final SpriteMetrics08 sprite) {
    this.flags_00 = flags;
    this.x_04 = (short)(-sprite.w_04 / 2);
    this.y_06 = (short)(-sprite.h_05 / 2);
    this.w_08 = sprite.w_04;
    this.h_0a = sprite.h_05;
    this.tpage_0c = (sprite.v_02 & 0x100) >>> 4 | (sprite.u_00 & 0x3ff) >>> 6;
    this.u_0e = (sprite.u_00 & 0x3f) * 4;
    this.v_0f = sprite.v_02;
    this.clutX_10 = sprite.clut_06 << 4 & 0x3ff;
    this.clutY_12 = sprite.clut_06 >>> 6 & 0x1ff;
  }
}
