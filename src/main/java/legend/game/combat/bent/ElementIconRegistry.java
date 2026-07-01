package legend.game.combat.bent;

import legend.game.characters.Element;
import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ElementIconRegistry extends MutableRegistry<ElementIconString> {
  public ElementIconRegistry() {
    super(new RegistryId("lod_core", "element_icons"));
  }
}
