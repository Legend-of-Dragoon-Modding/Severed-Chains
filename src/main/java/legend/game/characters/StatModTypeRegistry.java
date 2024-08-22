package legend.game.characters;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class StatModTypeRegistry extends MutableRegistry<StatModType<?, ?, ?>> {
  public StatModTypeRegistry() {
    super(new RegistryId("lod_core", "stat_mods"));
  }
}
