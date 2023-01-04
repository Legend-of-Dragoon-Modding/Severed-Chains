package legend.game.scripting;

import static legend.game.Scus94491BpeSegment_8004.scriptPtrs_8004de58;

public class GameVarParam extends Param {
  private final int index;

  public GameVarParam(final int index) {
    this.index = index;
  }

  @Override
  public int get() {
    return scriptPtrs_8004de58.get(this.index).deref().get();
  }

  @Override
  public Param set(final int val) {
    scriptPtrs_8004de58.get(this.index).deref().set(val);
    return this;
  }

  @Override
  public Param array(final int index) {
    return new GameVarParam(this.index + index);
  }

  @Override
  public String toString() {
    return "GameVar[%d] %d".formatted(this.index, this.get());
  }
}
