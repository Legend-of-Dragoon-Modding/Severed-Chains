package legend.game.combat.particles;

import legend.core.MathHelper;
import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.core.memory.types.QuadConsumer;
import legend.core.memory.types.TriConsumer;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TmdObjLoader;
import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.deff.DeffPart;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.effects.SpriteMetrics08;
import legend.game.combat.environment.BattleCamera;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.ScriptState;
import legend.game.tmd.Renderer;
import legend.game.types.CContainer;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiConsumer;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8003.GetClut;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Battle.ZERO;
import static legend.game.combat.Battle.deffManager_800c693c;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.SEffe.FUN_800cfc20;
import static legend.game.combat.SEffe.FUN_800e61e4;
import static legend.game.combat.SEffe.FUN_800e62a8;
import static legend.game.combat.SEffe.allocateEffectManager;
import static legend.game.combat.SEffe.getModelObjectTranslation;
import static legend.game.combat.SEffe.rotateAndTranslateEffect;
import static legend.game.combat.SEffe.scriptGetScriptedObjectPos;

public class ParticleManager {
  private static final short[] particleSubCounts_800fb794 = {0, 0, 4, 0, 8, 0, 16, 0, 0, 0, 1, 0, 2, 0, 4, 0, 3, 0, 5, 0};

  private int currentParticleIndex_8011a008;
  private ParticleEffectData98 firstParticle_8011a00c;
  private ParticleEffectData98 lastParticle_8011a010;

  private final BattleCamera camera;

  public ParticleManager(final BattleCamera camera) {
    this.camera = camera;
  }

  public ParticleEffectData98 allocateParticle(final ScriptState<? extends BattleObject> parent, final int type, final int particleCount, final int particleTypeId, final int _10, final int _14, final int _18, final int innerStuff, final int scriptIndex, final int parentScriptIndex) {
    final ParticleEffectData98 particle = new ParticleEffectData98(particleCount);

    final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state = allocateEffectManager(
      "Particle effect %x".formatted(particleTypeId),
      parent,
      null,
      this.particleEffectRenderers_80119b7c[particleTypeId >> 20],
      this::particleEffectDestructor,
      particle,
      new EffectManagerParams.ParticleType()
    );

    particle.myState_00 = state;
    final EffectManagerData6c<EffectManagerParams.ParticleType> manager = state.innerStruct_00;
    manager.params_10.flags_00 |= 0x5000_0000;
    manager.flags_04 |= 0x4_0000;

    if(this.firstParticle_8011a00c == null) {
      this.firstParticle_8011a00c = particle;
    }

    //LAB_801021a8
    if(this.lastParticle_8011a010 != null) {
      this.lastParticle_8011a010.next_94 = particle;
    }

    this.lastParticle_8011a010 = particle;

    //LAB_801021c0
    particle.parentScriptIndex_04 = parentScriptIndex;
    particle.particleInstancePrerenderCallback_84 = this.particleInstancePrerenderCallbacks_80119bac[type];
    particle.particleInstanceTickCallback_88 = this.particleInstanceTickCallbacks_80119cb0[type];
    particle.countFramesRendered_52 = 0;
    particle.halfW_34 = 0;
    particle.halfH_36 = 0;
    particle.scaleOrUseEffectAcceleration_6c = false;
    particle.next_94 = null;
    particle.initializerCallback_8c = this.initializerCallbacks_80119db4[type];

    //LAB_8010223c
    particle.effectInner_08.scriptIndex_00 = scriptIndex;
    particle.effectInner_08.parentScriptIndex_04 = parentScriptIndex;
    particle.effectInner_08.particleTypeId_08 = particleTypeId;
    particle.effectInner_08.totalCountParticleInstance_0c = (short)particleCount;
    particle.effectInner_08._10 = (short)_10;
    particle.effectInner_08._14 = (short)_14;
    particle.effectInner_08._18 = (short)_18 / (float)0x100;
    particle.effectInner_08.particleInnerStuff_1c = innerStuff;
    particle.effectInner_08.callbackIndex_20 = (short)type;

    //LAB_80102278
    for(int i = 0; i < particle.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 inst = particle.particleArray_68[i];
      this.currentParticleIndex_8011a008 = i;
      this.initializeParticleInstance(particle, inst, particle.effectInner_08);
      inst.particlePositionCopy1.set(inst.particlePosition_50);
    }

    //final EffectData98Sub94 s2_0 = effect._68[effect.count_50]; // This looks like a retail bug - index out of bounds

    //LAB_801022b4
    if(particle.callback90Type_61 != 0) {
      particle.particleInstanceReconstructorCallback_90 = this::FUN_801012d4;
    } else {
      //LAB_801022cc
      particle.particleInstanceReconstructorCallback_90 = this::FUN_801012a0;
    }

    //LAB_801022d4
    particle.countParticleSub_54 = 0;
    this.subParticleInitializers_80119b94[particleTypeId >> 20].accept(particle, null/*s2_0 see above*/, particle.effectInner_08, particleTypeId);
    return particle;
  }

  @Method(0x800fce10L)
  private void renderLineParticles(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleMetrics48 particleMetrics) {
    if(particleMetrics.flags_00 >= 0) {
      GPU.queueCommand((particleMetrics.z_04 + manager.params_10.z_22) / 4.0f, new GpuCommandLine()
        .translucent(Translucency.B_PLUS_F)
        .rgb(0, (int)(particleMetrics.colour0_40.x * 0xff), (int)(particleMetrics.colour0_40.y * 0xff), (int)(particleMetrics.colour0_40.z * 0xff))
        .rgb(1, (int)(particleMetrics.colour1_44.x * 0xff), (int)(particleMetrics.colour1_44.y * 0xff), (int)(particleMetrics.colour1_44.z * 0xff))
        .pos(0, particleMetrics.x0_08, particleMetrics.y0_10)
        .pos(1, particleMetrics.x1_0c, particleMetrics.y1_14)
      );
    }
    //LAB_800fcf08
  }

  @Method(0x800fcf18L)
  private void FUN_800fcf18(final EffectManagerData6c<EffectManagerParams.ParticleType> a0, final ParticleMetrics48 a1) {
    // no-op
  }

  @Method(0x800fe878L)
  @Nullable
  private ParticleEffectData98 findParticleParent(final ParticleEffectData98 effect) {
    if(this.firstParticle_8011a00c == effect) {
      return null;
    }

    //LAB_800fe894
    ParticleEffectData98 parent = this.firstParticle_8011a00c;
    do {
      if(parent.next_94 == effect) {
        break;
      }

      parent = parent.next_94;
    } while(parent != null);

    //LAB_800fe8b0
    return parent;
  }

  @Method(0x800fe8b8L)
  private void particleEffectDestructor(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;
    final ParticleEffectData98 effectParent = this.findParticleParent(effect);

    effect.delete();

    if(effectParent == null) {
      this.firstParticle_8011a00c = effect.next_94;
    } else {
      //LAB_800fe8f0
      effectParent.next_94 = effect.next_94;
    }

    //LAB_800fe8fc
    if(effect.next_94 == null) {
      this.lastParticle_8011a010 = effectParent;
    }
    //LAB_800fea30
    //LAB_800fea3c
  }

  @Method(0x801012a0L)
  private void FUN_801012a0(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if((manager.params_10.flags_24 & 0x4) != 0) {
      this.FUN_801010a0(manager, effect, particle);
    }
    //LAB_801012c4
  }

  @Method(0x801012d4L)
  private void FUN_801012d4(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if((manager.params_10.flags_24 & 0x4) == 0) {
      this.FUN_801010a0(manager, effect, particle);
    }
    //LAB_801012f8
  }

  @Method(0x80101308L)
  private void initializeParticleInstance(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    if((effectInner.particleInnerStuff_1c & 0xff) == 0) {
      effectInner.particleInnerStuff_1c &= 0xffff_ff00;
      effectInner.particleInnerStuff_1c |= particleInnerStuffDefaultsArray_801197ec[effectInner.callbackIndex_20].ticksRemaining_02;
    }

    //LAB_8010137c
    if((effectInner.particleInnerStuff_1c & 0xff00) == 0) {
      effectInner.particleInnerStuff_1c &= 0xffff_00ff;
      effectInner.particleInnerStuff_1c |= particleInnerStuffDefaultsArray_801197ec[effectInner.callbackIndex_20].colour_01 << 8;
    }

    //LAB_801013c0
    if((effectInner.particleInnerStuff_1c & 0xff_0000) == 0) {
      effectInner.particleInnerStuff_1c &= 0xff00_ffff;
      effectInner.particleInnerStuff_1c |= particleInnerStuffDefaultsArray_801197ec[effectInner.callbackIndex_20].renderFrameCount_00 << 16;
    }

    //LAB_80101400
    effect.callback90Type_61 = particleInnerStuffDefaultsArray_801197ec[effectInner.callbackIndex_20].callbackType_03;
    particle.unused_00 = 0x73;
    particle.unused_01 = 0x6d;
    particle.unused_02 = 0x6b;
    particle.particlePosition_50.set((short)0, (short)0, (short)0);
    particle.particleVelocity_58.set((short)0, (short)0, (short)0);
    particle.scaleHorizontal_06 = 0.0f;
    particle.scaleVertical_08 = 0.0f;
    particle.scaleHorizontalStep_0a = 0.0f;
    particle.scaleVerticalStep_0c = 0.0f;
    particle.particleAcceleration_60.set((short)0, (short)0, (short)0);
    particle.stepR_8a = 0;
    particle.stepG_8c = 0;
    particle.stepB_8e = 0;
    final int callbackIndex = effectInner.callbackIndex_20;
    brokenT2For800ff5c4 = (long)callbackIndex;
    particle.framesUntilRender_04 = (short)(seed_800fa754.nextInt(effectInner._14 + 1) + 1);
    particle.ticksRemaining_12 = (short)((effectInner.particleInnerStuff_1c & 0xff_0000) >>> 16);
    final float colour = ((effectInner.particleInnerStuff_1c & 0xff00) >>> 8) / (float)0x80;
    particle.r_84 = colour;
    particle.g_86 = colour;
    particle.b_88 = colour;
    particle.flags_90 |= 0x1;
    particle.angle_0e = seed_800fa754.nextFloat() * MathHelper.TWO_PI;
    particle.angleVelocity_10 = seed_800fa754.nextFloat() * MathHelper.TWO_PI / 8.0f - MathHelper.TWO_PI / 16.0f;
    particle.spriteRotation_70.x = seed_800fa754.nextFloat() * MathHelper.TWO_PI;
    particle.spriteRotation_70.y = seed_800fa754.nextFloat() * MathHelper.TWO_PI;
    particle.spriteRotation_70.z = seed_800fa754.nextFloat() * MathHelper.TWO_PI;
    particle.spriteRotationStep_78.x = seed_800fa754.nextFloat() * MathHelper.TWO_PI / 32.0f - MathHelper.TWO_PI / 64.0f;
    particle.spriteRotationStep_78.y = seed_800fa754.nextFloat() * MathHelper.TWO_PI / 32.0f - MathHelper.TWO_PI / 64.0f;
    particle.spriteRotationStep_78.z = 0.0f;
    particle.flags_90 = particle.flags_90 & 0xffff_fff1 | (seed_800fa754.nextFloat() < 0.5f ? 0 : 0x8);
    final ParticleInitialTransformationMetrics10 metrics = particleInitialTransformationMetrics_801198f0[callbackIndex];
    final int initialPositionMode = metrics.initialPositionMode_00;
    if(initialPositionMode == 1) {
      //LAB_80101840
      final int angle = seed_800fa754.nextInt(4097);
      final short baseTranslationMagnitude = effectInner._10;
      particle.particlePosition_50.x = rcos(angle) * baseTranslationMagnitude >> metrics.initialTranslationMagnitudeReductionFactor1_02;
      particle.particlePosition_50.y = 0.0f;
      particle.particlePosition_50.z = rsin(angle) * baseTranslationMagnitude >> metrics.initialTranslationMagnitudeReductionFactor1_02;
      //LAB_80101824
    } else if(initialPositionMode == 2) {
      //LAB_801018c8
      final int angle = seed_800fa754.nextInt(4097);
      final int baseTranslationMagnitude = seed_800fa754.nextInt(effectInner._10 + 1);
      particle.particlePosition_50.x = rcos(angle) * baseTranslationMagnitude >> metrics.initialTranslationMagnitudeReductionFactor1_02;
      particle.particlePosition_50.y = 0.0f;
      particle.particlePosition_50.z = rsin(angle) * baseTranslationMagnitude >> metrics.initialTranslationMagnitudeReductionFactor1_02;
    } else if(initialPositionMode == 3) {
      //LAB_80101990
      particle.particlePosition_50.y = seed_800fa754.nextInt(metrics.initialTranslationMagnitudeReductionFactor2_04 - metrics.initialTranslationMagnitudeReductionFactor1_02 + 1) + metrics.initialTranslationMagnitudeReductionFactor1_02;
    } else if(initialPositionMode == 4) {
      //LAB_801019e4
      final int angle1 = seed_800fa754.nextInt(4097);
      final int angle2 = seed_800fa754.nextInt(2049);
      particle.particlePosition_50.x = (rcos(angle1) * rsin(angle2) >> metrics.initialTranslationMagnitudeReductionFactor1_02) * effectInner._10 >> metrics.initialTranslationMagnitudeReductionFactor2_04;
      particle.particlePosition_50.y = rcos(angle2) * effectInner._10 >> metrics.initialTranslationMagnitudeReductionFactor2_04;
      particle.particlePosition_50.z = (rsin(angle1) * rsin(angle2) >> metrics.initialTranslationMagnitudeReductionFactor1_02) * effectInner._10 >> metrics.initialTranslationMagnitudeReductionFactor2_04;
    }

    //LAB_80101b10
    //LAB_80101b18
    effect.initializerCallback_8c.accept(effect, particle, effectInner);

    if(metrics.hasSpecialAccelerationHandling_06) {
      particle.particleVelocity_58.x = particle.particleVelocity_58.x * effectInner._18;
      particle.particleVelocity_58.y = particle.particleVelocity_58.y * effectInner._18;
      particle.particleVelocity_58.z = particle.particleVelocity_58.z * effectInner._18;
    }

    //LAB_80101ba4
    if(metrics.hasSpecialScaleStepHandling_07) {
      final float scaleStep = (byte)(seed_800fa754.nextInt(metrics.scaleStepUpperBound_09 - metrics.scaleStepLowerBound_08 + 1) + metrics.scaleStepLowerBound_08) / (float)0x1000;
      particle.scaleHorizontalStep_0a = scaleStep;
      particle.scaleVerticalStep_0c = scaleStep;
    }

    //LAB_80101c20
    particle.particlePositionCopy2_48.set(particle.particlePosition_50);
  }

