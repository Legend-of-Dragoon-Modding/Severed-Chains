package legend.game.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

/** 0x20 long */
public class TwoVectorsAndRotation implements MemoryRef {
  private final Value ref;

  public final VECTOR vector_00;
  public final VECTOR vector_0c;
  public final UnsignedIntRef rotation_18;
  public final UnsignedIntRef ui_1c;

  public TwoVectorsAndRotation(final Value ref) {
    this.ref = ref;

    this.vector_00 = this.ref.offset(4, 0x00L).cast(VECTOR::new);
    this.vector_0c = this.ref.offset(4, 0x0cL).cast(VECTOR::new);
    this.rotation_18 = this.ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this.ui_1c = this.ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
  }

  public TwoVectorsAndRotation set(final TwoVectorsAndRotation other) {
    this.vector_00.set(other.vector_00);
    this.vector_0c.set(other.vector_0c);
    this.rotation_18.set(other.rotation_18);
    this.ui_1c.set(other.ui_1c);
    return this;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
