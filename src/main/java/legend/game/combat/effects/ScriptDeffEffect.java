package legend.game.combat.effects;

import legend.game.combat.Battle;
import legend.game.scripting.ScriptState;

import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd04;

public class ScriptDeffEffect implements Effect<EffectManagerParams.VoidType> {
  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    ((Battle)engineState_8004dd04).scriptDeffTicker(state, state.innerStruct_00);
  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    ((Battle)engineState_8004dd04).scriptDeffDeallocator(state, state.innerStruct_00);
  }
}
