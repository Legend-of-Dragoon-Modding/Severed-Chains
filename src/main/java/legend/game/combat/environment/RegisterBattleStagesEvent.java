package legend.game.combat.environment;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterBattleStagesEvent extends RegistryEvent.Register<BattleStageDefinition> {
  public RegisterBattleStagesEvent(final MutableRegistry<BattleStageDefinition> registry) {
    super(registry);
  }
}
