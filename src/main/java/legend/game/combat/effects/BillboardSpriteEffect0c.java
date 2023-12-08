package legend.game.combat.effects;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.combat.Bttl;
import legend.game.scripting.ScriptState;

import static legend.game.combat.Bttl.calculateEffectTransforms;

public class BillboardSpriteEffect0c extends SpriteWithTrailEffect30.Sub implements Effect {
  public final SpriteMetrics08 metrics_04 = new SpriteMetrics08();

  @Method(0x800e9590L)
  public void renderBillboardSpriteEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final MV transformMatrix = new MV();
    calculateEffectTransforms(transformMatrix, manager);
    Bttl.renderBillboardSpriteEffect(this.metrics_04, manager.params_10, transformMatrix);
  }
}
