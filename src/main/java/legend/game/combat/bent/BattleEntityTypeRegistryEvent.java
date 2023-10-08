package legend.game.combat.bent;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class BattleEntityTypeRegistryEvent extends RegistryEvent.Register<BattleEntityType> {
  public BattleEntityTypeRegistryEvent(final MutableRegistry<BattleEntityType> registry) {
    super(registry);
  }
}
