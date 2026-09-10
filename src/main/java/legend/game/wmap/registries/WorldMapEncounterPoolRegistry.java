package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapEncounterPoolRegistry extends MutableRegistry<WorldMapEncounterPoolEntry> {
  public WorldMapEncounterPoolRegistry() {
    super(new RegistryId("lod_core", "world_map_encounter_pools"));
  }
}
