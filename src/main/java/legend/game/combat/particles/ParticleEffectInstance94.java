package legend.game.combat.particles;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import org.joml.Vector3f;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.particles.ParticleManager.particleInitialTransformationMetrics_801198f0;

public abstract class ParticleEffectInstance94 {
  public final int index;
  public final ParticleEffectData98 particle;

//  /** ubyte */
//  public int unused_00;
//  /** ubyte */
//  public int unused_01;
//  /** ubyte */
//  public int unused_02;

  public short framesUntilRender_04;
  public float scaleHorizontal_06;
  public float scaleVertical_08;
  public float scaleHorizontalStep_0a;
  public float scaleVerticalStep_0c;
  public float angle_0e;
  public float angleVelocity_10;
  public short ticksRemaining_12; // Scales colour, rotation, ... over time

  // Polymorphic section TODO move into instances
  public short _14; // position x // Monoxide: not sure what these are, they're set to x/y/z at one point, but definitely not used as a position
  public short _16; // position y // Monoxide: actually, maybe multipurpose, depending on effect? Sometimes used as angles, sometimes seem to be used as position modifiers
  public short _18; // position z
  public short _1a;
  public short _1c;
  public short _1e;
  //

  public short verticalPositionScale_20;
  public short ticksUntilMovementModeChanges_22; // Once this ticks down to 0, it looks like the particle movement changes (starts to accelerate, ???)
  public float angleAcceleration_24;

  public final Vector3f managerTranslation_2c = new Vector3f();
  public final Vector3f originalParticlePosition_3c = new Vector3f();
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

  public ParticleEffectInstance94(final int index, final ParticleEffectData98 particle) {
    this.index = index;
    this.particle = particle;
  }

