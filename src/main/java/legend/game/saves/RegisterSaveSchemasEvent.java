package legend.game.saves;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

/** Runs during mod boot before save previews/deserialization; must not allocate renderer resources. */
public final class RegisterSaveSchemasEvent extends RegistryEvent.Register<SaveSchema> {
  public RegisterSaveSchemasEvent(final MutableRegistry<SaveSchema> registry) {
    super(registry);
  }
}
