package legend.game.combat.particles;

import org.joml.Vector3f;

public class ParticleEffectInstance94 {
  public final int index;

  /** ubyte */
  public int unused_00;
  /** ubyte */
  public int unused_01;
  /** ubyte */
  public int unused_02;

  public short framesUntilRender_04;
  public float scaleHorizontal_06;
  public float scaleVertical_08;
  public float scaleHorizontalStep_0a;
  public float scaleVerticalStep_0c;
  public float angle_0e;
  public float angleVelocity_10;
  public short ticksRemaining_12; // Scales colour, rotation, ... over time
  public short _14; // position x // Monoxide: not sure what these are, they're set to x/y/z at one point, but definitely not used as a position
  public short _16; // position y // Monoxide: actually, maybe multipurpose, depending on effect? Sometimes used as angles, sometimes seem to be used as position modifiers
  public short _18; // position z
  public final Vector3f _1a = new Vector3f(); // position noise velocity?
  public short verticalPositionScale_20;
  public short ticksUntilMovementModeChanges_22; // Once this ticks down to 0, it looks like the particle movement changes (starts to accelerate, ???)
  public float angleAcceleration_24;

  public final Vector3f managerTranslation_2c = new Vector3f();
  public final Vector3f particlePositionCopy1 = new Vector3f();
  public Vector3f[] subParticlePositionsArray_44;
  public final Vector3f particlePositionCopy2_48 = new Vector3f();
  /** Translation of an attached model object? */
  public final Vector3f particlePosition_50 = new Vector3f();
  public final Vector3f particleVelocity_58 = new Vector3f();
  public final Vector3f particleAcceleration_60 = new Vector3f();
  /** particle rotation copied from effect manager */
  public final Vector3f managerRotation_68 = new Vector3f();
  public final Vector3f spriteRotation_70 = new Vector3f();
  public final Vector3f spriteRotationStep_78 = new Vector3f();
  public ParticleEffectInstance94Sub10[] particleInstanceSubArray_80;
  public float r_84;
  public float g_86;
  public float b_88;
  public float stepR_8a;
  public float stepG_8c;
  public float stepB_8e;
  public int flags_90;

  public ParticleEffectInstance94(final int index) {
    this.index = index;
  }
}
