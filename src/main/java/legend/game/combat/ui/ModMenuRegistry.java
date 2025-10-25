package legend.game.combat.ui;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ModMenuRegistry extends MutableRegistry<ModMenu> {
  public ModMenuRegistry() {
    super(new RegistryId("lod_core", "modmenu"));
  }
}
