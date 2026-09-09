package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.scripting.ScriptFile;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class LoadEnemyEvent extends BattleEvent {
  public final int enemyId;
  public final CombatantStruct1a8 combatant;
  public final Path script;
  public CompletableFuture<ScriptFile> scriptFuture;

  public LoadEnemyEvent(final Battle battle, final int enemyId, final CombatantStruct1a8 combatant, final Path script) {
    super(battle);
    this.enemyId = enemyId;
    this.combatant = combatant;
    this.script = script;
  }
}
