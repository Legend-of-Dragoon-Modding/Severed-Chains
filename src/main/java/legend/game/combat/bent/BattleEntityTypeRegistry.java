package legend.game.combat.bent;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class BattleEntityTypeRegistry extends MutableRegistry<BattleEntityType> {
  public BattleEntityTypeRegistry() {
    super(new RegistryId("lod-core", "battle_entity_type"));
  }
}
