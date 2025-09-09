package legend.game.inventory;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class SpellRegistry extends MutableRegistry<Spell> {
  public SpellRegistry() {
    super(new RegistryId("lod_core", "spells"));
  }
}
