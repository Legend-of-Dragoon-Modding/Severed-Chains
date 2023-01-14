package legend.game.combat.types;

import legend.core.memory.types.TriConsumer;
import legend.game.types.ScriptState;

public class EffectManagerData6c extends BattleScriptDataBase {
  public int flags_04;
  public int size_08;
  public int scriptIndex_0c;
  public int coord2Index_0d;
  public int scriptIndex_0e;

  public final EffectManagerData6cInner _10 = new EffectManagerData6cInner();
  public BttlScriptData6cSubBase1 effect_44;
  public TriConsumer<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c> ticker_48;
  public TriConsumer<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c> destructor_4c;
  public int parentScriptIndex_50;
  public int childScriptIndex_52;
  /** If replacing a child, this is the old child's ID */
  public int oldChildScriptIndex_54;
  /** If replaced as a child, this is the new child's ID */
  public int newChildScriptIndex_56;
  public BttlScriptData6cSubBase2 _58;
  public String type_5c;

  public void set(final EffectManagerData6c other) {
    this.flags_04 = other.flags_04;
    this.size_08 = other.size_08;
    this.scriptIndex_0c = other.scriptIndex_0c;
    this.coord2Index_0d = other.coord2Index_0d;
    this.scriptIndex_0e = other.scriptIndex_0e;
    this._10.set(other._10);
    this.effect_44 = other.effect_44;
    this.ticker_48 = other.ticker_48;
    this.destructor_4c = other.destructor_4c;
    this.parentScriptIndex_50 = other.parentScriptIndex_50;
    this.childScriptIndex_52 = other.childScriptIndex_52;
    this.oldChildScriptIndex_54 = other.oldChildScriptIndex_54;
    this.newChildScriptIndex_56 = other.newChildScriptIndex_56;
    this._58 = other._58;
    this.type_5c = other.type_5c;
  }
}
