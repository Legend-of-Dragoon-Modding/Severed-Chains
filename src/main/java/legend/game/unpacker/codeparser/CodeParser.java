package legend.game.unpacker.codeparser;

import legend.game.unpacker.FileData;

import java.util.Arrays;
import java.util.List;

public class CodeParser {
  private final List<Action> actions;

  public CodeParser(final Action... actions) {
    this.actions = Arrays.asList(actions);
  }

  public int parse(final FileData data, final int offset) throws CodeParserException {
    final ParserState state = new ParserState(data, offset);

    for(final Action action : this.actions) {
      action.perform(state);
    }

    return state.offset;
  }
}
