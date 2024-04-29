package legend.game.unpacker.codeparser;

public class FollowJalAction implements Action {
  private final int baseOffset;

  public FollowJalAction(final int baseOffset) {
    this.baseOffset = baseOffset;
  }

  @Override
  public void perform(final ParserState state) throws CodeParserException {
    state.offset = (state.data.readInt(state.offset) & 0x3ff_ffff) * 4 - this.baseOffset;
  }
}
