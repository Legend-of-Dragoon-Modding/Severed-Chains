package legend.game.characters;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class LevelUpActionRegistry extends MutableRegistry<LevelUpAction<?>> {
  public LevelUpActionRegistry() {
    super(new RegistryId("lod_core", "level_up_actions"));
  }
}
