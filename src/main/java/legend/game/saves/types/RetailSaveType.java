package legend.game.saves.types;

import legend.game.EngineState;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

public class RetailSaveType extends SaveType<RetailSaveDisplay> {
  @Override
  public SaveDisplay createDisplayData(final GameState52c gameState, final ActiveStatsa0[] activeStats, final EngineState engineState) {
    return null;
  }

  @Override
  public int serialize(final FileData data, final RetailSaveDisplay display) {
    return 0;
  }

  @Override
  public SaveDisplay deserialize(final FileData data, final RetailSaveDisplay display) {
    return null;
  }
}
