package legend.game.combat.particles;

import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.game.combat.effects.Effect;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;

import javax.annotation.Nullable;

import static legend.game.combat.particles.ParticleManager.instanceConstructors;
import static legend.game.combat.particles.ParticleManager.particleInnerStuffDefaultsArray_801197ec;

public abstract class ParticleEffectData98 implements Effect<EffectManagerParams.ParticleType> {
  public final ParticleManager manager;

  /** Can be -1 */
  @Nullable
  public final BattleObject parentBobj_04;
  public final ParticleEffectData98Inner24 effectInner_08;

  /** ushort */
  public final int countParticleInstance_50;
  /** ushort */
  public int countFramesRendered_52;
  /** ushort */
  public int countParticleSub_54;

  /** ubyte */
  public int w_5e;
  /** ubyte */
  public int h_5f;
  /**
   * Some kind of effect type flag or something; possibly multiuse? Gets used as a callback index at one point,
   * but can have values greater than length of callback array.
   */
  public final int renderType_60;
  public int callback90Type_61;

  /** Size in bytes of following array of structs */
  // public int size_64;
  public ParticleEffectInstance94[] particleArray_68;
  public boolean scaleOrUseEffectAcceleration_6c;

  public final Vector3f effectAcceleration_70 = new Vector3f();
  public int scaleParticleAcceleration_80;
//  public BiConsumer<EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleEffectInstance94> particleInstancePrerenderCallback_84;
//  public BiConsumer<EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleEffectInstance94> particleInstanceTickCallback_88;
//  public Consumer<ParticleEffectInstance94> initializerCallback_8c;
  public ParticleEffectData98 next_94;

  public Obj obj;
  public final MV transforms = new MV();

  public ParticleEffectData98(final ParticleManager manager, @Nullable final BattleObject parentBobj, final ParticleEffectData98Inner24 inner, final int type, final int count) {
    this.manager = manager;
    this.parentBobj_04 = parentBobj;
    this.effectInner_08 = inner;
    this.countParticleInstance_50 = count;
    this.renderType_60 = type;
    this.particleArray_68 = new ParticleEffectInstance94[count];

    this.callback90Type_61 = particleInnerStuffDefaultsArray_801197ec[inner.behaviourType_20].callbackType_03;

    for(int i = 0; i < this.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 inst = this.makeInstance(i);
      this.particleArray_68[i] = inst;
      inst.init();
      inst.originalParticlePosition_3c.set(inst.particlePosition_50);
    }
  }

  protected ParticleEffectInstance94 makeInstance(final int index) {
    return instanceConstructors[this.effectInner_08.behaviourType_20].apply(index, this);
  }

  protected abstract void init(final int flags);

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state) {

  }

  @Override
  public abstract void render(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state);

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state) {
    if(this.obj != null) {
      this.obj.delete();
      this.obj = null;
    }

    this.manager.deleteParticle(this);
  }

  public void reinitInstance(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectInstance94 inst) {
    if(this.callback90Type_61 == 0 && (manager.params_10.flags_24 & 0x4) != 0 || this.callback90Type_61 != 0 && (manager.params_10.flags_24 & 0x4) == 0) {
      inst.init();

      if(this.countParticleSub_54 != 0) {
        this.reinitInstanceType(manager, inst);
      }
    }
  }

  protected abstract void reinitInstanceType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectInstance94 inst);
}
