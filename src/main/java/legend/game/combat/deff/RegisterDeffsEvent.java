package legend.game.combat.deff;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterDeffsEvent extends RegistryEvent.Register<DeffPackage> {
  public RegisterDeffsEvent(final MutableRegistry<DeffPackage> registry) {
    super(registry);
  }
}
