package legend.game.combat.effects;

import legend.core.gpu.GpuCommandQuad;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;

import static legend.core.GameEngine.GPU;

public class FullScreenOverlayEffect0e implements Effect {
  public int r_00;
  public int g_02;
  public int b_04;
  public short stepR_06;
  public short stepG_08;
  public short stepB_0a;
  public int ticksRemaining_0c;

  @Method(0x800cea9cL)
  public void tickFullScreenOverlay(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final FullScreenOverlayEffect0e effect = (FullScreenOverlayEffect0e)manager.effect_44;

    if(effect.ticksRemaining_0c != 0) {
      effect.r_00 += effect.stepR_06;
      effect.g_02 += effect.stepG_08;
      effect.b_04 += effect.stepB_0a;
      effect.ticksRemaining_0c--;
    }

    //LAB_800ceb20
  }

  @Method(0x800ceb28L)
  public void renderFullScreenOverlay(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final FullScreenOverlayEffect0e a0 = (FullScreenOverlayEffect0e)manager.effect_44;

    GPU.queueCommand(30, new GpuCommandQuad()
      .translucent(Translucency.of(manager.params_10.flags_00 >>> 28 & 0b11))
      .rgb(a0.r_00 >> 8, a0.g_02 >> 8, a0.b_04 >> 8)
      .pos(-160, -120, 320, 280)
    );
  }
}
