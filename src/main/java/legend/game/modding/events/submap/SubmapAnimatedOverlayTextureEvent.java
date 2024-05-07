package legend.game.modding.events.submap;

import legend.core.opengl.Texture;

public class SubmapAnimatedOverlayTextureEvent extends SubmapEvent {
  public final int disk;
  public final int submapCut;
  public Texture texture;

  public SubmapAnimatedOverlayTextureEvent(final int disk, final int submapCut) {
    this.disk = disk;
    this.submapCut = submapCut;
  }
}
