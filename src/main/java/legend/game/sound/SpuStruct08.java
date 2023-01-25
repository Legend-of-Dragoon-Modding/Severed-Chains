package legend.game.sound;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

public class SpuStruct08 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef _00;
  public final ByteRef soundFileIndex_02;
  public final ByteRef soundIndex_03;
  public final IntRef bobjIndex_04;

  public SpuStruct08(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.soundFileIndex_02 = ref.offset(1, 0x02L).cast(ByteRef::new);
    this.soundIndex_03 = ref.offset(1, 0x03L).cast(ByteRef::new);
    this.bobjIndex_04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
