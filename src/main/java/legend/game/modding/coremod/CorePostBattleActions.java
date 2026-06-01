package legend.game.modding.coremod;

import legend.game.combat.postbattleactions.GameOverPostBattleAction;
import legend.game.combat.postbattleactions.PlayFmvPostBattleAction;
import legend.game.combat.postbattleactions.PostBattleAction;
import legend.game.combat.postbattleactions.RegisterPostBattleActionsEvent;
import legend.game.combat.postbattleactions.VictoryPostBattleAction;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.core.GameEngine.REGISTRIES;

public final class CorePostBattleActions {
  private CorePostBattleActions() { }

  private static final Registrar<PostBattleAction<?, ?>, RegisterPostBattleActionsEvent> REGISTRAR = new Registrar<>(REGISTRIES.postBattleActions, CoreMod.MOD_ID);

  public static final RegistryDelegate<VictoryPostBattleAction> VICTORY = REGISTRAR.register("victory", VictoryPostBattleAction::new);
  public static final RegistryDelegate<GameOverPostBattleAction> GAME_OVER = REGISTRAR.register("game_over", GameOverPostBattleAction::new);
  public static final RegistryDelegate<PlayFmvPostBattleAction> PLAY_FMV = REGISTRAR.register("play_fmv", PlayFmvPostBattleAction::new);

  static void register(final RegisterPostBattleActionsEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
