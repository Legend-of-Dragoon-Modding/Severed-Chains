package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapTravelTarget;
import legend.game.wmap.world.WorldMapDefinition;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

import java.util.Objects;

/** Posted once before accepting direct world-map travel. Listeners may redirect or cancel it. */
public class WorldMapWarpEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public WorldMapTravelTarget target;
  public boolean respectAccess;
  public boolean cancelled;
  /** Destination graph; preset switching can target a different graph from the active engine. */
  public final WorldMapDefinition definition;
  @Nullable public final RegistryId presetId;

  public WorldMapWarpEvent(final WMap engineState, final GameState52c gameState, final WorldMapTravelTarget target, final boolean respectAccess) {
    this(engineState, gameState, target, respectAccess, engineState.getWorldMapDefinition(), null);
  }

  public WorldMapWarpEvent(final WMap engineState, final GameState52c gameState, final WorldMapTravelTarget target, final boolean respectAccess, final WorldMapDefinition definition, @Nullable final RegistryId presetId) {
    super(engineState, gameState);
    this.target = Objects.requireNonNull(target, "target");
    this.respectAccess = respectAccess;
    this.definition = Objects.requireNonNull(definition, "definition");
    this.presetId = presetId;
  }
}
