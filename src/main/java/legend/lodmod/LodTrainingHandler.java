package legend.lodmod;

import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.ui.BattleAction;
import legend.game.combat.ui.GatherBattleActionsEvent;
import legend.game.modding.events.battle.BattleStartedEvent;
import legend.game.modding.events.battle.LoadEnemyEvent;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.unpacker.Loader;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;

public class LodTrainingHandler {
  /// Replace all monster scripts with the training script
  @EventListener
  public void handleLoadEnemy(final LoadEnemyEvent event) {
    if(event.encounter == LodEncounters.ENCOUNTER_TRAINING.get()) {
      event.scriptFuture = Loader
        .loadFile(Path.of("patches/scripts/training_monster.txt"))
        .thenApply(data -> SCRIPTS.assemble("Training Monster", new String(data.getBytes(), StandardCharsets.UTF_8)))
        .thenApply(assembled -> new ScriptFile("Training Monster", assembled))
      ;
    }
  }

  /// Disable turn-based and on-hit regen
  @EventListener
  public void battleStarted(final BattleStartedEvent event) {
    if(event.encounter == LodEncounters.ENCOUNTER_TRAINING.get()) {
      for(final ScriptState<PlayerBattleEntity> state : battleState_8006e398.playerBents_e40) {
        final PlayerBattleEntity player = state.innerStruct_00;
        player.hpRegen_134 = 0;
        player.mpRegen_136 = 0;
        player.spRegen_138 = 0;
        player.spPerPhysicalHit_12a = 0;
        player.mpPerPhysicalHit_12c = 0;
        player.spPerMagicalHit_12e = 0;
        player.mpPerMagicalHit_130 = 0;
      }
    }
  }

  /// Disable everything but attack/run
  @EventListener
  public void gatherBattleActions(final GatherBattleActionsEvent event) {
    if(event.encounter == LodEncounters.ENCOUNTER_TRAINING.get()) {
      for(final RegistryId id : REGISTRIES.battleActions) {
        final BattleAction action = REGISTRIES.battleActions.getEntry(id).get();

        if(action != LodBattleActions.ATTACK.get() && action != LodBattleActions.ADDITIONS.get() && action != LodBattleActions.ESCAPE.get()) {
          event.disabledActions.add(action);
        }
      }
    }
  }
}
