package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class UseItemResponse implements MemoryRef {
  private final Value ref;

  public final IntRef _00;
  public final IntRef value_04;
  public final LodString string_08;

  public UseItemResponse(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.value_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.string_08 = ref.offset(2, 0x08L).cast(LodString::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
