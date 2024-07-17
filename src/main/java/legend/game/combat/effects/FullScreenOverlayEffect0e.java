package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;

public class FullScreenOverlayEffect0e implements Effect<EffectManagerParams.VoidType> {
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

  @Override
  @Method(0x800cea9cL)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    if(this.ticksRemaining_0c != 0) {
      this.r_00 += this.stepR_06;
      this.g_02 += this.stepG_08;
      this.b_04 += this.stepB_0a;
      this.ticksRemaining_0c--;
    }

    //LAB_800ceb20
  }

  @Override
  @Method(0x800ceb28L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    // Make sure effect fills the whole screen
    final float fullWidth = Math.max(displayWidth_1f8003e0, RENDERER.window().getWidth() / (float)RENDERER.window().getHeight() * displayHeight_1f8003e4);
    final float extraWidth = fullWidth - displayWidth_1f8003e0;
    fullScreenEffect_800bb140.transforms.scaling(fullWidth, displayHeight_1f8003e4, 1.0f);
    fullScreenEffect_800bb140.transforms.transfer.set(-extraWidth / 2, 0.0f, 120.0f);

    //LAB_800139c4
    RENDERER.queueOrthoModel(RENDERER.opaqueQuad, fullScreenEffect_800bb140.transforms)
      .translucency(Translucency.of(manager.params_10.flags_00 >>> 28 & 0b11))
      .colour((this.r_00 >> 8) / 255.0f, (this.g_02 >> 8) / 255.0f, (this.b_04 >> 8) / 255.0f);
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }
}
