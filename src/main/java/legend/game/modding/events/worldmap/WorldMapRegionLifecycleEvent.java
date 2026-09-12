package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapRenderContext;

/**
 * Render-thread lifecycle for additive region resources. INIT follows successful asset adoption;
 * DELETE releases listener resources once before normal region disposal, or during failure cleanup.
 * Listeners own only their resources and must not request travel or mutate progression here.
 */
public class WorldMapRegionLifecycleEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public enum Phase { INIT, DELETE }

  public final Phase phase;
  public final WorldMapRenderContext context;

  public WorldMapRegionLifecycleEvent(final WMap engineState, final GameState52c gameState, final Phase phase, final WorldMapRenderContext context) {
    super(engineState, gameState);
    this.phase = phase;
    this.context = context;
  }
}
