package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class MessageBox20 implements MemoryRef {
  private final Value ref;

  public final Pointer<LodString> text_00;
  public final Pointer<Renderable58> renderable_04;
  public final Pointer<Renderable58> renderable_08;
  public final UnsignedByteRef state_0c;

  /** The number of frames the messagebox has been displayed */
  public final UnsignedIntRef ticks_10;

  public final UnsignedByteRef _15;

  public final IntRef menuIndex_18;
  public final UnsignedShortRef x_1c;
  public final UnsignedShortRef y_1e;

  public MessageBox20(final Value ref) {
    this.ref = ref;

    this.text_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, LodString::new));
    this.renderable_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, Renderable58::new));
    this.renderable_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, Renderable58::new));
    this.state_0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);

    this.ticks_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);

    this._15 = ref.offset(1, 0x15L).cast(UnsignedByteRef::new);

    this.menuIndex_18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this.x_1c = ref.offset(2, 0x1cL).cast(UnsignedShortRef::new);
    this.y_1e = ref.offset(2, 0x1eL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
