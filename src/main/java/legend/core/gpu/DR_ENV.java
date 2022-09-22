package legend.core.gpu;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class DR_ENV implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef tag;
  public final ArrayRef<UnsignedIntRef> code;

  public DR_ENV(final Value ref) {
    this.ref = ref;

    this.tag = new UnsignedIntRef(ref.offset(4, 0x0L));
    this.code = ref.offset(4, 0x4L).cast(ArrayRef.of(UnsignedIntRef.class, 15, 4, UnsignedIntRef::new));
  }

  public DR_ENV set(final DR_ENV other) {
    this.tag.set(other.tag);

    for(int i = 0; i < this.code.length(); i++) {
      this.code.get(i).set(other.code.get(i));
    }

    return this;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
