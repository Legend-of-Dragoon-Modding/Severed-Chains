package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class DirectionalPathSegmentData08 implements MemoryRef {
  private final Value ref;

  /**
   * Path segment index (actually path index + 1 because 0 can't have a sign).
   * The index can be positive or negative depending on Dart's direction.
   */
  public final ShortRef pathSegmentIndexAndDirection_00;

  public final UnsignedByteRef encounterRate_03;
  public final ByteRef battleStage_04;
  /** Index into a table which has the actual encounter ID */
  public final ByteRef encounterIndex_05;
  public final UnsignedByteRef modelIndex_06;

  public DirectionalPathSegmentData08(final Value ref) {
    this.ref = ref;

    this.pathSegmentIndexAndDirection_00 = ref.offset(2, 0x00L).cast(ShortRef::new);

    this.encounterRate_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.battleStage_04 = ref.offset(1, 0x04L).cast(ByteRef::new);
    this.encounterIndex_05 = ref.offset(1, 0x05L).cast(ByteRef::new);
    this.modelIndex_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
