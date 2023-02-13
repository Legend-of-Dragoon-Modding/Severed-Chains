package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;

public class CContainerSubfile1 implements MemoryRef {
  private final Value ref;

  public final ArrayRef<RelativePointer<TmdSubExtension>> tmdSubExtensionArr_00;

  public CContainerSubfile1(final Value ref) {
    this.ref = ref;

    this.tmdSubExtensionArr_00 = ref.offset(4, 0x00L).cast(ArrayRef.of(RelativePointer.classFor(TmdSubExtension.class), 4, 4, RelativePointer.deferred(4, ref.getAddress(), TmdSubExtension::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
