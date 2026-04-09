package legend.game.combat.postbattleactions;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterPostBattleActionsEvent extends RegistryEvent.Register<PostBattleAction<?, ?>> {
  public RegisterPostBattleActionsEvent(final MutableRegistry<PostBattleAction<?, ?>> registry) {
    super(registry);
  }
}
