package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;

public class PlayableSoundStruct implements MemoryRef {
  private final Value ref;

  public final BoolRef used_00;
  public final Pointer<SshdFile> sshdPtr_04;
  public final IntRef soundBufferPtr_08;

  public PlayableSoundStruct(final Value ref) {
    this.ref = ref;

    this.used_00 = ref.offset(4, 0x00L).cast(BoolRef::new);
    this.sshdPtr_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, SshdFile::new));
    this.soundBufferPtr_08 = ref.offset(4, 0x08L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
