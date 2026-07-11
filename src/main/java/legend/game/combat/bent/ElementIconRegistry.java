package legend.game.combat.bent;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ElementIconRegistry extends MutableRegistry<ElementIcon> {
  public ElementIconRegistry() {
    super(new RegistryId("lod_core", "element_icons"));
  }
}
