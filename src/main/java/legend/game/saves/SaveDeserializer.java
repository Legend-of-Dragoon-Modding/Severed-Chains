package legend.game.saves;

import legend.game.unpacker.FileData;

@FunctionalInterface
public interface SaveDeserializer {
  SavedGame deserialize(final SaveVersion version, final Campaign campaign, final String name, final FileData data);
}
