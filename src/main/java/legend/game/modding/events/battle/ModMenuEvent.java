package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ModMenuEvent extends BattleEvent {
  public final RegistryId modMenuId;
  public boolean disableModMenu = true;

  public ModMenuEvent(final RegistryId modMenuId) {
    this.modMenuId = modMenuId;
  }
}
