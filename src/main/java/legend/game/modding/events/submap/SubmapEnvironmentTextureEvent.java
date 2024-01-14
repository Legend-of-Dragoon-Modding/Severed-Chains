package legend.game.modding.events.submap;

import legend.core.opengl.Texture;

public class SubmapEnvironmentTextureEvent extends SubmapEvent {
  public final int disk;
  public final int submapCut;
  public Texture background;
  public Texture[] foregrounds;

  public SubmapEnvironmentTextureEvent(final int disk, final int submapCut) {
    this.disk = disk;
    this.submapCut = submapCut;
  }
}
