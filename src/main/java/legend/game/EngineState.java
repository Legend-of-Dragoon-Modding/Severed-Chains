package legend.game;

import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.types.CContainer;
import legend.game.types.Model124;
import legend.game.unpacker.FileData;

import java.util.function.Function;

import static legend.game.Scus94491BpeSegment_8002.sssqResetStuff;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;

public abstract class EngineState<T extends EngineState<T>> {
  public final EngineStateType<?> type;
  private final Function<RunningScript, FlowControl>[] functions = new Function[1024];

  protected EngineState(final EngineStateType<T> type) {
    this.type = type;
  }

  public boolean is(final EngineStateType<?> type) {
    return this.type == type;
  }

  public boolean canSave() {
    return false;
  }

  public abstract String getLocationForSave();
  public abstract FileData writeSaveData();

  public void init() {
    sssqResetStuff();
    submapId_800bd808 = -1;
  }

  public void destroy() {

  }

  /** Runs before scripts are ticked */
  public abstract void tick();

  /** Runs after scripts are ticked */
  public void postScriptTick(final boolean scriptsTicked) {

  }

  /** Runs after everything else is rendered */
  public void overlayTick() {

  }

  /** The amount we've multiplied this engine state's frame rate by (e.g. world map was 20FPS, we multiplied it by 3 to bring it to 60FPS) */
  public int tickMultiplier() {
    return 2;
  }

  public void restoreMusicAfterMenu() {

  }

  public Function<RunningScript, FlowControl>[] getScriptFunctions() {
    return this.functions;
  }

  public void modelLoaded(final Model124 model, final CContainer cContainer) {

  }

  public void menuClosed() {

  }

  public boolean advancesTime() {
    return true;
  }

  public boolean renderTextOnTopOfAllBoxes() {
    return true;
  }

  public boolean allowsWidescreen() {
    return true;
  }

  public boolean allowsHighQualityProjection() {
    return true;
  }
}
