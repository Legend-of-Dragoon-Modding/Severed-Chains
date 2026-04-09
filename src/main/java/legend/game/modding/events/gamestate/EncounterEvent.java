package legend.game.modding.events.gamestate;

import legend.game.EngineState;
import legend.game.combat.encounters.Encounter;
import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;

public class EncounterEvent<T extends EngineState> extends InGameEvent<T> {
  public Encounter encounter;
  public int battleStageId;

  public EncounterEvent(final T engineState, final GameState52c gameState, final Encounter encounter, final int battleStageId) {
    super(engineState, gameState);
    this.encounter = encounter;
    this.battleStageId = battleStageId;
  }
}
