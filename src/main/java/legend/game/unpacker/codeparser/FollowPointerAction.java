package legend.game.unpacker.codeparser;

public class FollowPointerAction implements Action {
  private final int baseOffset;

  public FollowPointerAction(final int baseOffset) {
    this.baseOffset = baseOffset;
  }

  @Override
  public void perform(final ParserState state) throws CodeParserException {
    state.offset = (state.data.readInt(state.offset) & 0x7fff_ffff) - this.baseOffset;
  }
}
