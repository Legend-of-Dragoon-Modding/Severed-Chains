package legend.lodmod;

import legend.game.combat.environment.BattleStageDefinition;
import legend.game.combat.environment.NativeBattleStageDefinition;
import legend.game.combat.environment.RegisterBattleStagesEvent;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.GameEngine.REGISTRIES;

public final class LodBattleStages {
  private static final Registrar<BattleStageDefinition, RegisterBattleStagesEvent> REGISTRAR = new Registrar<>(REGISTRIES.battleStages, LodMod.MOD_ID);

  static {
    for(int i = 0; i < NativeBattleStageDefinition.count(); i++) {
      final int index = i;
      REGISTRAR.register("stage_" + index, () -> new NativeBattleStageDefinition(index));
    }
  }

  private LodBattleStages() { }

  public static RegistryId id(final int index) {
    return new RegistryId(LodMod.MOD_ID, "stage_" + index);
  }

  /** Unknown legacy values recover to the first native stage. */
  public static BattleStageDefinition nativeStage(final int index) {
    final int resolved = index >= 0 && index < NativeBattleStageDefinition.count() ? index : 0;
    return REGISTRIES.battleStages.getEntry(id(resolved)).get();
  }

  public static void register(final RegisterBattleStagesEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
