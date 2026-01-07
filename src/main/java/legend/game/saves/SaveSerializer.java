package legend.game.saves;

import legend.core.memory.types.IntRef;
import legend.game.EngineState;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

public interface SaveSerializer {
  void serializer(final String name, final FileData data, final IntRef offset, final EngineState<?> engineState, final GameState52c gameState, final ActiveStatsa0[] activeStats);
}
