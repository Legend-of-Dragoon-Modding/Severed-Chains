package legend.game.characters;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class SpellRingRegistry extends MutableRegistry<SpellRing> {
  public SpellRingRegistry() {
    super(new RegistryId("lod_core", "spell_ring"));
  }
}