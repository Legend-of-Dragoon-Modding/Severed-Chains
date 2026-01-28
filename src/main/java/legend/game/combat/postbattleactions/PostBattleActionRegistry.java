package legend.game.combat.postbattleactions;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class PostBattleActionRegistry extends MutableRegistry<PostBattleAction> {
  public PostBattleActionRegistry() {
    super(new RegistryId("lod_core", "post_battle_actions"));
  }
}
