package legend.game.characters;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class StatTypeRegistry extends MutableRegistry<StatType<?>> {
  public StatTypeRegistry() {
    super(new RegistryId("lod_core", "stats"));
  }
}
