package legend.game.combat.types;

import legend.game.scripting.ScriptState;

import java.util.function.BiConsumer;

public class EffectManagerData6c extends BattleScriptDataBase {
  public int flags_04;
  public int size_08;
  public int scriptIndex_0c;
  public int coord2Index_0d;
  public ScriptState<EffectManagerData6c> myScriptState_0e;

  public final EffectManagerData6cInner _10 = new EffectManagerData6cInner();
  public BttlScriptData6cSubBase1 effect_44;
  public BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c> ticker_48;
  public BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c> destructor_4c;
  public ScriptState<EffectManagerData6c> parentScript_50;
  public ScriptState<EffectManagerData6c> childScript_52;
  /** If replacing a child, this is the old child's ID */
  public ScriptState<EffectManagerData6c> oldChildScript_54;
  /** If replaced as a child, this is the new child's ID */
  public ScriptState<EffectManagerData6c> newChildScript_56;
  public BttlScriptData6cSubBase2 _58;
  public String type_5c;

  public void set(final EffectManagerData6c other) {
    this.flags_04 = other.flags_04;
    this.size_08 = other.size_08;
    this.scriptIndex_0c = other.scriptIndex_0c;
    this.coord2Index_0d = other.coord2Index_0d;
    this.myScriptState_0e = other.myScriptState_0e;
    this._10.set(other._10);
    this.effect_44 = other.effect_44;
    this.ticker_48 = other.ticker_48;
    this.destructor_4c = other.destructor_4c;
    this.parentScript_50 = other.parentScript_50;
    this.childScript_52 = other.childScript_52;
    this.oldChildScript_54 = other.oldChildScript_54;
    this.newChildScript_56 = other.newChildScript_56;
    this._58 = other._58;
    this.type_5c = other.type_5c;
  }
}
