package legend.game.modding.events.gamestate;

import legend.game.EngineState;
import legend.game.combat.encounters.Encounter;
import legend.game.combat.environment.BattleStageDefinition;
import legend.lodmod.LodBattleStages;
import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;

public class EncounterEvent<T extends EngineState> extends InGameEvent<T> {
  public Encounter encounter;
  public int battleStageId;

  private BattleStageDefinition battleStage;
  private int selectedLegacyStage;

  public EncounterEvent(final T engineState, final GameState52c gameState, final Encounter encounter, final int battleStageId) {
    super(engineState, gameState);
    this.encounter = encounter;
    this.battleStageId = battleStageId;
  }

  /** Sets a registry-backed environment and publishes its compatibility alias to older listeners. */
  public void setBattleStage(final BattleStageDefinition stage) {
    this.battleStage = java.util.Objects.requireNonNull(stage, "stage");
    this.battleStageId = stage.legacyIndex();
    this.selectedLegacyStage = this.battleStageId;
  }

  /** A later edit to battleStageId wins; an unchanged alias preserves the typed selection. */
  public BattleStageDefinition resolveBattleStage() {
    if(this.battleStage != null && this.battleStageId == this.selectedLegacyStage) return this.battleStage;
    return LodBattleStages.nativeStage(this.battleStageId);
  }
}
