package legend.game.modding.events.submap;

import legend.core.opengl.Texture;
import legend.game.modding.events.engine.InGameEvent;
import legend.game.submap.SMap;
import legend.game.submap.Submap;
import legend.game.types.GameState52c;

public class SubmapEnvironmentTextureEvent extends InGameEvent<SMap> implements LoadedSubmapEvent {
  private final Submap submap;
  public final int disk;
  public final int submapCut;
  public Texture background;
  public final Texture[] foregrounds;

  public SubmapEnvironmentTextureEvent(final SMap engineState, final GameState52c gameState, final Submap submap, final int disk, final int submapCut, final int foregroundCount) {
    super(engineState, gameState);
    this.submap = submap;
    this.disk = disk;
    this.submapCut = submapCut;
    this.foregrounds = new Texture[foregroundCount];
  }

  @Override
  public Submap getSubmap() {
    return this.submap;
  }
}
