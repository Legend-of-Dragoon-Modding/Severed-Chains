package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapCoolonDestination;
import legend.game.wmap.world.WorldMapView;
import org.legendofdragoon.modloader.registries.RegistryId;

/** Resolves menu availability each interactive Coolon frame and again before accepting flight. */
public class WorldMapCoolonDestinationEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final RegistryId destinationId;
  public final WorldMapCoolonDestination destination;
  public final WorldMapView view;
  /** Retail destinations are all available by default; listeners may apply their own access policy. */
  public boolean available = true;

  public WorldMapCoolonDestinationEvent(final WMap engineState, final GameState52c gameState, final RegistryId destinationId, final WorldMapCoolonDestination destination, final WorldMapView view) {
    super(engineState, gameState);
    this.destinationId = destinationId;
    this.destination = destination;
    this.view = view;
  }
}
