package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;

/** Registered directed route; numeric geometry/encounter indices are assigned only by the engine adapter. */
public record WorldMapRouteData(int legacyIndex, RegistryId start, RegistryId end, RegistryId geometry, int direction, int encounterRate, int battleStage, @Nullable RegistryId encounterPool, int modelIndex, int legacyEncounterPlaceholder, @Nullable RegistryId avatar) {
  public WorldMapRouteData(final int legacyIndex, final RegistryId start, final RegistryId end, final RegistryId geometry, final int direction, final int encounterRate, final int battleStage, @Nullable final RegistryId encounterPool, final int modelIndex, final int legacyEncounterPlaceholder) {
    this(legacyIndex, start, end, geometry, direction, encounterRate, battleStage, encounterPool, modelIndex, legacyEncounterPlaceholder, null);
  }

  public WorldMapRouteData withAvatar(@Nullable final RegistryId avatar) {
    return new WorldMapRouteData(this.legacyIndex, this.start, this.end, this.geometry, this.direction, this.encounterRate, this.battleStage, this.encounterPool, this.modelIndex, this.legacyEncounterPlaceholder, avatar);
  }
  public WorldMapRouteData(final int legacyIndex, final RegistryId start, final RegistryId end, final RegistryId geometry, final int direction, final int encounterRate, final int battleStage, @Nullable final RegistryId encounterPool, final int modelIndex) {
    this(legacyIndex, start, end, geometry, direction, encounterRate, battleStage, encounterPool, modelIndex, -1);
  }
  public WorldMapRouteData {
    Objects.requireNonNull(start, "start");
    Objects.requireNonNull(end, "end");
    Objects.requireNonNull(geometry, "geometry");
  }
}
