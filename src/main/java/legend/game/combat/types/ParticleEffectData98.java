package legend.game.combat.types;

import legend.core.gte.TmdObjTable1c;
import legend.core.gte.VECTOR;
import legend.core.memory.types.QuadConsumer;
import legend.core.memory.types.TriConsumer;
import legend.game.scripting.ScriptState;

public class ParticleEffectData98 implements BttlScriptData6cSubBase1 {
  public ScriptState<EffectManagerData6c> myState_00;
  /** Parent? Can be -1 */
  public int scriptIndex_04;
  public final EffectData98Inner24 _08 = new EffectData98Inner24();

  public TmdObjTable1c tmd_30;
  public short _34;
  public short _36;

  /** ushort */
  public int count_50;
  /** ushort */
  public int _52;
  /** ushort */
  public int count_54;
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
  public byte _60;
  public byte _61;

  /** Size in bytes of following array of structs */
//  public int size_64;
  public ParticleEffectInstance94[] _68;
  public byte _6c;

  public final VECTOR vec_70 = new VECTOR();
  public int _80;
  public TriConsumer<EffectManagerData6c, ParticleEffectData98, ParticleEffectInstance94> _84;
  public QuadConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c, ParticleEffectData98, ParticleEffectInstance94> _88;
  public QuadConsumer<EffectManagerData6c, ParticleEffectData98, ParticleEffectInstance94, EffectData98Inner24> _8c;
  public QuadConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c, ParticleEffectData98, ParticleEffectInstance94> _90;
  public ParticleEffectData98 _94;
}
