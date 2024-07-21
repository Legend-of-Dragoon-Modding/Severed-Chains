package legend.game.combat.effects;

import legend.game.combat.Battle;
import legend.game.scripting.ScriptState;

import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd04;

public class ScriptDeffManualLoadingEffect extends ScriptDeffEffect {
  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    ((Battle)engineState_8004dd04).FUN_800e74e0(state, state.innerStruct_00);
  }
}
