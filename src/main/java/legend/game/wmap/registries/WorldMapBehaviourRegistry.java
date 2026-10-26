package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapBehaviour;
import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class WorldMapBehaviourRegistry extends MutableRegistry<WorldMapBehaviour> {
  public WorldMapBehaviourRegistry() {
    super(new RegistryId("lod_core", "world_map_behaviours"));
  }
}