  @Method(0x80101308L)
  public void init() {
    final ParticleEffectData98 particle = this.particle;
    final ParticleEffectData98Inner24 effectInner = particle.effectInner_08;
    this.particlePosition_50.zero();
    this.particleVelocity_58.zero();
    this.scaleHorizontal_06 = 0.0f;
    this.scaleVertical_08 = 0.0f;
    this.scaleHorizontalStep_0a = 0.0f;
    this.scaleVerticalStep_0c = 0.0f;
    this.particleAcceleration_60.zero();
    this.stepR_8a = 0;
    this.stepG_8c = 0;
    this.stepB_8e = 0;
    final int behaviourType = effectInner.behaviourType_20;
    this.framesUntilRender_04 = (short)(seed_800fa754.nextInt(effectInner._14 + 1) + 1);
    this.ticksRemaining_12 = (short)((effectInner.particleInnerStuff_1c & 0xff_0000) >>> 16);
    final float colour = ((effectInner.particleInnerStuff_1c & 0xff00) >>> 8) / (float)0x80;
    this.r_84 = colour;
    this.g_86 = colour;
    this.b_88 = colour;
    this.flags_90 |= 0x1;
    this.angle_0e = seed_800fa754.nextFloat() * MathHelper.TWO_PI;
    this.angleVelocity_10 = seed_800fa754.nextFloat() * MathHelper.TWO_PI / 8.0f - MathHelper.TWO_PI / 16.0f;
    this.spriteRotation_70.x = seed_800fa754.nextFloat() * MathHelper.TWO_PI;
    this.spriteRotation_70.y = seed_800fa754.nextFloat() * MathHelper.TWO_PI;
    this.spriteRotation_70.z = seed_800fa754.nextFloat() * MathHelper.TWO_PI;
    this.spriteRotationStep_78.x = seed_800fa754.nextFloat() * MathHelper.TWO_PI / 32.0f - MathHelper.TWO_PI / 64.0f;
    this.spriteRotationStep_78.y = seed_800fa754.nextFloat() * MathHelper.TWO_PI / 32.0f - MathHelper.TWO_PI / 64.0f;
    this.spriteRotationStep_78.z = 0.0f;
    this.flags_90 = this.flags_90 & 0xffff_fff1 | (seed_800fa754.nextFloat() < 0.5f ? 0 : 0x8);

    final ParticleInitialTransformationMetrics10 metrics = particleInitialTransformationMetrics_801198f0[behaviourType];
    if(metrics.initialPositionMode_00 == 1) {
      //LAB_80101840
      final int angle = seed_800fa754.nextInt(4097);
      final short baseTranslationMagnitude = effectInner._10;
      this.particlePosition_50.x = rcos(angle) * baseTranslationMagnitude >> metrics.initialTranslationMagnitudeReductionFactor1_02;
      this.particlePosition_50.y = 0.0f;
      this.particlePosition_50.z = rsin(angle) * baseTranslationMagnitude >> metrics.initialTranslationMagnitudeReductionFactor1_02;
      //LAB_80101824
    } else if(metrics.initialPositionMode_00 == 2) {
      //LAB_801018c8
      final int angle = seed_800fa754.nextInt(4097);
      final int baseTranslationMagnitude = seed_800fa754.nextInt(effectInner._10 + 1);
      this.particlePosition_50.x = rcos(angle) * baseTranslationMagnitude >> metrics.initialTranslationMagnitudeReductionFactor1_02;
      this.particlePosition_50.y = 0.0f;
      this.particlePosition_50.z = rsin(angle) * baseTranslationMagnitude >> metrics.initialTranslationMagnitudeReductionFactor1_02;
    } else if(metrics.initialPositionMode_00 == 3) {
      //LAB_80101990
      this.particlePosition_50.y = seed_800fa754.nextInt(metrics.initialTranslationMagnitudeReductionFactor2_04 - metrics.initialTranslationMagnitudeReductionFactor1_02 + 1) + metrics.initialTranslationMagnitudeReductionFactor1_02;
    } else if(metrics.initialPositionMode_00 == 4) {
      //LAB_801019e4
      final int angle1 = seed_800fa754.nextInt(4097);
      final int angle2 = seed_800fa754.nextInt(2049);
      this.particlePosition_50.x = (rcos(angle1) * rsin(angle2) >> metrics.initialTranslationMagnitudeReductionFactor1_02) * effectInner._10 >> metrics.initialTranslationMagnitudeReductionFactor2_04;
      this.particlePosition_50.y = rcos(angle2) * effectInner._10 >> metrics.initialTranslationMagnitudeReductionFactor2_04;
      this.particlePosition_50.z = (rsin(angle1) * rsin(angle2) >> metrics.initialTranslationMagnitudeReductionFactor1_02) * effectInner._10 >> metrics.initialTranslationMagnitudeReductionFactor2_04;
    }

    //LAB_80101b10
    //LAB_80101b18
    this.initType();

    if(metrics.hasSpecialAccelerationHandling_06) {
      this.particleVelocity_58.x = this.particleVelocity_58.x * effectInner._18;
      this.particleVelocity_58.y = this.particleVelocity_58.y * effectInner._18;
      this.particleVelocity_58.z = this.particleVelocity_58.z * effectInner._18;
    }

    //LAB_80101ba4
    if(metrics.hasSpecialScaleStepHandling_07) {
      final float scaleStep = (byte)(seed_800fa754.nextInt(metrics.scaleStepUpperBound_09 - metrics.scaleStepLowerBound_08 + 1) + metrics.scaleStepLowerBound_08) / (float)0x1000;
      this.scaleHorizontalStep_0a = scaleStep;
      this.scaleVerticalStep_0c = scaleStep;
    }

    //LAB_80101c20
    this.particlePositionCopy2_48.set(this.particlePosition_50);
  }

  protected abstract void initType();

  @Method(0x800fd084L)
  private void updateParticleRotationTranslationColour(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.managerTranslation_2c.set(manager.params_10.trans_04);
    this.managerRotation_68.set(manager.params_10.rot_10);

    if(this.ticksRemaining_12 == 0) {
      this.ticksRemaining_12 = 1;
    }

    if((this.particle.effectInner_08.particleInnerStuff_1c & 0x400_0000) != 0) {
      this.r_84 = manager.params_10.colour_1c.x / (float)0x80;
      this.g_86 = manager.params_10.colour_1c.y / (float)0x80;
      this.b_88 = manager.params_10.colour_1c.z / (float)0x80;

      if((manager.params_10.flags_24 & 0x1) == 0) {
        this.stepR_8a = 0;
        this.stepG_8c = 0;
        this.stepB_8e = 0;
        return;
      }
    }

    //LAB_800fd18c
    //LAB_800fd0dc
    this.stepR_8a = this.r_84 / this.ticksRemaining_12;
    this.stepG_8c = this.g_86 / this.ticksRemaining_12;
    this.stepB_8e = this.b_88 / this.ticksRemaining_12;

    //LAB_800fd1d4
  }

