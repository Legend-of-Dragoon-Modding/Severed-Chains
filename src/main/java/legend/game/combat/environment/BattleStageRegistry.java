package legend.game.combat.environment;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class BattleStageRegistry extends MutableRegistry<BattleStageDefinition> {
  public BattleStageRegistry() {
    super(new RegistryId("lod", "battle_stages"));
  }
}
