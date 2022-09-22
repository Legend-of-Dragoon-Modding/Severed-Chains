package legend.core.kernel;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

/**
 * Execution file data structure
 * <p>
 * Stores information for loading and executing a program. The data is stored in the first 2K bytes of the
 * execution file (PS-X EXE format). By adding stack information and transfering it to Exec(), the program is
 * activated.
 */
public class EXEC implements MemoryRef {
  private final Value ref;

  /** Execution start address */
  public final UnsignedIntRef pc0;
  /** GP register initial value */
  public final UnsignedIntRef gp0;
  /** Starting address of initialized text section */
  public final UnsignedIntRef t_addr;
  /** Size of text section */
  public final UnsignedIntRef t_size;
  /** Starting address of initialized data section */
  public final UnsignedIntRef d_addr;
  /** Size of initialized data section */
  public final UnsignedIntRef d_size;
  /** Uninitialized data section start address */
  public final UnsignedIntRef b_addr;
  /** Uninitialized data section size */
  public final UnsignedIntRef b_size;
  /** Stack start address (specified by user) */
  public final UnsignedIntRef s_addr;
  /** Stack size (specified by user) */
  public final UnsignedIntRef s_size;
  /** Register shunt variable */
  public final UnsignedIntRef sp;
  /** Register shunt variable */
  public final UnsignedIntRef fp;
  /** Register shunt variable */
  public final UnsignedIntRef gp;
  /** Register shunt variable */
  public final UnsignedIntRef ret;
  /** Register shunt variable */
  public final UnsignedIntRef base;

  public EXEC(final Value ref) {
    this.ref = ref;
    this.pc0 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.gp0 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.t_addr = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.t_size = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.d_addr = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.d_size = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this.b_addr = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this.b_size = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.s_addr = ref.offset(4, 0x20L).cast(UnsignedIntRef::new);
    this.s_size = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);
    this.sp = ref.offset(4, 0x28L).cast(UnsignedIntRef::new);
    this.fp = ref.offset(4, 0x2cL).cast(UnsignedIntRef::new);
    this.gp = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this.ret = ref.offset(4, 0x34L).cast(UnsignedIntRef::new);
    this.base = ref.offset(4, 0x38L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
