package legend.game.combat.effects;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;

public class ParticleEffectInstance94 {
  public final int index;

  /** ubyte */
  public int unused_00;
  /** ubyte */
  public int unused_01;
  /** ubyte */
  public int unused_02;

  public short framesUntilRender_04;
  public short scaleHorizontal_06;
  public short scaleVertical_08;
  public short scaleHorizontalStep_0a;
  public short scaleVerticalStep_0c;
  public short angle_0e;
  public short angleVelocity_10;
  public short ticksRemaining_12; // Scales colour, rotation, ... over time
  public short _14; // position x // Monoxide: not sure what these are, they're set to x/y/z at one point, but definitely not used as a position
  public short _16; // position y // Monoxide: actually, maybe multipurpose, depending on effect? Sometimes used as angles, sometimes seem to be used as position modifiers
  public short _18; // position z
  public final SVECTOR _1a = new SVECTOR(); // position noise velocity?
  public short verticalPositionScale_20;
  public short ticksUntilMovementModeChanges_22; // Once this ticks down to 0, it looks like the particle movement changes (starts to accelerate, ???)
  public short angleAcceleration_24;

  public final VECTOR translation_2c = new VECTOR();
  public final SVECTOR particlePositionCopy1 = new SVECTOR();
  public SVECTOR[] subParticlePositionsArray_44;
  public final SVECTOR particlePositionCopy2_48 = new SVECTOR();
  /** Translation of an attached model object? */
  public final SVECTOR particlePosition_50 = new SVECTOR();
  public final SVECTOR particleVelocity_58 = new SVECTOR();
  public final SVECTOR particleAcceleration_60 = new SVECTOR();
  /** particle rotation copied from effect manager */
  public final SVECTOR managerRotation_68 = new SVECTOR();
  public final SVECTOR spriteRotation_70 = new SVECTOR();
  public final SVECTOR spriteRotationStep_78 = new SVECTOR();
  public ParticleEffectInstance94Sub10[] particleInstanceSubArray_80;
  public int r_84;
  public int g_86;
  public int b_88;
  public int stepR_8a;
  public int stepG_8c;
  public int stepB_8e;
  public int flags_90;

  public ParticleEffectInstance94(final int index) {
    this.index = index;
  }
}
