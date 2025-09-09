package legend.game.characters;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ElementRegistry extends MutableRegistry<Element> {
  public ElementRegistry() {
    super(new RegistryId("lod_core", "elements"));
  }
}
