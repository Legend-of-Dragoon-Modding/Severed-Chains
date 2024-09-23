package legend.game.combat.deff;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class DeffRegistry extends MutableRegistry<DeffPackage> {
  public DeffRegistry() {
    super(new RegistryId("lod_core", "deffs"));
  }
}
