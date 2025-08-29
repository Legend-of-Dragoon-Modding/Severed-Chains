package legend.game.combat.encounters;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class EncounterRegistry extends MutableRegistry<Encounter> {
  public EncounterRegistry() {
    super(new RegistryId("lod", "encounters"));
  }
}
