package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

/** Immutable registration recipe; replacements materialize with the target's stable identity. */
public class WorldMapDataEntry<T> extends RegistryEntry {
  private final Function<RegistryId, T> factory;
  @Nullable public final RegistryId replaces;
  public final int priority;

  public WorldMapDataEntry(final Function<RegistryId, T> factory) {
    this(null, 0, factory);
  }

  public WorldMapDataEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, T> factory) {
    this.replaces = replaces;
    this.priority = priority;
    this.factory = Objects.requireNonNull(factory, "factory");
  }

  public final T create(final RegistryId effectiveId) {
    return Objects.requireNonNull(this.factory.apply(Objects.requireNonNull(effectiveId, "effectiveId")), "World map entry factory returned null");
  }
}
