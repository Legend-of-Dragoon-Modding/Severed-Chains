package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;

import static legend.core.GameEngine.MEMORY;

public class ScriptFile implements MemoryRef {
  private final Value ref;

  public final UnboundedArrayRef<IntRef> offsetArr_00;
  // Code is at an unknown starting point... just use the entrypoint offsets
//  public final UnboundedArrayRef<UnsignedIntRef> code_40;

  public ScriptFile(final Value ref) {
    this.ref = ref;

    this.offsetArr_00 = ref.offset(4, 0x00L).cast(UnboundedArrayRef.of(4, IntRef::new));
//    this.code_40 = ref.offset(4, 0x40L).cast(UnboundedArrayRef.of(4, UnsignedIntRef::new));
  }

  public int getOp(final int offset) {
    return (int)MEMORY.get(this.ref.getAddress() + offset, 4);
  }

  public void setOp(final int offset, final int value) {
    MEMORY.set(this.ref.getAddress() + offset, 4, value);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
