package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class TriangleIndicator140 implements MemoryRef {
  private final Value ref;

  public final IntRef playerX_08;
  public final IntRef playerY_0c;
  public final IntRef screenOffsetX_10;
  public final IntRef screenOffsetY_14;
  public final ArrayRef<ShortRef> _18;
  public final ArrayRef<ShortRef> _40;
  public final ArrayRef<ShortRef> _68;
  public final ArrayRef<IntRef> _90;
  public final ArrayRef<IntRef> _e0;

  public TriangleIndicator140(final Value ref) {
    this.ref = ref;

    this.playerX_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.playerY_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this.screenOffsetX_10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this.screenOffsetY_14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(2, 0x18L).cast(ArrayRef.of(ShortRef.class, 20, 2, ShortRef::new));
    this._40 = ref.offset(2, 0x40L).cast(ArrayRef.of(ShortRef.class, 20, 2, ShortRef::new));
    this._68 = ref.offset(2, 0x68L).cast(ArrayRef.of(ShortRef.class, 20, 2, ShortRef::new));
    this._90 = ref.offset(4, 0x90L).cast(ArrayRef.of(IntRef.class, 20, 4, IntRef::new));
    this._e0 = ref.offset(4, 0xe0L).cast(ArrayRef.of(IntRef.class, 20, 4, IntRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
