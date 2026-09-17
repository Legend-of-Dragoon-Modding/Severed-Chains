package legend.game.saves;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class SaveSchemaRegistry extends MutableRegistry<SaveSchema> {
  public SaveSchemaRegistry() {
    super(new RegistryId("lod_core", "save_schemas"));
  }
}
