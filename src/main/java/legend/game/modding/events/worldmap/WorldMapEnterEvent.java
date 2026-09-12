package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.SubmapEndpoint;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/**
 * Fires after entrance access passes and before the accepted entrance starts its fade.
 * Listeners may set {@link #cancelled} to keep the entrance prompt open without accepting travel.
 */
public class WorldMapEnterEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final RegistryId portal;
  public final SubmapEndpoint destination;
  public boolean cancelled;

  public WorldMapEnterEvent(final WMap engineState, final GameState52c gameState, final RegistryId portal, final SubmapEndpoint destination) {
    super(Objects.requireNonNull(engineState, "engineState"), Objects.requireNonNull(gameState, "gameState"));
    this.portal = Objects.requireNonNull(portal, "portal");
    this.destination = Objects.requireNonNull(destination, "destination");
  }
}
