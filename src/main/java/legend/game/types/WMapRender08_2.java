package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

//TODO maybe a RECT?
public class WMapRender08_2 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef _00;
  public final UnsignedShortRef _02;
  public final UnsignedShortRef _04;
  public final UnsignedShortRef _06;

  public WMapRender08_2(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
