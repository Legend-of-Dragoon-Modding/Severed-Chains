package legend.game.wmap.world;

import legend.game.modding.events.worldmap.WorldMapProgressionEvent;
import legend.game.types.Flags;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongSupplier;

import static legend.core.GameEngine.EVENTS;

/** Resolves active and proposed worlds using identical inputs without publishing candidate state. */
public final class WorldMapProgressionResolver {
  private final Map<RegistryId, LongSupplier> sources = new LinkedHashMap<>();
  private Map<RegistryId, Long> resolvedRevisions = Map.of();
  private long campaignRevision = -1;

  public void watch(final RegistryId source, final LongSupplier revision) {
    this.sources.put(Objects.requireNonNull(source, "source"), Objects.requireNonNull(revision, "revision"));
    this.resolvedRevisions = Map.of();
  }

  public boolean changed() { return !this.revisions().equals(this.resolvedRevisions); }
  public boolean changed(final GameState52c state) {
    return this.changed() || this.campaignRevision != state.campaignProgression.revision();
  }

  private Map<RegistryId, Long> revisions() {
    final Map<RegistryId, Long> values = new LinkedHashMap<>();
    this.sources.forEach((id, revision) -> values.put(id, revision.getAsLong()));
    return Map.copyOf(values);
  }

  public WorldMapProgression resolve(final WMap engine, final GameState52c state, final WorldMapRegistrySnapshot data, final WorldMapDefinition definition, @Nullable final RegistryId preset, final boolean candidate) {
    final Map<RegistryId, Long> revisions = this.revisions();
    final long campaignRevision = state.campaignProgression.revision();
    final Flags enabled = new Flags(state.wmapFlags_15c.count());
    enabled.set(state.wmapFlags_15c);
    if(candidate) {
      final Flags visited = new Flags(state.visitedLocations_17c.count());
      visited.set(state.visitedLocations_17c);
      final WorldMapPortalState identities = new WorldMapPortalState();
      identities.set(state.worldMapPortalState);
      identities.bind(definition, enabled, visited);
      data.applyStory(state.scriptFlags2_bc, enabled, definition);
    }
    final WorldMapProgression.Builder builder = new WorldMapProgression.Builder(state.scriptFlags2_bc, enabled).objective(data.objective(state.scriptFlags2_bc, definition));
    state.campaignProgression.facts().forEach(builder::fact);
    final WorldMapProgression result = EVENTS.postEvent(new WorldMapProgressionEvent(engine, state, builder, definition, preset, candidate)).progression.build();
    if(!candidate) {
      this.resolvedRevisions = revisions;
      this.campaignRevision = campaignRevision;
    }
    return result;
  }
}
