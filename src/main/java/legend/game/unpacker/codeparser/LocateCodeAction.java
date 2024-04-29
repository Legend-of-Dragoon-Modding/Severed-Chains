package legend.game.unpacker.codeparser;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import legend.game.unpacker.FileData;

import java.util.ArrayList;
import java.util.List;

public class LocateCodeAction implements Action {
  public static Builder builder() {
    return new Builder();
  }

  private final List<IntIntPair> ops;

  private LocateCodeAction(final List<IntIntPair> ops) {
    this.ops = ops;
  }

  @Override
  public void perform(final ParserState state) throws CodeParserException {
    for(int i = 0; i < 20; i++) {
      // Oscillate back and forth between positive and negative
      final int i2 = (i / 2 * (int)Math.signum(i % 2 - 0.5f)) * 0x4;

      if(this.codeMatches(state.data, state.offset + i2)) {
        state.offset += i2;
        return;
      }
    }

    throw new CodeParserException("Failed to locate code");
  }

  private boolean codeMatches(final FileData data, final int offset) {
    for(int i = 0; i < this.ops.size(); i++) {
      final IntIntPair pair = this.ops.get(i);

      if((data.readInt(offset + i * 0x4) & pair.rightInt()) != pair.leftInt()) {
        return false;
      }
    }

    return true;
  }

  public static class Builder {
    private final List<IntIntPair> ops = new ArrayList<>();

    public Builder op(final int op) {
      return this.op(op, 0xffffffff);
    }

    public Builder op(final int op, final int mask) {
      this.ops.add(IntIntPair.of(op, mask));
      return this;
    }

    public LocateCodeAction build() {
      return new LocateCodeAction(this.ops);
    }
  }
}
