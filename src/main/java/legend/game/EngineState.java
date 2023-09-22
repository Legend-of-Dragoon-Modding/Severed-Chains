package legend.game;

import legend.core.gte.ModelPart10;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.types.CContainer;
import legend.game.types.Model124;

import static legend.game.Scus94491BpeSegment_8002.adjustPartUvs;

public abstract class EngineState {
  public abstract void tick();

  public FlowControl executeScriptFunction(final int index, final RunningScript<?> script) {
    return null;
  }

  public void adjustModelPartUvs(final Model124 model, final ModelPart10 part) {
    adjustPartUvs(part, model.colourMap_9d);
  }

  public void modelLoaded(final Model124 model, final CContainer cContainer) {

  }

  public void menuClosed() {

  }
}
