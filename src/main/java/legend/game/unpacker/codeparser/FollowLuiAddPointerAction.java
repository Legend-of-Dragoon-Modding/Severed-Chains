package legend.game.unpacker.codeparser;

public class FollowLuiAddPointerAction implements Action {
  private final int baseOffset;
  private final int luiOffset;
  private final int addOffset;

  public FollowLuiAddPointerAction(final int baseOffset, final int luiOffset, final int addOffset) {
    this.baseOffset = baseOffset;
    this.luiOffset = luiOffset;
    this.addOffset = addOffset;
  }

  public FollowLuiAddPointerAction(final int baseOffset) {
    this(baseOffset, 0, 1);
  }

  @Override
  public void perform(final ParserState state) throws CodeParserException {
    final int lui = state.data.readShort(state.offset + this.luiOffset * 0x4);
    final int add = state.data.readShort(state.offset + this.addOffset * 0x4);
    state.offset = ((lui << 16) + add & 0x7fff_ffff) - this.baseOffset;
  }
}
