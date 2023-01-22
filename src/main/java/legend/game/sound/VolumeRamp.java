package legend.game.sound;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class VolumeRamp implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final ArrayRef<UnsignedByteRef> ramp_02;

  public VolumeRamp(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.ramp_02 = ref.offset(1, 0x02L).cast(ArrayRef.of(UnsignedByteRef.class, 0x80, 1, UnsignedByteRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
