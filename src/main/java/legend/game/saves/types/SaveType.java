package legend.game.saves.types;

import legend.game.EngineState;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class SaveType<Display extends SaveDisplay> extends RegistryEntry {
  public abstract SaveDisplay createDisplayData(final GameState52c gameState, final ActiveStatsa0[] activeStats, final EngineState engineState);
  public abstract int serialize(final FileData data, final Display display);
  public abstract SaveDisplay deserialize(final FileData data, final Display display);
}
