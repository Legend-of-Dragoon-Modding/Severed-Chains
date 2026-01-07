package legend.lodmod;

import legend.core.GameEngine;
import legend.game.EngineStateType;
import legend.game.RegisterEngineStateTypesEvent;
import legend.game.combat.Battle;
import legend.game.credits.Credits;
import legend.game.credits.FinalFmv;
import legend.game.submap.SMap;
import legend.game.title.GameOver;
import legend.game.wmap.WMap;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class LodEngineStateTypes {
  private LodEngineStateTypes() { }

  private static final Registrar<EngineStateType<?>, RegisterEngineStateTypesEvent> REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.engineStateTypes, LodMod.MOD_ID);

  public static final RegistryDelegate<EngineStateType<SMap>> SUBMAP = REGISTRAR.register("submap", () -> new EngineStateType<>(SMap.class, SMap::new));
  public static final RegistryDelegate<EngineStateType<WMap>> WORLD_MAP = REGISTRAR.register("world_map", () -> new EngineStateType<>(WMap.class, WMap::new));
  public static final RegistryDelegate<EngineStateType<Battle>> BATTLE = REGISTRAR.register("battle", () -> new EngineStateType<>(Battle.class, Battle::new));
  public static final RegistryDelegate<EngineStateType<FinalFmv>> FINAL_FMV = REGISTRAR.register("final_fmv", () -> new EngineStateType<>(FinalFmv.class, FinalFmv::new));
  public static final RegistryDelegate<EngineStateType<GameOver>> GAME_OVER = REGISTRAR.register("game_over", () -> new EngineStateType<>(GameOver.class, GameOver::new));
  public static final RegistryDelegate<EngineStateType<Credits>> CREDITS = REGISTRAR.register("credits", () -> new EngineStateType<>(Credits.class, Credits::new));

  static void register(final RegisterEngineStateTypesEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
