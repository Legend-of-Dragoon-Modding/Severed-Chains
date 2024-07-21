package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.game.scripting.ScriptState;

import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;

public class ShirleyTransformWipeEffect implements Effect<EffectManagerParams.ShirleyType> {
  @Override
  @Method(0x80118a24L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.ShirleyType>> state) {
    final EffectManagerData6c<EffectManagerParams.ShirleyType> manager = state.innerStruct_00;

    if(manager.params_10.depth_2c == 0) {
      return;
    }

    final int y = (int)(manager.params_10.trans_04.y - manager.params_10.height_28 / 2.0f);

    for(final var bent : battleState_8006e398.monsterBents_e50) {
      if(bent != null) {
        // Y can go slightly negative so double the height to ensure their feet don't disappear
        bent.innerStruct_00.scissor(0, displayHeight_1f8003e4 + y, displayWidth_1f8003e0, displayHeight_1f8003e4 * 2);
      }
    }
  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.ShirleyType>> state) {

  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.ShirleyType>> state) {
    for(final var bent : battleState_8006e398.monsterBents_e50) {
      if(bent != null) {
        bent.innerStruct_00.disableScissor();
      }
    }
  }
}
