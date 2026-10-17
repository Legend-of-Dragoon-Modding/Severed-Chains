package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/** Immutable metadata for a legacy world map place slot. Registry references supersede numeric compatibility fields when present. */
public record WorldMapPlace(RegistryId id, int legacyIndex, @Nullable String name, int thumbnail, int services, List<Integer> sounds,
                            @Nullable RegistryId thumbnailId, @Nullable List<RegistryId> serviceIds, @Nullable List<RegistryId> soundIds) {
  public WorldMapPlace(final RegistryId id, @Nullable final String name, final int thumbnail, final int services, final List<Integer> sounds) {
    this(id, -1, name, thumbnail, services, sounds, null, null, null);
  }

  public WorldMapPlace(final RegistryId id, final int legacyIndex, @Nullable final String name, final int thumbnail, final int services, final List<Integer> sounds) {
    this(id, legacyIndex, name, thumbnail, services, sounds, null, null, null);
  }

  public WorldMapPlace withLegacyIndex(final int legacyIndex) {
    return new WorldMapPlace(this.id, legacyIndex, this.name, this.thumbnail, this.services, this.sounds, this.thumbnailId, this.serviceIds, this.soundIds);
  }

  public WorldMapPlace {
    Objects.requireNonNull(id, "id");
    sounds = List.copyOf(sounds);
    if(serviceIds != null) serviceIds = List.copyOf(serviceIds);
    if(soundIds != null) soundIds = List.copyOf(soundIds);
  }

  public WorldMapPlace withName(@Nullable final String name) {
    return new WorldMapPlace(this.id, this.legacyIndex, name, this.thumbnail, this.services, this.sounds, this.thumbnailId, this.serviceIds, this.soundIds);
  }

  public WorldMapPlace withThumbnail(final RegistryId thumbnailId) {
    return new WorldMapPlace(this.id, this.legacyIndex, this.name, this.thumbnail, this.services, this.sounds,
      Objects.requireNonNull(thumbnailId, "thumbnailId"), this.serviceIds, this.soundIds);
  }

  public WorldMapPlace withServices(final List<RegistryId> serviceIds) {
    return new WorldMapPlace(this.id, this.legacyIndex, this.name, this.thumbnail, this.services, this.sounds,
      this.thumbnailId, Objects.requireNonNull(serviceIds, "serviceIds"), this.soundIds);
  }

  public WorldMapPlace withSounds(final List<RegistryId> soundIds) {
    return new WorldMapPlace(this.id, this.legacyIndex, this.name, this.thumbnail, this.services, this.sounds,
      this.thumbnailId, this.serviceIds, Objects.requireNonNull(soundIds, "soundIds"));
  }
}
