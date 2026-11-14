package legend.lodmod;

import legend.game.combat.ui.BattleAction;
import legend.game.combat.ui.GatherBattleActionsEvent;
import legend.game.modding.events.battle.LoadEnemyEvent;
import legend.game.scripting.ScriptFile;
import legend.game.unpacker.Loader;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.SCRIPTS;

public class LodTrainingHandler {
  /// If we're loading the training encounter, replace all monster scripts with the training script
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

  @EventListener
  public void gatherBattleActions(final GatherBattleActionsEvent event) {
    for(final RegistryId id : REGISTRIES.battleActions) {
      final BattleAction action = REGISTRIES.battleActions.getEntry(id).get();

      if(action != LodBattleActions.ATTACK.get() && action != LodBattleActions.ESCAPE.get()) {
        event.disabledActions.add(action);
      }
    }
  }
}
