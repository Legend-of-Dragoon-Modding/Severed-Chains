package legend.game.saves.types;

import legend.game.EngineState;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class SaveType<Display extends SaveDisplay> extends RegistryEntry {
  public abstract void serialize(final FileData data, final GameState52c gameState, final EngineState engineState);
  public abstract SaveDisplay deserialize(final FileData data);
}
