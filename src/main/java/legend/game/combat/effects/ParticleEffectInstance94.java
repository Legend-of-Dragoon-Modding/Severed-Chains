package legend.game.combat.effects;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;

public class ParticleEffectInstance94 {
  public final int index;

  /** ubyte */
  public int _00;
  /** ubyte */
  public int _01;
  /** ubyte */
  public int _02;

  public short framesUntilRender_04;
  public short _06; // instance scale x?
  public short _08; // instance scale y?
  public short _0a; // instance scale z?
  public short _0c;
  public short _0e;
  public short _10;
  public short _12; // Might be a color step count
  public short _14; // position x
  public short _16; // position y
  public short _18; // position z
  public final SVECTOR _1a = new SVECTOR(); // position noise velocity?
  public short _20;
  public short _22;
  public short _24;

  public final VECTOR _2c = new VECTOR();
  /** particle position copied from effect */
  public final SVECTOR _3c = new SVECTOR();
  public SVECTOR[] _44;
  public final SVECTOR _48 = new SVECTOR(); // particle position (origin?)
  /** Translation of an attached model object? */
  public final SVECTOR particlePosition_50 = new SVECTOR();
  public final SVECTOR particleVelocity_58 = new SVECTOR();
  public final SVECTOR particleAcceleration_60 = new SVECTOR();
  public final SVECTOR rot_68 = new SVECTOR();
  public final SVECTOR _70 = new SVECTOR();
  public final SVECTOR _78 = new SVECTOR();
  public ParticleEffectInstance94Sub10[] particleInstanceSubArray_80;
  public int r_84;
  public int g_86;
  public int b_88;
  public int stepR_8a;
  public int stepG_8c;
  public int stepB_8e;
  public int _90;

  public ParticleEffectInstance94(final int index) {
    this.index = index;
  }
}
