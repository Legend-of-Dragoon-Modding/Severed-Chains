package legend.game.characters;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterCharacterTemplatesEvent extends RegistryEvent.Register<CharacterTemplate> {
  public RegisterCharacterTemplatesEvent(final MutableRegistry<CharacterTemplate> registry) {
    super(registry);
  }
}
