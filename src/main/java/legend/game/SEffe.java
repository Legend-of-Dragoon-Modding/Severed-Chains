package legend.game;

import legend.core.memory.Method;
import legend.game.types.RunningScript;

import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

public final class SEffe {
  private SEffe() { }

  @Method(0x80115690L)
  public static long FUN_80115690(final RunningScript a0) {
    final long s0 = a0.params_20.get(0).deref().get();
    loadScriptFile(s0, a0.scriptState_04.deref().scriptPtr_14.deref(), 0, "S_EFFE Script", 0); //TODO unknown size
    scriptStatePtrArr_800bc1c0.get((int)s0).deref().commandPtr_18.set(a0.params_20.get(1).deref());
    return 0;
  }
}
