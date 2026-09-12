package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapTravelTarget;

import java.util.Objects;

/** Posted once before accepting direct world-map travel. Listeners may redirect or cancel it. */
public class WorldMapWarpEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public WorldMapTravelTarget target;
  public boolean respectAccess;
  public boolean cancelled;

  public WorldMapWarpEvent(final WMap engineState, final GameState52c gameState, final WorldMapTravelTarget target, final boolean respectAccess) {
    super(engineState, gameState);
    this.target = Objects.requireNonNull(target, "target");
    this.respectAccess = respectAccess;
  }
}
