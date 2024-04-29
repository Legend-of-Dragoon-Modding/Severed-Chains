package legend.game.unpacker.codeparser;

import legend.game.unpacker.FileData;

public class ParserState {
  public final FileData data;
  public int offset;

  public ParserState(final FileData data, final int offset) {
    this.data = data;
    this.offset = offset;
  }
}
