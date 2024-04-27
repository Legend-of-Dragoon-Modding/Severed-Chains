package legend.game.unpacker.codeparser;

import java.util.Arrays;
import java.util.List;

public class CodeParser {
  private final List<Action> actions;

  public CodeParser(final Action... actions) {
    this.actions = Arrays.asList(actions);
  }

  public void parse() {
    final ParserState state = new ParserState();

    for(final Action action : this.actions) {
      action.perform(state);
    }
  }
}
