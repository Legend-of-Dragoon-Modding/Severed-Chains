package legend.game;

import legend.core.gte.ModelPart10;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.types.CContainer;
import legend.game.types.Model124;

import java.util.function.Function;

import static legend.game.Scus94491BpeSegment_8002.adjustPartUvs;

public abstract class EngineState {
  private final Function<RunningScript, FlowControl>[] functions = new Function[1024];

  /** Runs before scripts are ticked */
  public abstract void tick();

  /** Runs after scripts are ticked */
  public void postScriptTick() {

  }

  /** Runs after everything else is rendered */
  public void overlayTick() {

  }

  public Function<RunningScript, FlowControl>[] getScriptFunctions() {
    return this.functions;
  }

  public void adjustModelPartUvs(final Model124 model, final ModelPart10 part) {
    adjustPartUvs(part, model.colourMap_9d);
  }

  public void modelLoaded(final Model124 model, final CContainer cContainer) {

  }

  public void menuClosed() {

  }
}
