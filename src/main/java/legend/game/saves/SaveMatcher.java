package legend.game.saves;

import legend.game.unpacker.FileData;

@FunctionalInterface
public interface SaveMatcher {
  FileData match(final SaveVersion version, final FileData data);
}
