package legend.game;

import legend.core.gte.ModelPart10;
import legend.game.types.Model124;

import static legend.game.Scus94491BpeSegment_8002.adjustPartUvs;

public abstract class EngineState {
  public abstract void tick();

  public void adjustModelPartUvs(final Model124 model, final ModelPart10 part) {
    adjustPartUvs(part, model.colourMap_9d);
  }
}
