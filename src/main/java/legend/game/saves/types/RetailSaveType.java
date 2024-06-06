package legend.game.saves.types;

import legend.core.memory.types.IntRef;
import legend.game.EngineState;
import legend.game.inventory.screens.controls.RetailSaveCard;
import legend.game.inventory.screens.controls.SaveCard;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

public class RetailSaveType extends SaveType<RetailSaveDisplay> {
  @Override
  public SaveDisplay createDisplayData(final GameState52c gameState, final ActiveStatsa0[] activeStats, final EngineState engineState) {
    return null;
  }

  @Override
  public void serialize(final FileData data, final RetailSaveDisplay display, final IntRef serializerOffset) {

  }

  @Override
  public RetailSaveDisplay deserialize(final FileData data, final IntRef serializerOffset) {
    return null;
  }

  @Override
  public SaveCard<RetailSaveDisplay> makeSaveCard() {
    return new RetailSaveCard();
  }
}
