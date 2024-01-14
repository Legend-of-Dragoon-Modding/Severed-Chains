package legend.game.modding.events.submap;

import legend.core.opengl.Texture;

public class SubmapEnvironmentTextureEvent extends SubmapEvent {
  public final int disk;
  public final int submapCut;
  public final int foregroundCount;
  public Texture background;
  public Texture[] foregrounds;

  public SubmapEnvironmentTextureEvent(final int disk, final int submapCut, final int foregroundCount) {
    this.disk = disk;
    this.submapCut = submapCut;
    this.foregroundCount = foregroundCount;
  }
}
