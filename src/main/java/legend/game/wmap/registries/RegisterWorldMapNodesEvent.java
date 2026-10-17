package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapNodesEvent extends RegistryEvent.Register<WorldMapNodeEntry> {
  public RegisterWorldMapNodesEvent(final MutableRegistry<WorldMapNodeEntry> registry) {
    super(registry);
  }
}
