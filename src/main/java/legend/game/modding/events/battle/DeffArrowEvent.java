package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import org.legendofdragoon.modloader.registries.RegistryId;

public class DeffArrowEvent extends BattleEvent  {
  public final RegistryId registryId;
  public boolean isDeff;

  public DeffArrowEvent(final RegistryId registryId, final boolean isDeff) {
    this.registryId = registryId;
    this.isDeff = isDeff;
  }
}
