package legend.game.types;

import legend.core.gte.TmdWithId;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.UnsignedIntRef;

/**
 * @see <a href="https://github.com/Legend-of-Dragoon-Modding/Legend-of-Dragoon-Java/issues/2">more information</a>
 */
public class ExtendedTmd implements MemoryRef {
  private final Value ref;

  public final RelativePointer<TmdWithId> tmdPtr_00;
  public final RelativePointer<TmdExtension> ext_04;
  public final UnsignedIntRef ptr_08;

  public ExtendedTmd(final Value ref) {
    this.ref = ref;

    this.tmdPtr_00 = ref.offset(4, 0x0L).cast(RelativePointer.deferred(4, TmdWithId::new));
    this.ext_04 = ref.offset(4, 0x4L).cast(RelativePointer.deferred(4, ref.getAddress(), TmdExtension::new));
    this.ptr_08 = ref.offset(4, 0x8L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
