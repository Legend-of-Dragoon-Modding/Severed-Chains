package legend.game.combat.bent;

import legend.game.modding.events.registries.RegistryEvent;
import legend.game.modding.registries.MutableRegistry;

public class BattleEntityTypeRegistryEvent extends RegistryEvent.Register<BattleEntityType> {
  public BattleEntityTypeRegistryEvent(final MutableRegistry<BattleEntityType> registry) {
    super(registry);
  }
}
