package legend.game.combat;

import legend.core.tags.Tag;
import legend.game.EngineState;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;

/** Default destination after battle; post-battle actions may override it. */
public record BattleReturnContext(RegistryId engineState, @Nullable Tag data) {
  public BattleReturnContext {
    Objects.requireNonNull(engineState, "engineState");
    data = data == null ? null : data.clone();
  }

  @Override
  public Tag data() {
    return this.data == null ? null : this.data.clone();
  }

  public static BattleReturnContext capture(final EngineState<?> state, final GameState52c gameState) {
    return new BattleReturnContext(state.type.getRegistryId(), state.writeSaveData(gameState));
  }
}
