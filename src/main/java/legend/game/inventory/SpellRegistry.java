package legend.game.inventory;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class SpellRegistry extends MutableRegistry<SpellStats0c> {
  public SpellRegistry() {
    super(new RegistryId("lod_core", "spells"));
  }
}
