package legend.game.modding.events.submap;

import legend.core.opengl.Texture;

public class SubmapEnvironmentTextureEvent extends SubmapEvent {
  public final int submapCut;
  public Texture background;
  public Texture[] foregrounds;

  public SubmapEnvironmentTextureEvent(final int submapCut) {
    this.submapCut = submapCut;
  }
}