  @Method(0x800fb95cL)
  private void FUN_800fb95c(final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rcos(particle._14) * particle._16 >> 12;
    particle.particlePosition_50.z = rsin(particle._14) * particle._16 >> 12;
  }

  @Method(0x800fb9c0L)
  private void FUN_800fb9c0(final EffectManagerData6c<EffectManagerParams.ParticleType> a0, final ParticleEffectData98 a1, final ParticleEffectInstance94 a2) {
    // no-op
  }

  @Method(0x800fb9c8L)
  private void FUN_800fb9c8(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particleVelocity_58.x -= particle._16;
    particle.particleVelocity_58.y += particle._14;
  }

  @Method(0x800fb9ecL)
  private void FUN_800fb9ec(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    this.FUN_800fb95c(particle);

    particle._14 += particle._18;
    particle._16 += particle._1a.x;
    particle.particleVelocity_58.y -= 2.0f;

    if(particle._1a.x >= 8.0f) {
      particle._1a.x -= 8.0f;
    }
  }

  @Method(0x800fba58L)
  private void FUN_800fba58(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    this.FUN_800fb95c(particle);

    particle.particlePosition_50.x += rcos((int)particle._1a.x) * particle._1a.y / 0x1000;
    particle.particlePosition_50.z += rsin((int)particle._1a.x) * particle._1a.y / 0x1000;
    particle._16 += particle._18;

    if(particle._18 >= 4) {
      particle._18 -= 4;
    }

    //LAB_800fbaf0
    particle._1a.x += particle._1a.z;
  }

  @Method(0x800fbb14L)
  private void FUN_800fbb14(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    this.FUN_800fb95c(particle);

    particle.particlePosition_50.x += rcos((int)particle._1a.x) * particle._1a.y / 0x1000;
    particle.particlePosition_50.y += rsin((int)particle._1a.x) * particle._1a.y / 0x1000;

    if(particle._16 >= particle._18 * 4) {
      particle._16 -= particle._18;

      if(particle._18 >= 4) {
        particle._18 -= 4;
      }
    }

    //LAB_800fbbbc
    particle._1a.x += particle._1a.z;
  }

  @Method(0x800fbbe0L)
  private void FUN_800fbbe0(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    this.FUN_800fb95c(particle);
    particle._16 += particle._1a.y;
    particle.particleVelocity_58.y += 20.0f;
    particle.angleVelocity_10 -= particle.angleAcceleration_24;
    if(particle._18 == 1) {
      if(particle.ticksUntilMovementModeChanges_22 != 0) {
        //LAB_800fbca4
        particle.particlePosition_50.y = -particle.managerTranslation_2c.y;
        particle.ticksUntilMovementModeChanges_22--;
      } else {
        particle.particlePosition_50.y = (rsin((int)particle._1a.x) * particle.verticalPositionScale_20 >> 12) - particle.managerTranslation_2c.y;
        particle._1a.x += 0x7f;
        particle.scaleHorizontalStep_0a = MathHelper.psxDegToRad(particle._1a.z);
        particle.scaleVerticalStep_0c = MathHelper.psxDegToRad(particle._1a.z);
        particle._1a.y += 5.0f;
        particle.verticalPositionScale_20 += 20;
      }
      //LAB_800fbcc0
    } else if(particle.managerTranslation_2c.y + particle.particlePosition_50.y >= -1000) {
      particle._1a.y = 0.0f;
      particle._18 = 1;
      particle.scaleHorizontalStep_0a = MathHelper.psxDegToRad(particle._1a.z);
      particle.scaleVerticalStep_0c = MathHelper.psxDegToRad(particle._1a.z);
    }

    //LAB_800fbcf4
  }

  @Method(0x800fbd04L)
  private void FUN_800fbd04(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    this.FUN_800fb95c(particle);

    particle._16 += particle._18;
    if(particle._18 >= 3) {
      particle._18 -= 3;
    }

    //LAB_800fbd44
    particle._14 += particle._1a.x;
  }

  @Method(0x800fbd68L)
  private void FUN_800fbd68(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    final Vector3f sp0x38 = new Vector3f();
    if(particle._18 == 0) {
      this.FUN_800fb95c(particle);
      particle.particlePosition_50.y = 0.0f;
      particle.particlePosition_50.z /= 2.0f;
      sp0x38.set(MathHelper.psxDegToRad(particle._1a.x), 0.0f, 0.0f);
    } else {
      //LAB_800fbdb8
      particle.particlePosition_50.x = 0.0f;
      particle.particlePosition_50.y = rsin(particle._14) * particle._16 >> 11;
      particle.particlePosition_50.z = rcos(particle._14) * particle._16 >> 12;
      sp0x38.set(0.0f, 0.0f, MathHelper.psxDegToRad(particle._1a.x));
    }

    //LAB_800fbe10
    particle._14 -= 0x80;
    particle._1a.x -= 8.0f;

    final Vector3f sp0x28 = new Vector3f();
    rotateAndTranslateEffect(manager, sp0x38, particle.particlePosition_50, sp0x28);
    particle.particlePosition_50.set(sp0x28);
  }

  @Method(0x800fbe94L)
  private void FUN_800fbe94(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    this.FUN_800fb95c(particle);
    particle.particlePosition_50.y = 0.0f;
    particle.particlePosition_50.z /= 2.0f;
    final Vector3f sp0x38 = new Vector3f(MathHelper.psxDegToRad(particle._18), 0.0f, MathHelper.psxDegToRad(particle._1a.x));
    particle._14 += 0x80;

    final Vector3f sp0x28 = new Vector3f();
    rotateAndTranslateEffect(manager, sp0x38, particle.particlePosition_50, sp0x28);
    particle.particlePosition_50.set(sp0x28);
  }

  @Method(0x800fbf50L)
  private void FUN_800fbf50(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    this.FUN_800fb95c(particle);

    particle._16 = (short)(particle._16 * particle._18 >> 8);

    final float v1 = particle._1a.y / 4.0f;
    if(particle._16 < v1) {
      particle._16 = (short)v1;
    }

    //LAB_800fbfac
    particle._14 += particle._1a.x;
  }

  @Method(0x800fbfd0L)
  private void FUN_800fbfd0(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.y = rsin(particle._14) * particle._16 >> 12;
    particle.particlePosition_50.z = rcos(particle._14) * particle._16 >> 12;
    particle._16 += particle._18;
    if(particle._16 < 0) {
      particle._16 = 0;
    }

    //LAB_800fc044
    particle._14 += particle._1a.x;
  }

  @Method(0x800fc068L)
  private void FUN_800fc068(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    this.FUN_800fb95c(particle);

    particle._16 += particle._18;

    if(particle.particlePosition_50.y + particle.managerTranslation_2c.y >= manager.params_10.y_30) {
      particle.ticksRemaining_12 = 1;
    }
    //LAB_800fc0bc
  }

