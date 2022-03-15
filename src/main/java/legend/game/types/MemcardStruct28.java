package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.CString;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class MemcardStruct28 implements MemoryRef {
  private final Value ref;

  public final CString name_00;
  public final IntRef _18;

  public MemcardStruct28(final Value ref) {
    this.ref = ref;

    this.name_00 = ref.offset(0x18, 0x00L).cast(CString::new);
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
