package legend.game.combat.effects;

import legend.core.gpu.GpuCommandQuad;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;

import static legend.core.GameEngine.GPU;

public class FullScreenOverlayEffect0e implements Effect {
  private int r_00;
  private int g_02;
  private int b_04;
  private final short stepR_06;
  private final short stepG_08;
  private final short stepB_0a;
  private int ticksRemaining_0c;

  public FullScreenOverlayEffect0e(final int r, final int g, final int b, final int fullR, final int fullG, final int fullB, final int ticks) {
    this.r_00 = r;
    this.g_02 = g;
    this.b_04 = b;
    this.stepR_06 = (short)((fullR - r) / ticks);
    this.stepG_08 = (short)((fullG - g) / ticks);
    this.stepB_0a = (short)((fullB - b) / ticks);
    this.ticksRemaining_0c = ticks;
  }

  @Method(0x800cea9cL)
  public void tickFullScreenOverlay(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    if(this.ticksRemaining_0c != 0) {
      this.r_00 += this.stepR_06;
      this.g_02 += this.stepG_08;
      this.b_04 += this.stepB_0a;
      this.ticksRemaining_0c--;
    }

    //LAB_800ceb20
  }

  @Method(0x800ceb28L)
  public void renderFullScreenOverlay(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    GPU.queueCommand(30, new GpuCommandQuad()
      .translucent(Translucency.of(manager.params_10.flags_00 >>> 28 & 0b11))
      .rgb(this.r_00 >> 8, this.g_02 >> 8, this.b_04 >> 8)
      .pos(-160, -120, 320, 280)
    );
  }
}
