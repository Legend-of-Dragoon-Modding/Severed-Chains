package legend.game.saves.types;

import legend.core.memory.types.IntRef;
import legend.game.EngineState;
import legend.game.inventory.screens.controls.SaveCard;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class SaveType<Display extends SaveDisplay> extends RegistryEntry {
  public abstract SaveDisplay createDisplayData(final GameState52c gameState, final ActiveStatsa0[] activeStats, final EngineState engineState);
  public abstract void serialize(final FileData data, final Display display, final IntRef offset);
  public abstract Display deserialize(final FileData data, final IntRef offset);
  public abstract SaveCard<Display> makeSaveCard();
}
