package legend.game.modding.events.saves;

import legend.game.EngineState;
import legend.game.saves.types.SaveType;
import legend.game.types.GameState52c;
import legend.lodmod.LodMod;

public class SaveTypeEvent extends SaveEvent {
  public final GameState52c gameState;
  public final EngineState engineState;
  public SaveType<?> saveType = LodMod.ENHANCED_SAVE_TYPE.get();

  public SaveTypeEvent(final GameState52c gameState, final EngineState engineState) {
    this.gameState = gameState;
    this.engineState = engineState;
  }
}
