package legend.game.types;

import legend.core.gte.TmdWithId;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;

/**
 * @see <a href="https://github.com/Legend-of-Dragoon-Modding/Legend-of-Dragoon-Java/issues/2">more information</a>
 */
public class CContainer implements MemoryRef {
  private final Value ref;

  public final RelativePointer<TmdWithId> tmdPtr_00;
  public final RelativePointer<CContainerSubfile1> ext_04;
  public final RelativePointer<CContainerSubfile2> ptr_08;

  public CContainer(final Value ref) {
    this.ref = ref;

    this.tmdPtr_00 = ref.offset(4, 0x0L).cast(RelativePointer.deferred(4, TmdWithId::new));
    this.ext_04 = ref.offset(4, 0x4L).cast(RelativePointer.deferred(4, ref.getAddress(), CContainerSubfile1::new));
    this.ptr_08 = ref.offset(4, 0x8L).cast(RelativePointer.deferred(4, ref.getAddress(), CContainerSubfile2::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
