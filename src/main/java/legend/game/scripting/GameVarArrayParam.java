package legend.game.scripting;

import static legend.core.GameEngine.MEMORY;
import static legend.game.Scus94491BpeSegment_8004.scriptPtrs_8004de58;

public class GameVarArrayParam extends Param {
  private final int varIndex;
  private final int arrIndex;

  public GameVarArrayParam(final int varIndex, final int arrIndex) {
    this.varIndex = varIndex;
    this.arrIndex = arrIndex;
  }

  @Override
  public int get() {
    return (int)MEMORY.get(scriptPtrs_8004de58.get(this.varIndex).getPointer() + this.arrIndex * 4, 4);
  }

  @Override
  public Param set(final int val) {
    MEMORY.set(scriptPtrs_8004de58.get(this.varIndex).getPointer() + this.arrIndex * 4, 4, val);
    return this;
  }

  @Override
  public Param array(final int index) {
    return new GameVarArrayParam(this.varIndex, this.arrIndex + index);
  }

  @Override
  public String toString() {
    return "GameVar[%d][%d] %d".formatted(this.varIndex, this.arrIndex, this.get());
  }
}
