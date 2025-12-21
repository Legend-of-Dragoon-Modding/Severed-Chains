package legend.game.combat.ui;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class BattleActionRegistry extends MutableRegistry<BattleAction> {
  public BattleActionRegistry() {
    super(new RegistryId("lod", "battle_actions"));
  }
}
