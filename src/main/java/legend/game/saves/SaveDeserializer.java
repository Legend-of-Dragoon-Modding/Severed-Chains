package legend.game.saves;

import legend.game.unpacker.FileData;

public interface SaveDeserializer {
  SavedGame deserialize(final Campaign campaign, final String name, final FileData data);
}
