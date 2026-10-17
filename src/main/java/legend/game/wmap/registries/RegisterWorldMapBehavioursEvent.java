package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapBehaviour;
import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterWorldMapBehavioursEvent extends RegistryEvent.Register<WorldMapBehaviour> {
  public RegisterWorldMapBehavioursEvent(final MutableRegistry<WorldMapBehaviour> registry) {
    super(registry);
  }
}
