package legend.game.credits;

import legend.core.memory.Method;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.fmv.Fmv;

/** Plays the final cutscene tying everything up */
public class FinalFmv extends EngineState {
  private int ticks;

  @Override
  @Method(0x800d9e08L)
  public void tick() {
    super.tick();

    if(this.ticks++ > 94) {
      Fmv.playCurrentFmv(17, EngineStateEnum.CREDITS_04);
    }
  }
}
