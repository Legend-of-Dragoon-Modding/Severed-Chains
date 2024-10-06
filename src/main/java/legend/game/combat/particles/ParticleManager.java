package legend.game.combat.particles;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.memory.types.TriConsumer;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.environment.BattleCamera;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.ScriptState;
import org.joml.Math;
import org.joml.Vector3f;

import javax.annotation.Nullable;

import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.SEffe.allocateEffectManager;
import static legend.game.combat.SEffe.calculateBentPartPosition;
import static legend.game.combat.SEffe.rotateAndTranslateEffect;
import static legend.game.combat.SEffe.scriptGetScriptedObjectPos;

public class ParticleManager {
  public static final short[] particleSubCounts_800fb794 = {0, 0, 4, 0, 8, 0, 16, 0, 0, 0, 1, 0, 2, 0, 4, 0, 3, 0, 5, 0};

  private ParticleEffectData98 firstParticle_8011a00c;
  private ParticleEffectData98 lastParticle_8011a010;

  public final BattleCamera camera;

  public ParticleManager(final BattleCamera camera) {
    this.camera = camera;
  }

  public ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> allocateParticle(final ScriptState<? extends BattleObject> parent, final int behaviourType, final int particleCount, final int particleTypeId, final int _10, final int _14, final int _18, int innerStuff, final int parentScriptIndex) {
    if((innerStuff & 0xff) == 0) {
      innerStuff |= particleInnerStuffDefaultsArray_801197ec[behaviourType].ticksRemaining_02;
    }

    if((innerStuff & 0xff00) == 0) {
      innerStuff |= particleInnerStuffDefaultsArray_801197ec[behaviourType].colour_01 << 8;
    }

    if((innerStuff & 0xff_0000) == 0) {
      innerStuff |= particleInnerStuffDefaultsArray_801197ec[behaviourType].renderFrameCount_00 << 16;
    }

    final int renderType = particleTypeId >> 20;

    final ParticleEffectData98 particle = switch(renderType) {
      case 0 -> new QuadParticle(this, parentScriptIndex, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 1 -> new TmdParticle(this, parentScriptIndex, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 2 -> new LineParticle(this, parentScriptIndex, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 3 -> new PixelParticle(this, parentScriptIndex, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 4 -> new JkNotActuallyALineParticle(this, parentScriptIndex, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 5 -> new WhyIsThereANoParticle(this, parentScriptIndex, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      default -> throw new RuntimeException("Invalid particle type");
    };

    final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state = allocateEffectManager(
      "Particle effect %x".formatted(particleTypeId),
      parent,
      particle,
      new EffectManagerParams.ParticleType()
    );

    final EffectManagerData6c<EffectManagerParams.ParticleType> manager = state.innerStruct_00;
    manager.params_10.flags_00 |= 0x5000_0000;
    manager.flags_04 |= 0x4_0000;

    if(this.firstParticle_8011a00c == null) {
      this.firstParticle_8011a00c = particle;
    }

    if(this.lastParticle_8011a010 != null) {
      this.lastParticle_8011a010.next_94 = particle;
    }

    this.lastParticle_8011a010 = particle;

    particle.init(particleTypeId);
    return state;
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
  public void deleteParticle(final ParticleEffectData98 particle) {
    final ParticleEffectData98 parent = this.findParticleParent(particle);

    if(parent == null) {
      this.firstParticle_8011a00c = particle.next_94;
    } else {
      //LAB_800fe8f0
      parent.next_94 = particle.next_94;
    }

    //LAB_800fe8fc
    if(particle.next_94 == null) {
      this.lastParticle_8011a010 = parent;
    }

    //LAB_800fea30
    //LAB_800fea3c
  }

  /**
   * Sets a particle's position based on an angle and distance
   */
  @Method(0x800fb95cL)
  private static void setParticlePositionAlongVector(final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rcos(particle._14) * particle._16 >> 12;
    particle.particlePosition_50.z = rsin(particle._14) * particle._16 >> 12;
  }

  @Method(0x800fb9c0L)
  private static void preRenderNoop(final EffectManagerData6c<EffectManagerParams.ParticleType> a0, final ParticleEffectData98 a1, final ParticleEffectInstance94 a2) {
    // no-op
  }

  @Method(0x800fb9c8L)
  private static void preRender2(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particleVelocity_58.x -= particle._16;
    particle.particleVelocity_58.y += particle._14;
  }

  @Method(0x800fb9ecL)
  private static void preRender4(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    setParticlePositionAlongVector(particle);

    particle._14 += particle._18;
    particle._16 += particle._1a.x;
    particle.particleVelocity_58.y -= 2.0f;

    if(particle._1a.x >= 8.0f) {
      particle._1a.x -= 8.0f;
    }
  }

  @Method(0x800fba58L)
  private static void preRender5(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    setParticlePositionAlongVector(particle);

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
  private static void preRender6(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    setParticlePositionAlongVector(particle);

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
  private static void preRender8(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    setParticlePositionAlongVector(particle);
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
  private static void preRender10(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    setParticlePositionAlongVector(particle);

    particle._16 += particle._18;
    if(particle._18 >= 3) {
      particle._18 -= 3;
    }

    //LAB_800fbd44
    particle._14 += particle._1a.x;
  }

  @Method(0x800fbd68L)
  private static void preRender16(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    final Vector3f sp0x38 = new Vector3f();
    if(particle._18 == 0) {
      setParticlePositionAlongVector(particle);
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
  private static void preRender20(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    setParticlePositionAlongVector(particle);
    particle.particlePosition_50.y = 0.0f;
    particle.particlePosition_50.z /= 2.0f;
    final Vector3f sp0x38 = new Vector3f(MathHelper.psxDegToRad(particle._18), 0.0f, MathHelper.psxDegToRad(particle._1a.x));
    particle._14 += 0x80;

    final Vector3f sp0x28 = new Vector3f();
    rotateAndTranslateEffect(manager, sp0x38, particle.particlePosition_50, sp0x28);
    particle.particlePosition_50.set(sp0x28);
  }

  @Method(0x800fbf50L)
  private static void preRender22(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    setParticlePositionAlongVector(particle);

    particle._16 = (short)(particle._16 * particle._18 >> 8);

    final float v1 = particle._1a.y / 4.0f;
    if(particle._16 < v1) {
      particle._16 = (short)v1;
    }

    //LAB_800fbfac
    particle._14 += particle._1a.x;
  }

  @Method(0x800fbfd0L)
  private static void preRender23(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
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
  private static void preRender24(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    setParticlePositionAlongVector(particle);

    particle._16 += particle._18;

    if(particle.particlePosition_50.y + particle.managerTranslation_2c.y >= manager.params_10.y_30) {
      particle.ticksRemaining_12 = 1;
    }
    //LAB_800fc0bc
  }

  @Method(0x800fc0d0L)
  private static void preRender25(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.particlePosition_50.y + particle.managerTranslation_2c.y >= -400 && particle._14 == 0) {
      particle._14 = 1;
      particle.particleAcceleration_60.y = -8.0f;
      //LAB_800fc11c
    } else if(effect.parentScriptIndex_04 != -1 && particle._14 == 0) {
      final Vector3f sp0x10 = new Vector3f();
      scriptGetScriptedObjectPos(effect.parentScriptIndex_04, sp0x10);
      particle.particleVelocity_58.x = (sp0x10.x - (particle.originalParticlePosition_3c.x + particle.managerTranslation_2c.x)) / particle._1a.z;
      particle.particleVelocity_58.y = (sp0x10.y - (particle.originalParticlePosition_3c.y + particle.managerTranslation_2c.y)) / particle._1a.z;
      particle.particleVelocity_58.z = (sp0x10.z - (particle.originalParticlePosition_3c.z + particle.managerTranslation_2c.z)) / particle._1a.z;
      particle.particleVelocity_58.x += particle._18;
      particle.particleVelocity_58.y += particle._1a.x;
      particle.particleVelocity_58.z += particle._1a.y;
    }
    //LAB_800fc1ec
  }

  @Method(0x800fc1fcL)
  private static void preRender27(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    setParticlePositionAlongVector(particle);

    particle._16 += particle._18;
    if(particle.particlePosition_50.y + particle.managerTranslation_2c.y >= manager.params_10.y_30) {
      particle.particlePosition_50.y = manager.params_10.y_30 - particle.managerTranslation_2c.y;
      particle.particleVelocity_58.y = -particle.particleVelocity_58.y / 2.0f;
    }
    //LAB_800fc26c
  }

  @Method(0x800fc280L)
  private static void preRender32(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.z = rcos(particle._14) * 2 * particle._1a.y / 0x1000;
    particle.particlePosition_50.x = rsin((int)particle._1a.z) * particle._1a.y / 0x1000;
    particle._14 += particle._16;
    particle._1a.z = particle._1a.z + particle._16 * 2;
    particle.particlePosition_50.y += 0x40 + ((rcos(particle._18) >> 1) + 0x800) * particle._1a.y / 0x8000;
    particle._18 += particle._1a.x;
  }

  @Method(0x800fc348L)
  private static void preRender34(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rcos(particle._14) * particle._1a.x / 0x1000 * rsin(particle._18) / 0x1000;
    particle.particlePosition_50.y = particle._18 * 2 - 0x800;
    particle.particlePosition_50.z = rsin(particle._14) * particle._1a.x / 0x1000 * rsin(particle._18) / 0x1000;
    particle._14 += particle._16;
  }

  @Method(0x800fc410L)
  private static void preRender39(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particleVelocity_58.y += particle._14 / 0x100;
  }

  @Method(0x800fc42cL)
  private static void preRender41(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = particle._14 + rcos((int)particle._1a.x) * particle._1a.z / 0x1000;
    particle.particlePosition_50.z = particle._18 + rsin((int)particle._1a.x) * particle._1a.z / 0x1000;
    particle._1a.z += 16.0f;
    particle._1a.x += particle._1a.y;
  }

  @Method(0x800fc528L)
  private static void preRender49(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rsin(particle._14) * (particle._18 >> 1) >> 12;
    particle.particlePosition_50.z = rcos(particle._14) * particle._18 >> 12;
    particle._14 += particle._16;
  }

  @Method(0x800fc5a8L)
  private static void preRender50(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rsin(particle._14) * particle._18 >> 12;
    particle.particlePosition_50.z = rcos(particle._14) * particle._18 >> 12;
    particle._18 = (short)(particle._18 * 7 / 8);
  }

  @Method(0x800fc61cL)
  private static void preRender52(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.scaleVertical_08 = MathHelper.psxDegToRad((short)((rcos(particle._14) * particle._16 >> 12) * manager.params_10.scale_16.y));
    particle._14 -= particle._18;

    if(particle._16 > 0) {
      particle._16 -= particle._1a.x;
    }

    //LAB_800fc6a8
  }

  @Method(0x800fc6bcL)
  private static void preRender53(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.x = rsin(particle._14) * particle._16 >> 12;
    particle.particlePosition_50.z = rcos(particle._14) * particle._16 >> 12;
    particle.particlePosition_50.x += rsin(particle._18) << 8 >> 12;
    particle.particlePosition_50.z += rcos(particle._18) << 8 >> 12;
    particle._18 += particle._1a.x;
  }

  @Method(0x800fc768L)
  private static void preRender58(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle._1a.x += particle._14;
    particle._1a.y += particle._16;
    particle._1a.z += particle._18;
    particle.particleVelocity_58.x = particle._1a.x / 0x100;
    particle.particleVelocity_58.y = particle._1a.y / 0x100;
    particle.particleVelocity_58.z = particle._1a.z / 0x100;
  }

  @Method(0x800fc7c8L)
  private static void preRender60(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
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

  @Method(0x800fea68L)
  private static void initializerNoop(final ParticleEffectData98 a1, final ParticleEffectInstance94 a2, final ParticleEffectData98Inner24 a3) {
    // no-op
  }

  @Method(0x800fea70L)
  private static int initializer0(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
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
  private static void initializer1(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int s0 = initializer0(effect, particle, effectInner);
    particle.particleVelocity_58.x = rcos(s0) >> 6;
    particle.particleVelocity_58.y = 0.0f;
    particle.particleVelocity_58.z = rsin(s0) >> 6;
  }

  @Method(0x800fecccL)
  private static void initializer2(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int angle = seed_800fa754.nextInt(4097);
    particle.particleVelocity_58.x = rcos(angle) >> 10;
    particle.particleVelocity_58.y = -(seed_800fa754.nextInt(33) + 13);
    particle.particleVelocity_58.z = rsin(angle) >> 10;
    particle._14 = (short)(seed_800fa754.nextInt(3) + 1);
    particle._16 = (short)(seed_800fa754.nextInt(3));
    particle.framesUntilRender_04 = (short)(particle.framesUntilRender_04 / 4 * 4 + 1);
  }

  @Method(0x800fee9cL)
  private static void initializer3(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int angle = seed_800fa754.nextInt(4097);
    particle.particleVelocity_58.x = rcos(angle) / 0x80;
    particle.particleVelocity_58.y = 0.0f;
    particle.particleVelocity_58.z = rsin(angle) / 0x80;
    particle.framesUntilRender_04 = 1;
    particle._14 = (short)(seed_800fa754.nextInt(6));
  }

  @Method(0x800fefe4L)
  private static void initializer4(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    initializer3(effect, particle, effectInner);
    particle._14 = (short)(seed_800fa754.nextInt(4097));
    particle._16 = effectInner._10;
    particle._18 = (short)(seed_800fa754.nextInt(91) + 10);
    particle._1a.x = 120.0f;
    particle.particleVelocity_58.x = 0.0f;
    particle.particleVelocity_58.y = -seed_800fa754.nextInt(11) - 5;
    particle.particleVelocity_58.z = 0.0f;
  }

  @Method(0x800ff15cL)
  private static void initializer5(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
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
  private static void initializer6(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    initializer5(effect, particle, effectInner);
    particle.verticalPositionScale_20 = (short)0;
    particle.ticksUntilMovementModeChanges_22 = (short)(0x8000 / particle.ticksRemaining_12);
    //TODO should this still be << 8?
    particle.angleAcceleration_24 = MathHelper.psxDegToRad((effectInner.particleInnerStuff_1c >>> 8 & 0xff) << 8);
  }

  @Method(0x800ff430L)
  private static void initializer8(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
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
  private static void initializer9(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    initializer8(effect, particle, effectInner);
    particle.ticksUntilMovementModeChanges_22 = 20;
    particle.verticalPositionScale_20 = 10;
  }

  @Method(0x800ff5c4L)
  private static void initializer10(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    // This method uses an uninitialized variable - the way the ASM was generated, it uses t2 which is set
    // in the calling method to the behaviourType. Since this is the 10th behaviour, t2 will always be 10.
    final int t2 = 10;

    particle.particleVelocity_58.y = -(seed_800fa754.nextInt(61) + 60) * effectInner._18;
    particle._14 = (short)seed_800fa754.nextInt(4097);
    particle._16 = effectInner._10;
    particle._18 = (short)100;
    particle._1a.x = t2 * 4;
    particle.particleAcceleration_60.y = -particle.particleVelocity_58.y / particle.ticksRemaining_12;
  }

  @Method(0x800ff6d4L)
  private static void initializer11(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    initializer10(effect, particle, effectInner);
    particle._18 = 0;
  }

  @Method(0x800ff6fcL)
  private static void initializer16(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.angleVelocity_10 = MathHelper.TWO_PI / 32.0f;
    final int v1 = particle.index >>> 1;
    particle.angle_0e = MathHelper.psxDegToRad(v1 << 7);
    particle._14 = (short)(v1 << 7);
    particle._16 = effectInner._10;
    particle._18 = (short)(particle.index & 0x1);
    particle._1a.x = v1 << 3;

    final float colour = Math.max(0, (effectInner.particleInnerStuff_1c >>> 8 & 0xff) - particle.index * 16) / (float)0x80;

    //LAB_800ff754
    particle.ticksRemaining_12 = -1;
    particle.r_84 = colour;
    particle.g_86 = colour;
    particle.b_88 = colour;
  }

  @Method(0x800ff788L)
  private static void initializer20(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.ticksRemaining_12 = -1;
    particle._14 = (short)seed_800fa754.nextInt(4097);
    particle._16 = effectInner._10;
    particle._18 = (short)seed_800fa754.nextInt(4097);
    particle._1a.x = seed_800fa754.nextInt(4097);
  }

  @Method(0x800ff890L)
  private static void initializer21(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
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
  private static void initializer22(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    initializer10(effect, particle, effectInner);
    final short s2 = effectInner._10;
    particle._16 = s2;
    particle._1a.y = s2;
    particle._18 = (short)(effectInner._18 * 0xe0);
  }

  @Method(0x800ffadcL)
  private static void initializer23(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
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
  private static void initializer24(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleAcceleration_60.y = 8.0f;
    particle._14 = (short)(particle.index << 9);
    particle._16 = effectInner._10;
    particle.framesUntilRender_04 = (short)(particle.index >>> 2 | 0x1);
    particle._18 = (short)(effectInner._18 * 64.0f);
    particle.particleVelocity_58.y = effectInner._14 * -0x40 >> 8;
  }

  @Method(0x800ffbd8L)
  private static void initializer25(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
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
  private static void initializer28(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = (short)(seed_800fa754.nextInt(4097));
    particle._16 = effectInner._10;
    particle._18 = (short)(effectInner._18 * 32.0f);
    particle.particleAcceleration_60.y = effectInner._18 * 2.0f;
  }

  @Method(0x800ffefcL)
  private static void initializer30(final ParticleEffectData98 a1, final ParticleEffectInstance94 a2, final ParticleEffectData98Inner24 a3) {
    // no-op
  }

  @Method(0x800fff04L)
  private static void initializer32(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = 0;
    particle._18 = 0;
    particle._1a.z = 0.0f;
    final float v0 = effectInner._18 * 64.0f;
    particle._16 = (short)v0;
    particle._1a.x = v0;
    particle._1a.y = effectInner._10;
  }

  @Method(0x800fff30L)
  private static void initializer33(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.x = seed_800fa754.nextInt(769) + 256;
  }

  @Method(0x800fffa0L)
  private static void initializer34(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._1a.x = effectInner._10;
    particle._14 = (short)(seed_800fa754.nextInt(4097));
    particle._16 = (short)((seed_800fa754.nextInt(123) + 64) * effectInner._18);
    particle._18 = (short)(0x800 / effect.countParticleInstance_50 * particle.index);
  }

  @Method(0x801000b8L)
  private static void initializer35(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.x = -particle.particlePosition_50.x / 32.0f;
    particle.particleVelocity_58.z = -particle.particlePosition_50.z / 32.0f;
  }

  @Method(0x801000f8L)
  private static void initializer39(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    initializer21(effect, particle, effectInner);
    particle.particleVelocity_58.y = -Math.abs(particle.particleVelocity_58.y);
    particle._14 = (short)(effectInner._18 * 0x300);
  }

  @Method(0x80100150L)
  private static void initializer41(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._1a.x = (short)(seed_800fa754.nextInt(4097));
    particle._14 = (short)particle.particlePosition_50.x;
    particle._16 = (short)particle.particlePosition_50.y;
    particle._18 = (short)particle.particlePosition_50.z;
    particle._1a.z = effectInner._10 / 4.0f;
    particle.particleVelocity_58.y = effectInner._18 * -64.0f;
    particle._1a.y = (seed_800fa754.nextInt(513) - 256) * effectInner._18;
  }

  @Method(0x8010025cL)
  private static void initializer42(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.y = 64.0f;
    final int angle = seed_800fa754.nextInt(4097);
    if(effectInner.behaviourType_20 == 0x2a) {
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
  private static void initializer46(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int s4 = effectInner._10; //TODO read with lw here but as a short everywhere else? Is this a bug?
    initializer21(effect, particle, effectInner);

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
  private static void initializer44(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 particleEffect) {
    initializer21(effect, particle, particleEffect);
    particle._14 = 0;
    particle.particleVelocity_58.y = -Math.abs(particle.particleVelocity_58.y);
    particle.particleAcceleration_60.set(particle.particleVelocity_58).negate().div(particle.ticksRemaining_12);
  }

  @Method(0x801005b8L)
  private static void initializer47(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int s2 = effectInner._10;
    initializer21(effect, particle, effectInner);
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
  private static void initializer48(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.set(particle.particlePosition_50).negate().div(particle.ticksRemaining_12);
  }

  @Method(0x80100800L)
  private static void initializer49(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._16 = (short)(effectInner._18 * 64.0f);
    particle._18 = effectInner._10;
    particle._14 = (short)(seed_800fa754.nextInt(4097));
  }

  @Method(0x80100878L)
  private static void initializer50(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.y = -64.0f;
    particle._18 = effectInner._10;
    particle._16 = (short)(effectInner._18 * 0x2000);
    particle._14 = (short)(seed_800fa754.nextInt(4097));
  }

  @Method(0x801008f8L)
  private static void initializer52(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.angle_0e = 0.0f;
    particle.angleVelocity_10 = 0.0f;
    particle.spriteRotation_70.zero();
    particle.spriteRotationStep_78.zero();
    particle._18 = (short)(effectInner._18 * 256.0f);
    particle._14 = 0x800;
    particle._16 = (short)(effectInner._10 << 4);
    particle._1a.x = effectInner._18 * 64.0f;
    particle.framesUntilRender_04 = (short)(particle.index * (effectInner._14 / effect.countParticleInstance_50));
  }

  @Method(0x80100978L)
  private static void initializer53(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = (short)(seed_800fa754.nextInt(4097));
    particle._16 = (short)(seed_800fa754.nextInt((effectInner._10 & 0xffff) + 1));
    particle._18 = (short)(seed_800fa754.nextInt(4097));
    particle._1a.x = (seed_800fa754.nextInt(41) + 150) * effectInner._18;
    particle.particleVelocity_58.y = -64.0f;
  }

  @Method(0x80100af4L)
  private static void initializer54(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    initializer21(effect, particle, effectInner);
    particle.particleVelocity_58.x = particle.particleVelocity_58.x * effectInner._18;
    particle.particleVelocity_58.y = particle.particleVelocity_58.y * effectInner._18;
    particle.particleVelocity_58.z = particle.particleVelocity_58.z * effectInner._18;
    particle.particleAcceleration_60.set(particle.particleVelocity_58).negate().div(particle.ticksRemaining_12);
  }

  @Method(0x80100bb4L)
  private static void initializer55(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.y = seed_800fa754.nextInt(33) + 16;
  }

  @Method(0x80100c18L)
  private static void initializer58(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = (short)(-particle.particlePosition_50.x / 2.0f * effectInner._18);
    particle._16 = (short)(-particle.particlePosition_50.y / 2.0f * effectInner._18);
    particle._18 = (short)(-particle.particlePosition_50.z / 2.0f * effectInner._18);
    particle._1a.zero();
  }

  @Method(0x80100cacL)
  private static void initializer59(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    initializer21(effect, particle, effectInner);
    particle.particleVelocity_58.y = -Math.abs(particle.particleVelocity_58.y);
    particle.particleVelocity_58.x = 0.0f;
    particle._14 = 0;
  }

  @Method(0x80100cecL)
  private static void initializer60(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = 0;
    particle.particleAcceleration_60.y = effectInner._18 * 2.0f;
  }

  @Method(0x80100d00L)
  private static void initializer64(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    calculateBentPartPosition(SCRIPTS.getObject(effect.parentScriptIndex_04, BattleEntity27c.class), particle.particlePosition_50, particle.index);
  }

  @Method(0x80100d58L)
  private static void tickNoop(final EffectManagerData6c<EffectManagerParams.ParticleType> a1, final ParticleEffectData98 a2, final ParticleEffectInstance94 a3) {
    // no-op
  }

  @Method(0x80100d60L)
  private static void tick2(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.framesUntilRender_04 == 0 && effect.parentScriptIndex_04 != -1) {
      final Vector3f translation = manager.getPosition();

      final Vector3f parentTranslation = new Vector3f();
      scriptGetScriptedObjectPos(effect.parentScriptIndex_04, parentTranslation);

      final Vector3f diffTranslation = new Vector3f().set(translation).sub(parentTranslation);
      particle.particlePosition_50.sub(diffTranslation);
    }
    //LAB_80100e14
  }

  @Method(0x80100e28L)
  private static void tickResetColourStep(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.framesUntilRender_04 == 0) {
      particle.stepR_8a = 0;
      particle.stepG_8c = 0;
      particle.stepB_8e = 0;
    }
    //LAB_80100e44
  }

  @Method(0x80100e4cL)
  private static void tickSlowColourStep(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.framesUntilRender_04 == 0) {
      particle.stepR_8a = -1.0f / particle.ticksRemaining_12;
      particle.stepG_8c = -1.0f / particle.ticksRemaining_12;
      particle.stepB_8e = -1.0f / particle.ticksRemaining_12;
    }
    //LAB_80100e98
  }

  @Method(0x80100ea0L)
  private static void tick30(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    final int s1 = effect.effectInner_08._10 & 0xffff;

    final Vector3f parentTranslation = new Vector3f();
    scriptGetScriptedObjectPos(effect.parentScriptIndex_04, parentTranslation);

    final Vector3f diffTranslation = new Vector3f().set(parentTranslation).sub(manager.getPosition());

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

  public static final TriConsumer<EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleEffectData98, ParticleEffectInstance94>[] particleInstancePrerenderCallbacks_80119bac = new TriConsumer[65];
  static {
    particleInstancePrerenderCallbacks_80119bac[0] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[1] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[2] = ParticleManager::preRender2;
    particleInstancePrerenderCallbacks_80119bac[3] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[4] = ParticleManager::preRender4;
    particleInstancePrerenderCallbacks_80119bac[5] = ParticleManager::preRender5;
    particleInstancePrerenderCallbacks_80119bac[6] = ParticleManager::preRender6;
    particleInstancePrerenderCallbacks_80119bac[7] = ParticleManager::preRender6;
    particleInstancePrerenderCallbacks_80119bac[8] = ParticleManager::preRender8;
    particleInstancePrerenderCallbacks_80119bac[9] = ParticleManager::preRender8;
    particleInstancePrerenderCallbacks_80119bac[10] = ParticleManager::preRender10;
    particleInstancePrerenderCallbacks_80119bac[11] = ParticleManager::preRender10;
    particleInstancePrerenderCallbacks_80119bac[12] = ParticleManager::preRender5;
    particleInstancePrerenderCallbacks_80119bac[13] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[14] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[15] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[16] = ParticleManager::preRender16;
    particleInstancePrerenderCallbacks_80119bac[17] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[18] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[19] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[20] = ParticleManager::preRender20;
    particleInstancePrerenderCallbacks_80119bac[21] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[22] = ParticleManager::preRender22;
    particleInstancePrerenderCallbacks_80119bac[23] = ParticleManager::preRender23;
    particleInstancePrerenderCallbacks_80119bac[24] = ParticleManager::preRender24;
    particleInstancePrerenderCallbacks_80119bac[25] = ParticleManager::preRender25;
    particleInstancePrerenderCallbacks_80119bac[26] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[27] = ParticleManager::preRender27;
    particleInstancePrerenderCallbacks_80119bac[28] = ParticleManager::preRender24;
    particleInstancePrerenderCallbacks_80119bac[29] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[30] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[31] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[32] = ParticleManager::preRender32;
    particleInstancePrerenderCallbacks_80119bac[33] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[34] = ParticleManager::preRender34;
    particleInstancePrerenderCallbacks_80119bac[35] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[36] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[37] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[38] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[39] = ParticleManager::preRender39;
    particleInstancePrerenderCallbacks_80119bac[40] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[41] = ParticleManager::preRender41;
    particleInstancePrerenderCallbacks_80119bac[42] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[43] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[44] = ParticleManager::preRender39;
    particleInstancePrerenderCallbacks_80119bac[45] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[46] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[47] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[48] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[49] = ParticleManager::preRender49;
    particleInstancePrerenderCallbacks_80119bac[50] = ParticleManager::preRender50;
    particleInstancePrerenderCallbacks_80119bac[51] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[52] = ParticleManager::preRender52;
    particleInstancePrerenderCallbacks_80119bac[53] = ParticleManager::preRender53;
    particleInstancePrerenderCallbacks_80119bac[54] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[55] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[56] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[57] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[58] = ParticleManager::preRender58;
    particleInstancePrerenderCallbacks_80119bac[59] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[60] = ParticleManager::preRender60;
    particleInstancePrerenderCallbacks_80119bac[61] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[62] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[63] = ParticleManager::preRenderNoop; // no-op
    particleInstancePrerenderCallbacks_80119bac[64] = ParticleManager::preRenderNoop; // no-op
  }

  public static final TriConsumer<EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleEffectData98, ParticleEffectInstance94>[] particleInstanceTickCallbacks_80119cb0 = new TriConsumer[65];
  static {
    particleInstanceTickCallbacks_80119cb0[0] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[1] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[2] = ParticleManager::tick2;
    particleInstanceTickCallbacks_80119cb0[3] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[4] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[5] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[6] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[7] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[8] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[9] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[10] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[11] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[12] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[13] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[14] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[15] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[16] = ParticleManager::tickResetColourStep;
    particleInstanceTickCallbacks_80119cb0[17] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[18] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[19] = ParticleManager::tickResetColourStep;
    particleInstanceTickCallbacks_80119cb0[20] = ParticleManager::tickResetColourStep;
    particleInstanceTickCallbacks_80119cb0[21] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[22] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[23] = ParticleManager::tickSlowColourStep;
    particleInstanceTickCallbacks_80119cb0[24] = ParticleManager::tickResetColourStep;
    particleInstanceTickCallbacks_80119cb0[25] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[26] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[27] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[28] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[29] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[30] = ParticleManager::tick30;
    particleInstanceTickCallbacks_80119cb0[31] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[32] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[33] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[34] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[35] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[36] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[37] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[38] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[39] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[40] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[41] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[42] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[43] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[44] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[45] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[46] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[47] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[48] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[49] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[50] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[51] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[52] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[53] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[54] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[55] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[56] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[57] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[58] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[59] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[60] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[61] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[62] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[63] = ParticleManager::tickNoop; // no-op
    particleInstanceTickCallbacks_80119cb0[64] = ParticleManager::tickNoop; // no-op
  }

  public static final TriConsumer<ParticleEffectData98, ParticleEffectInstance94, ParticleEffectData98Inner24>[] initializerCallbacks_80119db4 = new TriConsumer[65];
  static {
    initializerCallbacks_80119db4[0] = ParticleManager::initializer0;
    initializerCallbacks_80119db4[1] = ParticleManager::initializer1;
    initializerCallbacks_80119db4[2] = ParticleManager::initializer2;
    initializerCallbacks_80119db4[3] = ParticleManager::initializer3;
    initializerCallbacks_80119db4[4] = ParticleManager::initializer4;
    initializerCallbacks_80119db4[5] = ParticleManager::initializer5;
    initializerCallbacks_80119db4[6] = ParticleManager::initializer6;
    initializerCallbacks_80119db4[7] = ParticleManager::initializer6;
    initializerCallbacks_80119db4[8] = ParticleManager::initializer8;
    initializerCallbacks_80119db4[9] = ParticleManager::initializer9;
    initializerCallbacks_80119db4[10] = ParticleManager::initializer10;
    initializerCallbacks_80119db4[11] = ParticleManager::initializer11;
    initializerCallbacks_80119db4[12] = ParticleManager::initializer5;
    initializerCallbacks_80119db4[13] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[14] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[15] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[16] = ParticleManager::initializer16;
    initializerCallbacks_80119db4[17] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[18] = ParticleManager::initializer0;
    initializerCallbacks_80119db4[19] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[20] = ParticleManager::initializer20;
    initializerCallbacks_80119db4[21] = ParticleManager::initializer21;
    initializerCallbacks_80119db4[22] = ParticleManager::initializer22;
    initializerCallbacks_80119db4[23] = ParticleManager::initializer23;
    initializerCallbacks_80119db4[24] = ParticleManager::initializer24;
    initializerCallbacks_80119db4[25] = ParticleManager::initializer25;
    initializerCallbacks_80119db4[26] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[27] = ParticleManager::initializer24;
    initializerCallbacks_80119db4[28] = ParticleManager::initializer28;
    initializerCallbacks_80119db4[29] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[30] = ParticleManager::initializer30; // no-op
    initializerCallbacks_80119db4[31] = ParticleManager::initializer0;
    initializerCallbacks_80119db4[32] = ParticleManager::initializer32;
    initializerCallbacks_80119db4[33] = ParticleManager::initializer33;
    initializerCallbacks_80119db4[34] = ParticleManager::initializer34;
    initializerCallbacks_80119db4[35] = ParticleManager::initializer35;
    initializerCallbacks_80119db4[36] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[37] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[38] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[39] = ParticleManager::initializer39;
    initializerCallbacks_80119db4[40] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[41] = ParticleManager::initializer41;
    initializerCallbacks_80119db4[42] = ParticleManager::initializer42;
    initializerCallbacks_80119db4[43] = ParticleManager::initializer42;
    initializerCallbacks_80119db4[44] = ParticleManager::initializer44;
    initializerCallbacks_80119db4[45] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[46] = ParticleManager::initializer46;
    initializerCallbacks_80119db4[47] = ParticleManager::initializer47;
    initializerCallbacks_80119db4[48] = ParticleManager::initializer48;
    initializerCallbacks_80119db4[49] = ParticleManager::initializer49;
    initializerCallbacks_80119db4[50] = ParticleManager::initializer50;
    initializerCallbacks_80119db4[51] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[52] = ParticleManager::initializer52;
    initializerCallbacks_80119db4[53] = ParticleManager::initializer53;
    initializerCallbacks_80119db4[54] = ParticleManager::initializer54;
    initializerCallbacks_80119db4[55] = ParticleManager::initializer55;
    initializerCallbacks_80119db4[56] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[57] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[58] = ParticleManager::initializer58;
    initializerCallbacks_80119db4[59] = ParticleManager::initializer59;
    initializerCallbacks_80119db4[60] = ParticleManager::initializer60;
    initializerCallbacks_80119db4[61] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[62] = ParticleManager::initializer0;
    initializerCallbacks_80119db4[63] = ParticleManager::initializerNoop; // no-op
    initializerCallbacks_80119db4[64] = ParticleManager::initializer64;
  }
}
