package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapView;

import java.util.Objects;

/**
 * Announces a newly resolved world map view after progression resolution.
 * This immutable snapshot is observational; listeners must not mutate progression or trigger resolution here.
 */
public class WorldMapResolvedEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapView view;

  public WorldMapResolvedEvent(final WMap engineState, final GameState52c gameState, final WorldMapView view) {
    super(Objects.requireNonNull(engineState, "engineState"), Objects.requireNonNull(gameState, "gameState"));
    this.view = Objects.requireNonNull(view, "view");
  }
}
