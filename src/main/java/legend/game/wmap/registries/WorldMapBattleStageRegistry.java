package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapBattleStageRegistry extends MutableRegistry<WorldMapBattleStageEntry> {
  public WorldMapBattleStageRegistry() {
    super(new RegistryId("lod_core", "world_map_battle_stages"));
  }
}
