package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

import javax.annotation.Nullable;

public class MathStruct implements MemoryRef {
  @Nullable
  private final Value ref;

  public final UnsignedIntRef ui_00;
  public final UnsignedIntRef ui_04;
  public final UnsignedIntRef ui_08;
  public final UnsignedByteRef ub_0c;
  public final UnsignedByteRef ub_0d;
  public final UnsignedByteRef ub_0e;

  public MathStruct(final Value ref) {
    this.ref = ref;

    this.ui_00 = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.ui_04 = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.ui_08 = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.ub_0c = ref.offset(1, 0xcL).cast(UnsignedByteRef::new);
    this.ub_0d = ref.offset(1, 0xdL).cast(UnsignedByteRef::new);
    this.ub_0e = ref.offset(1, 0xeL).cast(UnsignedByteRef::new);
  }

  public MathStruct() {
    this.ref = null;

    this.ui_00 = new UnsignedIntRef();
    this.ui_04 = new UnsignedIntRef();
    this.ui_08 = new UnsignedIntRef();
    this.ub_0c = new UnsignedByteRef();
    this.ub_0d = new UnsignedByteRef();
    this.ub_0e = new UnsignedByteRef();
  }

  @Override
  public long getAddress() {
    return this.ref != null ? this.ref.getAddress() : 0;
  }
}
