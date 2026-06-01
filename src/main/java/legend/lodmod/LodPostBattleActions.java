package legend.lodmod;

import legend.game.combat.postbattleactions.BossKillPostBattleAction;
import legend.game.combat.postbattleactions.DiedInArenaFightPostBattleAction;
import legend.game.combat.postbattleactions.MerchantPostBattleAction;
import legend.game.combat.postbattleactions.PostBattleAction;
import legend.game.combat.postbattleactions.RegisterPostBattleActionsEvent;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.core.GameEngine.REGISTRIES;

public final class LodPostBattleActions {
  private LodPostBattleActions() { }

  private static final Registrar<PostBattleAction<?, ?>, RegisterPostBattleActionsEvent> REGISTRAR = new Registrar<>(REGISTRIES.postBattleActions, LodMod.MOD_ID);

  public static final RegistryDelegate<BossKillPostBattleAction> BOSS_KILL = REGISTRAR.register("boss_kill", BossKillPostBattleAction::new);
  public static final RegistryDelegate<DiedInArenaFightPostBattleAction> DIED_IN_ARENA_FIGHT = REGISTRAR.register("died_in_arena_fight", DiedInArenaFightPostBattleAction::new);
  public static final RegistryDelegate<MerchantPostBattleAction> MERCHANT = REGISTRAR.register("merchant", MerchantPostBattleAction::new);

  static void register(final RegisterPostBattleActionsEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
