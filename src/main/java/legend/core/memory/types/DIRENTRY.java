package legend.core.memory.types;

import legend.core.memory.Value;

/** 0x34 bytes */
public class DIRENTRY implements MemoryRef {
  private final Value ref;

  /** 0x0 */
  public final legend.core.memory.types.CString name;
  /** 0x20 */
  public final UnsignedIntRef attr;
  /** 0x24 */
  public final UnsignedIntRef size;
  /** 0x28 */
  public final Pointer<DIRENTRY> next;
  /** 0x2c */
  public final legend.core.memory.types.CString system;

  public DIRENTRY(final Value ref) {
    this.ref = ref;

    this.name   = ref.offset(20, 0x00L).cast(legend.core.memory.types.CString::new);
    this.attr   = ref.offset( 4, 0x20L).cast(UnsignedIntRef::new);
    this.size   = ref.offset( 4, 0x24L).cast(UnsignedIntRef::new);
    this.next   = ref.offset( 4, 0x28L).cast(Pointer.deferred(4, DIRENTRY::new));
    this.system = ref.offset( 8, 0x2cL).cast(CString::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
