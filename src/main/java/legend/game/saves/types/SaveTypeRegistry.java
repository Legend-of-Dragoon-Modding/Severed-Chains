package legend.game.saves.types;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class SaveTypeRegistry extends MutableRegistry<SaveType<?>> {
  public SaveTypeRegistry() {
    super(new RegistryId("lod-core", "save_types"));
  }
}
