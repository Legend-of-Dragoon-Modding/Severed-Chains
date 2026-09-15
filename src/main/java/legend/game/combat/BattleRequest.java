package legend.game.combat;

import legend.game.combat.encounters.Encounter;
import legend.game.combat.environment.BattleStageDefinition;

import javax.annotation.Nullable;
import java.util.Objects;

/** Complete battle entry contract; null return context preserves the legacy return path. */
public record BattleRequest(Encounter encounter, BattleStageDefinition stage, @Nullable BattleReturnContext returnContext) {
  public BattleRequest {
    Objects.requireNonNull(encounter, "encounter");
    Objects.requireNonNull(stage, "stage");
  }
}
