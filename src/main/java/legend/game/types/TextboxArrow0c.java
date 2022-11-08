package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class TextboxArrow0c implements MemoryRef {
  private final Value ref;

  public final IntRef _00;
  public final ShortRef x_04;
  public final ShortRef y_06;
  public final ShortRef spriteIndex_08;

  public TextboxArrow0c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.x_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.y_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.spriteIndex_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
