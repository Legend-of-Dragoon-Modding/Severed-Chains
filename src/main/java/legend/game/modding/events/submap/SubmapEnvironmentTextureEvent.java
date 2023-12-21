package legend.game.modding.events.submap;

import legend.core.opengl.Texture;

public class SubmapEnvironmentTextureEvent extends SubmapEvent {
  public final int submapCut;
  public final int index;
  public Texture texture;

  public SubmapEnvironmentTextureEvent(final int submapCut, final int index) {
    this.submapCut = submapCut;
    this.index = index;
  }
}
