package legend.game.wmap.preset;

import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Supplier;

/** A discoverable file or code-generated preset. Suppliers run when selected, after registries load. */
public record WorldMapPresetEntry(RegistryId id, String name, @Nullable Supplier<WorldMapPreset> loader, @Nullable Path file) {
  public static final WorldMapPresetEntry VANILLA = new WorldMapPresetEntry(new RegistryId("lod_core", "registered_world_map"), "Vanilla", null);

  public WorldMapPresetEntry {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(name, "name");
  }

  public WorldMapPresetEntry(final WorldMapPreset preset) {
    this(preset.id(), preset.name(), () -> preset);
  }

  public WorldMapPresetEntry(final RegistryId id, final String name, @Nullable final Supplier<WorldMapPreset> loader) {
    this(id, name, loader, null);
  }

  @Nullable
  public WorldMapPreset load() {
    if(this.loader == null) {
      return null;
    }
    final WorldMapPreset preset = Objects.requireNonNull(this.loader.get(), "Preset supplier returned null: " + this.id);
    if(!this.id.equals(preset.id())) {
      throw new IllegalArgumentException("Preset supplier identity differs from catalog entry: " + this.id);
    }
    return preset;
  }
}
