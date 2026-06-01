package legend.game.combat.ui;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterBattleActionsEvent extends RegistryEvent.Register<BattleAction> {
  public RegisterBattleActionsEvent(final MutableRegistry<BattleAction> registry) {
    super(registry);
  }
}
