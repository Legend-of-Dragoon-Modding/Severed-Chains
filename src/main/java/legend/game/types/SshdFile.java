package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class SshdFile implements MemoryRef {
  public static final long MAGIC = 0x6468_5353L; //SShd

  private final Value ref;

  public final IntRef size_04;

  public final UnsignedIntRef magic_0c;

  public final UnsignedIntRef _10;

  public final IntRef ptr_14;
  public final IntRef ptr_18;
  public final IntRef ptr_1c;
  public final IntRef ptr_20;

  public SshdFile(final Value ref) {
    this.ref = ref;

    this.size_04 = ref.offset(4, 0x04L).cast(IntRef::new);

    this.magic_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);

    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);

    this.ptr_14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this.ptr_18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this.ptr_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.ptr_20 = ref.offset(4, 0x20L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
