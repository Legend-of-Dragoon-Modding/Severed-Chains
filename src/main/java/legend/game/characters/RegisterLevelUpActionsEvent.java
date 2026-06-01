package legend.game.characters;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterLevelUpActionsEvent extends RegistryEvent.Register<LevelUpAction<?>> {
  public RegisterLevelUpActionsEvent(final MutableRegistry<LevelUpAction<?>> registry) {
    super(registry);
  }
}
