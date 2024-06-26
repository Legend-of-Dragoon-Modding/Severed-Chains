package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.game.scripting.ScriptState;

import static legend.game.combat.SEffe.applyScreenDarkening;

public class ScreenDarkeningEffect implements Effect<EffectManagerParams.VoidType> {
  @Override
  @Method(0x80115b2cL)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final int currentVal = state.storage_44[8];
    final int targetVal = state.storage_44[9];

    if(currentVal == targetVal) {
      state.deallocateWithChildren();
    } else {
      //LAB_80115b80
      applyScreenDarkening(currentVal);

      //LAB_80115bd4
      if(currentVal > targetVal) {
        state.storage_44[8]--;
      } else {
        //LAB_80115bb4
        state.storage_44[8]++;
      }
    }

    //LAB_80115bd8
  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Override
  @Method(0x80115bf0L)
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    applyScreenDarkening(state.storage_44[9]);
  }
}
