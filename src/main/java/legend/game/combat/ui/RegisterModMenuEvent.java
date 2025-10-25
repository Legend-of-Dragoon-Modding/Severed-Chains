package legend.game.combat.ui;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterModMenuEvent extends RegistryEvent.Register<ModMenu> {
  public RegisterModMenuEvent(final MutableRegistry<ModMenu> registry) {
    super(registry);
  }
}