  @Method(0x800fd460L)
  protected boolean tick(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.framesUntilRender_04--;

    if(this.framesUntilRender_04 > 0) {
      return false;
    }

    //LAB_800fd53c
    if(this.framesUntilRender_04 == 0)  {
      this.updateParticleRotationTranslationColour(manager);

      if((manager.params_10.flags_24 & 0x10) != 0) {
        this.particlePosition_50.y = 0.0f;

        if(this.particle.renderType_60 == 2 || this.particle.renderType_60 == 5) {
          //LAB_800fd4f0
          //LAB_800fd504
          for(int i = 0; i < this.particle.countParticleSub_54; i++) {
            this.subParticlePositionsArray_44[i].y = 0.0f;
          }
        }
      }

      //LAB_800fd520
      if((manager.params_10.flags_24 & 0x40) != 0) {
        this.particleVelocity_58.y = 0.0f;
      }
    }

    //LAB_800fd54c
    this.tickType(manager);

    if((this.flags_90 & 0x1) == 0) {
      return false;
    }

    if(this.ticksRemaining_12 > 0) {
      this.ticksRemaining_12--;
    }

    //LAB_800fd58c
    if(this.ticksRemaining_12 == 0 && (manager.params_10.flags_24 & 0x80) == 0) {
      this.flags_90 &= 0xffff_fffe;
      this.particle.reinitInstance(manager, this);
      return false;
    }

    //LAB_800fd5e0
    return true;
  }

  protected abstract void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager);

  @Method(0x800fd1dcL)
  protected void tickAttributes(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final Vector3f colour) {
    if((this.particle.effectInner_08.particleInnerStuff_1c & 0x800_0000) == 0 || (manager.params_10.flags_24 & 0x1) != 0) {
      //LAB_800fd23c
      this.r_84 -= this.stepR_8a;
      this.g_86 -= this.stepG_8c;
      this.b_88 -= this.stepB_8e;
    } else {
      this.r_84 = manager.params_10.colour_1c.x / 128.0f;
      this.g_86 = manager.params_10.colour_1c.y / 128.0f;
      this.b_88 = manager.params_10.colour_1c.z / 128.0f;
    }

    //LAB_800fd26c
    colour.x = this.r_84;
    colour.y = this.g_86;
    colour.z = this.b_88;

    this.particlePosition_50.add(this.particleVelocity_58);
    this.particleVelocity_58.add(this.particleAcceleration_60);

    if(this.particlePosition_50.y + this.managerTranslation_2c.y >= manager.params_10.y_30) {
      if((manager.params_10.flags_24 & 0x20) != 0) {
        this.ticksRemaining_12 = 1;
      }

      //LAB_800fd324
      if((manager.params_10.flags_24 & 0x8) != 0) {
        this.particlePosition_50.y = manager.params_10.y_30 - this.managerTranslation_2c.y;
        this.particleVelocity_58.y = -this.particleVelocity_58.y / 2.0f;
      }
    }

    //LAB_800fd358
    if((this.particle.effectInner_08.particleInnerStuff_1c & 0x200_0000) == 0) {
      this.scaleHorizontal_06 += this.scaleHorizontalStep_0a;
      this.scaleVertical_08 += this.scaleVerticalStep_0c;
    }

    //LAB_800fd38c
    if((this.particle.effectInner_08.particleInnerStuff_1c & 0x100_0000) == 0) {
      this.angle_0e += this.angleVelocity_10;
      this.spriteRotation_70.add(this.spriteRotationStep_78);
    } else {
      //LAB_800fd3e4
      this.angle_0e = MathHelper.psxDegToRad((short)(manager.params_10.flags_24 >>> 12 & 0xff0));
    }

    //LAB_800fd3f8
    this.particleVelocity_58.y += manager.params_10._2c >> 8;

    if(this.particle.scaleOrUseEffectAcceleration_6c) {
      this.particleVelocity_58.add(this.particle.effectAcceleration_70);
    }
    //LAB_800fd458
  }

  protected abstract void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager);

  /**
   * Sets a particle's position based on an angle and distance
   */
  @Method(0x800fb95cL)
  protected static void setParticlePositionAlongVector(final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rcos(particle._14) * particle._16 >> 12;
    particle.particlePosition_50.z = rsin(particle._14) * particle._16 >> 12;
  }
}
