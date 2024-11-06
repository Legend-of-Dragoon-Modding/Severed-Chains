package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.environment.BattleCamera;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.ScriptState;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

import static legend.game.combat.SEffe.allocateEffectManager;

public class ParticleManager {
  public static final short[] particleSubCounts_800fb794 = {0, 0, 4, 0, 8, 0, 16, 0, 0, 0, 1, 0, 2, 0, 4, 0, 3, 0, 5, 0};

  private ParticleEffectData98 firstParticle_8011a00c;
  private ParticleEffectData98 lastParticle_8011a010;

  public final BattleCamera camera;

  public ParticleManager(final BattleCamera camera) {
    this.camera = camera;
  }

  public ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> allocateParticle(final ScriptState<? extends BattleObject> allocator, final int behaviourType, final int particleCount, final int particleTypeId, final int _10, final int _14, final int _18, int innerStuff, @Nullable final BattleObject parentBobj) {
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
      case 0 -> new QuadParticle(this, parentBobj, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 1 -> new TmdParticle(this, parentBobj, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 2 -> new LineParticle(this, parentBobj, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 3 -> new PixelParticle(this, parentBobj, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 4 -> new JkNotActuallyALineParticle(this, parentBobj, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      case 5 -> new WhyIsThereANoParticle(this, parentBobj, new ParticleEffectData98Inner24((short)_10, (short)_14, (short)_18 / (float)0x100, innerStuff, behaviourType), renderType, particleCount);
      default -> throw new RuntimeException("Invalid particle type");
    };

    final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state = allocateEffectManager(
      "Particle effect %x".formatted(particleTypeId),
      allocator,
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

  /** TODO move into instance classes */
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

  /** TODO move into instance classes */
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

  public static final BiFunction<Integer, ParticleEffectData98, ParticleEffectInstance94>[] instanceConstructors = new BiFunction[65];
  static {
    instanceConstructors[0] = Instance0::new;
    instanceConstructors[1] = Instance1::new;
    instanceConstructors[2] = Instance2::new;
    instanceConstructors[3] = Instance3::new;
    instanceConstructors[4] = Instance4::new;
    instanceConstructors[5] = Instance5::new;
    instanceConstructors[6] = Instance6::new;
    instanceConstructors[7] = Instance7::new;
    instanceConstructors[8] = Instance8::new;
    instanceConstructors[9] = Instance9::new;
    instanceConstructors[10] = Instance10::new;
    instanceConstructors[11] = Instance11::new;
    instanceConstructors[12] = Instance12::new;
    instanceConstructors[13] = Instance13::new;
    instanceConstructors[14] = Instance14::new;
    instanceConstructors[15] = Instance15::new;
    instanceConstructors[16] = Instance16::new;
    instanceConstructors[17] = Instance17::new;
    instanceConstructors[18] = Instance18::new;
    instanceConstructors[19] = Instance19::new;
    instanceConstructors[20] = Instance20::new;
    instanceConstructors[21] = Instance21::new;
    instanceConstructors[22] = Instance22::new;
    instanceConstructors[23] = Instance23::new;
    instanceConstructors[24] = Instance24::new;
    instanceConstructors[25] = Instance25::new;
    instanceConstructors[26] = Instance26::new;
    instanceConstructors[27] = Instance27::new;
    instanceConstructors[28] = Instance28::new;
    instanceConstructors[29] = Instance29::new;
    instanceConstructors[30] = Instance30::new;
    instanceConstructors[31] = Instance31::new;
    instanceConstructors[32] = Instance32::new;
    instanceConstructors[33] = Instance33::new;
    instanceConstructors[34] = Instance34::new;
    instanceConstructors[35] = Instance35::new;
    instanceConstructors[36] = Instance36::new;
    instanceConstructors[37] = Instance37::new;
    instanceConstructors[38] = Instance38::new;
    instanceConstructors[39] = Instance39::new;
    instanceConstructors[40] = Instance40::new;
    instanceConstructors[41] = Instance41::new;
    instanceConstructors[42] = Instance42::new;
    instanceConstructors[43] = Instance43::new;
    instanceConstructors[44] = Instance44::new;
    instanceConstructors[45] = Instance45::new;
    instanceConstructors[46] = Instance46::new;
    instanceConstructors[47] = Instance47::new;
    instanceConstructors[48] = Instance48::new;
    instanceConstructors[49] = Instance49::new;
    instanceConstructors[50] = Instance50::new;
    instanceConstructors[51] = Instance51::new;
    instanceConstructors[52] = Instance52::new;
    instanceConstructors[53] = Instance53::new;
    instanceConstructors[54] = Instance54::new;
    instanceConstructors[55] = Instance55::new;
    instanceConstructors[56] = Instance56::new;
    instanceConstructors[57] = Instance57::new;
    instanceConstructors[58] = Instance58::new;
    instanceConstructors[59] = Instance59::new;
    instanceConstructors[60] = Instance60::new;
    instanceConstructors[61] = Instance61::new;
    instanceConstructors[62] = Instance62::new;
    instanceConstructors[63] = Instance63::new;
    instanceConstructors[64] = Instance64::new;
  }
}
