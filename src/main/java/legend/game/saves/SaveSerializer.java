package legend.game.saves;

import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

public interface SaveSerializer {
  int serializer(final String name, final FileData data, final GameState52c state, final ActiveStatsa0[] activeStats);
}
