package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class MenuGlyph06 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef glyph_00;

  public final ShortRef x_02;
  public final ShortRef y_04;

  public MenuGlyph06(final Value ref) {
    this.ref = ref;

    this.glyph_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this.x_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.y_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
