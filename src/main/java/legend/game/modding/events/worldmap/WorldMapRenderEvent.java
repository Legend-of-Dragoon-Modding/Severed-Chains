package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapRenderContext;

/**
 * Adds rendering after the region presentation on each rendered PLAY frame, before overlay UI.
 * Runs on the render thread. Listeners may queue their own models but must not mutate engine state.
 * Pair owned resources with WorldMapRegionLifecycleEvent; this event never replaces region drawing.
 */
public class WorldMapRenderEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapRenderContext context;

  public WorldMapRenderEvent(final WMap engineState, final GameState52c gameState, final WorldMapRenderContext context) {
    super(engineState, gameState);
    this.context = context;
  }
}
