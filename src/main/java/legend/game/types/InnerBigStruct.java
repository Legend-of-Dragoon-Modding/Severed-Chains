package legend.game.types;

import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class InnerBigStruct implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef ui_00;
  public final MATRIX m_04;

  public final UnsignedIntRef ui_18;
  public final UnsignedIntRef ui_1c;
  public final UnsignedIntRef ui_20;

  /** Pointer to another struct. Struct has a pointer back to this at +0x4c */
  public final UnsignedIntRef ui_48;

  public InnerBigStruct(final Value ref) {
    this.ref = ref;

    this.ui_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.m_04 = ref.offset(4, 0x04L).cast(MATRIX::new);

    this.ui_18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this.ui_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.ui_20 = ref.offset(4, 0x20L).cast(UnsignedIntRef::new);

    this.ui_48 = ref.offset(4, 0x48L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
