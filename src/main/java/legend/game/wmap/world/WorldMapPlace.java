package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/** Immutable metadata for a legacy world map place slot. */
public record WorldMapPlace(RegistryId id, int legacyIndex, @Nullable String name, int thumbnail, int services, List<Integer> sounds) {
  public WorldMapPlace(final RegistryId id, @Nullable final String name, final int thumbnail, final int services, final List<Integer> sounds) {
    this(id, -1, name, thumbnail, services, sounds);
  }

  public WorldMapPlace withLegacyIndex(final int legacyIndex) {
    return new WorldMapPlace(this.id, legacyIndex, this.name, this.thumbnail, this.services, this.sounds);
  }

  public WorldMapPlace {
    Objects.requireNonNull(id, "id");
    sounds = List.copyOf(sounds);
  }

  public WorldMapPlace withName(@Nullable final String name) {
    return new WorldMapPlace(this.id, this.legacyIndex, name, this.thumbnail, this.services, this.sounds);
  }
}
