package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapEncounterPool;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapEncounterPoolEntry extends WorldMapDataEntry<WorldMapEncounterPool> {
  public WorldMapEncounterPoolEntry(final Function<RegistryId, WorldMapEncounterPool> factory) {
    super(factory);
  }

  public WorldMapEncounterPoolEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapEncounterPool> factory) {
    super(replaces, priority, factory);
  }
}
