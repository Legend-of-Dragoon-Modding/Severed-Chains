package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapPoint;
import legend.game.wmap.world.WorldMapTraversal;
import org.legendofdragoon.modloader.registries.RegistryId;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Resolves available connections when movement reaches a world map junction.
 * Listeners may filter or reorder {@link #connections}; adding unknown connections is rejected by the engine.
 * {@link #available} is the immutable candidate snapshot for {@link #viewVersion}.
 */
public class WorldMapJunctionEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapPoint position;
  /** Authoritative junction identity; null only for the legacy event constructor. */
  @Nullable public final RegistryId node;
  public final long viewVersion;
  public final List<WorldMapTraversal.Connection> available;
  public final List<WorldMapTraversal.Connection> connections;

  public WorldMapJunctionEvent(final WMap engineState, final GameState52c gameState, final WorldMapPoint position, final long viewVersion, final List<WorldMapTraversal.Connection> available) {
    this(engineState, gameState, null, position, viewVersion, available);
  }

  public WorldMapJunctionEvent(final WMap engineState, final GameState52c gameState, @Nullable final RegistryId node, final WorldMapPoint position, final long viewVersion, final List<WorldMapTraversal.Connection> available) {
    super(Objects.requireNonNull(engineState, "engineState"), Objects.requireNonNull(gameState, "gameState"));
    this.node = node;
    this.position = Objects.requireNonNull(position, "position");
    this.viewVersion = viewVersion;
    this.available = List.copyOf(available);
    this.connections = new ArrayList<>(this.available);
  }

  public List<WorldMapTraversal.Connection> validatedConnections() {
    return validateConnections(this.available, this.connections);
  }

  /** Preserves candidate multiplicity: listeners may remove/reorder, never invent or duplicate. */
  public static List<WorldMapTraversal.Connection> validateConnections(final List<WorldMapTraversal.Connection> available, final List<WorldMapTraversal.Connection> selected) {
    final List<WorldMapTraversal.Connection> remaining = new ArrayList<>(Objects.requireNonNull(available, "available"));
    final List<WorldMapTraversal.Connection> result = List.copyOf(Objects.requireNonNull(selected, "selected"));
    for(final WorldMapTraversal.Connection connection : result) {
      if(!remaining.remove(connection)) throw new IllegalArgumentException("WorldMapJunctionEvent added an unavailable or duplicate connection: " + connection);
    }
    return result;
  }
}
