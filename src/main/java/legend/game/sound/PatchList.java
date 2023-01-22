package legend.game.sound;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;

public class PatchList implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef patchCount_00;
  public final UnboundedArrayRef<RelativePointer<SequenceList>> patches_02;

  public PatchList(final Value ref) {
    this.ref = ref;

    this.patchCount_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.patches_02 = ref.offset(2, 0x02L).cast(UnboundedArrayRef.of(2, RelativePointer.deferred(2, ref.getAddress(), value -> new SequenceList(ref, value), -1), () -> this.patchCount_00.get() + 1));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  public static class SequenceList implements MemoryRef {
    private final Value ref;

    public final UnsignedShortRef sequenceCount_00;
    public final UnboundedArrayRef<RelativePointer<Sequence>> sequences_02;

    public SequenceList(final Value baseRef, final Value ref) {
      this.ref = ref;

      this.sequenceCount_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
      this.sequences_02 = ref.offset(2, 0x02L).cast(UnboundedArrayRef.of(2, RelativePointer.deferred(1, baseRef.getAddress(), Sequence::new, -1), () -> this.sequenceCount_00.get() + 1));
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }
}
