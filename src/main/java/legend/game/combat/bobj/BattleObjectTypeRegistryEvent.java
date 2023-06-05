package legend.game.combat.bobj;

import legend.game.modding.events.registries.RegistryEvent;
import legend.game.modding.registries.MutableRegistry;

public class BattleObjectTypeRegistryEvent extends RegistryEvent.Register<BattleObjectType> {
  public BattleObjectTypeRegistryEvent(final MutableRegistry<BattleObjectType> registry) {
    super(registry);
  }
}
