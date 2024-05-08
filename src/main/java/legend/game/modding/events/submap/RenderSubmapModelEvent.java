package legend.game.modding.events.submap;

import legend.game.models.Model124;

public class RenderSubmapModelEvent extends SubmapEvent {
  public final Model124 model;

  public RenderSubmapModelEvent(final Model124 model) {
    this.model = model;
  }
}
