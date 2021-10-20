package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class AnmFile implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef id_00;
  public final UnsignedByteRef version_01;
  public final UnsignedShortRef flag_02;
  /** Number of sprite groups */
  public final UnsignedShortRef n_sprite_gp_04;
  /** Number of sequences */
  public final UnsignedShortRef n_sequence_06;

  private final UnboundedArrayRef<AnmSequence> sequences;
  private UnboundedArrayRef<AnmSpriteGroup> spriteGroups;

  public AnmFile(final Value ref) {
    this.ref = ref;

    this.id_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.version_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this.flag_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.n_sprite_gp_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.n_sequence_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);

    this.sequences = ref.offset(4, 0x08L).cast(UnboundedArrayRef.of(0x8, AnmSequence::new, this::sequenceCount));
  }

  public UnboundedArrayRef<AnmSequence> getSequences() {
    return this.sequences;
  }

  public UnboundedArrayRef<AnmSpriteGroup> getSpriteGroups() {
    // Check if the location has changed
    if(this.spriteGroups != null && this.spriteGroups.getAddress() != this.getAddress() + (this.n_sequence_06.get() + 1) * 8L) {
      this.spriteGroups = null;
    }

    if(this.spriteGroups == null) {
      this.spriteGroups = this.ref.offset(4, (this.n_sequence_06.get() + 1) * 8L).cast(UnboundedArrayRef.of(0x18, AnmSpriteGroup::new));
    }

    return this.spriteGroups;
  }

  private int sequenceCount() {
    return this.n_sequence_06.get();
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
