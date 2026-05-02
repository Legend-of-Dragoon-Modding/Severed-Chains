package legend.game.characters;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class CharacterTemplateRegistry extends MutableRegistry<CharacterTemplate> {
  public CharacterTemplateRegistry() {
    super(new RegistryId("lod_core", "character_templates"));
  }
}