  @Method(0x800fc0d0L)
  private void FUN_800fc0d0(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.particlePosition_50.y + particle.managerTranslation_2c.y >= -400 && particle._14 == 0) {
      particle._14 = 1;
      particle.particleAcceleration_60.y = -8.0f;
      //LAB_800fc11c
    } else if(effect.parentScriptIndex_04 != -1 && particle._14 == 0) {
      final Vector3f sp0x10 = new Vector3f();
      scriptGetScriptedObjectPos(effect.parentScriptIndex_04, sp0x10);
      particle.particleVelocity_58.x = (sp0x10.x - (particle.particlePositionCopy1.x + particle.managerTranslation_2c.x)) / particle._1a.z;
      particle.particleVelocity_58.y = (sp0x10.y - (particle.particlePositionCopy1.y + particle.managerTranslation_2c.y)) / particle._1a.z;
      particle.particleVelocity_58.z = (sp0x10.z - (particle.particlePositionCopy1.z + particle.managerTranslation_2c.z)) / particle._1a.z;
      particle.particleVelocity_58.x += particle._18;
      particle.particleVelocity_58.y += particle._1a.x;
      particle.particleVelocity_58.z += particle._1a.y;
    }
    //LAB_800fc1ec
  }

  @Method(0x800fc1fcL)
  private void FUN_800fc1fc(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    this.FUN_800fb95c(particle);

    particle._16 += particle._18;
    if(particle.particlePosition_50.y + particle.managerTranslation_2c.y >= manager.params_10.y_30) {
      particle.particlePosition_50.y = manager.params_10.y_30 - particle.managerTranslation_2c.y;
      particle.particleVelocity_58.y = -particle.particleVelocity_58.y / 2.0f;
    }
    //LAB_800fc26c
  }

  @Method(0x800fc280L)
  private void FUN_800fc280(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.z = rcos(particle._14) * 2 * particle._1a.y / 0x1000;
    particle.particlePosition_50.x = rsin((int)particle._1a.z) * particle._1a.y / 0x1000;
    particle._14 += particle._16;
    particle._1a.z = particle._1a.z + particle._16 * 2;
    particle.particlePosition_50.y += 0x40 + ((rcos(particle._18) >> 1) + 0x800) * particle._1a.y / 0x8000;
    particle._18 += particle._1a.x;
  }

  @Method(0x800fc348L)
  private void FUN_800fc348(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rcos(particle._14) * particle._1a.x / 0x1000 * rsin(particle._18) / 0x1000;
    particle.particlePosition_50.y = particle._18 * 2 - 0x800;
    particle.particlePosition_50.z = rsin(particle._14) * particle._1a.x / 0x1000 * rsin(particle._18) / 0x1000;
    particle._14 += particle._16;
  }

  @Method(0x800fc410L)
  private void FUN_800fc410(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particleVelocity_58.y += particle._14 / 0x100;
  }

  @Method(0x800fc42cL)
  private void FUN_800fc42c(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = particle._14 + rcos((int)particle._1a.x) * particle._1a.z / 0x1000;
    particle.particlePosition_50.z = particle._18 + rsin((int)particle._1a.x) * particle._1a.z / 0x1000;
    particle._1a.z += 16.0f;
    particle._1a.x += particle._1a.y;
  }

  @Method(0x800fc4bcL)
  private void FUN_800fc4bc(final MV out, final EffectManagerData6c<EffectManagerParams.ParticleType> a1, final ParticleMetrics48 particleMetrics) {
    out.rotationXYZ(particleMetrics.rotation_38);
    out.transfer.set(particleMetrics.translation_18);
    out.scaleLocal(particleMetrics.scale_28);
  }

  @Method(0x800fc528L)
  private void FUN_800fc528(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rsin(particle._14) * (particle._18 >> 1) >> 12;
    particle.particlePosition_50.z = rcos(particle._14) * particle._18 >> 12;
    particle._14 += particle._16;
  }

  @Method(0x800fc5a8L)
  private void FUN_800fc5a8(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rsin(particle._14) * particle._18 >> 12;
    particle.particlePosition_50.z = rcos(particle._14) * particle._18 >> 12;
    particle._18 = (short)(particle._18 * 7 / 8);
  }

  @Method(0x800fc61cL)
  private void FUN_800fc61c(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.scaleVertical_08 = MathHelper.psxDegToRad((short)((rcos(particle._14) * particle._16 >> 12) * manager.params_10.scale_16.y));
    particle._14 -= particle._18;

    if(particle._16 > 0) {
      particle._16 -= particle._1a.x;
    }

    //LAB_800fc6a8
  }

  @Method(0x800fc6bcL)
  private void FUN_800fc6bc(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rsin(particle._14) * particle._16 >> 12;
    particle.particlePosition_50.z = rcos(particle._14) * particle._16 >> 12;
    particle.particlePosition_50.x += rsin(particle._18) << 8 >> 12;
    particle.particlePosition_50.z += rcos(particle._18) << 8 >> 12;
    particle._18 += particle._1a.x;
  }

  @Method(0x800fc768L)
  private void FUN_800fc768(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle._1a.x += particle._14;
    particle._1a.y += particle._16;
    particle._1a.z += particle._18;
    particle.particleVelocity_58.x = particle._1a.x / 0x100;
    particle.particleVelocity_58.y = particle._1a.y / 0x100;
    particle.particleVelocity_58.z = particle._1a.z / 0x100;
  }

  @Method(0x800fc7c8L)
  private void FUN_800fc7c8(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.particlePosition_50.y + particle.managerTranslation_2c.y >= manager.params_10.y_30) {
      particle.particlePosition_50.y = manager.params_10.y_30 - particle.managerTranslation_2c.y;
      particle.particleVelocity_58.y = -particle.particleVelocity_58.y / 2.0f;
      if(particle._14 == 0) {
        final int angle = seed_800fa754.nextInt(0x1001);
        particle.particleVelocity_58.x = (rcos(angle) >>> 8) * effect.effectInner_08._18;
        particle.particleVelocity_58.z = (rcos(angle) >>> 8) * effect.effectInner_08._18;
      }

      //LAB_800fc8d8
      particle._14 = 1;
    }
    //LAB_800fc8e0
  }

  @Method(0x800fc8f8L)
  private void FUN_800fc8f8(@Nullable Vector3f in, @Nullable final Vector3f out) {
    if(in == null) {
      in = new Vector3f();
    }

    //LAB_800fc920
    final MV wsTransposed = new MV();
    worldToScreenMatrix_800c3548.transpose(wsTransposed);
    final Vector3f wsNegated = new Vector3f(wsTransposed.transfer).negate();
    wsNegated.mul(wsTransposed, in);

    if(out != null) {
      wsNegated.set(wsTransposed.transfer).negate();
      wsNegated.z += 0x1000;

      final Vector3f sp0x10 = new Vector3f();
      wsNegated.mul(wsTransposed, sp0x10);
      sp0x10.sub(in);
      final float angle = MathHelper.atan2(sp0x10.x, sp0x10.z);
      out.y = angle;

      //LAB_800fca44
      //TODO is this right?
      final float sin = MathHelper.sin(-angle);
      final float cos = MathHelper.cosFromSin(sin, -angle);
      out.x = MathHelper.atan2(-sp0x10.y, cos * sp0x10.z - sin * sp0x10.x);
      out.z = 0.0f;
    }
    //LAB_800fca5c
  }

  /** Returns Z */
  @Method(0x800fca78L)
  private float FUN_800fca78(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final Vector3f translation, final GpuCommandPoly cmd) {
    final Vector2f ref = new Vector2f();
    final float z = FUN_800cfc20(particle.managerRotation_68, particle.managerTranslation_2c, translation, ref);
    if(z >= 40) {
      final float zScale = (float)0x5000 / z;
      final float horizontalScale = zScale * (manager.params_10.scale_16.x + particle.scaleHorizontal_06);
      final float verticalScale = zScale * (manager.params_10.scale_16.y + particle.scaleVertical_08);

      final float angle;
      if((manager.params_10.flags_24 & 0x2) != 0) {
        final Vector3f sp0x38 = new Vector3f();
        this.FUN_800fc8f8(null, sp0x38);

        //LAB_800fcb90
        //LAB_800fcbd4
        final float sp18 = (particle.particlePositionCopy2_48.z - translation.z) * -Math.abs(MathHelper.sin(sp0x38.y - manager.params_10.rot_10.y)) - (translation.x - particle.particlePositionCopy2_48.x) * -Math.abs(MathHelper.cos(sp0x38.y + manager.params_10.rot_10.y));
        final float sp1c = translation.y - particle.particlePositionCopy2_48.y;
        angle = -MathHelper.atan2(sp1c, sp18) + MathHelper.TWO_PI / 4.0f;
        particle.particlePositionCopy2_48.set(translation);
      } else {
        angle = particle.angle_0e + manager.params_10.rot_10.x - MathHelper.TWO_PI / 1.6f;
      }

      //LAB_800fcc20
      final float right = effect.w_5e / 2.0f * horizontalScale;
      final float left = -right;
      final float bottom = effect.h_5f / 2.0f * verticalScale;
      final float top = -bottom;
      final float cos = MathHelper.cos(angle);
      final float sin = MathHelper.sin(angle);

      // Rotate coords
      final float rotX0 = left * cos;
      final float rotY0 = left * sin;
      final float rotX1 = right * cos;
      final float rotY1 = right * sin;
      final float rotX2 = top * sin;
      final float rotY2 = top * cos;
      final float rotX3 = bottom * sin;
      final float rotY3 = bottom * cos;

      cmd.pos(0, ref.x + rotX0 - rotX2, ref.y + rotY0 + rotY2);
      cmd.pos(1, ref.x + rotX1 - rotX2, ref.y + rotY1 + rotY2);
      cmd.pos(2, ref.x + rotX0 - rotX3, ref.y + rotY0 + rotY3);
      cmd.pos(3, ref.x + rotX1 - rotX3, ref.y + rotY1 + rotY3);
    }

    //LAB_800fcde0
    return z;
  }

  /** Returns Z */
  @Method(0x800fca78L)
  private float FUN_800fca78(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectInstance94 particle, final Vector3f translation, final MV transforms) {
    final Vector2f ref = new Vector2f();
    final float z = FUN_800cfc20(particle.managerRotation_68, particle.managerTranslation_2c, translation, ref);
    if(z >= 40) {
      final float zScale = (float)0x5000 / z;
      final float horizontalScale = zScale * (manager.params_10.scale_16.x + particle.scaleHorizontal_06);
      final float verticalScale = zScale * (manager.params_10.scale_16.y + particle.scaleVertical_08);

      final float angle;
      if((manager.params_10.flags_24 & 0x2) != 0) {
        final Vector3f sp0x38 = new Vector3f();
        this.FUN_800fc8f8(null, sp0x38);

        //LAB_800fcb90
        //LAB_800fcbd4
        final float sp18 = (particle.particlePositionCopy2_48.z - translation.z) * -Math.abs(MathHelper.sin(sp0x38.y - manager.params_10.rot_10.y)) - (translation.x - particle.particlePositionCopy2_48.x) * -Math.abs(MathHelper.cos(sp0x38.y + manager.params_10.rot_10.y));
        final float sp1c = translation.y - particle.particlePositionCopy2_48.y;
        angle = -MathHelper.atan2(sp1c, sp18) + MathHelper.TWO_PI / 4.0f;
        particle.particlePositionCopy2_48.set(translation);
      } else {
        angle = particle.angle_0e + manager.params_10.rot_10.x - MathHelper.TWO_PI / 1.6f;
      }

      transforms.transfer.x = ref.x;
      transforms.transfer.y = ref.y;
      transforms.rotationZ(angle).scale(horizontalScale, verticalScale, 1.0f);
    }

    //LAB_800fcde0
    return z;
  }

  @Method(0x800fea68L)
  private void FUN_800fea68(final ParticleEffectData98 a1, final ParticleEffectInstance94 a2, final ParticleEffectData98Inner24 a3) {
    // no-op
  }

  /**
   * Used by particle renderer index 1 (renders ice chunk particles and ???). Used renderCtmd
   * Seems to involve lit TMDs.
   */
  @Method(0x800fcf20L)
  private void renderTmdParticle(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final TmdObjTable1c tmd, final Obj obj, final ParticleMetrics48 particleMetrics, final int tpage) {
    if(particleMetrics.flags_00 >= 0) {
      final MV transforms = new MV();
      this.FUN_800fc4bc(transforms, manager, particleMetrics);
      if((particleMetrics.flags_00 & 0x40) == 0) {
        FUN_800e61e4(particleMetrics.colour0_40.x, particleMetrics.colour0_40.y, particleMetrics.colour0_40.z);
      }

      //LAB_800fcf94
      GsSetLightMatrix(transforms);
      final MV transformMatrix = new MV();

      if(RenderEngine.legacyMode != 0) {
        transforms.compose(worldToScreenMatrix_800c3548, transformMatrix);
      } else {
        transformMatrix.set(transforms);
      }

      if((particleMetrics.flags_00 & 0x400_0000) == 0) {
        transformMatrix.rotationXYZ(manager.params_10.rot_10);
        transformMatrix.scaleLocal(manager.params_10.scale_16);
      }

      //LAB_800fcff8
      GTE.setTransforms(transformMatrix);
      zOffset_1f8003e8 = 0;
      if((manager.params_10.flags_00 & 0x4000_0000) != 0) {
        tmdGp0Tpage_1f8003ec = manager.params_10.flags_00 >>> 23 & 0x60;
      } else {
        //LAB_800fd038
        tmdGp0Tpage_1f8003ec = tpage;
      }

      //LAB_800fd040
      final ModelPart10 dobj = new ModelPart10();
      dobj.attribute_00 = particleMetrics.flags_00;
      dobj.tmd_08 = tmd;

      final int oldZShift = zShift_1f8003c4;
      final int oldZMax = zMax_1f8003cc;
      final int oldZMin = zMin;
      zShift_1f8003c4 = 2;
      zMax_1f8003cc = 0xffe;
      zMin = 0xb;
      Renderer.renderDobj2(dobj, false, 0x20);
      zShift_1f8003c4 = oldZShift;
      zMax_1f8003cc = oldZMax;
      zMin = oldZMin;

      RENDERER.queueModel(obj, transformMatrix)
        .lightDirection(lightDirectionMatrix_800c34e8)
        .lightColour(lightColourMatrix_800c3508)
        .backgroundColour(GTE.backgroundColour)
        .ctmdFlags((dobj.attribute_00 & 0x4000_0000) != 0 ? 0x12 : 0x0)
        .tmdTranslucency(tmdGp0Tpage_1f8003ec >>> 5 & 0b11)
        .battleColour(((Battle)currentEngineState_8004dd04)._800c6930.colour_00);

      if((particleMetrics.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      }
    }
    //LAB_800fd064
  }

  @Method(0x800fd084L)
  private void updateParticleRotationTranslationColour(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.managerTranslation_2c.set(manager.params_10.trans_04);
    particle.managerRotation_68.set(manager.params_10.rot_10);

    if(particle.ticksRemaining_12 == 0) {
      particle.ticksRemaining_12 = 1;
    }

    if((effect.effectInner_08.particleInnerStuff_1c & 0x400_0000) != 0) {
      particle.r_84 = manager.params_10.colour_1c.x / (float)0xff;
      particle.g_86 = manager.params_10.colour_1c.y / (float)0xff;
      particle.b_88 = manager.params_10.colour_1c.z / (float)0xff;

      if((manager.params_10.flags_24 & 0x1) == 0) {
        particle.stepR_8a = 0;
        particle.stepG_8c = 0;
        particle.stepB_8e = 0;
        return;
      }
    }

    //LAB_800fd18c
    //LAB_800fd0dc
    particle.stepR_8a = particle.r_84 / particle.ticksRemaining_12;
    particle.stepG_8c = particle.g_86 / particle.ticksRemaining_12;
    particle.stepB_8e = particle.b_88 / particle.ticksRemaining_12;

    //LAB_800fd1d4
  }

  @Method(0x800fd1dcL)
  private void tickParticleAttributes(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final Vector3f colour) {
    if((effect.effectInner_08.particleInnerStuff_1c & 0x800_0000) == 0 || (manager.params_10.flags_24 & 0x1) != 0) {
      //LAB_800fd23c
      particle.r_84 -= particle.stepR_8a;
      particle.g_86 -= particle.stepG_8c;
      particle.b_88 -= particle.stepB_8e;
    } else {
      particle.r_84 = manager.params_10.colour_1c.x / (float)0xff;
      particle.g_86 = manager.params_10.colour_1c.y / (float)0xff;
      particle.b_88 = manager.params_10.colour_1c.z / (float)0xff;
    }

    //LAB_800fd26c
    colour.x = particle.r_84;
    colour.y = particle.g_86;
    colour.z = particle.b_88;

    particle.particlePosition_50.add(particle.particleVelocity_58);
    particle.particleVelocity_58.add(particle.particleAcceleration_60);

    if(particle.particlePosition_50.y + particle.managerTranslation_2c.y >= manager.params_10.y_30) {
      if((manager.params_10.flags_24 & 0x20) != 0) {
        particle.ticksRemaining_12 = 1;
      }

      //LAB_800fd324
      if((manager.params_10.flags_24 & 0x8) != 0) {
        particle.particlePosition_50.y = manager.params_10.y_30 - particle.managerTranslation_2c.y;
        particle.particleVelocity_58.y = -particle.particleVelocity_58.y / 2.0f;
      }
    }

    //LAB_800fd358
    if((effect.effectInner_08.particleInnerStuff_1c & 0x200_0000) == 0) {
      particle.scaleHorizontal_06 += particle.scaleHorizontalStep_0a;
      particle.scaleVertical_08 += particle.scaleVerticalStep_0c;
    }

    //LAB_800fd38c
    if((effect.effectInner_08.particleInnerStuff_1c & 0x100_0000) == 0) {
      particle.angle_0e += particle.angleVelocity_10;
      particle.spriteRotation_70.add(particle.spriteRotationStep_78);
    } else {
      //LAB_800fd3e4
      particle.angle_0e = MathHelper.psxDegToRad((short)(manager.params_10.flags_24 >>> 12 & 0xff0));
    }

    //LAB_800fd3f8
    particle.particleVelocity_58.y += manager.params_10._2c >> 8;

    if(effect.scaleOrUseEffectAcceleration_6c) {
      particle.particleVelocity_58.add(effect.effectAcceleration_70);
    }
    //LAB_800fd458
  }

  @Method(0x800fd460L)
  private boolean tickParticleInstance(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.framesUntilRender_04--;

    final short framesUntilRender = particle.framesUntilRender_04;

    //LAB_800fd53c
    if(framesUntilRender >= 0)  {
      if(framesUntilRender != 0) {
        return false;
      }

      this.updateParticleRotationTranslationColour(manager, effect, particle);

      if((manager.params_10.flags_24 & 0x10) != 0) {
        particle.particlePosition_50.y = 0.0f;

        if(effect.subParticleType_60 == 2 || effect.subParticleType_60 == 5) {
          //LAB_800fd4f0
          //LAB_800fd504
          for(int i = 0; i < effect.countParticleSub_54; i++) {
            particle.subParticlePositionsArray_44[i].y = 0.0f;
          }
        }
      }

      //LAB_800fd520
      if((manager.params_10.flags_24 & 0x40) != 0) {
        particle.particleVelocity_58.y = 0.0f;
      }
    }

    //LAB_800fd54c
    effect.particleInstanceTickCallback_88.accept(state, manager, effect, particle);

    if((particle.flags_90 & 0x1) == 0) {
      return false;
    }

    if(particle.ticksRemaining_12 > 0) {
      particle.ticksRemaining_12--;
    }

    //LAB_800fd58c
    if(particle.ticksRemaining_12 == 0 && (manager.params_10.flags_24 & 0x80) == 0) {
      particle.flags_90 &= 0xffff_fffe;
      effect.particleInstanceReconstructorCallback_90.accept(manager, effect, particle);
      return false;
    }

    //LAB_800fd5e0
    return true;
  }

  @Method(0x800fd600L)
  private void renderTmdParticleEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;
    effect.countFramesRendered_52++;

    if(effect.obj == null) {
      effect.obj = TmdObjLoader.fromObjTable("Particle", effect.tmd_30);
    }

    //LAB_800fd660
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 particle = effect.particleArray_68[i];
      if(this.tickParticleInstance(state, manager, effect, particle)) {
        effect.particleInstancePrerenderCallback_84.accept(manager, effect, particle);
        final Vector3f colour = new Vector3f();
        this.tickParticleAttributes(manager, effect, particle, colour);

        final Vector3f rotatedAndTranslatedPosition = new Vector3f();
        rotateAndTranslateEffect(manager, null, particle.particlePosition_50, rotatedAndTranslatedPosition);

        final ParticleMetrics48 particleMetrics = new ParticleMetrics48();
        particleMetrics.flags_00 = manager.params_10.flags_00;
        particleMetrics.translation_18.set(particle.managerTranslation_2c).add(rotatedAndTranslatedPosition);

        particleMetrics.scale_28.set(
          manager.params_10.scale_16.x + particle.scaleHorizontal_06,
          manager.params_10.scale_16.y + particle.scaleVertical_08,
          manager.params_10.scale_16.x + particle.scaleHorizontal_06 // This is correct
        );

        particleMetrics.rotation_38.set(particle.spriteRotation_70).add(particle.managerRotation_68);
        particleMetrics.colour0_40.set(colour.x, colour.y, colour.z);
        particleMetrics.colour1_44.set(0, 0, 0);
        this.renderTmdParticle(manager, effect.tmd_30, effect.obj, particleMetrics, effect.tpage_56);
      }
      //LAB_800fd7e0
    }

    //LAB_800fd7fc
    if(effect.scaleOrUseEffectAcceleration_6c) {
      effect.effectAcceleration_70.mul(effect.scaleParticleAcceleration_80);
    }
    //LAB_800fd858
  }

  @Method(0x800fd87cL)
  private void renderLineParticleEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;
    effect.countFramesRendered_52++;

    if(effect.countParticleInstance_50 != 0) {
      final ParticleMetrics48 particleMetrics = new ParticleMetrics48();

      //LAB_800fd8dc
      for(int i = 0; i < effect.countParticleInstance_50; i++) {
        final ParticleEffectInstance94 particle = effect.particleArray_68[i];

        if(this.tickParticleInstance(state, manager, effect, particle)) {
          //LAB_800fd918
          for(int j = effect.countParticleSub_54 - 1; j > 0; j--) {
            particle.subParticlePositionsArray_44[j].set(particle.subParticlePositionsArray_44[j - 1]);
          }

          //LAB_800fd950
          particle.subParticlePositionsArray_44[0].set(particle.particlePosition_50);
          effect.particleInstancePrerenderCallback_84.accept(manager, effect, particle);

          final Vector3f colour = new Vector3f();
          this.tickParticleAttributes(manager, effect, particle, colour);

          final Vector3f colourMod = new Vector3f();
          if((effect.effectInner_08.particleInnerStuff_1c & 0x1000_0000) == 0 || (particle.flags_90 & 0x8) == 0) {
            //LAB_800fd9f4
            colourMod.set(0, 0, 0);
          } else {
            colourMod.set(colour).negate().div(2.0f);
          }

          //LAB_800fda00
          particle.flags_90 = particle.flags_90 & 0xffff_fff7 | (~(particle.flags_90 >>> 3) & 0x1) << 3;

          //LAB_800fda58
          //LAB_800fda90
          MathHelper.clamp(colour.add(colourMod), 0.0f, 0.5f);

          //LAB_800fdac8
          if((particle.flags_90 & 0x6) != 0) {
            particleMetrics.flags_00 = manager.params_10.flags_00 & 0x67ff_ffff | (particle.flags_90 >>> 1 & 0x3) << 28;
          } else {
            particleMetrics.flags_00 = manager.params_10.flags_00;
          }

          //LAB_800fdb14
          final Vector3f stepColour = new Vector3f();
          stepColour.set(colour).div(effect.countParticleSub_54);

          final Vector2f ref1 = new Vector2f();
          float z = FUN_800cfc20(particle.managerRotation_68, particle.managerTranslation_2c, particle.subParticlePositionsArray_44[0], ref1) / 4;
          particleMetrics.x0_08 = ref1.x;
          particleMetrics.y0_10 = ref1.y;

          if(z + manager.params_10.z_22 >= 0xa0) {
            if(z + manager.params_10.z_22 >= 0xffe) {
              z = 0xffe - manager.params_10.z_22;
            }

            //LAB_800fdbc0
            particleMetrics.z_04 = z;

            //LAB_800fdbe4
            for(int k = 0; k < effect.countParticleSub_54 - 1; k++) {
              particleMetrics.colour0_40.set(colour.x, colour.y, colour.z);
              colour.sub(stepColour);
              particleMetrics.colour1_44.set(colour.x, colour.y, colour.z);

              final Vector2f ref2 = new Vector2f();
              FUN_800cfc20(particle.managerRotation_68, particle.managerTranslation_2c, particle.subParticlePositionsArray_44[k], ref2);
              particleMetrics.x1_0c = ref2.x;
              particleMetrics.y1_14 = ref2.y;
              this.lineParticleRenderers_801197c0[effect.subParticleType_60 - 2].accept(manager, particleMetrics);
              particleMetrics.x0_08 = particleMetrics.x1_0c;
              particleMetrics.y0_10 = particleMetrics.y1_14;
            }
            //LAB_800fdcc8
          }
        }
        //LAB_800fdd28
      }
    }

    //LAB_800fdd44
    if(effect.scaleOrUseEffectAcceleration_6c) {
      effect.effectAcceleration_70.mul(effect.scaleParticleAcceleration_80);
    }
    //LAB_800fdda0
  }

  @Method(0x800fddd0L)
  private void renderNoParticlesWhatsoever(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    // no-op
  }

  @Method(0x800fddd8L)
  private void renderPixelParticleEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;
    effect.countFramesRendered_52++;

    //LAB_800fde38
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 particle = effect.particleArray_68[i];

      if(this.tickParticleInstance(state, manager, effect, particle)) {
        effect.particleInstancePrerenderCallback_84.accept(manager, effect, particle);

        final Vector3f colour = new Vector3f();
        this.tickParticleAttributes(manager, effect, particle, colour);

        final Vector2f ref = new Vector2f();

        float z = FUN_800cfc20(particle.managerRotation_68, particle.managerTranslation_2c, particle.particlePosition_50, ref) / 4.0f;
        final float zCombined = z + manager.params_10.z_22;
        if(zCombined >= 0xa0) {
          if(zCombined >= 0xffe) {
            z = 0xffe - manager.params_10.z_22;
          }

          //LAB_800fdf44
          // gp0 command 68h, which is an opaque dot (1x1)
          GPU.queueCommand((z + manager.params_10.z_22) / 4.0f, new GpuCommandQuad()
            .rgb((int)(colour.x * 0xff), (int)(colour.y * 0xff), (int)(colour.z * 0xff))
            .pos(ref.x, ref.y, 1, 1)
          );
        }
      }
      //LAB_800fdfcc
    }

    //LAB_800fdfe8
    if(effect.scaleOrUseEffectAcceleration_6c) {
      effect.effectAcceleration_70.mul(effect.scaleParticleAcceleration_80);
    }
    //LAB_800fe044
  }

  /** Has some kind of sub-particles */
  @Method(0x800fe120L)
  private void renderQuadParticleEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;

    effect.countFramesRendered_52++;

    if(effect.obj == null) {
      final QuadBuilder builder = new QuadBuilder("Particle")
        .bpp(Bpp.BITS_4)
        .clut((effect.clut_5c & 0b111111) * 16, effect.clut_5c >>> 6)
        .vramPos(effect.u_58 & 0x3c0, effect.v_5a < 256 ? 0 : 256)
        .uv((effect.u_58 & 0x3f) * 4, effect.v_5a)
        .pos(-effect.w_5e / 2.0f, -effect.h_5f / 2.0f, 0.0f)
        .size(effect.w_5e, effect.h_5f);

      if((manager.params_10.flags_00 & 1 << 30) != 0) {
        builder.translucency(Translucency.of(manager.params_10.flags_00 >>> 28 & 0b11));
      }

      effect.obj = builder.build();
    }

    final Vector3f colour = new Vector3f();
    final Vector3f colourMod = new Vector3f();
    final Vector3f colourStep = new Vector3f();

    //LAB_800fe180
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 particle = effect.particleArray_68[i];

      if(this.tickParticleInstance(state, manager, effect, particle)) {
        //LAB_800fe1bc
        for(int j = effect.countParticleSub_54 - 1; j > 0; j--) {
          particle.particleInstanceSubArray_80[j].copy(particle.particleInstanceSubArray_80[j - 1]);
        }

        //LAB_800fe1fc
        effect.particleInstancePrerenderCallback_84.accept(manager, effect, particle);

        this.tickParticleAttributes(manager, effect, particle, colour);

        if((effect.effectInner_08.particleInnerStuff_1c & 0x1000_0000) == 0 || (particle.flags_90 & 0x8) == 0) {
          //LAB_800fe280
          colourMod.set(0, 0, 0);
        } else {
          colourMod.set(colour).negate().div(2.0f);
        }

        //LAB_800fe28c
        particle.flags_90 = particle.flags_90 & 0xffff_fff7 | (~(particle.flags_90 >>> 3) & 0x1) << 3;

        if((manager.params_10.flags_00 & 0x400_0000) == 0) {
          // This is super bugged in retail and passes garbage as the last 3 params to both methods.
          // Hopefully this is fine with them all zeroed. This is used for the Glare's bewitching attack.
          particle.managerRotation_68.y = MathHelper.atan2(
            this.camera.refpointRawComponent(0, null, ZERO) - particle.particlePosition_50.x,
            this.camera.refpointRawComponent(2, null, ZERO) - particle.particlePosition_50.z
          ) + MathHelper.TWO_PI / 4.0f;
        }

        //LAB_800fe300
        MathHelper.clamp(colour.add(colourMod), 0.0f, 0.5f);

        final GpuCommandPoly cmd1 = new GpuCommandPoly(4)
          .clut((effect.clut_5c & 0b111111) * 16, effect.clut_5c >>> 6)
          .vramPos(effect.u_58 & 0x3c0, effect.v_5a < 256 ? 0 : 256)
          .rgb((int)(colour.x * 0xff), (int)(colour.y * 0xff), (int)(colour.z * 0xff))
          .uv(0, (effect.u_58 & 0x3f) * 4, effect.v_5a)
          .uv(1, (effect.u_58 & 0x3f) * 4 + effect.w_5e - 1, effect.v_5a)
          .uv(2, (effect.u_58 & 0x3f) * 4, effect.v_5a + effect.h_5f - 1)
          .uv(3, (effect.u_58 & 0x3f) * 4 + effect.w_5e - 1, effect.v_5a + effect.h_5f - 1);

        if((manager.params_10.flags_00 & 1 << 30) != 0) {
          cmd1.translucent(Translucency.of(manager.params_10.flags_00 >>> 28 & 0b11));
        }

        this.FUN_800fca78(manager, effect, particle, particle.particlePosition_50, cmd1);
        final float instZ = this.FUN_800fca78(manager, particle, particle.particlePosition_50, effect.transforms) / 4.0f;
        float effectZ = manager.params_10.z_22;
        if(effectZ + instZ >= 160) {
          if(effectZ + instZ >= 4094) {
            effectZ = 4094 - instZ;
          }

          //LAB_800fe548
          GPU.queueCommand((instZ + effectZ) / 4.0f, cmd1);
          effect.transforms.transfer.x += GPU.getOffsetX();
          effect.transforms.transfer.y += GPU.getOffsetY();
          effect.transforms.transfer.z = effectZ * 4.0f;
          RENDERER.queueOrthoModel(effect.obj, effect.transforms)
            .colour(colour);
        }

        //LAB_800fe564
        if((effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000) != 0) {
          ParticleEffectInstance94Sub10 particleSub = particle.particleInstanceSubArray_80[0];
          particleSub.x0_00 = cmd1.getX(0);
          particleSub.y0_02 = cmd1.getY(0);
          particleSub.x1_04 = cmd1.getX(1);
          particleSub.y1_06 = cmd1.getY(1);
          particleSub.x2_08 = cmd1.getX(2);
          particleSub.y2_0a = cmd1.getY(2);
          particleSub.x3_0c = cmd1.getX(3);
          particleSub.y3_0e = cmd1.getY(3);
          colourStep.set(colour).div(effect.countParticleSub_54);

          final int count = Math.min(-particle.framesUntilRender_04, effect.countParticleSub_54);

          //LAB_800fe61c
          //LAB_800fe628
          for(int k = 0; k < count; k++) {
            effectZ = manager.params_10.z_22;
            if(effectZ + instZ >= 160) {
              if(effectZ + instZ >= 4094) {
                effectZ = 4094 - instZ;
              }

              final GpuCommandPoly cmd2 = new GpuCommandPoly(cmd1);

              particleSub = particle.particleInstanceSubArray_80[k];

              //LAB_800fe644
              cmd2
                .rgb((int)(colour.x * 0xff), (int)(colour.y * 0xff), (int)(colour.z * 0xff))
                .pos(0, particleSub.x0_00, particleSub.y0_02)
                .pos(1, particleSub.x1_04, particleSub.y1_06)
                .pos(2, particleSub.x2_08, particleSub.y2_0a)
                .pos(3, particleSub.x3_0c, particleSub.y3_0e);

              //LAB_800fe78c
              GPU.queueCommand((instZ + effectZ) / 4.0f, cmd2);
            }

            colour.sub(colourStep);
          }
        }
      }
      //LAB_800fe7b8
    }

    //LAB_800fe7ec
    if(effect.scaleOrUseEffectAcceleration_6c) {
      effect.effectAcceleration_70.mul(effect.scaleParticleAcceleration_80);
    }
    //LAB_800fe848
  }

  @Method(0x800fea70L)
  private int FUN_800fea70(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int angle = seed_800fa754.nextInt(4097);
    particle.particleVelocity_58.x = rcos(angle) >> 8;
    particle.particleVelocity_58.z = rsin(angle) >> 8;
    particle.particleVelocity_58.y = -seed_800fa754.nextInt(91) - 10;

    final float colourStep = (seed_800fa754.nextInt(101) - 50) / (float)0x80;
    particle.r_84 += colourStep;
    particle.g_86 += colourStep;
    particle.b_88 += colourStep;

    return angle;
  }

  @Method(0x800fec3cL)
  private void FUN_800fec3c(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int s0 = this.FUN_800fea70(effect, particle, effectInner);
    particle.particleVelocity_58.x = rcos(s0) >> 6;
    particle.particleVelocity_58.y = 0.0f;
    particle.particleVelocity_58.z = rsin(s0) >> 6;
  }

  @Method(0x800fecccL)
  private void FUN_800feccc(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int angle = seed_800fa754.nextInt(4097);
    particle.particleVelocity_58.x = rcos(angle) >> 10;
    particle.particleVelocity_58.y = -(seed_800fa754.nextInt(33) + 13);
    particle.particleVelocity_58.z = rsin(angle) >> 10;
    particle._14 = (short)(seed_800fa754.nextInt(3) + 1);
    particle._16 = (short)(seed_800fa754.nextInt(3));
    particle.framesUntilRender_04 = (short)(particle.framesUntilRender_04 / 4 * 4 + 1);
  }

  @Method(0x800fee9cL)
  private void FUN_800fee9c(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int angle = seed_800fa754.nextInt(4097);
    particle.particleVelocity_58.x = rcos(angle) / 0x80;
    particle.particleVelocity_58.y = 0.0f;
    particle.particleVelocity_58.z = rsin(angle) / 0x80;
    particle.framesUntilRender_04 = 1;
    particle._14 = (short)(seed_800fa754.nextInt(6));
  }

  @Method(0x800fefe4L)
  private void FUN_800fefe4(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    this.FUN_800fee9c(effect, particle, effectInner);
    particle._14 = (short)(seed_800fa754.nextInt(4097));
    particle._16 = effectInner._10;
    particle._18 = (short)(seed_800fa754.nextInt(91) + 10);
    particle._1a.x = 120.0f;
    particle.particleVelocity_58.x = 0.0f;
    particle.particleVelocity_58.y = -seed_800fa754.nextInt(11) - 5;
    particle.particleVelocity_58.z = 0.0f;
  }

  @Method(0x800ff15cL)
  private void FUN_800ff15c(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.y = -seed_800fa754.nextInt(61) - 60;

    final float scaleStep;
    if(vsyncMode_8007a3b8 != 4) {
      scaleStep = (short)-(seed_800fa754.nextInt(14) + 5) / (float)0x1000;
    } else {
      //LAB_800ff248
      scaleStep = (short)-(seed_800fa754.nextInt(21) + 10) / (float)0x1000;
    }

    particle.scaleHorizontalStep_0a = scaleStep;
    particle.scaleVerticalStep_0c = scaleStep;

    //LAB_800ff2b4
    particle._14 = (short)seed_800fa754.nextInt(4097);
    particle._1a.x = seed_800fa754.nextInt(4097);
    particle._1a.z = seed_800fa754.nextInt(1025) - 512;

    particle._1a.y = 100.0f;
    particle._16 = effectInner._10;
    particle._18 = (short)(effectInner._18 * 100);
  }

  @Method(0x800ff3e0L)
  private void FUN_800ff3e0(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    this.FUN_800ff15c(effect, particle, effectInner);
    particle.verticalPositionScale_20 = (short)0;
    particle.ticksUntilMovementModeChanges_22 = (short)(0x8000 / particle.ticksRemaining_12);
    //TODO should this still be << 8?
    particle.angleAcceleration_24 = MathHelper.psxDegToRad((effectInner.particleInnerStuff_1c >>> 8 & 0xff) << 8);
  }

  @Method(0x800ff430L)
  private void FUN_800ff430(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._16 = effectInner._10;
    particle._18 = (short)0;
    particle._1a.x = 0.0f;
    particle._1a.y = 50.0f;
    particle.verticalPositionScale_20 = (short)100;
    particle.ticksUntilMovementModeChanges_22 = (short)0;
    particle.angleAcceleration_24 = particle.angleVelocity_10 / particle.ticksRemaining_12;

    particle.particleVelocity_58.y = seed_800fa754.nextInt(31) + 10;
    particle._1a.z = seed_800fa754.nextInt(41) + 40;
    particle._14 = (short)seed_800fa754.nextInt(4097);
  }

  @Method(0x800ff590L)
  private void FUN_800ff590(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    this.FUN_800ff430(effect, particle, effectInner);
    particle.ticksUntilMovementModeChanges_22 = 20;
    particle.verticalPositionScale_20 = 10;
  }

  /**
   * {@link #FUN_800ff5c4} uses t2 which isn't set... assuming the value even matters, this is to pass in t2 from the previous method
   */
  private static Long brokenT2For800ff5c4;

  @Method(0x800ff5c4L)
  private void FUN_800ff5c4(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    if(brokenT2For800ff5c4 == null) {
      throw new RuntimeException("t2 was not set");
    }

    final long t2 = brokenT2For800ff5c4;
    brokenT2For800ff5c4 = null;

    particle.particleVelocity_58.y = -(seed_800fa754.nextInt(61) + 60) * effectInner._18;
    particle._14 = (short)seed_800fa754.nextInt(4097);
    particle._16 = effectInner._10;
    particle._18 = (short)100;
    particle._1a.x = t2 * 4;
    particle.particleAcceleration_60.y = -particle.particleVelocity_58.y / particle.ticksRemaining_12;
  }

  @Method(0x800ff6d4L)
  private void FUN_800ff6d4(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    this.FUN_800ff5c4(effect, particle, effectInner);
    particle._18 = 0;
  }

  @Method(0x800ff6fcL)
  private void FUN_800ff6fc(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int particleIndex = this.currentParticleIndex_8011a008;
    particle.angleVelocity_10 = MathHelper.TWO_PI / 32.0f;
    final int v1 = particleIndex >>> 1;
    particle.angle_0e = MathHelper.psxDegToRad(v1 << 7);
    particle._14 = (short)(v1 << 7);
    particle._16 = effectInner._10;
    particle._18 = (short)(particleIndex & 0x1);
    particle._1a.x = v1 << 3;

    final float colour = Math.max(0, (effectInner.particleInnerStuff_1c >>> 8 & 0xff) - particleIndex * 16) / (float)0x80;

    //LAB_800ff754
    particle.ticksRemaining_12 = -1;
    particle.r_84 = colour;
    particle.g_86 = colour;
    particle.b_88 = colour;
  }

  @Method(0x800ff788L)
  private void FUN_800ff788(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.ticksRemaining_12 = -1;
    particle._14 = (short)seed_800fa754.nextInt(4097);
    particle._16 = effectInner._10;
    particle._18 = (short)seed_800fa754.nextInt(4097);
    particle._1a.x = seed_800fa754.nextInt(4097);
  }

  @Method(0x800ff890L)
  private void FUN_800ff890(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int angle1 = seed_800fa754.nextInt(4097);
    final int angle2 = seed_800fa754.nextInt(2049);

    particle.particleVelocity_58.x = rcos(angle1) * rsin(angle2) / 0x40 >> 12;
    particle.particleVelocity_58.y = rcos(angle2) / 0x40;
    particle.particleVelocity_58.z = rsin(angle1) * rsin(angle2) / 0x40 >> 12;

    particle.ticksRemaining_12 += (short)(seed_800fa754.nextInt(21) - 10);

    if(particle.ticksRemaining_12 <= 0) {
      particle.ticksRemaining_12 = 1;
    }
    //LAB_800ffa60
  }

  @Method(0x800ffa80L)
  private void FUN_800ffa80(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    this.FUN_800ff5c4(effect, particle, effectInner);
    final short s2 = effectInner._10;
    particle._16 = s2;
    particle._1a.y = s2;
    particle._18 = (short)(effectInner._18 * 0xe0);
  }

  @Method(0x800ffadcL)
  private void FUN_800ffadc(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = (short)(seed_800fa754.nextInt(4097));
    final int v0 = -effectInner._10 >> 5;
    particle.r_84 = 0;
    particle.g_86 = 0;
    particle.b_88 = 0;
    particle._16 = effectInner._10;
    particle._18 = (short)(v0 * effectInner._18);
    particle._1a.x = effectInner._18 * 128.0f;
  }

  @Method(0x800ffb80L)
  private void FUN_800ffb80(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final long v0;
    long a0;
    particle.particleAcceleration_60.y = 8.0f;
    a0 = this.currentParticleIndex_8011a008;
    v0 = a0 << 9;
    particle._14 = (short)v0;
    a0 = a0 >>> 2;
    a0 = a0 | 0x1L;
    particle._16 = effectInner._10;
    particle.framesUntilRender_04 = (short)a0;
    particle._18 = (short)(effectInner._18 * 64.0f);
    particle.particleVelocity_58.y = effectInner._14 * -0x40 >> 8;
  }

  @Method(0x800ffbd8L)
  private void FUN_800ffbd8(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = 0;
    particle._18 = (short)(seed_800fa754.nextInt(21) - 10);
    particle._1a.x = seed_800fa754.nextInt(21) - 10;
    particle._1a.y = seed_800fa754.nextInt(81) - 40;
    particle._1a.z = particle.ticksRemaining_12;
    particle.particleVelocity_58.x = (seed_800fa754.nextInt(41) + 44) * effectInner._18;
    particle.particleVelocity_58.y = (seed_800fa754.nextInt(81) - 40) * effectInner._18;
    particle.particleVelocity_58.z = (seed_800fa754.nextInt(41) + 44) * effectInner._18;
    particle.ticksRemaining_12 += 20;
  }

  @Method(0x800ffe80L)
  private void FUN_800ffe80(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = (short)(seed_800fa754.nextInt(4097));
    particle._16 = effectInner._10;
    particle._18 = (short)(effectInner._18 * 32.0f);
    particle.particleAcceleration_60.y = effectInner._18 * 2.0f;
  }

  @Method(0x800ffefcL)
  private void FUN_800ffefc(final ParticleEffectData98 a1, final ParticleEffectInstance94 a2, final ParticleEffectData98Inner24 a3) {
    // no-op
  }

  @Method(0x800fff04L)
  private void FUN_800fff04(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = 0;
    particle._18 = 0;
    particle._1a.z = 0.0f;
    final float v0 = effectInner._18 * 64.0f;
    particle._16 = (short)v0;
    particle._1a.x = v0;
    particle._1a.y = effectInner._10;
  }

  @Method(0x800fff30L)
  private void FUN_800fff30(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.x = seed_800fa754.nextInt(769) + 256;
  }

  @Method(0x800fffa0L)
  private void FUN_800fffa0(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._1a.x = effectInner._10;
    particle._14 = (short)(seed_800fa754.nextInt(4097));
    particle._16 = (short)((seed_800fa754.nextInt(123) + 64) * effectInner._18);
    particle._18 = (short)(0x800 / effect.countParticleInstance_50 * this.currentParticleIndex_8011a008);
  }

  @Method(0x801000b8L)
  private void FUN_801000b8(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.x = -particle.particlePosition_50.x / 32.0f;
    particle.particleVelocity_58.z = -particle.particlePosition_50.z / 32.0f;
  }

  @Method(0x801000f8L)
  private void FUN_801000f8(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    this.FUN_800ff890(effect, particle, effectInner);
    particle.particleVelocity_58.y = -Math.abs(particle.particleVelocity_58.y);
    particle._14 = (short)(effectInner._18 * 0x300);
  }

  @Method(0x80100150L)
  private void FUN_80100150(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._1a.x = (short)(seed_800fa754.nextInt(4097));
    particle._14 = (short)particle.particlePosition_50.x;
    particle._16 = (short)particle.particlePosition_50.y;
    particle._18 = (short)particle.particlePosition_50.z;
    particle._1a.z = effectInner._10 / 4.0f;
    particle.particleVelocity_58.y = effectInner._18 * -64.0f;
    particle._1a.y = (seed_800fa754.nextInt(513) - 256) * effectInner._18;
  }

  @Method(0x8010025cL)
  private void FUN_8010025c(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.y = 64.0f;
    final int angle = seed_800fa754.nextInt(4097);
    if(effectInner.callbackIndex_20 == 0x2a) {
      final int velocityMagnitude = (effectInner._10 & 0xffff) >>> 5;
      particle.particleVelocity_58.x = rcos(angle) * velocityMagnitude >> 12;
      particle.particleVelocity_58.y = rsin(angle) * velocityMagnitude >> 12;
    } else {
      //LAB_80100328
      particle.particleVelocity_58.x = rcos(angle) >>> 7;
      particle.particleVelocity_58.y = rsin(angle) >>> 7;
    }
  }

  @Method(0x801003e8L)
  private void FUN_801003e8(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int s4 = effectInner._10; //TODO read with lw here but as a short everywhere else? Is this a bug?
    this.FUN_800ff890(effect, particle, effectInner);

    final int angle1 = seed_800fa754.nextInt(4097);
    final int angle2 = seed_800fa754.nextInt(2049);
    particle.ticksRemaining_12 = (short)(effectInner.particleInnerStuff_1c >>> 16 & 0xff);
    particle.particlePosition_50.x = (rcos(angle1) * rsin(angle2) >> 12) * s4 >> 12;
    particle.particlePosition_50.y = rcos(angle2) * s4 >> 12;
    particle.particlePosition_50.z = (rsin(angle1) * rsin(angle2) >> 12) * s4 >> 12;
    particle.particleVelocity_58.x = rcos(angle1) * rsin(angle2) >> 18;
    particle.particleVelocity_58.y = rcos(angle2) >> 6;
    particle.particleVelocity_58.z = rsin(angle1) * rsin(angle2) >> 18;
  }

  @Method(0x80100364L)
  private void FUN_80100364(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 particleEffect) {
    this.FUN_800ff890(effect, particle, particleEffect);
    particle._14 = 0;
    particle.particleVelocity_58.y = -Math.abs(particle.particleVelocity_58.y);
    particle.particleAcceleration_60.set(particle.particleVelocity_58).negate().div(particle.ticksRemaining_12);
  }

  @Method(0x801005b8L)
  private void FUN_801005b8(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int s2 = effectInner._10;
    this.FUN_800ff890(effect, particle, effectInner);
    final int angle = seed_800fa754.nextInt(4097);
    particle.particlePosition_50.y = rsin(angle) * s2 >> 12;
    particle.particlePosition_50.z = rcos(angle) * s2 >> 12;
    particle.particleVelocity_58.x = (seed_800fa754.nextInt(65) + 54) * effectInner._18;
    particle.particleAcceleration_60.x = -particle.particleVelocity_58.x / particle.ticksRemaining_12;
    particle.particleAcceleration_60.y = 16.0f;
    final int a1_0 = -((rsin(angle) * s2 >> 12) + s2) / 2;
    particle.particleVelocity_58.y = (seed_800fa754.nextInt(-a1_0 + 1) + a1_0) * effectInner._18;
  }

  @Method(0x801007b4L)
  private void FUN_801007b4(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.set(particle.particlePosition_50).negate().div(particle.ticksRemaining_12);
  }

  @Method(0x80100800L)
  private void FUN_80100800(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._16 = (short)(effectInner._18 * 64.0f);
    particle._18 = effectInner._10;
    particle._14 = (short)(seed_800fa754.nextInt(4097));
  }

  @Method(0x80100878L)
  private void FUN_80100878(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.y = -64.0f;
    particle._18 = effectInner._10;
    particle._16 = (short)(effectInner._18 * 0x2000);
    particle._14 = (short)(seed_800fa754.nextInt(4097));
  }

  @Method(0x801008f8L)
  private void FUN_801008f8(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.angle_0e = 0.0f;
    particle.angleVelocity_10 = 0.0f;
    particle.spriteRotation_70.zero();
    particle.spriteRotationStep_78.zero();
    particle._18 = (short)(effectInner._18 * 256.0f);
    particle._14 = 0x800;
    particle._16 = (short)(effectInner._10 << 4);
    particle._1a.x = effectInner._18 * 64.0f;
    particle.framesUntilRender_04 = (short)(this.currentParticleIndex_8011a008 * (effectInner._14 / effectInner.totalCountParticleInstance_0c));
  }

  @Method(0x80100978L)
  private void FUN_80100978(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = (short)(seed_800fa754.nextInt(4097));
    particle._16 = (short)(seed_800fa754.nextInt((effectInner._10 & 0xffff) + 1));
    particle._18 = (short)(seed_800fa754.nextInt(4097));
    particle._1a.x = (seed_800fa754.nextInt(41) + 150) * effectInner._18;
    particle.particleVelocity_58.y = -64.0f;
  }

  @Method(0x80100af4L)
  private void FUN_80100af4(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    this.FUN_800ff890(effect, particle, effectInner);
    particle.particleVelocity_58.x = particle.particleVelocity_58.x * effectInner._18;
    particle.particleVelocity_58.y = particle.particleVelocity_58.y * effectInner._18;
    particle.particleVelocity_58.z = particle.particleVelocity_58.z * effectInner._18;
    particle.particleAcceleration_60.set(particle.particleVelocity_58).negate().div(particle.ticksRemaining_12);
  }

  @Method(0x80100bb4L)
  private void FUN_80100bb4(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.y = seed_800fa754.nextInt(33) + 16;
  }

  @Method(0x80100c18L)
  private void FUN_80100c18(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = (short)(-particle.particlePosition_50.x / 2.0f * effectInner._18);
    particle._16 = (short)(-particle.particlePosition_50.y / 2.0f * effectInner._18);
    particle._18 = (short)(-particle.particlePosition_50.z / 2.0f * effectInner._18);
    particle._1a.zero();
  }

  @Method(0x80100cacL)
  private void FUN_80100cac(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    this.FUN_800ff890(effect, particle, effectInner);
    particle.particleVelocity_58.y = -Math.abs(particle.particleVelocity_58.y);
    particle.particleVelocity_58.x = 0.0f;
    particle._14 = 0;
  }

  @Method(0x80100cecL)
  private void FUN_80100cec(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = 0;
    particle.particleAcceleration_60.y = effectInner._18 * 2.0f;
  }

  @Method(0x80100d00L)
  private void FUN_80100d00(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final Vector3f translation = new Vector3f();
    getModelObjectTranslation(SCRIPTS.getObject(effectInner.parentScriptIndex_04, BattleEntity27c.class), translation, this.currentParticleIndex_8011a008);
    particle.particlePosition_50.set(translation);
  }

  @Method(0x80100d58L)
  private void FUN_80100d58(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> a1, final ParticleEffectData98 a2, final ParticleEffectInstance94 a3) {
    // no-op
  }

  @Method(0x80100d60L)
  private void FUN_80100d60(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.framesUntilRender_04 == 0 && effect.parentScriptIndex_04 != -1) {
      final Vector3f translation = new Vector3f();
      scriptGetScriptedObjectPos(state.index, translation);

      final Vector3f parentTranslation = new Vector3f();
      scriptGetScriptedObjectPos(effect.parentScriptIndex_04, parentTranslation);

      final Vector3f diffTranslation = new Vector3f().set(translation).sub(parentTranslation);
      particle.particlePosition_50.sub(diffTranslation);
    }
    //LAB_80100e14
  }

  @Method(0x80100e28L)
  private void FUN_80100e28(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.framesUntilRender_04 == 0) {
      particle.stepR_8a = 0;
      particle.stepG_8c = 0;
      particle.stepB_8e = 0;
    }
    //LAB_80100e44
  }

  @Method(0x80100e4cL)
  private void FUN_80100e4c(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.framesUntilRender_04 == 0) {
      particle.stepR_8a = -1.0f / particle.ticksRemaining_12;
      particle.stepG_8c = -1.0f / particle.ticksRemaining_12;
      particle.stepB_8e = -1.0f / particle.ticksRemaining_12;
    }
    //LAB_80100e98
  }

  @Method(0x80100ea0L)
  private void FUN_80100ea0(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state, final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    final int s1 = effect.effectInner_08._10 & 0xffff;

    final Vector3f selfTranslation = new Vector3f();
    final Vector3f parentTranslation = new Vector3f();
    scriptGetScriptedObjectPos(effect.myState_00.index, selfTranslation);
    scriptGetScriptedObjectPos(effect.parentScriptIndex_04, parentTranslation);

    final Vector3f diffTranslation = new Vector3f().set(parentTranslation).sub(selfTranslation);

    final int mod = s1 * 2 + 1;
    particle.particleVelocity_58.x = diffTranslation.x / 8.0f + (seed_800fa754.nextInt(mod) - s1 >>> 4);
    particle.particleVelocity_58.y = diffTranslation.y / 8.0f + (seed_800fa754.nextInt(mod) - s1 >>> 4);
    particle.particleVelocity_58.z = diffTranslation.z / 8.0f + (seed_800fa754.nextInt(mod) - s1 >>> 4);
    particle.particleVelocity_58.x *= effect.effectInner_08._18;
    particle.particleVelocity_58.y *= effect.effectInner_08._18;
    particle.particleVelocity_58.z *= effect.effectInner_08._18;

    if(particle.particleVelocity_58.x == 0) {
      particle.particleVelocity_58.x = 1.0f;
    }

    //LAB_80101068
    particle.ticksRemaining_12 = (short)(diffTranslation.x / particle.particleVelocity_58.x);
  }

  @Method(0x801010a0L)
  private void FUN_801010a0(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    // Calculate the index of this array element
    this.currentParticleIndex_8011a008 = particle.index;

    this.initializeParticleInstance(effect, particle, effect.effectInner_08);

    if(effect.countParticleSub_54 != 0) {
      final int v1 = effect.subParticleType_60;

      if(v1 == 0) {
        //LAB_801011a0
        final GpuCommandPoly cmd = new GpuCommandPoly(4);

        //LAB_801011d8
        for(int i = 0; i < effect.countParticleSub_54; i++) {
          final ParticleEffectInstance94Sub10 s0 = particle.particleInstanceSubArray_80[i];

          this.FUN_800fca78(manager, effect, particle, particle.particlePosition_50, cmd);
          s0.x0_00 = cmd.getX(0);
          s0.y0_02 = cmd.getY(0);
          s0.x1_04 = cmd.getX(1);
          s0.y1_06 = cmd.getY(1);
          s0.x2_08 = cmd.getX(2);
          s0.y2_0a = cmd.getY(2);
          s0.x3_0c = cmd.getX(3);
          s0.y3_0e = cmd.getY(3);
        }
        //LAB_8010114c
      } else if(v1 == 2 || v1 >= 4 && v1 < 6) {
        //LAB_80101160
        //LAB_80101170
        for(int i = 0; i < effect.countParticleSub_54; i++) {
          particle.subParticlePositionsArray_44[i].set(particle.particlePosition_50);
        }
      }
    }
    //LAB_8010127c
  }

  @Method(0x80101c68L)
  private void FUN_80101c68(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner, final int flags) {
    effect.subParticleType_60 = 3;
  }

  @Method(0x80101c74L)
  private void FUN_80101c74(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner, final int flags) {
    effect.subParticleType_60 = (byte)(flags >> 20);
    effect.countParticleSub_54 = (short)flags;

    //LAB_80101cb0
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 inst = effect.particleArray_68[i];
      inst.subParticlePositionsArray_44 = new Vector3f[effect.countParticleSub_54];
      Arrays.setAll(inst.subParticlePositionsArray_44, n -> new Vector3f().set(inst.particlePosition_50));
      //LAB_80101cdc
      //LAB_80101d04
    }
    //LAB_80101d1c
  }

  @Method(0x80101d3cL)
  private void FUN_80101d3c(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner, final int flags) {
    effect.countParticleSub_54 = 0;
    effect.subParticleType_60 = 1;

    if((flags & 0xf_ff00) == 0xf_ff00) {
      effect.tmd_30 = deffManager_800c693c.tmds_2f8[flags & 0xff];
      effect.tpage_56 = 0x20;
    } else {
      //LAB_80101d98
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)deffManager_800c693c.getDeffPart(0x300_0000 | flags & 0xf_ffff);
      final CContainer extTmd = tmdType.tmd_0c;
      final TmdWithId tmd = extTmd.tmdPtr_00;
      effect.tmd_30 = tmd.tmd.objTable[0];
      effect.tpage_56 = (int)((tmd.id & 0xffff_0000L) >>> 11);
    }

    //LAB_80101dd8
    if((effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000) != 0) {
      effect.countParticleSub_54 = particleSubCounts_800fb794[(effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000) >>> 28];

      //LAB_80101e3c
      for(int i = 0; i < effect.countParticleInstance_50; i++) {
        effect.particleArray_68[i].subParticlePositionsArray_44 = new Vector3f[effect.countParticleSub_54];
        Arrays.setAll(effect.particleArray_68[i].subParticlePositionsArray_44, n -> new Vector3f());
      }
    }
    //LAB_80101e6c
  }

  @Method(0x80101e84L)
  private void FUN_80101e84(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner, final int flags) {
    effect.subParticleType_60 = 0;
    effect.countParticleSub_54 = 0;

    if((effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000) != 0) {
      final int index = (effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000) >>> 28;
      effect.countParticleSub_54 = particleSubCounts_800fb794[index];

      //LAB_80101f2c
      for(int i = 0; i < effect.countParticleInstance_50; i++) {
        final ParticleEffectInstance94 inst = effect.particleArray_68[i];
        inst.particleInstanceSubArray_80 = new ParticleEffectInstance94Sub10[effect.countParticleSub_54];
        Arrays.setAll(inst.particleInstanceSubArray_80, n -> new ParticleEffectInstance94Sub10());
      }
    }

    //LAB_80101f70
    if((flags & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = deffManager_800c693c.spriteMetrics_39c[flags & 0xff];
      effect.u_58 = metrics.u_00;
      effect.v_5a = metrics.v_02;
      effect.w_5e = metrics.w_04;
      effect.h_5f = metrics.h_05;
      effect.clut_5c = metrics.clut_06;
    } else {
      //LAB_80101fec
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)deffManager_800c693c.getDeffPart(flags | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      effect.u_58 = deffMetrics.u_00;
      effect.v_5a = deffMetrics.v_02;
      effect.w_5e = deffMetrics.w_04 * 4;
      effect.h_5f = deffMetrics.h_06;
      effect.clut_5c = GetClut(deffMetrics.clutX_08, deffMetrics.clutY_0a);
    }

    //LAB_80102048
    effect.halfW_34 = (short)(effect.w_5e >>> 1);
    effect.halfH_36 = (short)(effect.h_5f >>> 1);
  }

  public static final ParticleInnerStuff04[] particleInnerStuffDefaultsArray_801197ec = {
    new ParticleInnerStuff04(32, 127, 35, 1),
    new ParticleInnerStuff04(32, 127, 35, 0),
    new ParticleInnerStuff04(10, 64, 0, 0),
    new ParticleInnerStuff04(19, 64, 0, 0),
    new ParticleInnerStuff04(19, 64, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 1),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(27, 127, 0, 0),
    new ParticleInnerStuff04(48, 127, 0, 1),
    new ParticleInnerStuff04(48, 127, 0, 1),
    new ParticleInnerStuff04(48, 127, 0, 0),
    new ParticleInnerStuff04(48, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(26, 64, 0, 0),
    new ParticleInnerStuff04(26, 128, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 1),
    new ParticleInnerStuff04(255, 128, 0, 0),
    new ParticleInnerStuff04(26, 128, 0, 1),
    new ParticleInnerStuff04(32, 128, 0, 0),
    new ParticleInnerStuff04(32, 128, 0, 0),
    new ParticleInnerStuff04(255, 128, 0, 0),
    new ParticleInnerStuff04(20, 128, 0, 0),
    new ParticleInnerStuff04(48, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(10, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(255, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(48, 127, 0, 0),
    new ParticleInnerStuff04(255, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(255, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(16, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 1),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 1),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(96, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(32, 127, 35, 0),
    new ParticleInnerStuff04(32, 127, 0, 0),
    new ParticleInnerStuff04(4, 127, 0, 0),
  };

  public static final ParticleInitialTransformationMetrics10[] particleInitialTransformationMetrics_801198f0 = {
    new ParticleInitialTransformationMetrics10(1, 12, 0, true, true, 5, 80),
    new ParticleInitialTransformationMetrics10(1, 12, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(1, 12, 0, true, true, 10, 80),
    new ParticleInitialTransformationMetrics10(1, 12, 0, true, true, 10, 80),
    new ParticleInitialTransformationMetrics10(1, 12, 0, false, true, 10, 80),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, true, -30, -10),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, true, -30, -10),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(3, -1024, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, true, -30, -10),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, true, 5, 80),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, true, 5, 80),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, true, 5, 80),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(4, 12, 12, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(1, 12, 0, true, true, 5, 80),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, true, 5, 80),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(2, 12, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(2, 12, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(4, 12, 12, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(4, 12, 12, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(4, 12, 12, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(4, 12, 12, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(4, 12, 12, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(1, 12, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
    new ParticleInitialTransformationMetrics10(1, 12, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, true, false, 0, 0),
    new ParticleInitialTransformationMetrics10(0, 0, 0, false, false, 0, 0),
  };

  /**
   * <ol start="0">
   *   <li>{@link this#renderLineParticles}</li>
   *   <li>null</li>
   *   <li>{@link this#FUN_800fcf18}</li>
   * </ol>
   */
  private final BiConsumer<EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleMetrics48>[] lineParticleRenderers_801197c0 = new BiConsumer[3];
  {
    this.lineParticleRenderers_801197c0[0] = this::renderLineParticles;
    this.lineParticleRenderers_801197c0[1] = null;
    this.lineParticleRenderers_801197c0[2] = this::FUN_800fcf18; // no-op
  }

  private final TriConsumer<EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleEffectData98, ParticleEffectInstance94>[] particleInstancePrerenderCallbacks_80119bac = new TriConsumer[65];
  {
    this.particleInstancePrerenderCallbacks_80119bac[0] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[1] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[2] = this::FUN_800fb9c8;
    this.particleInstancePrerenderCallbacks_80119bac[3] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[4] = this::FUN_800fb9ec;
    this.particleInstancePrerenderCallbacks_80119bac[5] = this::FUN_800fba58;
    this.particleInstancePrerenderCallbacks_80119bac[6] = this::FUN_800fbb14;
    this.particleInstancePrerenderCallbacks_80119bac[7] = this::FUN_800fbb14;
    this.particleInstancePrerenderCallbacks_80119bac[8] = this::FUN_800fbbe0;
    this.particleInstancePrerenderCallbacks_80119bac[9] = this::FUN_800fbbe0;
    this.particleInstancePrerenderCallbacks_80119bac[10] = this::FUN_800fbd04;
    this.particleInstancePrerenderCallbacks_80119bac[11] = this::FUN_800fbd04;
    this.particleInstancePrerenderCallbacks_80119bac[12] = this::FUN_800fba58;
    this.particleInstancePrerenderCallbacks_80119bac[13] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[14] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[15] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[16] = this::FUN_800fbd68;
    this.particleInstancePrerenderCallbacks_80119bac[17] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[18] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[19] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[20] = this::FUN_800fbe94;
    this.particleInstancePrerenderCallbacks_80119bac[21] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[22] = this::FUN_800fbf50;
    this.particleInstancePrerenderCallbacks_80119bac[23] = this::FUN_800fbfd0;
    this.particleInstancePrerenderCallbacks_80119bac[24] = this::FUN_800fc068;
    this.particleInstancePrerenderCallbacks_80119bac[25] = this::FUN_800fc0d0;
    this.particleInstancePrerenderCallbacks_80119bac[26] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[27] = this::FUN_800fc1fc;
    this.particleInstancePrerenderCallbacks_80119bac[28] = this::FUN_800fc068;
    this.particleInstancePrerenderCallbacks_80119bac[29] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[30] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[31] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[32] = this::FUN_800fc280;
    this.particleInstancePrerenderCallbacks_80119bac[33] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[34] = this::FUN_800fc348;
    this.particleInstancePrerenderCallbacks_80119bac[35] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[36] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[37] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[38] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[39] = this::FUN_800fc410;
    this.particleInstancePrerenderCallbacks_80119bac[40] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[41] = this::FUN_800fc42c;
    this.particleInstancePrerenderCallbacks_80119bac[42] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[43] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[44] = this::FUN_800fc410;
    this.particleInstancePrerenderCallbacks_80119bac[45] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[46] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[47] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[48] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[49] = this::FUN_800fc528;
    this.particleInstancePrerenderCallbacks_80119bac[50] = this::FUN_800fc5a8;
    this.particleInstancePrerenderCallbacks_80119bac[51] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[52] = this::FUN_800fc61c;
    this.particleInstancePrerenderCallbacks_80119bac[53] = this::FUN_800fc6bc;
    this.particleInstancePrerenderCallbacks_80119bac[54] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[55] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[56] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[57] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[58] = this::FUN_800fc768;
    this.particleInstancePrerenderCallbacks_80119bac[59] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[60] = this::FUN_800fc7c8;
    this.particleInstancePrerenderCallbacks_80119bac[61] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[62] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[63] = this::FUN_800fb9c0; // no-op
    this.particleInstancePrerenderCallbacks_80119bac[64] = this::FUN_800fb9c0; // no-op
  }

  private final QuadConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>>, EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleEffectData98, ParticleEffectInstance94>[] particleInstanceTickCallbacks_80119cb0 = new QuadConsumer[65];
  {
    this.particleInstanceTickCallbacks_80119cb0[0] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[1] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[2] = this::FUN_80100d60;
    this.particleInstanceTickCallbacks_80119cb0[3] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[4] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[5] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[6] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[7] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[8] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[9] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[10] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[11] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[12] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[13] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[14] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[15] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[16] = this::FUN_80100e28;
    this.particleInstanceTickCallbacks_80119cb0[17] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[18] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[19] = this::FUN_80100e28;
    this.particleInstanceTickCallbacks_80119cb0[20] = this::FUN_80100e28;
    this.particleInstanceTickCallbacks_80119cb0[21] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[22] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[23] = this::FUN_80100e4c;
    this.particleInstanceTickCallbacks_80119cb0[24] = this::FUN_80100e28;
    this.particleInstanceTickCallbacks_80119cb0[25] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[26] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[27] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[28] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[29] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[30] = this::FUN_80100ea0;
    this.particleInstanceTickCallbacks_80119cb0[31] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[32] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[33] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[34] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[35] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[36] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[37] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[38] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[39] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[40] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[41] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[42] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[43] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[44] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[45] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[46] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[47] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[48] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[49] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[50] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[51] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[52] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[53] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[54] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[55] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[56] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[57] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[58] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[59] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[60] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[61] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[62] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[63] = this::FUN_80100d58; // no-op
    this.particleInstanceTickCallbacks_80119cb0[64] = this::FUN_80100d58; // no-op
  }

  private final TriConsumer<ParticleEffectData98, ParticleEffectInstance94, ParticleEffectData98Inner24>[] initializerCallbacks_80119db4 = new TriConsumer[65];
  {
    this.initializerCallbacks_80119db4[0] = this::FUN_800fea70;
    this.initializerCallbacks_80119db4[1] = this::FUN_800fec3c;
    this.initializerCallbacks_80119db4[2] = this::FUN_800feccc;
    this.initializerCallbacks_80119db4[3] = this::FUN_800fee9c;
    this.initializerCallbacks_80119db4[4] = this::FUN_800fefe4;
    this.initializerCallbacks_80119db4[5] = this::FUN_800ff15c;
    this.initializerCallbacks_80119db4[6] = this::FUN_800ff3e0;
    this.initializerCallbacks_80119db4[7] = this::FUN_800ff3e0;
    this.initializerCallbacks_80119db4[8] = this::FUN_800ff430;
    this.initializerCallbacks_80119db4[9] = this::FUN_800ff590;
    this.initializerCallbacks_80119db4[10] = this::FUN_800ff5c4;
    this.initializerCallbacks_80119db4[11] = this::FUN_800ff6d4;
    this.initializerCallbacks_80119db4[12] = this::FUN_800ff15c;
    this.initializerCallbacks_80119db4[13] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[14] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[15] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[16] = this::FUN_800ff6fc;
    this.initializerCallbacks_80119db4[17] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[18] = this::FUN_800fea70;
    this.initializerCallbacks_80119db4[19] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[20] = this::FUN_800ff788;
    this.initializerCallbacks_80119db4[21] = this::FUN_800ff890;
    this.initializerCallbacks_80119db4[22] = this::FUN_800ffa80;
    this.initializerCallbacks_80119db4[23] = this::FUN_800ffadc;
    this.initializerCallbacks_80119db4[24] = this::FUN_800ffb80;
    this.initializerCallbacks_80119db4[25] = this::FUN_800ffbd8;
    this.initializerCallbacks_80119db4[26] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[27] = this::FUN_800ffb80;
    this.initializerCallbacks_80119db4[28] = this::FUN_800ffe80;
    this.initializerCallbacks_80119db4[29] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[30] = this::FUN_800ffefc; // no-op
    this.initializerCallbacks_80119db4[31] = this::FUN_800fea70;
    this.initializerCallbacks_80119db4[32] = this::FUN_800fff04;
    this.initializerCallbacks_80119db4[33] = this::FUN_800fff30;
    this.initializerCallbacks_80119db4[34] = this::FUN_800fffa0;
    this.initializerCallbacks_80119db4[35] = this::FUN_801000b8;
    this.initializerCallbacks_80119db4[36] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[37] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[38] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[39] = this::FUN_801000f8;
    this.initializerCallbacks_80119db4[40] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[41] = this::FUN_80100150;
    this.initializerCallbacks_80119db4[42] = this::FUN_8010025c;
    this.initializerCallbacks_80119db4[43] = this::FUN_8010025c;
    this.initializerCallbacks_80119db4[44] = this::FUN_80100364;
    this.initializerCallbacks_80119db4[45] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[46] = this::FUN_801003e8;
    this.initializerCallbacks_80119db4[47] = this::FUN_801005b8;
    this.initializerCallbacks_80119db4[48] = this::FUN_801007b4;
    this.initializerCallbacks_80119db4[49] = this::FUN_80100800;
    this.initializerCallbacks_80119db4[50] = this::FUN_80100878;
    this.initializerCallbacks_80119db4[51] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[52] = this::FUN_801008f8;
    this.initializerCallbacks_80119db4[53] = this::FUN_80100978;
    this.initializerCallbacks_80119db4[54] = this::FUN_80100af4;
    this.initializerCallbacks_80119db4[55] = this::FUN_80100bb4;
    this.initializerCallbacks_80119db4[56] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[57] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[58] = this::FUN_80100c18;
    this.initializerCallbacks_80119db4[59] = this::FUN_80100cac;
    this.initializerCallbacks_80119db4[60] = this::FUN_80100cec;
    this.initializerCallbacks_80119db4[61] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[62] = this::FUN_800fea70;
    this.initializerCallbacks_80119db4[63] = this::FUN_800fea68; // no-op
    this.initializerCallbacks_80119db4[64] = this::FUN_80100d00;
  }

  /**
   * Particle effect renderers
   * <ol start="0">
   *   <li>{@link #renderQuadParticleEffect}</li>
   *   <li>{@link #renderTmdParticleEffect}</li>
   *   <li>{@link #renderLineParticleEffect}</li>
   *   <li>{@link #renderPixelParticleEffect}</li>
   *   <li>{@link #renderLineParticleEffect}</li>
   *   <li>{@link #renderNoParticlesWhatsoever}</li>
   * </ol>
   */
  private final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>>, EffectManagerData6c<EffectManagerParams.ParticleType>>[] particleEffectRenderers_80119b7c = new BiConsumer[6];
  {
    this.particleEffectRenderers_80119b7c[0] = this::renderQuadParticleEffect;
    this.particleEffectRenderers_80119b7c[1] = this::renderTmdParticleEffect;
    this.particleEffectRenderers_80119b7c[2] = this::renderLineParticleEffect;
    this.particleEffectRenderers_80119b7c[3] = this::renderPixelParticleEffect;
    this.particleEffectRenderers_80119b7c[4] = this::renderLineParticleEffect;
    this.particleEffectRenderers_80119b7c[5] = this::renderNoParticlesWhatsoever; // no-op
  }

  /**
   * <ol start="0">
   *   <li>{@link #FUN_80101e84}</li>
   *   <li>{@link #FUN_80101d3c}</li>
   *   <li>{@link #FUN_80101c74}</li>
   *   <li>{@link #FUN_80101c68}</li>
   *   <li>{@link #FUN_80101c74}</li>
   *   <li>{@link #FUN_80101c74}</li>
   * </ol>
   */
  private final QuadConsumer<ParticleEffectData98, ParticleEffectInstance94, ParticleEffectData98Inner24, Integer>[] subParticleInitializers_80119b94 = new QuadConsumer[6];
  {
    this.subParticleInitializers_80119b94[0] = this::FUN_80101e84;
    this.subParticleInitializers_80119b94[1] = this::FUN_80101d3c;
    this.subParticleInitializers_80119b94[2] = this::FUN_80101c74;
    this.subParticleInitializers_80119b94[3] = this::FUN_80101c68;
    this.subParticleInitializers_80119b94[4] = this::FUN_80101c74;
    this.subParticleInitializers_80119b94[5] = this::FUN_80101c74;
  }
}
