package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;

public class UiType implements MemoryRef {
  private final Value ref;

  public UnsignedShortRef entryCount_06;

  public UnboundedArrayRef<UiPart> entries_08;

  public UnsignedShortRef _0a;

  public UiType(final Value ref) {
    this.ref = ref;

    this.entryCount_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);

    this.entries_08 = ref.offset(4, 0x08L).cast(UnboundedArrayRef.of(0x8, UiPart::new));

    this._0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
  }

  public ArrayRef<RenderableMetrics14> getMetrics(final int offset) {
    final int count = (int)this.ref.offset(4, offset).get();
    return this.ref.offset(4, offset).offset(0x4L).cast(ArrayRef.of(RenderableMetrics14.class, count, 0x14, RenderableMetrics14::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
