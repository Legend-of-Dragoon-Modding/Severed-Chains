package legend.game.combat.effects;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.combat.SEffe;
import legend.game.combat.deff.DeffPart;
import legend.game.scripting.ScriptState;

import static legend.game.combat.Battle.deffManager_800c693c;
import static legend.game.combat.SEffe.calculateEffectTransforms;

public class BillboardSpriteEffect0c extends SpriteWithTrailEffect30.Sub implements Effect<EffectManagerParams.VoidType> {
  public final SpriteMetrics08 metrics_04 = new SpriteMetrics08();

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Override
  @Method(0x800e9590L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    final MV transformMatrix = new MV();
    calculateEffectTransforms(transformMatrix, manager);
    SEffe.renderBillboardSpriteEffect(this.metrics_04, manager.params_10, transformMatrix);
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Method(0x800e95f0L)
  public void set(final int flag) {
    this.flags_00 = flag | 0x400_0000;

    if((flag & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = deffManager_800c693c.spriteMetrics_39c[flag & 0xff];
      this.metrics_04.u_00 = metrics.u_00;
      this.metrics_04.v_02 = metrics.v_02;
      this.metrics_04.w_04 = metrics.w_04;
      this.metrics_04.h_05 = metrics.h_05;
      this.metrics_04.clut_06 = metrics.clut_06;
    } else {
      //LAB_800e9658
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)deffManager_800c693c.getDeffPart(flag | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      this.metrics_04.u_00 = deffMetrics.u_00;
      this.metrics_04.v_02 = deffMetrics.v_02;
      this.metrics_04.w_04 = deffMetrics.w_04 * 4;
      this.metrics_04.h_05 = deffMetrics.h_06;
      this.metrics_04.clut_06 = deffMetrics.clutY_0a << 6 | (deffMetrics.clutX_08 & 0x3f0) >>> 4;
    }
    //LAB_800e96bc
  }
}
