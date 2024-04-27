package legend.game.unpacker.codeparser;

import legend.game.unpacker.FileData;

public class UseFileAction implements Action {
  private final FileData data;

  public UseFileAction(final FileData data) {
    this.data = data;
  }

  @Override
  public void perform(final ParserState state) {
    state.data = this.data;
  }
}
