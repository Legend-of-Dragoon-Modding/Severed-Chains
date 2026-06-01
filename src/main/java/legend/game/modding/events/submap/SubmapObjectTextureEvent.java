package legend.game.modding.events.submap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.core.opengl.Texture;
import legend.game.modding.events.engine.InGameEvent;
import legend.game.submap.SMap;
import legend.game.submap.Submap;
import legend.game.types.GameState52c;

import java.util.function.Consumer;

public class SubmapObjectTextureEvent extends InGameEvent<SMap> implements LoadedSubmapEvent {
  private final Submap submap;
  public final int disk;
  public final int submapCut;
  public final Int2ObjectMap<Consumer<Texture.Builder>> textures = new Int2ObjectOpenHashMap<>();

  public SubmapObjectTextureEvent(final SMap engineState, final GameState52c gameState, final Submap submap, final int disk, final int submapCut) {
    super(engineState, gameState);
    this.submap = submap;
    this.disk = disk;
    this.submapCut = submapCut;
  }

  @Override
  public Submap getSubmap() {
    return this.submap;
  }
}
