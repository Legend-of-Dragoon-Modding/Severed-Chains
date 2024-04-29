package legend.game.unpacker.codeparser;

public class JumpAction implements Action {
  private final int offset;

  public JumpAction(final int offset) {
    this.offset = offset;
  }

  @Override
  public void perform(final ParserState state) {
    state.offset = this.offset;
  }
}
