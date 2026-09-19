package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.SubmapEndpoint;
import legend.game.wmap.world.WorldMapDefinition;
import legend.game.wmap.world.WorldMapPortal;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;
import java.util.function.IntPredicate;

/**
 * Resolves an arrival before world map position initialization.
 * Listeners may replace the origin or portal with a non-null destination; the engine validates the result.
 */
public class WorldMapArrivalEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final SubmapEndpoint originalOrigin;
  public final RegistryId originalPortal;
  public SubmapEndpoint origin;
  public RegistryId portal;

  public WorldMapArrivalEvent(final WMap engineState, final GameState52c gameState, final SubmapEndpoint origin, final RegistryId portal) {
    super(Objects.requireNonNull(engineState, "engineState"), Objects.requireNonNull(gameState, "gameState"));
    this.originalOrigin = Objects.requireNonNull(origin, "origin");
    this.originalPortal = Objects.requireNonNull(portal, "portal");
    this.origin = origin;
    this.portal = portal;
  }

  public WorldMapPortal validate(final WorldMapDefinition definition, final IntPredicate arrivalAllowed) {
    return validateSelection(this.originalPortal, this.portal, this.origin, definition, arrivalAllowed);
  }

  /** An unchanged retail fallback is valid even when its story flag is disabled. */
  public static WorldMapPortal validateSelection(final RegistryId originalPortal, final RegistryId portal, final SubmapEndpoint origin, final WorldMapDefinition definition, final IntPredicate arrivalAllowed) {
    Objects.requireNonNull(originalPortal, "originalPortal");
    Objects.requireNonNull(origin, "WorldMapArrivalEvent origin");
    final WorldMapPortal selected = definition.portal(Objects.requireNonNull(portal, "WorldMapArrivalEvent portal"));
    if(!originalPortal.equals(portal) && !arrivalAllowed.test(selected.legacyIndex())) throw new IllegalArgumentException("WorldMapArrivalEvent selected an unavailable arrival: " + portal);
    return selected;
  }
}
