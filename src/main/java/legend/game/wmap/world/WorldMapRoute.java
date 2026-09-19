package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;

/** One directed legacy traversal over one physical world map path segment. */
public record WorldMapRoute(RegistryId id, int legacyIndex, RegistryId start, RegistryId end, int segmentIndex, int direction,
                            int encounterRate, int battleStage, int encounterIndex, int modelIndex,
                            @Nullable RegistryId avatar, @Nullable RegistryId battleStageId) {
  public WorldMapRoute(final RegistryId id, final int legacyIndex, final RegistryId start, final RegistryId end, final int segmentIndex,
                       final int direction, final int encounterRate, final int battleStage, final int encounterIndex, final int modelIndex) {
    this(id, legacyIndex, start, end, segmentIndex, direction, encounterRate, battleStage, encounterIndex, modelIndex, null, null);
  }

  public WorldMapRoute(final RegistryId id, final int legacyIndex, final RegistryId start, final RegistryId end, final int segmentIndex,
                       final int direction, final int encounterRate, final int battleStage, final int encounterIndex, final int modelIndex,
                       @Nullable final RegistryId avatar) {
    this(id, legacyIndex, start, end, segmentIndex, direction, encounterRate, battleStage, encounterIndex, modelIndex, avatar, null);
  }

  public WorldMapRoute withAvatar(@Nullable final RegistryId avatar) {
    return new WorldMapRoute(this.id, this.legacyIndex, this.start, this.end, this.segmentIndex, this.direction, this.encounterRate,
      this.battleStage, this.encounterIndex, this.modelIndex, avatar, this.battleStageId);
  }

  public WorldMapRoute {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(start, "start");
    Objects.requireNonNull(end, "end");
  }
}
