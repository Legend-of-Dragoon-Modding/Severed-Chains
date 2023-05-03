package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;

public class AdditionOverlaysHit20 implements MemoryRef {
  public final Value ref;

  public final ByteRef _00;
  public final ByteRef _01;
  public final ByteRef _02;

  public final ShortRef shadowColor_08;
  public final ShortRef _0a;
  public final ShortRef _0c;
  public final ShortRef _0e;
  public final ShortRef tickSuccessLowerBound_10;
  public final ShortRef tickSuccessUpperBound_12;
  public final Pointer<ArrayRef<AdditionOverlaysBorder0e>> targetBorderArray;
  /**
   * 0-13: rotating borders, 14 and 16: target border frames, 15: target border
   */
  public final Pointer<ArrayRef<AdditionOverlaysBorder0e>> borderArray;
  public final ByteRef _1c;

  public AdditionOverlaysHit20(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(ByteRef::new);
    this._01 = ref.offset(1, 0x01L).cast(ByteRef::new);
    this._02 = ref.offset(1, 0x02L).cast(ByteRef::new);

    this.shadowColor_08 = ref.offset(2, 0x08).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0a).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0c).cast(ShortRef::new);
    this._0e = ref.offset(2, 0x0e).cast(ShortRef::new);
    this.tickSuccessLowerBound_10 = ref.offset(2, 0x10).cast(ShortRef::new);
    this.tickSuccessUpperBound_12 = ref.offset(2, 0x12).cast(ShortRef::new);
    this.targetBorderArray = ref.offset(4, 0x14).cast(Pointer.deferred(4, ArrayRef.of(AdditionOverlaysBorder0e.class, 0x2a, 0xe, AdditionOverlaysBorder0e::new)));
    this.borderArray = ref.offset(4, 0x18).cast(Pointer.deferred(4, ArrayRef.of(AdditionOverlaysBorder0e.class, 0xee, 0xe, AdditionOverlaysBorder0e::new)));
    this._1c = ref.offset(1, 0x1c).cast(ByteRef::new);
  }

  @Override
  public long getAddress() { return this.ref.getAddress(); }
}
