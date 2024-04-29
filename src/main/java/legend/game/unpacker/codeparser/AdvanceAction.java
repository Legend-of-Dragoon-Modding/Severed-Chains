package legend.game.unpacker.codeparser;

public class AdvanceAction implements Action {
  private final int amount;

  public AdvanceAction(final int amount) {
    this.amount = amount;
  }

  @Override
  public void perform(final ParserState state) {
    state.offset += this.amount;
  }
}
