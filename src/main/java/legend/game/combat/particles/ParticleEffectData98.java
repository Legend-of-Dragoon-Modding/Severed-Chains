package legend.game.combat.particles;

import legend.core.gte.MV;
import legend.core.gte.TmdObjTable1c;
import legend.core.memory.types.QuadConsumer;
import legend.core.memory.types.TriConsumer;
import legend.core.opengl.Obj;
import legend.game.combat.effects.Effect;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;

import java.util.Arrays;

public class ParticleEffectData98 implements Effect<EffectManagerParams.ParticleType> {
  private final ParticleManager manager;
  private final int type;

  public ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> myState_00;
  /** Can be -1 */
  public int parentScriptIndex_04;
  public final ParticleEffectData98Inner24 effectInner_08 = new ParticleEffectData98Inner24();

  public TmdObjTable1c tmd_30;
  public short halfW_34;
  public short halfH_36;

  /** ushort */
  public int countParticleInstance_50;
  /** ushort */
  public int countFramesRendered_52;
  /** ushort */
  public int countParticleSub_54;
  /** ushort */
  public int tpage_56;
  /** ushort */
  public int u_58;
  /** ushort */
  public int v_5a;
  /** ushort */
  public int clut_5c;
  /** ubyte */
  public int w_5e;
  /** ubyte */
  public int h_5f;
  /**
   * Some kind of effect type flag or something; possibly multiuse? Gets used as a callback index at one point,
   * but can have values greater than length of callback array.
   */
  public byte subParticleType_60;
  public int callback90Type_61;

  /** Size in bytes of following array of structs */
  // public int size_64;
  public ParticleEffectInstance94[] particleArray_68;
  public boolean scaleOrUseEffectAcceleration_6c;

  public final Vector3f effectAcceleration_70 = new Vector3f();
  public int scaleParticleAcceleration_80;
  public TriConsumer<EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleEffectData98, ParticleEffectInstance94> particleInstancePrerenderCallback_84;
  public QuadConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>>, EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleEffectData98, ParticleEffectInstance94> particleInstanceTickCallback_88;
  public TriConsumer<ParticleEffectData98, ParticleEffectInstance94, ParticleEffectData98Inner24> initializerCallback_8c;
  public TriConsumer<EffectManagerData6c<EffectManagerParams.ParticleType>, ParticleEffectData98, ParticleEffectInstance94> particleInstanceReconstructorCallback_90;
  public ParticleEffectData98 next_94;

  public Obj obj;
  public final MV transforms = new MV();

  public ParticleEffectData98(final ParticleManager manager, final int type, final int count) {
    this.manager = manager;
    this.type = type;
    this.countParticleInstance_50 = count;
    this.particleArray_68 = new ParticleEffectInstance94[count];
    Arrays.setAll(this.particleArray_68, ParticleEffectInstance94::new);
  }

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state) {

  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state) {
    this.manager.particleEffectRenderers_80119b7c[this.type].accept(state);
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state) {
    if(this.obj != null) {
      this.obj.delete();
      this.obj = null;
    }

    this.manager.deleteParticle(this);
  }
}
