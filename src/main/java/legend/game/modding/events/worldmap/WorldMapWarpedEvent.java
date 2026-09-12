package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapTravelTarget;

/** Direct travel has installed the route position and the destination map and player are ready. */
public class WorldMapWarpedEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapTravelTarget target;

  public WorldMapWarpedEvent(final WMap engineState, final GameState52c gameState, final WorldMapTravelTarget target) {
    super(engineState, gameState);
    this.target = target;
  }
}
