package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.SubmapEndpoint;
import legend.game.wmap.world.WorldMapTravel;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Resolves a travel destination without changing the existing travel choreography.
 * This event may occur before animation starts and is posted again if the destination is re-resolved.
 * Listeners must modify only {@link #destination}, without progression side effects.
 * This event does not provide cancellation.
 */
public class WorldMapTravelEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapTravel.Kind kind;
  @Nullable
  public final RegistryId portalId;
  public SubmapEndpoint destination;
  public final boolean worldMapArrival;

  public WorldMapTravelEvent(final WMap engineState, final GameState52c gameState, final WorldMapTravel.Kind kind, @Nullable final RegistryId portalId, final SubmapEndpoint destination, final boolean worldMapArrival) {
    super(Objects.requireNonNull(engineState, "engineState"), Objects.requireNonNull(gameState, "gameState"));
    this.kind = Objects.requireNonNull(kind, "kind");
    this.portalId = portalId;
    this.destination = Objects.requireNonNull(destination, "destination");
    this.worldMapArrival = worldMapArrival;
  }
}
