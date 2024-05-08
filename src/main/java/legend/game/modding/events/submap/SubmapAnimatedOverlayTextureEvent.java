package legend.game.modding.events.submap;

import legend.core.opengl.Texture;
import legend.game.models.Model124;

public class SubmapAnimatedOverlayTextureEvent extends SubmapEvent {
  public final int disk;
  public final int submapCut;
  public final Model124 model;
  public Texture texture;

  public SubmapAnimatedOverlayTextureEvent(final int disk, final int submapCut, final Model124 model) {
    this.disk = disk;
    this.submapCut = submapCut;
    this.model = model;
  }
}
