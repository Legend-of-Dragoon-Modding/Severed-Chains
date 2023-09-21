package legend.game.credits;

import legend.core.memory.Method;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.fmv.Fmv;

import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;

/** Plays the final cutscene tying everything up */
public class FinalFmv extends EngineState {
  @Override
  @Method(0x800d9e08L)
  public void tick() {
    pregameLoadingStage_800bb10c.incr();

    if(pregameLoadingStage_800bb10c.get() > 94) {
      Fmv.playCurrentFmv(17, EngineStateEnum.CREDITS_04);
    }
  }
}
