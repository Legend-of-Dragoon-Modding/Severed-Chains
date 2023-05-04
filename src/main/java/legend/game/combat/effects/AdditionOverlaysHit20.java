package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;

public class AdditionOverlaysHit20 implements MemoryRef {
  public final Value ref;

  public final ByteRef unused_00; // never used
  public final ByteRef hitSuccessful_01;
  public final ByteRef borderColorsArrayIndex_02; // Set only to 3

  public final ShortRef shadowColor_08;
  public final ShortRef totalHitFrames_0a;
  public final ShortRef frameBeginDisplay_0c;
  public final ShortRef numGrayFrames_0e;
  public final ShortRef frameSuccessLowerBound_10;
  public final ShortRef frameSuccessUpperBound_12;
  public final Pointer<ArrayRef<AdditionOverlaysBorder0e>> targetBorderArray_14;
  /**
   * 0-13: rotating borders, 14 and 16: target border frames, 15: target border
   */
  public final Pointer<ArrayRef<AdditionOverlaysBorder0e>> borderArray_18;
  public final ByteRef isCounter_1c;

  public AdditionOverlaysHit20(final Value ref) {
    this.ref = ref;

    this.unused_00 = ref.offset(1, 0x00L).cast(ByteRef::new);
    this.hitSuccessful_01 = ref.offset(1, 0x01L).cast(ByteRef::new);
    this.borderColorsArrayIndex_02 = ref.offset(1, 0x02L).cast(ByteRef::new);

    this.shadowColor_08 = ref.offset(2, 0x08).cast(ShortRef::new);
    this.totalHitFrames_0a = ref.offset(2, 0x0a).cast(ShortRef::new);
    this.frameBeginDisplay_0c = ref.offset(2, 0x0c).cast(ShortRef::new);
    this.numGrayFrames_0e = ref.offset(2, 0x0e).cast(ShortRef::new);
    this.frameSuccessLowerBound_10 = ref.offset(2, 0x10).cast(ShortRef::new);
    this.frameSuccessUpperBound_12 = ref.offset(2, 0x12).cast(ShortRef::new);
    this.targetBorderArray_14 = ref.offset(4, 0x14).cast(Pointer.deferred(4, ArrayRef.of(AdditionOverlaysBorder0e.class, 0x2a, 0xe, AdditionOverlaysBorder0e::new)));
    this.borderArray_18 = ref.offset(4, 0x18).cast(Pointer.deferred(4, ArrayRef.of(AdditionOverlaysBorder0e.class, 0xee, 0xe, AdditionOverlaysBorder0e::new)));
    this.isCounter_1c = ref.offset(1, 0x1c).cast(ByteRef::new);
  }

  @Override
  public long getAddress() { return this.ref.getAddress(); }
}
